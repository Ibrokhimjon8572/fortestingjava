package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"org.example.*"})
@EntityScan("org.example.entity")
@EnableJpaRepositories("org.example.repository")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}