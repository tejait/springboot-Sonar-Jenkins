package com.sonar.practice.integration;

import com.sonar.practice.controller.StudentController;
import com.sonar.practice.dto.StudentRequest;
import com.sonar.practice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentControllerIntegrationTest {

    @Autowired
    StudentController studentController;

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void create_and_get_student_endpoints() {
        StudentRequest req = new StudentRequest();
        req.setFirstName("Int");
        req.setLastName("Test");
        req.setEmail("int.test@example.com");
        req.setDateOfBirth(java.time.LocalDate.of(1995, 2, 2));

        ResponseEntity<?> createResp = studentController.createStudent(req);
        assertThat(createResp.getStatusCode().value()).isEqualTo(201);

        ResponseEntity<?> listResp = studentController.listStudents();
        assertThat(listResp.getStatusCode().value()).isEqualTo(200);
        assertThat(listResp.getBody().toString()).contains("int.test@example.com");

        // verify repository has entry
        assertThat(studentRepository.existsByEmail("int.test@example.com")).isTrue();
    }
}