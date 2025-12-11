package com.sonar.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracticeApplication {
    private static final Logger logger = LoggerFactory.getLogger(PracticeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
        logger.info("Application Started Successfully");
	}

}
