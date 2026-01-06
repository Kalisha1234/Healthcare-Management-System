package org.example;

import org.example.config.DBConfig;
import org.example.db.DatabaseInitializer;
import org.example.models.*;
import org.example.services.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static Scanner scanner = new Scanner(System.in);
    private static DepartmentService departmentService;
    private static PatientService patientService;
    private static DoctorService doctorService;
    private static AppointmentService appointmentService;

    public static void main(String[] args) {
        DBConfig dbConfig = new DBConfig();

        try (Connection conn = dbConfig.connect()) {
            System.out.println("=== Healthcare Management System ===\n");
            DatabaseInitializer.initializeAll(conn);

            // Initialize services
            departmentService = new DepartmentService(conn);
            patientService = new PatientService(conn);
            doctorService = new DoctorService(conn);
            appointmentService = new AppointmentService(conn);

            // Main menu loop
            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getIntInput("Enter choice: ");

                switch (choice) {
                    case 1 -> manageDepartments();
                    case 2 -> managePatients();
                    case 3 -> manageDoctors();
                    case 4 -> manageAppointments();
                    case 0 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Departments");
        System.out.println("2. Manage Patients");
        System.out.println("3. Manage Doctors");
        System.out.println("4. Manage Appointments");
        System.out.println("0. Exit");
    }

    private static void manageDepartments() {
        System.out.println("\n--- Department Management ---");
        System.out.println("1. Add Department");
        System.out.println("2. View All Departments");
        System.out.println("3. Update Department");
        System.out.println("4. Delete Department");
        int choice = getIntInput("Enter choice: ");

        try {
            switch (choice) {
                case 1 -> addDepartment();
                case 2 -> viewAllDepartments();
                case 3 -> updateDepartment();
                case 4 -> deleteDepartment();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void addDepartment() throws Exception {
        System.out.println("\n--- Add Department ---");
        String name = getStringInput("Enter department name: ");
        String description = getStringInput("Enter description: ");

        Department dept = new Department(name, description);
        departmentService.addDepartment(dept);
        System.out.println("Department added successfully! ID: " + dept.getDepartmentID());
    }

    private static void viewAllDepartments() throws Exception {
        System.out.println("\n--- All Departments ---");
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            for (Department dept : departments) {
                System.out.printf("ID: %d | Name: %s | Description: %s%n",
                    dept.getDepartmentID(), dept.getName(), dept.getDescription());
            }
        }
    }

    private static void updateDepartment() throws Exception {
        int id = getIntInput("Enter department ID to update: ");
        Department dept = departmentService.getDepartment(id);
        if (dept == null) {
            System.out.println("Department not found!");
            return;
        }

        String name = getStringInput("Enter new name (current: " + dept.getName() + "): ");
        String description = getStringInput("Enter new description (current: " + dept.getDescription() + "): ");

        dept.setName(name);
        dept.setDescription(description);
        departmentService.updateDepartment(dept);
        System.out.println("Department updated successfully!");
    }

    private static void deleteDepartment() throws Exception {
        int id = getIntInput("Enter department ID to delete: ");
        departmentService.deleteDepartment(id);
        System.out.println("Department deleted successfully!");
    }

    private static void managePatients() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Register Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Update Patient");
        System.out.println("4. Delete Patient");
        int choice = getIntInput("Enter choice: ");

        try {
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> viewAllPatients();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void registerPatient() throws Exception {
        System.out.println("\n--- Register Patient ---");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        LocalDate dob = getDateInput("Enter date of birth (YYYY-MM-DD): ");
        String gender = getStringInput("Enter gender: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");
        String address = getStringInput("Enter address: ");

        Patient patient = new Patient(firstName, lastName, dob, gender, email, phone, address);
        patientService.registerPatient(patient);
        System.out.println("Patient registered successfully! ID: " + patient.getPatientID());
    }

    private static void viewAllPatients() throws Exception {
        System.out.println("\n--- All Patients ---");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (Patient p : patients) {
                System.out.printf("ID: %d | Name: %s %s | DOB: %s | Email: %s | Phone: %s%n",
                    p.getPatientID(), p.getFirstName(), p.getLastName(), p.getDob(), p.getEmail(), p.getPhone());
            }
        }
    }

    private static void updatePatient() throws Exception {
        int id = getIntInput("Enter patient ID to update: ");
        Patient patient = patientService.getPatient(id);
        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        String phone = getStringInput("Enter new phone (current: " + patient.getPhone() + "): ");
        String address = getStringInput("Enter new address (current: " + patient.getAddress() + "): ");

        patient.setPhone(phone);
        patient.setAddress(address);
        patientService.updatePatient(patient);
        System.out.println("Patient updated successfully!");
    }

    private static void deletePatient() throws Exception {
        int id = getIntInput("Enter patient ID to delete: ");
        patientService.deletePatient(id);
        System.out.println("Patient deleted successfully!");
    }

    private static void manageDoctors() {
        System.out.println("\n--- Doctor Management ---");
        System.out.println("1. Add Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. Update Doctor");
        System.out.println("4. Delete Doctor");
        int choice = getIntInput("Enter choice: ");

        try {
            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> viewAllDoctors();
                case 3 -> updateDoctor();
                case 4 -> deleteDoctor();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void addDoctor() throws Exception {
        System.out.println("\n--- Add Doctor ---");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        int departmentID = getIntInput("Enter department ID: ");
        String phone = getStringInput("Enter phone: ");
        String email = getStringInput("Enter email: ");
        LocalDate hireDate = getDateInput("Enter hire date (YYYY-MM-DD): ");

        Doctor doctor = new Doctor(firstName, lastName, departmentID, phone, email, hireDate);
        doctorService.addDoctor(doctor);
        System.out.println("Doctor added successfully! ID: " + doctor.getDoctorID());
    }

    private static void viewAllDoctors() throws Exception {
        System.out.println("\n--- All Doctors ---");
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            for (Doctor d : doctors) {
                System.out.printf("ID: %d | Name: Dr. %s %s | Dept ID: %d | Email: %s | Phone: %s%n",
                    d.getDoctorID(), d.getFirstName(), d.getLastName(), d.getDepartmentID(), d.getEmail(), d.getPhone());
            }
        }
    }

    private static void updateDoctor() throws Exception {
        int id = getIntInput("Enter doctor ID to update: ");
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor == null) {
            System.out.println("Doctor not found!");
            return;
        }

        String phone = getStringInput("Enter new phone (current: " + doctor.getPhone() + "): ");
        String email = getStringInput("Enter new email (current: " + doctor.getEmail() + "): ");

        doctor.setPhone(phone);
        doctor.setEmail(email);
        doctorService.updateDoctor(doctor);
        System.out.println("Doctor updated successfully!");
    }

    private static void deleteDoctor() throws Exception {
        int id = getIntInput("Enter doctor ID to delete: ");
        doctorService.deleteDoctor(id);
        System.out.println("Doctor deleted successfully!");
    }

    private static void manageAppointments() {
        System.out.println("\n--- Appointment Management ---");
        System.out.println("1. Schedule Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. Cancel Appointment");
        System.out.println("4. Delete Appointment");
        int choice = getIntInput("Enter choice: ");

        try {
            switch (choice) {
                case 1 -> scheduleAppointment();
                case 2 -> viewAllAppointments();
                case 3 -> cancelAppointment();
                case 4 -> deleteAppointment();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void scheduleAppointment() throws Exception {
        System.out.println("\n--- Schedule Appointment ---");
        int patientID = getIntInput("Enter patient ID: ");
        int doctorID = getIntInput("Enter doctor ID: ");
        LocalDate date = getDateInput("Enter appointment date (YYYY-MM-DD): ");
        LocalTime time = getTimeInput("Enter appointment time (HH:MM): ");

        Appointment appointment = new Appointment(patientID, doctorID, date, time, "Scheduled");
        appointmentService.scheduleAppointment(appointment);
        System.out.println("Appointment scheduled successfully! ID: " + appointment.getAppointmentID());
    }

    private static void viewAllAppointments() throws Exception {
        System.out.println("\n--- All Appointments ---");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment a : appointments) {
                System.out.printf("ID: %d | Patient ID: %d | Doctor ID: %d | Date: %s | Time: %s | Status: %s%n",
                    a.getAppointmentID(), a.getPatientID(), a.getDoctorID(), 
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus());
            }
        }
    }

    private static void cancelAppointment() throws Exception {
        int id = getIntInput("Enter appointment ID to cancel: ");
        appointmentService.cancelAppointment(id);
        System.out.println("Appointment cancelled successfully!");
    }

    private static void deleteAppointment() throws Exception {
        int id = getIntInput("Enter appointment ID to delete: ");
        appointmentService.deleteAppointment(id);
        System.out.println("Appointment deleted successfully!");
    }

    // Helper methods
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }

    private static LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine().trim());
    }

    private static LocalTime getTimeInput(String prompt) {
        System.out.print(prompt);
        return LocalTime.parse(scanner.nextLine().trim());
    }
}
