package dev.dov.tasklist.web.security.expression;

import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.service.UserService;
import dev.dov.tasklist.web.security.JwtEntity;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        return userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean hasAnyRole(Authentication authentication, Role...roles){

        for(Role role : roles){

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if(authentication.getAuthorities().contains(authority)){
                return true;
            }
        }

        return false;
    }

    public boolean canAccessTask(Long taskId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long id = user.getId();

        return userService.isTaskOwner(id, taskId);
    }

}
