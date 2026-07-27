package com.grade.ui;

import com.grade.model.Student;
import com.grade.service.StudentService;
import com.grade.util.GradeCalculator;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Student Management CRUD View.
 * Contains Form for Add/Update/Delete/Clear, real-time input validation,
 * Search, Department/Year/Status Filters, Sorting, and JTable with custom renders.
 */
public class StudentManagementPanel extends JPanel {

    private final StudentService studentService;

    // Form Input Controls
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cmbDept;
    private JComboBox<String> cmbYear;
    private JComboBox<String> cmbSection;

    private JTextField txtMath;
    private JTextField txtScience;
    private JTextField txtEnglish;
    private JTextField txtHistory;
    private JTextField txtComputer;

    // Action Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    // Table & Search Controls
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterDept;
    private JComboBox<String> cmbFilterStatus;
    private JComboBox<String> cmbSortBy;
    private JButton btnReportCard;

    private JTable table;
    private DefaultTableModel tableModel;

    public StudentManagementPanel(StudentService studentService) {
        this.studentService = studentService;
        initUI();
        loadTableData(studentService.getAllStudents());
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.CANVAS_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- Top Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Management");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_DARK);

        JLabel lblSubtitle = new JLabel("Add, update, search, delete, and inspect student grade records");
        lblSubtitle.setFont(UIUtils.FONT_REGULAR);
        lblSubtitle.setForeground(UIUtils.TEXT_MUTED);

        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.add(lblTitle);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        titleContainer.add(lblSubtitle);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        // --- Main Split Container (Left Form, Right Table & Controls) ---
        JPanel mainSplit = new JPanel(new BorderLayout(20, 0));
        mainSplit.setOpaque(false);

        // ================= LEFT FORM PANEL =================
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setPreferredSize(new Dimension(360, 600));

        JLabel lblFormTitle = new JLabel("Student Entry Form");
        lblFormTitle.setFont(UIUtils.FONT_HEADER);
        lblFormTitle.setForeground(UIUtils.TEXT_DARK);

        JPanel formFields = new JPanel();
        formFields.setOpaque(false);
        formFields.setLayout(new BoxLayout(formFields, BoxLayout.Y_AXIS));
        formFields.setBorder(new EmptyBorder(15, 0, 15, 0));

        txtId = UIUtils.createStyledTextField(15);
        txtName = UIUtils.createStyledTextField(15);

        String[] depts = {"Computer Science", "Data Science", "Electrical Eng", "Mechanical Eng", "Business", "Information Tech"};
        cmbDept = UIUtils.createStyledComboBox(depts);

        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
        cmbYear = UIUtils.createStyledComboBox(years);

        String[] sections = {"A", "B", "C", "D"};
        cmbSection = UIUtils.createStyledComboBox(sections);

        txtMath = UIUtils.createStyledTextField(5);
        txtScience = UIUtils.createStyledTextField(5);
        txtEnglish = UIUtils.createStyledTextField(5);
        txtHistory = UIUtils.createStyledTextField(5);
        txtComputer = UIUtils.createStyledTextField(5);

        formFields.add(createFormField("Student ID *", txtId));
        formFields.add(Box.createRigidArea(new Dimension(0, 8)));
        formFields.add(createFormField("Full Name *", txtName));
        formFields.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel comboRow = new JPanel(new GridLayout(1, 3, 8, 0));
        comboRow.setOpaque(false);
        comboRow.add(createFormField("Department", cmbDept));
        comboRow.add(createFormField("Year", cmbYear));
        comboRow.add(createFormField("Section", cmbSection));
        formFields.add(comboRow);

        formFields.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblMarksHeader = new JLabel("Subject Marks (0 - 100)");
        lblMarksHeader.setFont(UIUtils.FONT_BOLD);
        lblMarksHeader.setForeground(UIUtils.PRIMARY);
        formFields.add(lblMarksHeader);
        formFields.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel marksRow1 = new JPanel(new GridLayout(1, 3, 8, 0));
        marksRow1.setOpaque(false);
        marksRow1.add(createFormField("Math", txtMath));
        marksRow1.add(createFormField("Science", txtScience));
        marksRow1.add(createFormField("English", txtEnglish));
        formFields.add(marksRow1);

        formFields.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel marksRow2 = new JPanel(new GridLayout(1, 2, 8, 0));
        marksRow2.setOpaque(false);
        marksRow2.add(createFormField("History", txtHistory));
        marksRow2.add(createFormField("Computer Sci", txtComputer));
        formFields.add(marksRow2);

        // Form Buttons
        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);
        btnGrid.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnAdd = UIUtils.createStyledButton("Add Record", UIUtils.PRIMARY, Color.WHITE);
        btnUpdate = UIUtils.createStyledButton("Update", UIUtils.SUCCESS, Color.WHITE);
        btnDelete = UIUtils.createStyledButton("Delete", UIUtils.DANGER, Color.WHITE);
        btnClear = UIUtils.createStyledButton("Clear Form", new Color(225, 227, 234), UIUtils.TEXT_DARK);

        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        formCard.add(lblFormTitle, BorderLayout.NORTH);
        formCard.add(new JScrollPane(formFields) {
            { setBorder(null); setOpaque(false); getViewport().setOpaque(false); }
        }, BorderLayout.CENTER);
        formCard.add(btnGrid, BorderLayout.SOUTH);

