package com.example.example100;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class Example100Application {

    public static void main(String[] args) {
        SpringApplication.run(Example100Application.class, args);
    }

}
