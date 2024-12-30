package com.example.oner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnerApplication.class, args);
    }

}
