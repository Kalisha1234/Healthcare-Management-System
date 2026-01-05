package org.example.services;

import org.example.dao.PatientDAO;
import org.example.models.Patient;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PatientService {
    private PatientDAO patientDAO;

    public PatientService(Connection conn) {
        this.patientDAO = new PatientDAO(conn);
    }

    public void registerPatient(Patient patient) throws SQLException, ValidationException {
        // Sanitize inputs
        patient.setFirstName(Validator.sanitizeString(patient.getFirstName()));
        patient.setLastName(Validator.sanitizeString(patient.getLastName()));
        patient.setEmail(Validator.sanitizeString(patient.getEmail()));
        patient.setPhone(Validator.sanitizeString(patient.getPhone()));
        patient.setAddress(Validator.sanitizeString(patient.getAddress()));
        patient.setGender(Validator.sanitizeString(patient.getGender()));

        // Validate inputs
        Validator.validateName(patient.getFirstName(), "First Name");
        Validator.validateName(patient.getLastName(), "Last Name");
        Validator.validateEmail(patient.getEmail());
        Validator.validatePhone(patient.getPhone());
        Validator.validateDateOfBirth(patient.getDob());
        Validator.validateGender(patient.getGender());
        Validator.validateNotEmpty(patient.getAddress(), "Address");

        patientDAO.create(patient);
    }

    public Patient getPatient(Integer id) throws SQLException {
        return patientDAO.findById(id);
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.findAll();
    }

    public void updatePatient(Patient patient) throws SQLException, ValidationException {
        // Sanitize inputs
        patient.setFirstName(Validator.sanitizeString(patient.getFirstName()));
        patient.setLastName(Validator.sanitizeString(patient.getLastName()));
        patient.setEmail(Validator.sanitizeString(patient.getEmail()));
        patient.setPhone(Validator.sanitizeString(patient.getPhone()));
        patient.setAddress(Validator.sanitizeString(patient.getAddress()));
        patient.setGender(Validator.sanitizeString(patient.getGender()));

        // Validate inputs
        Validator.validateName(patient.getFirstName(), "First Name");
        Validator.validateName(patient.getLastName(), "Last Name");
        Validator.validateEmail(patient.getEmail());
        Validator.validatePhone(patient.getPhone());
        Validator.validateDateOfBirth(patient.getDob());
        Validator.validateGender(patient.getGender());
        Validator.validateNotEmpty(patient.getAddress(), "Address");

        patientDAO.update(patient);
    }

    public void deletePatient(Integer id) throws SQLException {
        patientDAO.delete(id);
    }
}
