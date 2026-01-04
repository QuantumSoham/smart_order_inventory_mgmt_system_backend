package com.chubb.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String credential;
    private String password;
}
