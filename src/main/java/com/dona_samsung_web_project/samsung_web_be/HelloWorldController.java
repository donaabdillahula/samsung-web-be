package com.dona_samsung_web_project.samsung_web_be;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController { 

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }
    
}
