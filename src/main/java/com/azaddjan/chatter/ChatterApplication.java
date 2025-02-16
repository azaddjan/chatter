package com.azaddjan.chatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = "com.azaddjan")
@EnableJpaRepositories(basePackages = "com.azaddjan.repository")  // ✅ Ensures repositories are scanned
@EntityScan(basePackages = "com.azaddjan.entity")
@EnableRetry
public class ChatterApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatterApplication.class, args);
    }

}
