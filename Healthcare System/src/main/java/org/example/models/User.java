package org.example.models;

import java.time.LocalDateTime;

public class User {
    private Integer userID;
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public User(String username, String password, String role, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isActive = true;
    }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
