package dev.dov.tasklist.config;

import dev.dov.tasklist.repository.TaskRepository;
import dev.dov.tasklist.repository.UserRepository;
import dev.dov.tasklist.service.*;
import dev.dov.tasklist.service.impl.*;
import dev.dov.tasklist.service.props.JwtProperties;
import dev.dov.tasklist.service.props.MinioProperties;
import dev.dov.tasklist.web.security.JwtTokenFilter;
import dev.dov.tasklist.web.security.JwtTokenProvider;
import dev.dov.tasklist.web.security.JwtUserDetailsService;
import freemarker.template.Configuration;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AuthenticationManager authenticationManager;

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {

        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("ZXJ3d2Vsa3Igd2VyIDtsZXdrcndsZWtyIGx3a2Vyd2xla3Jsa3dlcmt3ZXJrZXdrcmxld3czNDI0MjI=");

        return jwtProperties;

    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService(userService());
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider( jwtProperties(),
                userDetailsService(),
                userService()
        );
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public MainServiceImpl mailService() {
        return new MainServiceImpl(configuration(), mailSender());
    }

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, testPasswordEncoder(), mailService());
    }


    @Bean
    @Primary
    public TaskService taskService(){
        return new TaskServiceImpl(taskRepository, userService(), imageService());
    }

    @Bean
    @Primary
    public AuthService authService() {
        return new AuthServiceImpl(authenticationManager, userService(), tokenProvider());
    }

}
