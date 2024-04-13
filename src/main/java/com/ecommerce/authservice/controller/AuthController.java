package com.ecommerce.authservice.controller;

import com.ecommerce.authservice.configuration.security.jwt.JwtUtils;
import com.ecommerce.authservice.dto.request.LoginRequestDto;
import com.ecommerce.authservice.dto.request.RegisterRequestDto;
import com.ecommerce.authservice.dto.response.GenericResponse;
import com.ecommerce.authservice.dto.response.JwtResponse;
import com.ecommerce.authservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public GenericResponse<?> signin(@RequestBody LoginRequestDto loginRequestDto){
        try {
            logger.info("Request {}",loginRequestDto.toString());
        JwtResponse jwtResponse = authService.auth(loginRequestDto);
        GenericResponse<JwtResponse> response = GenericResponse.success(HttpStatus.OK.toString(),jwtResponse);
        return response;
        }catch (Exception e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @PostMapping("/signup")
    public GenericResponse<?> signup(@RequestBody RegisterRequestDto registerRequestDto){
        try {
            String signup = authService.register(registerRequestDto);
            GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),signup);
            return response;
        }catch (Exception e){
            GenericResponse<?> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @PostMapping("/validateToken")
    public GenericResponse<?> validatedToken(@RequestHeader("Authorization") String authToken, @RequestHeader("User") Long userId) {

        try {
            logger.info("Token {}",authToken);
            logger.info("User {}",userId);
            authToken = jwtUtils.extractTokenWithoutBearer(authToken);
            authService.validateToken(authToken,userId);
            GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),"OK");
            return response;
        }catch (Exception e){
            GenericResponse<String> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
    @GetMapping("/logout")
    public GenericResponse<?> logout(@RequestHeader("Authorization") String authToken, @RequestHeader("User") Long userId) {

        try {
            logger.info("Token {}",authToken);
            logger.info("User {}",userId);
            authToken = jwtUtils.extractTokenWithoutBearer(authToken);
            authService.logout(authToken,userId);
            GenericResponse<String> response = GenericResponse.success(HttpStatus.OK.toString(),"OK");
            return response;
        }catch (Exception e){
            GenericResponse<String> response = GenericResponse.error(e.getMessage());
            return response;
        }
    }
}
