package dev.dov.tasklist.config;

import dev.dov.tasklist.web.security.JwtTokenFilter;
import dev.dov.tasklist.web.security.JwtTokenProvider;
import dev.dov.tasklist.web.security.expression.CustomSecurityExceptionHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor_ = @__(@Lazy))
public class ApplicationConfig {

    private final JwtTokenProvider tokenProvider;
    private final ApplicationContext applicationContext;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("Task list API")
                        .description("Demo Spring Boot application")
                        .version("1.0")
                        );
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){

        return configuration.getAuthenticationManager();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler(){
        DefaultMethodSecurityExpressionHandler expressionHandler = new CustomSecurityExceptionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity){

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exceptionHandling ->{
                            exceptionHandling.authenticationEntryPoint((request, response, authException) ->{
                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                    response.getWriter().write("Unauthorized");
                            });
                            exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                                response.getWriter().write("Unauthorized");
                            });
                        })
                .authorizeHttpRequests(configure ->{
                    configure.requestMatchers("/api/v1/auth/**").permitAll();
                    configure.requestMatchers("/swagger-ui/**").permitAll();
                    configure.requestMatchers("/v3/api-docs/**").permitAll();
                    configure.anyRequest().authenticated();
                })
                .anonymous(anonymous -> anonymous.disable())
                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }


}
