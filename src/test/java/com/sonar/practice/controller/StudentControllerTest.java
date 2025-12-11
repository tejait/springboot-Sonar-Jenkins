package com.sonar.practice.controller;


import com.sonar.practice.dto.StudentRequest;
import com.sonar.practice.dto.StudentResponse;
import com.sonar.practice.service.StudentService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    com.fasterxml.jackson.databind.ObjectMapper objectMapper;

 @MockBean
    StudentService studentService;

    @Test
    void createStudent_returnsCreated() throws Exception {
        StudentRequest req = new StudentRequest();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setEmail("john.doe@example.com");
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));

        StudentResponse resp = StudentResponse.builder()
                .id(1L)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .build();

        //when(studentService.createStudent(any(StudentRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));
    }

    @Test
    void updateStudent_returnsOk() throws Exception {
        StudentRequest req = new StudentRequest();
        req.setFirstName("Jane");
        req.setLastName("Roe");
        req.setEmail("jane.roe@example.com");

        StudentResponse resp = StudentResponse.builder()
                .id(2L)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .build();

        when(studentService.updateStudent(eq(2L), any(StudentRequest.class))).thenReturn(resp);

        mockMvc.perform(put("/api/students/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));
    }

    @Test
    void getAndListAndDelete_studentEndpoints() throws Exception {
        StudentResponse resp = StudentResponse.builder()
                .id(5L)
                .firstName("A")
                .lastName("B")
                .email("a.b@example.com")
                .build();

        when(studentService.getStudent(5L)).thenReturn(resp);
        when(studentService.listStudents()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/students/5"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(resp))));

        mockMvc.perform(delete("/api/students/9"))
                .andExpect(status().isNoContent());
        verify(studentService).deleteStudent(9L);
    }
}