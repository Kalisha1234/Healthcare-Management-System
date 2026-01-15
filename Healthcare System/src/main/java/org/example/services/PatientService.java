package org.example.services;

import org.example.dao.PatientDAO;
import org.example.models.Patient;
import org.example.utils.CacheManager;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PatientService {
    private static PatientService instance;
    private PatientDAO patientDAO;
    private CacheManager<Integer, Patient> cache;
    private static final String ALL_PATIENTS_KEY = "all_patients";

    private PatientService(Connection conn) {
        this.patientDAO = new PatientDAO(conn);
        this.cache = new CacheManager<>();
    }

    public static PatientService getInstance(Connection conn) {
        if (instance == null) {
            instance = new PatientService(conn);
        }
        return instance;
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
        
        // Invalidate cache after create
        //Create  Operations affect list of patients since there have been new additions
        // Caches must be invalidated since our cache is now not up to date
        cache.invalidateListCaches();
    }

    public Patient getPatient(Integer id) throws SQLException {
        // Check cache first
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        //if we found the item in the cache..we have increased performance by avoiding a DB call
        
        // Fetch from database and cache
        Patient patient = patientDAO.findById(id);
        if (patient != null) {
            cache.put(id, patient);
            //if the item was found in the DB we add it to the cache for future requests
        }
        return patient;
    }

    public List<Patient> getAllPatients() throws SQLException {
        long startTime = System.nanoTime();
        
        // Check cache first
        List<Patient> cached = cache.getList(ALL_PATIENTS_KEY);
        if (cached != null) {
            long duration = (System.nanoTime() - startTime) / 1_000_000;
            System.out.println("✓ Cache HIT - Patients loaded from cache (" + duration + "ms)");
            return cached;
        }
        
        // Fetch from database and cache
        List<Patient> patients = patientDAO.findAll();
        cache.putList(ALL_PATIENTS_KEY, patients);
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("✗ Cache MISS - Patients loaded from database (" + duration + "ms)");
        return patients;
    }

    public String getCacheStatus() {
        return cache.getCacheStatus();
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
        
        // Invalidate cache after update
        cache.remove(patient.getPatientID());
        cache.invalidateListCaches();
    }

    public void deletePatient(Integer id) throws SQLException {
        patientDAO.delete(id);
        
        // Invalidate cache after delete
        cache.remove(id);
        cache.invalidateListCaches();
    }
}
