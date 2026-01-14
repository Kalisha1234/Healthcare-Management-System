-- Healthcare Management System - SQL Scripts
-- PostgreSQL Database Schema

-- ============================================
-- 1. USERS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Users (
    UserID SERIAL PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(20) NOT NULL CHECK (Role IN ('ADMIN', 'RECEPTIONIST')),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. DEPARTMENTS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Departments (
    DepartmentID SERIAL PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. PATIENT TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Patient (
    PatientID SERIAL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DOB DATE NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Address TEXT NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 4. DOCTORS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Doctors (
    DoctorID SERIAL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DepartmentID INT NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    HireDate DATE NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID) ON DELETE CASCADE
);

-- ============================================
-- 5. APPOINTMENTS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Appointments (
    AppointmentID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentDate DATE NOT NULL,
    AppointmentTime TIME NOT NULL,
    Status VARCHAR(20) NOT NULL CHECK (Status IN ('Scheduled', 'Completed', 'Cancelled')),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID) ON DELETE CASCADE
);

-- ============================================
-- 6. PRESCRIPTIONS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS Prescriptions (
    PrescriptionID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    PrescriptionDate DATE NOT NULL,
    Notes TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID) ON DELETE CASCADE
);

-- ============================================
-- 7. PRESCRIPTION ITEMS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS PrescriptionItems (
    ItemID SERIAL PRIMARY KEY,
    PrescriptionID INT NOT NULL,
    MedicationName VARCHAR(100) NOT NULL,
    Dosage VARCHAR(50) NOT NULL,
    Frequency VARCHAR(50) NOT NULL,
    Duration VARCHAR(50) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PrescriptionID) REFERENCES Prescriptions(PrescriptionID) ON DELETE CASCADE
);

-- ============================================
-- 8. PATIENT FEEDBACK TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS PatientFeedback (
    FeedbackID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comments TEXT,
    FeedbackDate DATE NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID) ON DELETE CASCADE
);

