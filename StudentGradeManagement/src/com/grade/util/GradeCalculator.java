package com.grade.util;

/**
 * Utility class providing mathematical and grade calculation operations.
 */
public class GradeCalculator {

    /**
     * Calculates total marks across 5 subjects.
     */
    public static int calculateTotal(int m1, int m2, int m3, int m4, int m5) {
        return m1 + m2 + m3 + m4 + m5;
    }

    /**
     * Calculates average marks across 5 subjects.
     */
    public static double calculateAverage(int m1, int m2, int m3, int m4, int m5) {
        return calculateTotal(m1, m2, m3, m4, m5) / 5.0;
    }

    /**
     * Calculates letter grade based on average percentage.
     * Scale:
     * 90 - 100 : A+
     * 80 - 89  : A
     * 70 - 79  : B
     * 60 - 69  : C
     * 50 - 59  : D
     * Below 50 : F
     */
    public static String calculateGrade(double avg) {
        if (avg >= 90.0) return "A+";
        if (avg >= 80.0) return "A";
        if (avg >= 70.0) return "B";
        if (avg >= 60.0) return "C";
        if (avg >= 50.0) return "D";
        return "F";
    }

    /**
     * Calculates GPA on a 4.0 scale based on average percentage.
     */
    public static double calculateGPA(double avg) {
        if (avg >= 90.0) return 4.0;
        if (avg >= 85.0) return 3.8;
        if (avg >= 80.0) return 3.5;
        if (avg >= 75.0) return 3.2;
        if (avg >= 70.0) return 3.0;
        if (avg >= 65.0) return 2.7;
        if (avg >= 60.0) return 2.3;
        if (avg >= 55.0) return 2.0;
        if (avg >= 50.0) return 1.5;
        return 0.0;
    }

    /**
     * Determines pass/fail status.
     * Criteria: Average >= 50.0 AND all individual subject marks >= 40.
     */
    public static boolean isPass(int m1, int m2, int m3, int m4, int m5) {
        double avg = calculateAverage(m1, m2, m3, m4, m5);
        return avg >= 50.0 && m1 >= 40 && m2 >= 40 && m3 >= 40 && m4 >= 40 && m5 >= 40;
    }

    /**
     * Returns the highest score among the 5 subject marks.
     */
    public static int getHighestScore(int m1, int m2, int m3, int m4, int m5) {
        return Math.max(m1, Math.max(m2, Math.max(m3, Math.max(m4, m5))));
    }

    /**
     * Returns the subject name associated with the highest score.
     */
    public static String getHighestSubjectName(int m1, int m2, int m3, int m4, int m5) {
        int highest = getHighestScore(m1, m2, m3, m4, m5);
        if (highest == m1) return "Mathematics";
        if (highest == m2) return "Science";
        if (highest == m3) return "English";
        if (highest == m4) return "History";
        return "Computer Science";
    }

    /**
     * Returns the lowest score among the 5 subject marks.
     */
    public static int getLowestScore(int m1, int m2, int m3, int m4, int m5) {
        return Math.min(m1, Math.min(m2, Math.min(m3, Math.min(m4, m5))));
    }

    /**
     * Returns the subject name associated with the lowest score.
     */
    public static String getLowestSubjectName(int m1, int m2, int m3, int m4, int m5) {
        int lowest = getLowestScore(m1, m2, m3, m4, m5);
        if (lowest == m1) return "Mathematics";
        if (lowest == m2) return "Science";
        if (lowest == m3) return "English";
        if (lowest == m4) return "History";
        return "Computer Science";
    }

    /**
     * Validates if a mark is in the range 0 to 100.
     */
    public static boolean isValidMark(int mark) {
        return mark >= 0 && mark <= 100;
    }
}
