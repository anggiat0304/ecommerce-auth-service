package com.ecommerce.authservice.controller;

import com.ecommerce.authservice.configuration.security.gateway.Wso2ApiGatewayService;
import com.ecommerce.authservice.configuration.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secured")
public class TestController {

    @Autowired
    private Wso2ApiGatewayService wso2ApiGatewayService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/test")
    public ResponseEntity<?> securedEndpoint(@RequestHeader("Authorization") String authToken) {
        // Validate authToken using auth-service or JWT verification
        // ...
        authToken = jwtUtils.extractTokenWithoutBearer(authToken);
        jwtUtils.validateJwtToken(authToken);
        // Forward the request to the actual backend using WSO2 API Gateway
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<?> response = wso2ApiGatewayService.callBackendApi("/test", HttpMethod.POST, requestEntity);
        return ResponseEntity.ok("taik");
    }
}
