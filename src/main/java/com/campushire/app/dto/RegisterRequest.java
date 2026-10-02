package com.campushire.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2 to 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email is too long")
    @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Enter a valid email address")
    private String email;

    // BCrypt only reads the first 72 bytes, so we cap the length at 72
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$",
             message = "Password must be 8 to 72 characters with at least one letter and one number")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "STUDENT|RECRUITER", message = "Role must be STUDENT or RECRUITER")
    private String role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}