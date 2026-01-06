package org.example.services;

import org.example.dao.DoctorDAO;
import org.example.models.Doctor;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DoctorService {
    private DoctorDAO doctorDAO;

    public DoctorService(Connection conn) {
        this.doctorDAO = new DoctorDAO(conn);
    }

    public void addDoctor(Doctor doctor) throws SQLException, ValidationException {
        // Sanitize inputs
        doctor.setFirstName(Validator.sanitizeString(doctor.getFirstName()));
        doctor.setLastName(Validator.sanitizeString(doctor.getLastName()));
        doctor.setEmail(Validator.sanitizeString(doctor.getEmail()));
        doctor.setPhone(Validator.sanitizeString(doctor.getPhone()));

        // Validate inputs
        Validator.validateName(doctor.getFirstName(), "First Name");
        Validator.validateName(doctor.getLastName(), "Last Name");
        Validator.validateEmail(doctor.getEmail());
        Validator.validatePhone(doctor.getPhone());
        Validator.validatePositiveInteger(doctor.getDepartmentID(), "Department ID");
        Validator.validateDate(doctor.getHireDate(), "Hire Date");

        doctorDAO.create(doctor);
    }

    public Doctor getDoctor(Integer id) throws SQLException {
        return doctorDAO.findById(id);
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDAO.findAll();
    }

    public void updateDoctor(Doctor doctor) throws SQLException, ValidationException {
        // Sanitize inputs
        doctor.setFirstName(Validator.sanitizeString(doctor.getFirstName()));
        doctor.setLastName(Validator.sanitizeString(doctor.getLastName()));
        doctor.setEmail(Validator.sanitizeString(doctor.getEmail()));
        doctor.setPhone(Validator.sanitizeString(doctor.getPhone()));

        // Validate inputs
        Validator.validateName(doctor.getFirstName(), "First Name");
        Validator.validateName(doctor.getLastName(), "Last Name");
        Validator.validateEmail(doctor.getEmail());
        Validator.validatePhone(doctor.getPhone());
        Validator.validatePositiveInteger(doctor.getDepartmentID(), "Department ID");
        Validator.validateDate(doctor.getHireDate(), "Hire Date");

        doctorDAO.update(doctor);
    }

    public void deleteDoctor(Integer id) throws SQLException {
        doctorDAO.delete(id);
    }
}
