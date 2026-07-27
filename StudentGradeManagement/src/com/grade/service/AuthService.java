package com.grade.service;

/**
 * Service providing user authentication with default credentials.
 */
public class AuthService {

    private static String adminUsername = "Priyadharshini";
    private static String adminPassword = "admin123";

    /**
     * Authenticates username and password against credentials.
     */
    public static boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        String userTrim = username.trim();
        // Allow either 'Priyadharshini' or 'admin' for convenience
        return (adminUsername.equalsIgnoreCase(userTrim) || "admin".equalsIgnoreCase(userTrim)) && adminPassword.equals(password);
    }

    /**
     * Updates the password if current password matches.
     */
    public static boolean changePassword(String currentPass, String newPass) {
        if (adminPassword.equals(currentPass) && newPass != null && newPass.length() >= 4) {
            adminPassword = newPass;
            return true;
        }
        return false;
    }
}
