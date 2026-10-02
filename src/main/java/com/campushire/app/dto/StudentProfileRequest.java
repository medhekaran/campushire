package com.campushire.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StudentProfileRequest(
        @NotBlank(message = "Branch is required")
        String branch,

        @NotNull(message = "CGPA is required")
        @DecimalMin(value = "0.0", message = "CGPA cannot be negative")
        @Digits(integer = 1, fraction = 2, message = "CGPA must be below 10 with at most 2 decimals")
        BigDecimal cgpa,

        @NotNull(message = "Backlogs is required")
        @Min(value = 0, message = "Backlogs cannot be negative")
        Integer backlogs
) {
}