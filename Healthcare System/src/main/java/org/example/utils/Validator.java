package org.example.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s'-]+$");

    public static String sanitizeString(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("\\s+", " ");
    }

    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        validateNotEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validatePhone(String phone) throws ValidationException {
        validateNotEmpty(phone, "Phone");
        String cleanPhone = phone.replaceAll("[\\s()-]", "");
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw new ValidationException("Phone must be 10-15 digits");
        }
    }

    public static void validateName(String name, String fieldName) throws ValidationException {
        validateNotEmpty(name, fieldName);
        if (name.length() < 2 || name.length() > 100) {
            throw new ValidationException(fieldName + " must be between 2 and 100 characters");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ValidationException(fieldName + " must contain only letters, spaces, hyphens, or apostrophes (no numbers or special characters)");
        }
    }

    public static void validateDate(LocalDate date, String fieldName) throws ValidationException {
        if (date == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    public static void validateDateOfBirth(LocalDate dob) throws ValidationException {
        validateDate(dob, "Date of Birth");
        if (dob.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of Birth cannot be in the future");
        }
        if (dob.isBefore(LocalDate.now().minusYears(150))) {
            throw new ValidationException("Date of Birth is too far in the past");
        }
    }

    public static void validateGender(String gender) throws ValidationException {
        validateNotEmpty(gender, "Gender");
        String normalized = gender.toLowerCase();
        if (!normalized.equals("male") && !normalized.equals("female") && !normalized.equals("other")) {
            throw new ValidationException("Gender must be Male, Female, or Other");
        }
    }

    public static void validateAppointmentStatus(String status) throws ValidationException {
        validateNotEmpty(status, "Status");
        if (!status.equals("Scheduled") && !status.equals("Completed") && !status.equals("Cancelled")) {
            throw new ValidationException("Status must be Scheduled, Completed, or Cancelled");
        }
    }

    public static void validatePositiveInteger(Integer value, String fieldName) throws ValidationException {
        if (value == null || value <= 0) {
            throw new ValidationException(fieldName + " must be a positive number");
        }
    }

    public static void validateFutureDate(LocalDate date, String fieldName) throws ValidationException {
        validateDate(date, fieldName);
        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " must be in the future");
        }
    }
}
