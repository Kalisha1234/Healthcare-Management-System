package org.example.utils;

import org.example.models.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchUtil {
    
    public static List<Patient> searchPatients(List<Patient> patients, String query) {
        if (query == null || query.trim().isEmpty()) {
            return patients;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return patients.stream()
            .filter(p -> 
                p.getPatientID().toString().contains(lowerQuery) ||
                p.getFirstName().toLowerCase().contains(lowerQuery) ||
                p.getLastName().toLowerCase().contains(lowerQuery) ||
                p.getEmail().toLowerCase().contains(lowerQuery) ||
                p.getPhone().contains(lowerQuery)
            )
            .collect(Collectors.toList());
    }

    public static List<Doctor> searchDoctors(List<Doctor> doctors, String query) {
        if (query == null || query.trim().isEmpty()) {
            return doctors;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return doctors.stream()
            .filter(d -> 
                d.getDoctorID().toString().contains(lowerQuery) ||
                d.getFirstName().toLowerCase().contains(lowerQuery) ||
                d.getLastName().toLowerCase().contains(lowerQuery) ||
                d.getEmail().toLowerCase().contains(lowerQuery) ||
                d.getPhone().contains(lowerQuery)
            )
            .collect(Collectors.toList());
    }

    public static List<Department> searchDepartments(List<Department> departments, String query) {
        if (query == null || query.trim().isEmpty()) {
            return departments;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return departments.stream()
            .filter(d -> 
                d.getDepartmentID().toString().contains(lowerQuery) ||
                d.getName().toLowerCase().contains(lowerQuery) ||
                (d.getDescription() != null && d.getDescription().toLowerCase().contains(lowerQuery))
            )
            .collect(Collectors.toList());
    }

    public static List<Appointment> searchAppointments(List<Appointment> appointments, String query) {
        if (query == null || query.trim().isEmpty()) {
            return appointments;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return appointments.stream()
            .filter(a -> 
                a.getAppointmentID().toString().contains(lowerQuery) ||
                a.getPatientID().toString().contains(lowerQuery) ||
                a.getDoctorID().toString().contains(lowerQuery) ||
                a.getStatus().toLowerCase().contains(lowerQuery)
            )
            .collect(Collectors.toList());
    }
}
