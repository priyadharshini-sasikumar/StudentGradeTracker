package com.grade.main;

import com.grade.service.StudentService;
import com.grade.ui.LoginFrame;

import javax.swing.*;

/**
 * Entry point for the Student Grade Management System Desktop Application.
 * Configures system rendering properties, sets Look & Feel, initializes
 * backend services, and launches the UI on the Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Enable high-DPI scaling and text antialiasing for crisp modern GUI rendering
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            // Set System Look & Feel for native OS frame accents
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system Look & Feel: " + e.getMessage());
        }

        // Launch application GUI safely on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            StudentService studentService = new StudentService();
            LoginFrame loginFrame = new LoginFrame(studentService);
            loginFrame.setVisible(true);
        });
    }
}