-- ============================================
-- 9. MEDICAL INVENTORY TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS MedicalInventory (
    ItemID SERIAL PRIMARY KEY,
    ItemName VARCHAR(100) NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    ExpiryDate DATE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- INDEXES FOR PERFORMANCE
-- ============================================

-- Patient indexes
CREATE INDEX IF NOT EXISTS idx_patient_email ON Patient(Email);
CREATE INDEX IF NOT EXISTS idx_patient_name ON Patient(FirstName, LastName);
CREATE INDEX IF NOT EXISTS idx_patient_phone ON Patient(Phone);

-- Doctor indexes
CREATE INDEX IF NOT EXISTS idx_doctor_name ON Doctors(FirstName, LastName);
CREATE INDEX IF NOT EXISTS idx_doctor_email ON Doctors(Email);
CREATE INDEX IF NOT EXISTS idx_doctor_dept ON Doctors(DepartmentID);

-- Appointment indexes
CREATE INDEX IF NOT EXISTS idx_appointment_patient ON Appointments(PatientID);
CREATE INDEX IF NOT EXISTS idx_appointment_doctor ON Appointments(DoctorID);
CREATE INDEX IF NOT EXISTS idx_appointment_date ON Appointments(AppointmentDate);
CREATE INDEX IF NOT EXISTS idx_appointment_status ON Appointments(Status);

-- Department indexes
CREATE INDEX IF NOT EXISTS idx_department_name ON Departments(Name);

-- Prescription indexes
CREATE INDEX IF NOT EXISTS idx_prescription_patient ON Prescriptions(PatientID);
CREATE INDEX IF NOT EXISTS idx_prescription_doctor ON Prescriptions(DoctorID);

-- ============================================
-- SAMPLE DATA
-- ============================================

-- Insert default admin user
INSERT INTO Users (Username, Password, Role) 
VALUES ('admin', 'admin123', 'ADMIN')
ON CONFLICT (Username) DO NOTHING;

-- Insert sample departments
INSERT INTO Departments (Name, Description) VALUES
('Cardiology', 'Heart and cardiovascular care'),
('Neurology', 'Brain and nervous system'),
('Pediatrics', 'Children''s healthcare'),
('Orthopedics', 'Bone and joint care'),
('Dermatology', 'Skin conditions'),
('Oncology', 'Cancer treatment'),
('Radiology', 'Medical imaging'),
('Emergency Medicine', 'Emergency care'),
('Psychiatry', 'Mental health'),
('General Surgery', 'Surgical procedures');

-- Insert sample patients
INSERT INTO Patient (FirstName, LastName, DOB, Gender, Email, Phone, Address) VALUES
('John', 'Smith', '1990-05-15', 'Male', 'john.smith@email.com', '1234567890', '123 Main St'),
('Jane', 'Doe', '1985-08-22', 'Female', 'jane.doe@email.com', '1234567891', '456 Oak Ave'),
('Michael', 'Johnson', '1978-03-10', 'Male', 'michael.j@email.com', '1234567892', '789 Pine Rd');

-- Insert sample doctors
INSERT INTO Doctors (FirstName, LastName, DepartmentID, Phone, Email, HireDate) VALUES
('Sarah', 'Anderson', 1, '9000000001', 'dr.sarah.anderson@hospital.com', '2015-01-15'),
('Robert', 'Martinez', 2, '9000000002', 'dr.robert.martinez@hospital.com', '2016-03-20'),
('Lisa', 'Taylor', 3, '9000000003', 'dr.lisa.taylor@hospital.com', '2017-06-10');

-- Insert sample appointments
INSERT INTO Appointments (PatientID, DoctorID, AppointmentDate, AppointmentTime, Status) VALUES
(1, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', 'Scheduled'),
(2, 2, CURRENT_DATE + INTERVAL '2 days', '14:30:00', 'Scheduled'),
(3, 3, CURRENT_DATE + INTERVAL '3 days', '09:00:00', 'Scheduled');

-- ============================================
-- MONGODB COLLECTIONS (Document Structure)
-- ============================================

-- Collection: patient_notes
-- {
--   "_id": ObjectId,
--   "patientID": Integer,
--   "doctorID": Integer,
--   "note": String,
--   "type": String (Consultation, Diagnosis, Treatment, Follow-up, General),
--   "timestamp": String (ISO DateTime)
-- }

-- Collection: medical_logs
-- {
--   "_id": ObjectId,
--   "patientID": Integer,
--   "action": String,
--   "details": String,
--   "performedBy": String,
--   "timestamp": String (ISO DateTime)
-- }

-- ============================================
-- CLEANUP SCRIPTS (Use with caution)
-- ============================================

-- Drop all tables (cascades to dependent tables)
-- DROP TABLE IF EXISTS MedicalInventory CASCADE;
-- DROP TABLE IF EXISTS PatientFeedback CASCADE;
-- DROP TABLE IF EXISTS PrescriptionItems CASCADE;
-- DROP TABLE IF EXISTS Prescriptions CASCADE;
-- DROP TABLE IF EXISTS Appointments CASCADE;
-- DROP TABLE IF EXISTS Doctors CASCADE;
-- DROP TABLE IF EXISTS Patient CASCADE;
-- DROP TABLE IF EXISTS Departments CASCADE;
-- DROP TABLE IF EXISTS Users CASCADE;

-- ============================================
-- USEFUL QUERIES
-- ============================================

-- Get all patients with their appointment count
SELECT p.PatientID, p.FirstName, p.LastName, COUNT(a.AppointmentID) as AppointmentCount
FROM Patient p
LEFT JOIN Appointments a ON p.PatientID = a.PatientID
GROUP BY p.PatientID, p.FirstName, p.LastName;

-- Get doctors with their department names
SELECT d.DoctorID, d.FirstName, d.LastName, dept.Name as Department
FROM Doctors d
JOIN Departments dept ON d.DepartmentID = dept.DepartmentID;

-- Get upcoming appointments
SELECT a.AppointmentID, p.FirstName || ' ' || p.LastName as Patient,
       d.FirstName || ' ' || d.LastName as Doctor,
       a.AppointmentDate, a.AppointmentTime, a.Status
FROM Appointments a
JOIN Patient p ON a.PatientID = p.PatientID
JOIN Doctors d ON a.DoctorID = d.DoctorID
WHERE a.AppointmentDate >= CURRENT_DATE
ORDER BY a.AppointmentDate, a.AppointmentTime;
