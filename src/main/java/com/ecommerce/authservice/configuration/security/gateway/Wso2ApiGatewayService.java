package com.ecommerce.authservice.configuration.security.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Wso2ApiGatewayService {

    @Value("${wso2.api.gateway.url}")
    private String wso2ApiGatewayUrl;

    @Value("${wso2.api.gateway.context}")
    private String wso2ApiGatewayContext;

    @Autowired
    private  RestTemplate restTemplate;

    public ResponseEntity<?> callBackendApi(String path, HttpMethod method, HttpEntity<?> requestEntity) {
        String apiUrl = wso2ApiGatewayUrl + wso2ApiGatewayContext + path;
        return restTemplate.exchange(apiUrl, method, requestEntity, String.class);
    }
}
