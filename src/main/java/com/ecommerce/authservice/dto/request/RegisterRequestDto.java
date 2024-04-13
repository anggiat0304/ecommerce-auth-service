package com.ecommerce.authservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String password;
    private Set<String> role;

    @Override
    public String toString() {
        return "RegisterRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
