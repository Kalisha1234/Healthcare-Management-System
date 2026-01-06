package org.example.models;

import java.time.LocalDateTime;

public class Department {
    private Integer departmentID;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Department() {}

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getDepartmentID() { return departmentID; }
    public void setDepartmentID(Integer departmentID) { this.departmentID = departmentID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
