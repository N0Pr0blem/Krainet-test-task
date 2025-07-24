package com.krainet.test_task.security;


public interface SecurityService {
    TokenDetails authenticate(String username, String password);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);

    String getRoleFromToken(String token);

    Long getIdFromToken(String token);
}
