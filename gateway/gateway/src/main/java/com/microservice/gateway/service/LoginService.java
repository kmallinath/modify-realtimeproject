package com.microservice.gateway.service;


import com.microservice.gateway.dtos.LoginRequest;
import com.microservice.gateway.dtos.UserDto;
import com.microservice.gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        String enteredEmail = loginRequest.getEmail();
        String enteredPassword = loginRequest.getPassword();
        String userServiceUrl = "http://localhost:8080/api/user/login";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                userServiceUrl,
                HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                String.class
        );

        return responseEntity.getBody();


    }

}
