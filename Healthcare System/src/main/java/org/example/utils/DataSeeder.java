package org.example.utils;

import org.example.models.*;
import org.example.services.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataSeeder {
    
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "Robert", "Lisa", "James", "Mary", "William", "Patricia", "Richard", "Jennifer", "Thomas", "Linda", "Charles", "Barbara", "Daniel", "Elizabeth", "Matthew", "Susan", "Joseph", "Jessica", "Christopher", "Karen", "Andrew", "Nancy", "Ryan", "Betty", "Joshua", "Margaret", "Kevin", "Sandra", "Brian", "Ashley", "George", "Kimberly", "Edward", "Donna", "Ronald", "Carol", "Timothy", "Michelle", "Jason", "Amanda", "Jeffrey", "Melissa", "Frank", "Deborah"};
    
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Thompson", "White", "Harris", "Clark", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts", "Gomez", "Phillips", "Evans"};
    
    public static void seedData(Connection conn) {
        try {
            DepartmentService deptService = DepartmentService.getInstance(conn);
            PatientService patientService = PatientService.getInstance(conn);
            DoctorService doctorService = DoctorService.getInstance(conn);
            AppointmentService appointmentService = AppointmentService.getInstance(conn);
            
            System.out.println("Seeding database with sample data...");
            
            // Seed 10 Departments
            String[] deptNames = {"Cardiology", "Neurology", "Pediatrics", "Orthopedics", "Dermatology", "Oncology", "Radiology", "Emergency Medicine", "Psychiatry", "General Surgery"};
            String[] deptDescs = {"Heart and cardiovascular care", "Brain and nervous system", "Children's healthcare", "Bone and joint care", "Skin conditions", "Cancer treatment", "Medical imaging", "Emergency care", "Mental health", "Surgical procedures"};
            
            List<Integer> deptIds = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Department dept = new Department(deptNames[i], deptDescs[i]);
                deptService.addDepartment(dept);
                deptIds.add(dept.getDepartmentID());
                System.out.println("Created Department ID: " + dept.getDepartmentID() + " - " + dept.getName());
            }
            
            // Seed 50 Patients
            List<Integer> patientIds = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                String firstName = FIRST_NAMES[i % FIRST_NAMES.length];
                String lastName = LAST_NAMES[i % LAST_NAMES.length];
                LocalDate dob = LocalDate.of(1950 + (i % 50), (i % 12) + 1, (i % 28) + 1);
                String gender = i % 2 == 0 ? "Male" : "Female";
                String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@email.com";
                String phone = String.format("%010d", 1000000000L + i);
                String address = (100 + i) + " Main St, City " + (i % 10);
                
                Patient patient = new Patient(firstName, lastName, dob, gender, email, phone, address);
                patientService.registerPatient(patient);
                patientIds.add(patient.getPatientID());
            }
            
            // Seed 15 Doctors
            String[] docFirstNames = {"Sarah", "Robert", "Lisa", "James", "Maria", "David", "Jennifer", "Michael", "Patricia", "William", "Linda", "Richard", "Barbara", "Thomas", "Susan"};
            String[] docLastNames = {"Anderson", "Martinez", "Taylor", "White", "Garcia", "Brown", "Wilson", "Moore", "Jackson", "Thomas", "Lee", "Harris", "Clark", "Lewis", "Robinson"};
            
            List<Integer> doctorIds = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                String firstName = docFirstNames[i];
                String lastName = docLastNames[i];
                int deptId = deptIds.get(i % 10);
                String phone = String.format("%010d", 9000000000L + i);
                String email = "dr." + firstName.toLowerCase() + "." + lastName.toLowerCase() + "@hospital.com";
                LocalDate hireDate = LocalDate.of(2010 + (i % 13), (i % 12) + 1, (i % 28) + 1);
                
                Doctor doctor = new Doctor(firstName, lastName, deptId, phone, email, hireDate);
                doctorService.addDoctor(doctor);
                doctorIds.add(doctor.getDoctorID());
            }
            
            // Seed 20 Appointments
            for (int i = 0; i < 20; i++) {
                int patientId = patientIds.get(i % 50);
                int doctorId = doctorIds.get(i % 15);
                LocalDate apptDate = LocalDate.now().plusDays(i + 1);
                LocalTime apptTime = LocalTime.of(9 + (i % 8), (i % 4) * 15);
                String status = i % 3 == 0 ? "Completed" : "Scheduled";
                
                Appointment appointment = new Appointment(patientId, doctorId, apptDate, apptTime, status);
                appointmentService.scheduleAppointment(appointment);
            }
            
            System.out.println("✓ Database seeded successfully!");
            System.out.println("  - 10 Departments");
            System.out.println("  - 50 Patients");
            System.out.println("  - 15 Doctors");
            System.out.println("  - 20 Appointments");
            
        } catch (Exception e) {
            System.err.println("Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
