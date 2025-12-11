package com.sonar.practice.service.impl;

import com.sonar.practice.dto.StudentRequest;
import com.sonar.practice.dto.StudentResponse;
import com.sonar.practice.entity.Student;
import com.sonar.practice.repository.StudentRepository;
import com.sonar.practice.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Student email already exists");
        }
        Student student = mapToEntity(new Student(), request);
        return mapToResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        if (!student.getEmail().equals(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Student email already exists");
        }
        Student updated = studentRepository.save(mapToEntity(student, request));
        return mapToResponse(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudent(Long id) {
        return studentRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> listStudents() {
        return studentRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    private Student mapToEntity(Student student, StudentRequest request) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());
        return student;
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
            .id(student.getId())
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .email(student.getEmail())
            .dateOfBirth(student.getDateOfBirth())
            .createdAt(student.getCreatedAt())
            .build();
    }
}