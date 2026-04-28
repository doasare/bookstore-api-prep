package com.amigoscode.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InterviewPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewPracticeApplication.class, args);
    }

}
