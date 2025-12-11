package com.sonar.practice.service;

import com.sonar.practice.dto.StudentRequest;
import com.sonar.practice.dto.StudentResponse;
import java.util.List;

public interface StudentService {

    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Long id, StudentRequest request);

    void deleteStudent(Long id);

    StudentResponse getStudent(Long id);

    List<StudentResponse> listStudents();
}
