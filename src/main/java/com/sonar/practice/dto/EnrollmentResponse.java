package com.sonar.practice.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EnrollmentResponse {

    Long id;
    Long studentId;
    Long courseId;
    OffsetDateTime enrolledAt;
    String status;
}