        // ================= RIGHT TABLE & TOOLBAR =================
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar: Search, Filters, Report Button
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setOpaque(false);

        txtSearch = UIUtils.createStyledTextField(14);
        txtSearch.setToolTipText("Search by Student ID or Name");

        JLabel lblSearchIcon = new JLabel("🔍 Search:");
        lblSearchIcon.setFont(UIUtils.FONT_BOLD);
        lblSearchIcon.setForeground(UIUtils.TEXT_MUTED);

        String[] filterDepts = {"All Departments", "Computer Science", "Data Science", "Electrical Eng", "Mechanical Eng", "Business", "Information Tech"};
        cmbFilterDept = UIUtils.createStyledComboBox(filterDepts);

        String[] filterStatuses = {"All Status", "PASS", "FAIL"};
        cmbFilterStatus = UIUtils.createStyledComboBox(filterStatuses);

        String[] sortOptions = {"Sort: Average (High-Low)", "Sort: Average (Low-High)", "Sort: Name (A-Z)", "Sort: ID (Asc)"};
        cmbSortBy = UIUtils.createStyledComboBox(sortOptions);

        btnReportCard = UIUtils.createStyledButton("View Report Card", UIUtils.INFO, Color.WHITE);
        btnReportCard.setPreferredSize(new Dimension(145, 36));

        toolbar.add(lblSearchIcon);
        toolbar.add(txtSearch);
        toolbar.add(cmbFilterDept);
        toolbar.add(cmbFilterStatus);
        toolbar.add(cmbSortBy);
        toolbar.add(btnReportCard);

        // JTable setup
        String[] columns = {"ID", "Name", "Department", "Year", "Sec", "Math", "Sci", "Eng", "Hist", "Comp", "Total", "Avg", "Grade", "GPA", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(UIUtils.FONT_REGULAR);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(UIUtils.FONT_BOLD);
        table.getTableHeader().setBackground(UIUtils.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(UIUtils.TEXT_DARK);

        // Render pass/fail badges in status column
        table.getColumnModel().getColumn(14).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(UIUtils.FONT_BOLD);
                if ("PASS".equals(val)) {
                    lbl.setForeground(UIUtils.SUCCESS);
                } else {
                    lbl.setForeground(UIUtils.DANGER);
                }
                return lbl;
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1));

        tableCard.add(toolbar, BorderLayout.NORTH);
        tableCard.add(tableScroll, BorderLayout.CENTER);

        mainSplit.add(formCard, BorderLayout.WEST);
        mainSplit.add(tableCard, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);

        // --- Event Listeners ---

        btnAdd.addActionListener(e -> handleAddStudent());
        btnUpdate.addActionListener(e -> handleUpdateStudent());
        btnDelete.addActionListener(e -> handleDeleteStudent());
        btnClear.addActionListener(e -> clearForm());

        btnReportCard.addActionListener(e -> handleViewReportCard());

