# Database Documentation

## Overview

The Healthcare Management System uses a hybrid database approach:
- **PostgreSQL (Supabase)**: Relational data (patients, doctors, appointments)
- **MongoDB Atlas**: Unstructured data (notes, logs)

## Database Design Principles

### Third Normal Form (3NF)

All PostgreSQL tables are normalized to 3NF:

1. **First Normal Form (1NF)**
   - All columns contain atomic values
   - No repeating groups
   - Each row is unique (primary key)

2. **Second Normal Form (2NF)**
   - Meets 1NF requirements
   - No partial dependencies (all non-key attributes depend on entire primary key)

3. **Third Normal Form (3NF)**
   - Meets 2NF requirements
   - No transitive dependencies (non-key attributes don't depend on other non-key attributes)

## PostgreSQL Schema

### Entity Relationship Diagram

See `Hospital-Management.jpg` for visual representation.

### Tables

#### 1. Users
**Purpose**: System authentication and authorization

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| UserID | SERIAL | PRIMARY KEY | Auto-incrementing user ID |
| Username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| Password | VARCHAR(255) | NOT NULL | User password (plain text for demo) |
| Role | VARCHAR(20) | NOT NULL, CHECK | ADMIN or RECEPTIONIST |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**3NF Compliance**: ✓ No transitive dependencies

#### 2. Departments
**Purpose**: Hospital department organization

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| DepartmentID | SERIAL | PRIMARY KEY | Auto-incrementing department ID |
| Name | VARCHAR(100) | NOT NULL | Department name |
| Description | TEXT | | Department description |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**3NF Compliance**: ✓ No transitive dependencies

#### 3. Patient
**Purpose**: Patient demographic information

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| PatientID | SERIAL | PRIMARY KEY | Auto-incrementing patient ID |
| FirstName | VARCHAR(50) | NOT NULL | Patient first name |
| LastName | VARCHAR(50) | NOT NULL | Patient last name |
| DOB | DATE | NOT NULL | Date of birth |
| Gender | VARCHAR(10) | NOT NULL | Gender |
| Email | VARCHAR(100) | UNIQUE, NOT NULL | Email address |
| Phone | VARCHAR(15) | NOT NULL | Phone number |
| Address | TEXT | NOT NULL | Full address |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**3NF Compliance**: ✓ All attributes depend only on PatientID

#### 4. Dcotors (Doctors)
**Purpose**: Doctor profiles and assignments

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| DoctorID | SERIAL | PRIMARY KEY | Auto-incrementing doctor ID |
| FirstName | VARCHAR(50) | NOT NULL | Doctor first name |
| LastName | VARCHAR(50) | NOT NULL | Doctor last name |
| DepartmentID | INT | FOREIGN KEY | Reference to Departments |
| Phone | VARCHAR(15) | NOT NULL | Phone number |
| Email | VARCHAR(100) | UNIQUE, NOT NULL | Email address |
| HireDate | DATE | NOT NULL | Employment start date |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**Foreign Keys**:
- DepartmentID → Departments(DepartmentID) ON DELETE CASCADE

**3NF Compliance**: ✓ Department name not stored (retrieved via FK)

#### 5. Appointments
**Purpose**: Patient-doctor appointment scheduling

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| AppointmentID | SERIAL | PRIMARY KEY | Auto-incrementing appointment ID |
| PatientID | INT | FOREIGN KEY, NOT NULL | Reference to Patient |
| DoctorID | INT | FOREIGN KEY, NOT NULL | Reference to Dcotors |
| AppointmentDate | DATE | NOT NULL | Appointment date |
| AppointmentTime | TIME | NOT NULL | Appointment time |
| Status | VARCHAR(20) | NOT NULL, CHECK | Scheduled/Completed/Cancelled |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**Foreign Keys**:
- PatientID → Patient(PatientID) ON DELETE CASCADE
- DoctorID → Dcotors(DoctorID) ON DELETE CASCADE

**3NF Compliance**: ✓ Patient/Doctor names not stored (retrieved via FKs)

#### 6. Prescriptions
**Purpose**: Prescription records

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| PrescriptionID | SERIAL | PRIMARY KEY | Auto-incrementing prescription ID |
| PatientID | INT | FOREIGN KEY, NOT NULL | Reference to Patient |
| DoctorID | INT | FOREIGN KEY, NOT NULL | Reference to Dcotors |
| PrescriptionDate | DATE | NOT NULL | Prescription date |
| Notes | TEXT | | Additional notes |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**Foreign Keys**:
- PatientID → Patient(PatientID) ON DELETE CASCADE
- DoctorID → Dcotors(DoctorID) ON DELETE CASCADE

**3NF Compliance**: ✓ Medication details in separate table

#### 7. PrescriptionItems
**Purpose**: Individual medications in prescriptions

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| ItemID | SERIAL | PRIMARY KEY | Auto-incrementing item ID |
| PrescriptionID | INT | FOREIGN KEY, NOT NULL | Reference to Prescriptions |
| MedicationName | VARCHAR(100) | NOT NULL | Medication name |
| Dosage | VARCHAR(50) | NOT NULL | Dosage amount |
| Frequency | VARCHAR(50) | NOT NULL | How often to take |
| Duration | VARCHAR(50) | NOT NULL | How long to take |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |

**Foreign Keys**:
- PrescriptionID → Prescriptions(PrescriptionID) ON DELETE CASCADE

**3NF Compliance**: ✓ Separates prescription header from line items

#### 8. PatientFeedback
**Purpose**: Patient satisfaction ratings

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| FeedbackID | SERIAL | PRIMARY KEY | Auto-incrementing feedback ID |
| PatientID | INT | FOREIGN KEY, NOT NULL | Reference to Patient |
| DoctorID | INT | FOREIGN KEY, NOT NULL | Reference to Dcotors |
| Rating | INT | CHECK (1-5) | Rating 1-5 stars |
| Comments | TEXT | | Feedback comments |
| FeedbackDate | DATE | NOT NULL | Feedback date |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |

**Foreign Keys**:
- PatientID → Patient(PatientID) ON DELETE CASCADE
- DoctorID → Dcotors(DoctorID) ON DELETE CASCADE

**3NF Compliance**: ✓ No transitive dependencies

#### 9. MedicalInventory
**Purpose**: Medical supplies tracking

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| ItemID | SERIAL | PRIMARY KEY | Auto-incrementing item ID |
| ItemName | VARCHAR(100) | NOT NULL | Item name |
| Category | VARCHAR(50) | NOT NULL | Item category |
| Quantity | INT | NOT NULL | Stock quantity |
| UnitPrice | DECIMAL(10,2) | NOT NULL | Price per unit |
| ExpiryDate | DATE | | Expiration date |
| CreatedAt | TIMESTAMP | DEFAULT NOW | Record creation time |
| UpdatedAt | TIMESTAMP | DEFAULT NOW | Last update time |

**3NF Compliance**: ✓ No transitive dependencies

## Indexes

Performance optimization through strategic indexing:

### Patient Indexes
- `idx_patient_email`: Fast email lookups
- `idx_patient_name`: Fast name searches (composite)
- `idx_patient_phone`: Fast phone searches

### Doctor Indexes
- `idx_doctor_name`: Fast name searches (composite)
- `idx_doctor_email`: Fast email lookups
- `idx_doctor_dept`: Fast department filtering

### Appointment Indexes
- `idx_appointment_patient`: Fast patient appointment lookups
- `idx_appointment_doctor`: Fast doctor schedule lookups
- `idx_appointment_date`: Fast date range queries
- `idx_appointment_status`: Fast status filtering

### Department Indexes
- `idx_department_name`: Fast department name searches

### Prescription Indexes
- `idx_prescription_patient`: Fast patient prescription history
- `idx_prescription_doctor`: Fast doctor prescription history

## MongoDB Schema

### Collection: patient_notes

**Purpose**: Medical notes added by healthcare staff

**Document Structure**:
```json
{
  "_id": ObjectId("..."),
  "patientID": 123,
  "doctorID": 45,
  "note": "Patient shows improvement...",
  "type": "Consultation",
  "timestamp": "2026-01-08T14:30:00"
}
```

**Fields**:
- `_id`: MongoDB auto-generated ID
- `patientID`: Reference to PostgreSQL Patient.PatientID
- `doctorID`: Reference to PostgreSQL Dcotors.DoctorID
- `note`: Free-form text note
- `type`: Consultation, Diagnosis, Treatment, Follow-up, General
- `timestamp`: ISO 8601 datetime string

**Why NoSQL**: Flexible schema for varying note structures, high write volume

### Collection: medical_logs

**Purpose**: Audit trail of all system operations

**Document Structure**:
```json
{
  "_id": ObjectId("..."),
  "patientID": 123,
  "action": "Patient Registered",
  "details": "New patient: John Doe",
  "performedBy": "admin",
  "timestamp": "2026-01-08T14:30:00"
}
```

**Fields**:
- `_id`: MongoDB auto-generated ID
- `patientID`: Reference to PostgreSQL Patient.PatientID
- `action`: Action performed (e.g., "Patient Registered")
- `details`: Additional details about the action
- `performedBy`: Username who performed action
- `timestamp`: ISO 8601 datetime string

**Why NoSQL**: Append-only logs, no updates needed, flexible structure

## Relationships

### One-to-Many
- Departments → Doctors (1:N)
- Patient → Appointments (1:N)
- Doctor → Appointments (1:N)
- Patient → Prescriptions (1:N)
- Doctor → Prescriptions (1:N)
- Prescriptions → PrescriptionItems (1:N)

### Referential Integrity
- All foreign keys use `ON DELETE CASCADE`
- Ensures orphaned records are automatically removed
- Maintains data consistency

## Data Integrity Constraints

### Check Constraints
- `Users.Role`: Must be 'ADMIN' or 'RECEPTIONIST'
- `Appointments.Status`: Must be 'Scheduled', 'Completed', or 'Cancelled'
- `PatientFeedback.Rating`: Must be between 1 and 5

### Unique Constraints
- `Users.Username`: Prevents duplicate usernames
- `Patient.Email`: Prevents duplicate patient emails
- `Dcotors.Email`: Prevents duplicate doctor emails

### Not Null Constraints
- All critical fields marked NOT NULL
- Ensures data completeness

## Backup and Recovery

### PostgreSQL (Supabase)
- Automatic daily backups
- Point-in-time recovery available
- Export via pg_dump

### MongoDB (Atlas)
- Automatic continuous backups
- Point-in-time recovery available
- Export via mongodump

## Security Considerations

### Current Implementation
- Plain text passwords (demo only)
- Environment variable for DB password
- Prepared statements prevent SQL injection

### Production Recommendations
- Hash passwords with bcrypt
- Use connection pooling
- Implement row-level security
- Enable SSL/TLS connections
- Regular security audits

## Performance Considerations

### Caching Strategy
- In-memory cache for frequently accessed data
- Cache invalidation on write operations
- Singleton services share cache

### Query Optimization
- Indexes on frequently queried columns
- Prepared statements for parameterized queries
- Batch operations where possible

### Connection Management
- Single connection reused across application
- Connection closed on application exit
- Connection pooling recommended for production

## Future Enhancements

- Add full-text search indexes
- Implement database partitioning for large tables
- Add materialized views for reporting
- Implement database replication
- Add database monitoring and alerting
