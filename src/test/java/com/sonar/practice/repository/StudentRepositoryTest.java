package com.sonar.practice.repository;

import com.sonar.practice.entity.Student;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void saveAndFindStudent() {
        Student s = new Student();
        s.setFirstName("Test");
        s.setLastName("Student");
        s.setEmail("test.student@example.com");
        s.setDateOfBirth(LocalDate.of(2000, 1, 1));

        Student saved = studentRepository.save(s);

        assertThat(saved.getId()).isNotNull();
        assertThat(studentRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void existsByEmailWorks() {
        Student s = new Student();
        s.setFirstName("Exist");
        s.setLastName("Email");
        s.setEmail("exists@example.com");
        studentRepository.save(s);

        assertThat(studentRepository.existsByEmail("exists@example.com")).isTrue();
        assertThat(studentRepository.existsByEmail("nope@example.com")).isFalse();
    }
}