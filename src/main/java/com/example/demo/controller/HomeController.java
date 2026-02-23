package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/") //시작 주소를 받아준다
    public String index() {
        return "index";
    }

}
