package com.krainet.test_task.service;


import com.krainet.test_task.security.TokenDetails;

public interface SecurityService {
    TokenDetails authenticate(String username, String password);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);

    String getRoleFromToken(String token);

    Long getIdFromToken(String token);
}
