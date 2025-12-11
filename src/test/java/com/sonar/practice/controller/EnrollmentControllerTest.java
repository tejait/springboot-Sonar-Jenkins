package com.sonar.practice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonar.practice.dto.EnrollmentRequest;
import com.sonar.practice.dto.EnrollmentResponse;
import com.sonar.practice.service.EnrollmentService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

  @MockBean
    EnrollmentService enrollmentService;

    @Test
    void enroll_returnsCreated() throws Exception {
        EnrollmentRequest req = new EnrollmentRequest();
        req.setStudentId(1L);
        req.setCourseId(2L);

        EnrollmentResponse resp = EnrollmentResponse.builder()
                .id(10L)
                .studentId(req.getStudentId())
                .courseId(req.getCourseId())
                .enrolledAt(OffsetDateTime.now())
                .build();

        when(enrollmentService.enrollStudent(any(EnrollmentRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));
    }

    @Test
    void cancel_and_getForStudent() throws Exception {
        EnrollmentResponse resp = EnrollmentResponse.builder()
                .id(20L)
                .studentId(3L)
                .courseId(4L)
                .build();

        when(enrollmentService.getEnrollmentsForStudent(3L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/enrollments/student/3"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(resp))));

        mockMvc.perform(delete("/api/enrollments/20"))
                .andExpect(status().isNoContent());
        verify(enrollmentService).cancelEnrollment(20L);
    }
}