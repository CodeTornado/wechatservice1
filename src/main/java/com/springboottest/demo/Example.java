package com.springboottest.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Example {

    @RequestMapping("/CC")
    String home() {
        return "Hello World!5";
    }

}
