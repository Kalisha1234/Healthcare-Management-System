package org.example.services;

import org.example.dao.DoctorDAO;
import org.example.models.Doctor;
import org.example.utils.CacheManager;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DoctorService {
    private static DoctorService instance;
    private DoctorDAO doctorDAO;
    private CacheManager<Integer, Doctor> cache;
    private static final String ALL_DOCTORS_KEY = "all_doctors";

    private DoctorService(Connection conn) {
        this.doctorDAO = new DoctorDAO(conn);
        this.cache = new CacheManager<>();
    }

    public static DoctorService getInstance(Connection conn) {
        if (instance == null) {
            instance = new DoctorService(conn);
        }
        return instance;
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
        cache.invalidateListCaches();
    }

    public Doctor getDoctor(Integer id) throws SQLException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        Doctor doctor = doctorDAO.findById(id);
        if (doctor != null) {
            cache.put(id, doctor);
        }
        return doctor;
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> cached = cache.getList(ALL_DOCTORS_KEY);
        if (cached != null) {
            System.out.println("✓ Cache HIT - Doctors loaded from cache");
            return cached;
        }
        System.out.println("✗ Cache MISS - Doctors loaded from database");
        List<Doctor> doctors = doctorDAO.findAll();
        cache.putList(ALL_DOCTORS_KEY, doctors);
        return doctors;
    }

    public String getCacheStatus() {
        return cache.getCacheStatus();
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
        cache.remove(doctor.getDoctorID());
        cache.invalidateListCaches();
    }

    public void deleteDoctor(Integer id) throws SQLException {
        doctorDAO.delete(id);
        cache.remove(id);
        cache.invalidateListCaches();
    }
}
