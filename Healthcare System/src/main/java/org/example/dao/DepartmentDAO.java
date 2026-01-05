package org.example.dao;

import org.example.models.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private Connection conn;

    public DepartmentDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Department department) throws SQLException {
        String sql = "INSERT INTO Departments (Name, Description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                department.setDepartmentID(rs.getInt(1));
            }
        }
    }

    public Department findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Departments WHERE DepartmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public List<Department> findAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM Departments";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(mapResultSet(rs));
            }
        }
        return departments;
    }

    public void update(Department department) throws SQLException {
        String sql = "UPDATE Departments SET Name=?, Description=?, UpdatedAt=CURRENT_TIMESTAMP WHERE DepartmentID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());
            stmt.setInt(3, department.getDepartmentID());
            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Departments WHERE DepartmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Department mapResultSet(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setName(rs.getString("Name"));
        department.setDescription(rs.getString("Description"));
        department.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        department.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return department;
    }
}
