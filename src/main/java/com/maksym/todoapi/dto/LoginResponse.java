package com.maksym.todoapi.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;

    public LoginResponse(String accessToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
