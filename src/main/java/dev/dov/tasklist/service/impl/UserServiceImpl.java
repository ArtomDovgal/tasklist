package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.repository.UserRepository;
import dev.dov.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        log.warn("before found userRepository.findByUsername(username)1");
        Optional<User> user = userRepository.findByUsername(username);
        log.warn("after found userRepository.findByUsername(username)2");
        if(user.isEmpty()){
            log.warn("we dont find user");
            throw new IllegalStateException("User not exists");
        }

        log.warn("user is found");
        return user.get();
    }

    @Override
    @Transactional
    @Caching(put= {
            @CachePut(value = "UserService::getById", key="#user.id"),
            @CachePut(value = "UserService::getByUsername", key="#user.username")
    }
    )
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User create(User user) {
        Optional<User> smth = userRepository.findByUsername(user.getUsername());
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalStateException("User already exists");
        }
        if(!user.getPassword().equals(user.getPasswordConfirmation())){
            throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId" + '.' + "taskId")
    @Transactional(readOnly = true)
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#id")
    public void delete(Long id) {

        userRepository.deleteById(id);
    }
}
