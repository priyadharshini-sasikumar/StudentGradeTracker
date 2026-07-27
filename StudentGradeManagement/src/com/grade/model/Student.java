package com.grade.model;

import com.grade.util.GradeCalculator;
import java.io.Serializable;

/**
 * Model class representing a Student in the Grade Management System.
 * Fully encapsulated with private fields, constructors, getters, setters,
 * and calculated metrics for grades, GPA, total, and pass/fail status.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String name;
    private String department;
    private String year;
    private String section;

    // Marks for 5 Subjects (0 - 100)
    private int mathMarks;
    private int scienceMarks;
    private int englishMarks;
    private int historyMarks;
    private int computerMarks;

    /**
     * Default constructor.
     */
    public Student() {
    }

    /**
     * Full constructor.
     */
    public Student(String studentId, String name, String department, String year, String section,
                   int mathMarks, int scienceMarks, int englishMarks, int historyMarks, int computerMarks) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.year = year;
        this.section = section;
        this.mathMarks = mathMarks;
        this.scienceMarks = scienceMarks;
        this.englishMarks = englishMarks;
        this.historyMarks = historyMarks;
        this.computerMarks = computerMarks;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getMathMarks() {
        return mathMarks;
    }

    public void setMathMarks(int mathMarks) {
        this.mathMarks = mathMarks;
    }

    public int getScienceMarks() {
        return scienceMarks;
    }

    public void setScienceMarks(int scienceMarks) {
        this.scienceMarks = scienceMarks;
    }

    public int getEnglishMarks() {
        return englishMarks;
    }

    public void setEnglishMarks(int englishMarks) {
        this.englishMarks = englishMarks;
    }

    public int getHistoryMarks() {
        return historyMarks;
    }

    public void setHistoryMarks(int historyMarks) {
        this.historyMarks = historyMarks;
    }

    public int getComputerMarks() {
        return computerMarks;
    }

    public void setComputerMarks(int computerMarks) {
        this.computerMarks = computerMarks;
    }

    // Dynamic Calculations via GradeCalculator

    public int getTotalMarks() {
        return GradeCalculator.calculateTotal(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public double getAverageMarks() {
        return GradeCalculator.calculateAverage(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public String getLetterGrade() {
        return GradeCalculator.calculateGrade(getAverageMarks());
    }

    public double getGpa() {
        return GradeCalculator.calculateGPA(getAverageMarks());
    }

    public boolean isPass() {
        return GradeCalculator.isPass(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public String getStatus() {
        return isPass() ? "PASS" : "FAIL";
    }

    public int getHighestSubjectScore() {
        return GradeCalculator.getHighestScore(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public String getHighestSubjectName() {
        return GradeCalculator.getHighestSubjectName(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public int getLowestSubjectScore() {
        return GradeCalculator.getLowestScore(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    public String getLowestSubjectName() {
        return GradeCalculator.getLowestSubjectName(mathMarks, scienceMarks, englishMarks, historyMarks, computerMarks);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", year='" + year + '\'' +
                ", section='" + section + '\'' +
                ", avg=" + String.format("%.2f", getAverageMarks()) +
                ", grade='" + getLetterGrade() + '\'' +
                '}';
    }
}
