package com.krainet.test_task.service.impl;

import com.krainet.test_task.exception.AuthException;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.service.SecurityService;
import com.krainet.test_task.security.TokenDetails;
import com.krainet.test_task.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    private Logger logger = LogManager.getLogger(SecurityServiceImpl.class);

    @Override
    public TokenDetails authenticate(String username, String password) {
        logger.info("Login request - " + username);
        UserEntity user = userService.getUserByUsername(username);

        logger.info("User try to login - " + user.toString());
        if (!user.getIsActive()) {
            throw new AuthException("Account disabled", "ACCOUNT_DISABLED");
        }
        logger.info("Is password right - " + user.getPassword().equals(passwordEncoder.encode(password)));
        if (!user.getPassword().equals(passwordEncoder.encode(password))) {
            throw new AuthException("Invalid password", "INVALID_PASSWORD");
        }

        return generateToken(user).toBuilder()
                .id(user.getId())
                .role(user.getRole())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);


            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};

        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return new TokenDetails().toBuilder()
                .token(token)
                .expiredAt(expirationDate)
                .issuedAt(createdDate)
                .build();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token)
                .get("username", String.class);
    }

    public Long getIdFromToken(String token) {
        return Long.parseLong(getClaims(token)
                .getSubject());

    }

    @Override
    public String getRoleFromToken(String token) {
        return getClaims(token)
                .get("role", String.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
