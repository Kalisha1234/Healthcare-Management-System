# Healthcare Management System

A comprehensive healthcare management system built with JavaFX, PostgreSQL, and MongoDB for managing patients, doctors, departments, appointments, and medical records.

## Features

- **Patient Management**: Register, update, delete, and search patients
- **Doctor Management**: Manage doctor profiles and department assignments
- **Department Management**: Organize hospital departments
- **Appointment Scheduling**: Schedule and manage patient appointments
- **User Management**: Role-based access control (Admin/Receptionist)
- **Patient Notes**: Add and view medical notes (MongoDB)
- **Medical Logs**: Automatic audit trail (MongoDB)
- **Caching System**: In-memory caching for performance
- **Database Indexing**: Optimized search queries
- **Input Validation**: Comprehensive validation with regex patterns

## Technologies

- **Java 25**
- **JavaFX 21.0.1** - UI framework
- **PostgreSQL 42.7.8** - Relational database (Supabase)
- **MongoDB 4.11.1** - NoSQL database for logs/notes
- **Maven** - Build tool

## Prerequisites

- JDK 25 or higher
- Maven 3.6+
- PostgreSQL (Supabase account)
- MongoDB Atlas account

## Setup Instructions

### 1. Clone Repository
```bash
git clone <repository-url>
cd Healthcare-Management-System/Healthcare\ System
```

### 2. Configure Environment Variable
```bash
# Windows
setx DB_PASSWORD "your_supabase_password"

# Linux/Mac
export DB_PASSWORD="your_supabase_password"
```

### 3. Update Database Connections

Edit `src/main/java/org/example/config/DBConfig.java`:
```java
private static final String URL = "jdbc:postgresql://your-supabase-url:5432/postgres";
```

Edit `src/main/java/org/example/config/MongoDBConfig.java`:
```java
private static final String CONNECTION_STRING = "your-mongodb-connection-string";
```

### 4. Install Dependencies
```bash
mvn clean install
```

### 5. Run Application
```bash
mvn javafx:run
```

## Default Login
- **Username**: admin
- **Password**: admin123

## Database Schema

See `Hospital-Management.jpg` for ERD diagram.

### PostgreSQL Tables (3NF)
1. **Users** - System users
2. **Departments** - Hospital departments
3. **Patient** - Patient information
4. **Doctors** - Doctor profiles
5. **Appointments** - Appointment scheduling
6. **Prescriptions** - Prescription records
7. **PrescriptionItems** - Prescription line items
8. **PatientFeedback** - Patient feedback
9. **MedicalInventory** - Medical supplies

### MongoDB Collections
1. **patient_notes** - Medical notes
2. **medical_logs** - Audit trail

## Project Structure

```
Healthcare System/
├── src/main/java/org/example/
│   ├── config/          # Database configurations
│   ├── controllers/     # UI controllers
│   ├── dao/             # Data access objects
│   ├── db/              # Database initializers
│   ├── models/          # Entity models
│   ├── services/        # Business logic
│   └── utils/           # Utilities (cache, validation, search)
├── src/main/resources/
│   ├── fxml/            # JavaFX views
│   └── styles.css       # Styling
└── pom.xml              # Maven configuration
```

## Usage

### Patient Management
1. Navigate to "Patients"
2. Add/Update/Delete patients
3. Search by name, email, phone, or ID
4. Add notes: Select patient → "📝 Add Note"
5. View notes: Select patient → "📋 View Notes"

### Doctor Management
1. Navigate to "Doctors"
2. Select department
3. Add/Update/Delete doctors

### Appointments
1. Navigate to "Appointments"
2. Select patient and doctor
3. Choose date, time, status
4. Click "➕ Add Appointment"

## Validation Rules

- **Name**: Letters, spaces, hyphens, apostrophes (2-100 chars)
- **Email**: Valid email format
- **Phone**: 10-15 digits
- **Date of Birth**: Must be in past

## Data Seeding

Automatically seeds sample data:
- 10 Departments
- 50 Patients
- 15 Doctors
- 20 Appointments

To disable, comment out in `HealthcareApp.java`:
```java
// DataSeeder.seedData(connection);
```

## Architecture

4-Layer Architecture:
1. **Presentation** - JavaFX UI
2. **Business Logic** - Services (validation, caching)
3. **Data Access** - DAOs
4. **Database** - PostgreSQL + MongoDB

## Caching

- Singleton services share cache across views
- Cache invalidated on create/update/delete
- Console logs show cache HIT/MISS

## Performance

- Database indexes on frequently queried columns
- Connection pooling
- In-memory caching
- Prepared statements

## Troubleshooting

**Database Connection Issues:**
- Verify `DB_PASSWORD` environment variable
- Check Supabase/MongoDB connections

**Build Issues:**
- Run `mvn clean install`
- Verify Maven 3.6+ installed

## License

Educational purposes.

## Contributors

Bernice Adime Mawuena
