package com.example.blog_common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class BlogCommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogCommonApplication.class, args);
    }
}
