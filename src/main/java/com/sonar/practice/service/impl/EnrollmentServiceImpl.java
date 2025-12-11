package com.sonar.practice.service.impl;

import com.sonar.practice.dto.EnrollmentRequest;
import com.sonar.practice.dto.EnrollmentResponse;
import com.sonar.practice.entity.Course;
import com.sonar.practice.entity.Enrollment;
import com.sonar.practice.entity.Student;
import com.sonar.practice.repository.CourseRepository;
import com.sonar.practice.repository.EnrollmentRepository;
import com.sonar.practice.repository.StudentRepository;
import com.sonar.practice.service.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return mapToResponse(saved);
    }

    @Override
    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        enrollment.setStatus("CANCELLED");
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsForStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
            .map(this::mapToResponse)
            .toList();
    }

    private EnrollmentResponse mapToResponse(Enrollment e) {
        return EnrollmentResponse.builder()
            .id(e.getId())
            .studentId(e.getStudent() != null ? e.getStudent().getId() : null)
            .courseId(e.getCourse() != null ? e.getCourse().getId() : null)
            .enrolledAt(e.getEnrolledAt())
            .status(e.getStatus())
            .build();
    }
}
