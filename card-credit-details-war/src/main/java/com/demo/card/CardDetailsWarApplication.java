package com.demo.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.demo.card")
public class CardDetailsWarApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CardDetailsWarApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CardDetailsWarApplication.class);
    }
}
