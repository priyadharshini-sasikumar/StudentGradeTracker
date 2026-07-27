package com.grade.service;

import com.grade.model.Student;
import com.grade.util.FileUtil;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class handling all in-memory business logic operations, searching,
 * filtering, sorting, statistics calculation, and automatic file persistence.
 */
public class StudentService {

    private final List<Student> students;

    public StudentService() {
        this.students = FileUtil.loadStudents();
    }

    /**
     * Returns an unmodifiable list of all student records.
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Adds a new student record. Returns false if student ID already exists.
     */
    public boolean addStudent(Student student) {
        if (student == null || student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }
        if (findStudentById(student.getStudentId()) != null) {
            return false; // Duplicate ID
        }
        students.add(student);
        saveChanges();
        return true;
    }

    /**
     * Updates an existing student record based on studentId.
     */
    public boolean updateStudent(Student updatedStudent) {
        if (updatedStudent == null || updatedStudent.getStudentId() == null) {
            return false;
        }
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equalsIgnoreCase(updatedStudent.getStudentId())) {
                students.set(i, updatedStudent);
                saveChanges();
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(String studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId().equalsIgnoreCase(studentId));
        if (removed) {
            saveChanges();
        }
        return removed;
    }

    /**
     * Finds a single student by ID.
     */
    public Student findStudentById(String studentId) {
        if (studentId == null) return null;
        return students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(studentId.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches students matching ID or Name (case-insensitive partial match).
     */
    public List<Student> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(students);
        }
        String q = query.trim().toLowerCase();
        return students.stream()
                .filter(s -> s.getStudentId().toLowerCase().contains(q) || s.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    /**
     * Filters students by Department, Year, and Pass/Fail Status.
     */
    public List<Student> filterStudents(String department, String year, String status) {
        return students.stream()
                .filter(s -> department == null || department.equalsIgnoreCase("All Departments") || s.getDepartment().equalsIgnoreCase(department))
                .filter(s -> year == null || year.equalsIgnoreCase("All Years") || s.getYear().equalsIgnoreCase(year))
                .filter(s -> status == null || status.equalsIgnoreCase("All Status") || s.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    /**
     * Sorts students by average marks.
     */
    public List<Student> sortStudentsByAverage(boolean descending) {
        List<Student> list = new ArrayList<>(students);
        if (descending) {
            list.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
        } else {
            list.sort(Comparator.comparingDouble(Student::getAverageMarks));
        }
        return list;
    }

    /**
     * Sorts students by name.
     */
    public List<Student> sortStudentsByName(boolean ascending) {
        List<Student> list = new ArrayList<>(students);
        if (ascending) {
            list.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        } else {
            list.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER).reversed());
        }
        return list;
    }

    /**
     * Sorts students by Student ID.
     */
    public List<Student> sortStudentsById(boolean ascending) {
        List<Student> list = new ArrayList<>(students);
        if (ascending) {
            list.sort(Comparator.comparing(Student::getStudentId));
        } else {
            list.sort(Comparator.comparing(Student::getStudentId).reversed());
        }
        return list;
    }

    // --- Statistics & Summary Metrics ---

    public int getTotalStudentsCount() {
        return students.size();
    }

    public double getClassAverage() {
        if (students.isEmpty()) return 0.0;
        double sum = 0;
        for (Student s : students) {
            sum += s.getAverageMarks();
        }
        return sum / students.size();
    }

    public double getHighestScore() {
        if (students.isEmpty()) return 0.0;
        return students.stream().mapToDouble(Student::getAverageMarks).max().orElse(0.0);
    }

    public double getLowestScore() {
        if (students.isEmpty()) return 0.0;
        return students.stream().mapToDouble(Student::getAverageMarks).min().orElse(0.0);
    }

    public Student getHighestScoringStudent() {
        if (students.isEmpty()) return null;
        return students.stream().max(Comparator.comparingDouble(Student::getAverageMarks)).orElse(null);
    }

    public Student getLowestScoringStudent() {
        if (students.isEmpty()) return null;
        return students.stream().min(Comparator.comparingDouble(Student::getAverageMarks)).orElse(null);
    }

    public long getPassCount() {
        return students.stream().filter(Student::isPass).count();
    }

    public long getFailCount() {
        return students.size() - getPassCount();
    }

    public double getPassPercentage() {
        if (students.isEmpty()) return 0.0;
        return (getPassCount() * 100.0) / students.size();
    }

    /**
     * Returns a map containing counts for each grade (A+, A, B, C, D, F).
     */
    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("A+", 0);
        map.put("A", 0);
        map.put("B", 0);
        map.put("C", 0);
        map.put("D", 0);
        map.put("F", 0);

        for (Student s : students) {
            String grade = s.getLetterGrade();
            map.put(grade, map.getOrDefault(grade, 0) + 1);
        }
        return map;
    }

    /**
     * Saves changes to disk file.
     */
    public void saveChanges() {
        FileUtil.saveStudents(students);
    }
}
