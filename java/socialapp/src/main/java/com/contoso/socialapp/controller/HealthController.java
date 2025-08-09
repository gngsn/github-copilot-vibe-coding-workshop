package com.contoso.socialapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Social App is running successfully!";
    }
    
    @GetMapping("/info")
    public String info() {
        return "Spring Boot Social Media Application - Version 1.0";
    }
}
