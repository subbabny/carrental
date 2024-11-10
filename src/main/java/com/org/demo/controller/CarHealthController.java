package com.org.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/car")
public class CarHealthController {

    @GetMapping("health")
    public String display(){
        return "Service is UP";
    }
}
