package org.example.utils;

import org.example.models.User;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    public static boolean isReceptionist() {
        return currentUser != null && "RECEPTIONIST".equals(currentUser.getRole());
    }

    public static void logout() {
        currentUser = null;
    }
}
