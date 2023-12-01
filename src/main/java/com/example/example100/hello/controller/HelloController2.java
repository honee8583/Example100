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

    /**
     * 4. GetMapping 어노테이션을 사용하여 "hello rest" 문자열을 반환하는 Rest형식의 함수를 작성하시오.
     */
    @GetMapping("/hello-rest")
    public String helloRest() {
        return "hello rest";
    }
}
