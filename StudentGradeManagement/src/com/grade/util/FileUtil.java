package com.grade.util;

import com.grade.model.Student;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File utility class for loading and saving student records using Object Serialization.
 * Automatically initializes sample data if no file is present.
 */
public class FileUtil {

    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = "data/students.dat";

    /**
     * Saves the list of students to file via ObjectOutputStream.
     */
    public static boolean saveStudents(List<Student> students) {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
                oos.writeObject(students);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the list of students from file. If file is missing or corrupt, returns sample data.
     */
    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Data file not found. Initializing with sample student data...");
            List<Student> sampleData = createSampleData();
            saveStudents(sampleData);
            return sampleData;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Student>) obj;
            }
        } catch (Exception e) {
            System.err.println("Error loading student data (file may be outdated/corrupted). Generating fresh sample data: " + e.getMessage());
            List<Student> sampleData = createSampleData();
            saveStudents(sampleData);
            return sampleData;
        }
        return createSampleData();
    }

    /**
     * Generates a rich, realistic sample dataset of 10 students.
     */
    public static List<Student> createSampleData() {
        List<Student> list = new ArrayList<>();

        list.add(new Student("STU-1001", "Alex Morgan", "Computer Science", "3rd Year", "A", 95, 88, 92, 85, 98));
        list.add(new Student("STU-1002", "Sophia Chen", "Data Science", "2nd Year", "A", 92, 96, 89, 94, 95));
        list.add(new Student("STU-1003", "Liam Johnson", "Electrical Eng", "4th Year", "B", 78, 82, 75, 80, 84));
        list.add(new Student("STU-1004", "Emma Watson", "Computer Science", "3rd Year", "A", 88, 90, 94, 86, 91));
        list.add(new Student("STU-1005", "Noah Miller", "Mechanical Eng", "2nd Year", "C", 65, 58, 62, 70, 60));
        list.add(new Student("STU-1006", "Ava Vance", "Data Science", "1st Year", "A", 96, 94, 98, 90, 97));
        list.add(new Student("STU-1007", "Ethan Hunt", "Computer Science", "4th Year", "B", 72, 70, 68, 75, 80));
        list.add(new Student("STU-1008", "Isabella Ross", "Electrical Eng", "1st Year", "C", 52, 48, 55, 60, 50));
        list.add(new Student("STU-1009", "Lucas Taylor", "Mechanical Eng", "2nd Year", "D", 45, 42, 38, 48, 40));
        list.add(new Student("STU-1010", "Mia Anderson", "Business", "3rd Year", "A", 84, 86, 90, 88, 85));

        return list;
    }
}
