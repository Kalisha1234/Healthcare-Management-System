package org.example.services;

import org.example.dao.DepartmentDAO;
import org.example.models.Department;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DepartmentService {
    private DepartmentDAO departmentDAO;

    public DepartmentService(Connection conn) {
        this.departmentDAO = new DepartmentDAO(conn);
    }

    public void addDepartment(Department department) throws SQLException, ValidationException {
        // Sanitize inputs
        department.setName(Validator.sanitizeString(department.getName()));
        department.setDescription(Validator.sanitizeString(department.getDescription()));

        // Validate inputs
        Validator.validateNotEmpty(department.getName(), "Department Name");

        departmentDAO.create(department);
    }

    public Department getDepartment(Integer id) throws SQLException {
        return departmentDAO.findById(id);
    }

    public List<Department> getAllDepartments() throws SQLException {
        return departmentDAO.findAll();
    }

    public void updateDepartment(Department department) throws SQLException, ValidationException {
        // Sanitize inputs
        department.setName(Validator.sanitizeString(department.getName()));
        department.setDescription(Validator.sanitizeString(department.getDescription()));

        // Validate inputs
        Validator.validateNotEmpty(department.getName(), "Department Name");

        departmentDAO.update(department);
    }

    public void deleteDepartment(Integer id) throws SQLException {
        departmentDAO.delete(id);
    }
}
