package com.example.springbootapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Application!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}


