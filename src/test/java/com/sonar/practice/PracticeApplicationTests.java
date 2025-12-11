package com.sonar.practice;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@EnableWebMvc
@SpringBootTest
class PracticeApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(PracticeApplicationTests.class);
	@Test
	void contextLoads() {
       // logger.info("testing log for jenkins first commit");
	}

}
