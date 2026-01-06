package org.example;

import org.example.config.DBConfig;
import org.example.db.DatabaseInitializer;
import org.example.models.*;
import org.example.services.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConsoleTest {
    public static void main(String[] args) {
        DBConfig dbConfig = new DBConfig();

        try (Connection conn = dbConfig.connect()) {
            System.out.println("=== Database Connection Established ===\n");
            
            // Initialize tables
            DatabaseInitializer.initializeAll(conn);
            System.out.println("=== Tables Initialized ===\n");

            // Initialize services
            DepartmentService departmentService = new DepartmentService(conn);
            PatientService patientService = new PatientService(conn);
            DoctorService doctorService = new DoctorService(conn);
            AppointmentService appointmentService = new AppointmentService(conn);

            // Test Department
            System.out.println("--- Testing Department Service ---");
            Department dept = new Department("Cardiology", "Heart and cardiovascular care");
            departmentService.addDepartment(dept);
            System.out.println("Created Department ID: " + dept.getDepartmentID());

            // Test Patient
            System.out.println("\n--- Testing Patient Service ---");
            Patient patient = new Patient("John", "Doe", LocalDate.of(1990, 5, 15), 
                "Male", "john.doe@email.com", "1234567890", "123 Main St");
            patientService.registerPatient(patient);
            System.out.println("Registered Patient ID: " + patient.getPatientID());

            // Test Doctor
            System.out.println("\n--- Testing Doctor Service ---");
            Doctor doctor = new Doctor("Jane", "Smith", dept.getDepartmentID(), 
                "0987654321", "jane.smith@hospital.com", LocalDate.of(2015, 3, 10));
            doctorService.addDoctor(doctor);
            System.out.println("Added Doctor ID: " + doctor.getDoctorID());

            // Test Appointment
            System.out.println("\n--- Testing Appointment Service ---");
            Appointment appointment = new Appointment(patient.getPatientID(), 
                doctor.getDoctorID(), LocalDate.now().plusDays(7), 
                LocalTime.of(10, 30), "Scheduled");
            appointmentService.scheduleAppointment(appointment);
            System.out.println("Scheduled Appointment ID: " + appointment.getAppointmentID());

            // Retrieve and display
            System.out.println("\n--- Retrieving Data ---");
            System.out.println("Patients: " + patientService.getAllPatients().size());
            System.out.println("Doctors: " + doctorService.getAllDoctors().size());
            System.out.println("Departments: " + departmentService.getAllDepartments().size());
            System.out.println("Appointments: " + appointmentService.getAllAppointments().size());

            System.out.println("\n=== All Tests Passed Successfully ===");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
