package com.sonar.practice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonar.practice.dto.EnrollmentRequest;
import com.sonar.practice.entity.Course;
import com.sonar.practice.entity.Student;
import com.sonar.practice.repository.CourseRepository;
import com.sonar.practice.repository.StudentRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void enroll_and_get_for_student() throws Exception {
        Student s = new Student();
        s.setFirstName("E");
        s.setLastName("Test");
        s.setEmail("enroll.test@example.com");
        s.setDateOfBirth(java.time.LocalDate.of(2000, 1, 1));
        s = studentRepository.save(s);

        Course c = new Course();
        c.setCode("ENR101");
        c.setName("Enroll 101");
        c.setFee(new BigDecimal("50.00"));
        c = courseRepository.save(c);

        EnrollmentRequest req = new EnrollmentRequest();
        req.setStudentId(s.getId());
        req.setCourseId(c.getId());

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/enrollments/student/" + s.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"courseId\":" + c.getId())));
    }
}