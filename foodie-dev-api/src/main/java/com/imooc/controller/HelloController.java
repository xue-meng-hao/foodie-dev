package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class HelloController {
    @ApiIgnore
    @GetMapping("/hello")
    public Object hello() {
        return "Hello world!";
    }

}
