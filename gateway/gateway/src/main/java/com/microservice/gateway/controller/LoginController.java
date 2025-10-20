package com.microservice.gateway.controller;

import com.microservice.gateway.dtos.LoginRequest;
import com.microservice.gateway.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {


    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Implement login logic here

        return loginService.login(loginRequest);


    }


}