        // Table selection auto-populates form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromTable(table.getSelectedRow());
            }
        });

        // Search & Filter listeners
        DocumentListener searchListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applySearchAndFilters(); }
            public void removeUpdate(DocumentEvent e) { applySearchAndFilters(); }
            public void changedUpdate(DocumentEvent e) { applySearchAndFilters(); }
        };
        txtSearch.getDocument().addDocumentListener(searchListener);

        cmbFilterDept.addActionListener(e -> applySearchAndFilters());
        cmbFilterStatus.addActionListener(e -> applySearchAndFilters());
        cmbSortBy.addActionListener(e -> applySearchAndFilters());
    }

    private JPanel createFormField(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel l = new JLabel(label);
        l.setFont(UIUtils.FONT_SMALL);
        l.setForeground(UIUtils.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 3)));
        p.add(field);
        return p;
    }

    private void loadTableData(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getYear(),
                    s.getSection(),
                    s.getMathMarks(),
                    s.getScienceMarks(),
                    s.getEnglishMarks(),
                    s.getHistoryMarks(),
                    s.getComputerMarks(),
                    s.getTotalMarks(),
                    String.format("%.1f", s.getAverageMarks()),
                    s.getLetterGrade(),
                    String.format("%.2f", s.getGpa()),
                    s.getStatus()
            });
        }
    }

    private void applySearchAndFilters() {
        String query = txtSearch.getText().trim();
        String dept = (String) cmbFilterDept.getSelectedItem();
        String status = (String) cmbFilterStatus.getSelectedItem();
        int sortIdx = cmbSortBy.getSelectedIndex();

        List<Student> results = studentService.searchStudents(query);

        // Filter by dept & status
        results.removeIf(s -> dept != null && !dept.equals("All Departments") && !s.getDepartment().equalsIgnoreCase(dept));
        results.removeIf(s -> status != null && !status.equals("All Status") && !s.getStatus().equalsIgnoreCase(status));

        // Sort
        switch (sortIdx) {
            case 0: // Avg High to Low
                results.sort((a, b) -> Double.compare(b.getAverageMarks(), a.getAverageMarks()));
                break;
            case 1: // Avg Low to High
                results.sort((a, b) -> Double.compare(a.getAverageMarks(), b.getAverageMarks()));
                break;
            case 2: // Name A-Z
                results.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            case 3: // ID Asc
                results.sort((a, b) -> a.getStudentId().compareToIgnoreCase(b.getStudentId()));
                break;
        }

        loadTableData(results);
    }

    private void handleAddStudent() {
        Student s = validateAndBuildStudent();
        if (s == null) return;

        if (studentService.addStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student record for " + s.getName() + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            applySearchAndFilters();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "A student with ID '" + s.getStudentId() + "' already exists!", "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateStudent() {
        Student s = validateAndBuildStudent();
        if (s == null) return;

        if (studentService.updateStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student record for " + s.getName() + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            applySearchAndFilters();
        } else {
            JOptionPane.showMessageDialog(this, "Student ID '" + s.getStudentId() + "' not found to update!", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a Student ID to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = studentService.findStudentById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete student record:\n" + s.getName() + " (" + s.getStudentId() + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentService.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                applySearchAndFilters();
                clearForm();
            }
        }
    }

    private void handleViewReportCard() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table first.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Student s = studentService.findStudentById(id);
        if (s != null) {
            ReportCardDialog dialog = new ReportCardDialog(SwingUtilities.getWindowAncestor(this), s);
            dialog.setVisible(true);
        }
    }

    private Student validateAndBuildStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showError("Student ID and Full Name are required.");
            return null;
        }

        try {
            int math = parseMark(txtMath.getText(), "Mathematics");
            int sci = parseMark(txtScience.getText(), "Science");
            int eng = parseMark(txtEnglish.getText(), "English");
            int hist = parseMark(txtHistory.getText(), "History");
            int comp = parseMark(txtComputer.getText(), "Computer Science");

            String dept = (String) cmbDept.getSelectedItem();
            String year = (String) cmbYear.getSelectedItem();
            String sec = (String) cmbSection.getSelectedItem();

            return new Student(id, name, dept, year, sec, math, sci, eng, hist, comp);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
            return null;
        }
    }

    private int parseMark(String text, String subject) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(subject + " mark cannot be empty.");
        }
        try {
            int val = Integer.parseInt(text.trim());
            if (!GradeCalculator.isValidMark(val)) {
                throw new IllegalArgumentException(subject + " mark must be between 0 and 100.");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(subject + " mark must be a valid integer number.");
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tableModel.getValueAt(row, 0));
        txtName.setText((String) tableModel.getValueAt(row, 1));
        cmbDept.setSelectedItem(tableModel.getValueAt(row, 2));
        cmbYear.setSelectedItem(tableModel.getValueAt(row, 3));
        cmbSection.setSelectedItem(tableModel.getValueAt(row, 4));

        txtMath.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        txtScience.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        txtEnglish.setText(String.valueOf(tableModel.getValueAt(row, 7)));
        txtHistory.setText(String.valueOf(tableModel.getValueAt(row, 8)));
        txtComputer.setText(String.valueOf(tableModel.getValueAt(row, 9)));
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        cmbDept.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);
        cmbSection.setSelectedIndex(0);

        txtMath.setText("");
        txtScience.setText("");
        txtEnglish.setText("");
        txtHistory.setText("");
        txtComputer.setText("");

        table.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}
