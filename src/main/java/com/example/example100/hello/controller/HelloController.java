package com.example.example100.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    /**
     * 1. "/first-url"을 주소로 가지는 반환값이 없는 주소를 작성하시오.
     * RequestMapping 어노테이션을 사용
     */
    @ResponseBody
    @RequestMapping(value = "/first-url", method= RequestMethod.GET)
    public void firstApi () {
    }

    /**
     * 2. "Hello World!" 문자열을 반환하는 주소를 작성하시오.
     * RequestMapping 어노테이션을 사용
     */
    @ResponseBody
    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public String helloWord() {
        return "Hello World!";
    }
}
