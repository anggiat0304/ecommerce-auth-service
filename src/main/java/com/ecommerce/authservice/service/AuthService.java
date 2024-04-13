package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.request.LoginRequestDto;
import com.ecommerce.authservice.dto.request.RegisterRequestDto;
import com.ecommerce.authservice.dto.response.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public JwtResponse auth(LoginRequestDto loginRequestDto) throws Exception;

    public String register(RegisterRequestDto registerRequestDto);

    public boolean validateToken(String token, Long userId);

    void logout (String token , Long userId);
}
