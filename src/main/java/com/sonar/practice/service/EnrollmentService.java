package com.sonar.practice.service;

import com.sonar.practice.dto.EnrollmentRequest;
import com.sonar.practice.dto.EnrollmentResponse;
import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse enrollStudent(EnrollmentRequest request);

    void cancelEnrollment(Long enrollmentId);

    List<EnrollmentResponse> getEnrollmentsForStudent(Long studentId);
}
