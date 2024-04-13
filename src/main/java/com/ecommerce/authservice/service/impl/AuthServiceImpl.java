package com.ecommerce.authservice.service.impl;

import com.ecommerce.authservice.configuration.security.jwt.JwtUtils;
import com.ecommerce.authservice.configuration.security.services.UserDetailsImpl;
import com.ecommerce.authservice.dto.Enum.ERole;
import com.ecommerce.authservice.dto.request.LoginRequestDto;
import com.ecommerce.authservice.dto.request.RegisterRequestDto;
import com.ecommerce.authservice.dto.response.JwtResponse;
import com.ecommerce.authservice.model.Login;
import com.ecommerce.authservice.model.Role;
import com.ecommerce.authservice.model.User;
import com.ecommerce.authservice.respositoriy.LoginRepository;
import com.ecommerce.authservice.respositoriy.RoleRepository;
import com.ecommerce.authservice.respositoriy.UserRepository;
import com.ecommerce.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    JwtUtils jwtUtils;

    @Override
    public JwtResponse auth(LoginRequestDto loginRequestDto) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Login looinUser = loginRepository.findLoginByUserId(userDetails.getId());
            if (looinUser !=null){
                throw new RuntimeException("User Has Been Logged");
            }

            List<String> roles = userRepository.findRoleByUserId(loginRequestDto.getUsername());
            Login login = new Login();
            login.setToken(jwt);
            login.setLogginAttemp(LocalDateTime.now());
            // Mengambil informasi user dari UserDetails
            User user = new User();
            user.setId(userDetails.getId());
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            login.setUser(user);

            loginRepository.saveAndFlush(login);

            JwtResponse jwtResponse = new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    roles);
            return jwtResponse;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String register(RegisterRequestDto registerRequestDto) {
        try {
            if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
                throw new IllegalArgumentException("username already exist");
            }

            // Create new user's account
            User user = new User();
            user.setUsername(registerRequestDto.getUsername());
            user.setPassword(encoder.encode(registerRequestDto.getPassword()));

            Set<String> strRoles = registerRequestDto.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "mod":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);
            return "User registered successfully!";
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token, Long userId) {
        try {
            jwtUtils.validateJwtToken(token);
            Login login = loginRepository.findLoginByTokenAndAndUserId(token,userId);
            if (login == null){
                throw new IllegalArgumentException("Token is doesn't valid");
            }
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }

    @Override
    public void logout(String token, Long userId) {
        try {
            validateToken(token,userId);
            loginRepository.deleteLoginByTokenAndUserId(token, userId);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
