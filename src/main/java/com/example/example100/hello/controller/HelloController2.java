package com.example.example100.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController2 {
    /**
     * 3. "hello spring" 문자열을 반환하는 api를 작성하시오.
     * Rest 방식으로 작성하시오.
     */
    @RequestMapping(value = "/hello-spring", method = RequestMethod.GET)
    public String helloSpring() {
        return "hello spring";
    }
}
