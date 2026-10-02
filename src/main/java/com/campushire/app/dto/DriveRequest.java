package com.campushire.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DriveRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name is too long")
        String companyName,

        @NotBlank(message = "Job title is required")
        @Size(max = 100, message = "Job title is too long")
        String jobTitle,

        @NotNull(message = "Package is required")
        @DecimalMin(value = "0.0", message = "Package cannot be negative")
        @Digits(integer = 3, fraction = 2, message = "Package must have at most 3 digits and 2 decimals")
        BigDecimal packageLpa,

        @NotNull(message = "Minimum CGPA is required")
        @DecimalMin(value = "0.0", message = "Minimum CGPA cannot be negative")
        @Digits(integer = 1, fraction = 2, message = "Minimum CGPA must be below 10 with at most 2 decimals")
        BigDecimal minCgpa,

        @NotNull(message = "Allowed backlogs is required")
        @Min(value = 0, message = "Allowed backlogs cannot be negative")
        Integer allowedBacklogs
) {
}