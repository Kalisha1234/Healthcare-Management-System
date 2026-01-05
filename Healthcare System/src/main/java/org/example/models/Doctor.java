package org.example.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Doctor {
    private Integer doctorID;
    private String firstName;
    private String lastName;
    private Integer departmentID;
    private String phone;
    private String email;
    private LocalDate hireDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Doctor() {}

    public Doctor(String firstName, String lastName, Integer departmentID, String phone, String email, LocalDate hireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentID = departmentID;
        this.phone = phone;
        this.email = email;
        this.hireDate = hireDate;
    }

    public Integer getDoctorID() { return doctorID; }
    public void setDoctorID(Integer doctorID) { this.doctorID = doctorID; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Integer getDepartmentID() { return departmentID; }
    public void setDepartmentID(Integer departmentID) { this.departmentID = departmentID; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
