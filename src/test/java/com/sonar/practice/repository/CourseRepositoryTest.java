package com.sonar.practice.repository;

import com.sonar.practice.entity.Course;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void saveAndExistsByCode() {
        Course c = new Course();
       c.setCode("CS101");
        c.setName("Intro to CS");
        c.setFee(new BigDecimal("100.00"));

        Course saved = courseRepository.save(c);

        assertThat(saved.getId()).isNotNull();
        assertThat(courseRepository.existsByCode("CS101")).isTrue();
    }
}