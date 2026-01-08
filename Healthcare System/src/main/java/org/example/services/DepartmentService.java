package org.example.services;

import org.example.dao.DepartmentDAO;
import org.example.models.Department;
import org.example.utils.CacheManager;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DepartmentService {
    private static DepartmentService instance;
    private DepartmentDAO departmentDAO;
    private CacheManager<Integer, Department> cache;
    private static final String ALL_DEPARTMENTS_KEY = "all_departments";

    private DepartmentService(Connection conn) {
        this.departmentDAO = new DepartmentDAO(conn);
        this.cache = new CacheManager<>();
    }

    public static DepartmentService getInstance(Connection conn) {
        if (instance == null) {
            instance = new DepartmentService(conn);
        }
        return instance;
    }

    public void addDepartment(Department department) throws SQLException, ValidationException {
        // Sanitize inputs
        department.setName(Validator.sanitizeString(department.getName()));
        department.setDescription(Validator.sanitizeString(department.getDescription()));

        // Validate inputs
        Validator.validateNotEmpty(department.getName(), "Department Name");

        departmentDAO.create(department);
        cache.invalidateListCaches();
    }

    public Department getDepartment(Integer id) throws SQLException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        Department dept = departmentDAO.findById(id);
        if (dept != null) {
            cache.put(id, dept);
        }
        return dept;
    }

    public List<Department> getAllDepartments() throws SQLException {
        long startTime = System.nanoTime();
        
        List<Department> cached = cache.getList(ALL_DEPARTMENTS_KEY);
        if (cached != null) {
            long duration = (System.nanoTime() - startTime) / 1_000_000;
            System.out.println("✓ Cache HIT - Departments loaded from cache (" + duration + "ms)");
            return cached;
        }
        
        List<Department> departments = departmentDAO.findAll();
        cache.putList(ALL_DEPARTMENTS_KEY, departments);
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("✗ Cache MISS - Departments loaded from database (" + duration + "ms)");
        return departments;
    }

    public String getCacheStatus() {
        return cache.getCacheStatus();
    }

    public void updateDepartment(Department department) throws SQLException, ValidationException {
        // Sanitize inputs
        department.setName(Validator.sanitizeString(department.getName()));
        department.setDescription(Validator.sanitizeString(department.getDescription()));

        // Validate inputs
        Validator.validateNotEmpty(department.getName(), "Department Name");

        departmentDAO.update(department);
        cache.remove(department.getDepartmentID());
        cache.invalidateListCaches();
    }

    public void deleteDepartment(Integer id) throws SQLException {
        departmentDAO.delete(id);
        cache.remove(id);
        cache.invalidateListCaches();
    }
}
