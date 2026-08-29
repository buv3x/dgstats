package com.datascience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);
    }
}
