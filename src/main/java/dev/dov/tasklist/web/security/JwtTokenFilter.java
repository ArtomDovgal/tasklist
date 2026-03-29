package dev.dov.tasklist.web.security;

import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String path = httpRequest.getServletPath();

        if (path.startsWith("/api/v1/auth")) {
            logger.warn("We here why?");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            bearerToken = bearerToken.substring(7);
        }

        if(bearerToken != null && jwtTokenProvider.validateToken(bearerToken)){
            logger.warn("We here1");
            try {
                logger.warn("We here2");
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if(authentication != null){
                    logger.warn("We here3");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (ResourceNotFoundException e){
                logger.warn("User not found from token");
            }
        }

        logger.warn("We here4");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
