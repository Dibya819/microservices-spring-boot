package com.dibya.programming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrafficSignalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrafficSignalServiceApplication.class, args);
    }
}
