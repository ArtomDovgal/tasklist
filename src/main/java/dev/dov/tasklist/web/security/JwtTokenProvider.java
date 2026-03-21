package dev.dov.tasklist.web.security;

import dev.dov.tasklist.domain.exeption.AccessDeniedException;
import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.service.UserService;
import dev.dov.tasklist.service.props.JwtProperties;
import dev.dov.tasklist.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private SecretKey key;

    @PostConstruct
    public void init(){
        //this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("Key length: " + key.getEncoded().length);
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles){

        Date now = new Date();

        Instant validity = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.HOURS);

        //Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .subject(username)
                .claim("id", userId)
                .claim("roles", resolveRoles(roles))
                .issuedAt(now)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();

    }

    public String createRefreshToken(Long userId, String username){

        Date now = new Date();
        Instant validity = Instant.now().plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        //Date validity = new Date(now.getTime() + jwtProperties.getRefresh());

        return Jwts.builder()
                .subject(username)
                .claim("id", userId)
                .issuedAt(now)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles){

        return roles.stream()
                .map(Enum::name)
                .toList();
    }

    public JwtResponse refreshUserTokens(String refreshToken){

        JwtResponse jwtResponse = new JwtResponse();

        if(!validateToken(refreshToken)){
            throw new AccessDeniedException();
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);

        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));

        return jwtResponse;
    }


    public boolean validateToken(String token){

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }

    private String getId(String token){

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id").toString();

    }

    public Authentication getAuthentication(String token){
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
