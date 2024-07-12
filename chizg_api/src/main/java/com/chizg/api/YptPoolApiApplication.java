package com.chizg.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class YptPoolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YptPoolApiApplication.class, args);
    }
}
