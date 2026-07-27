package com.grade.ui;

import com.grade.model.Student;
import com.grade.service.StudentService;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reports & Analytics View providing detailed class metrics, highest/lowest scoring student spotlights,
 * department performance breakdown, and report card selection.
 */
public class ReportsPanel extends JPanel {

    private final StudentService studentService;

    private JLabel lblHighestStudentVal;
    private JLabel lblHighestScoreVal;
    private JLabel lblLowestStudentVal;
    private JLabel lblLowestScoreVal;

    private JLabel lblPassCountVal;
    private JLabel lblFailCountVal;
    private JLabel lblPassPercentVal;

    private JTable deptTable;
    private DefaultTableModel deptTableModel;

    private JComboBox<String> cmbSelectStudent;
    private JButton btnGenerateReport;

    public ReportsPanel(StudentService studentService) {
        this.studentService = studentService;
        initUI();
        refreshReports();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.CANVAS_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- Top Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Class Performance & Reports");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_DARK);

        JLabel lblSubtitle = new JLabel("Comprehensive statistics, department analytics, and individual report card generation");
        lblSubtitle.setFont(UIUtils.FONT_REGULAR);
        lblSubtitle.setForeground(UIUtils.TEXT_MUTED);

        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.add(lblTitle);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        titleContainer.add(lblSubtitle);

        JButton btnRefresh = UIUtils.createStyledButton("Recalculate", UIUtils.PRIMARY, Color.WHITE);
        btnRefresh.addActionListener(e -> refreshReports());

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        // --- Top Row Spotlight Cards ---
        JPanel spotlightsGrid = new JPanel(new GridLayout(1, 3, 18, 0));
        spotlightsGrid.setOpaque(false);

        lblHighestStudentVal = new JLabel("N/A");
        lblHighestScoreVal = new JLabel("0.0%");

        lblLowestStudentVal = new JLabel("N/A");
        lblLowestScoreVal = new JLabel("0.0%");

        lblPassCountVal = new JLabel("0 Passed");
        lblFailCountVal = new JLabel("0 Failed");
        lblPassPercentVal = new JLabel("0.0%");

        spotlightsGrid.add(createSpotlightCard("HIGHEST PERFORMING STUDENT", lblHighestStudentVal, lblHighestScoreVal, UIUtils.SUCCESS));
        spotlightsGrid.add(createSpotlightCard("LOWEST PERFORMING STUDENT", lblLowestStudentVal, lblLowestScoreVal, UIUtils.DANGER));
        spotlightsGrid.add(createPassFailCard("PASS / FAIL SUMMARY", lblPassCountVal, lblFailCountVal, lblPassPercentVal, UIUtils.INFO));

        // --- Middle Split: Department Breakdown vs Report Generator ---
        JPanel centerSplit = new JPanel(new GridLayout(1, 2, 20, 0));
        centerSplit.setOpaque(false);

        // Left Card: Department-wise Breakdown Table
        JPanel deptCard = UIUtils.createCardPanel();
        deptCard.setLayout(new BorderLayout());
        deptCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblDeptTitle = new JLabel("Department Performance Breakdown");
        lblDeptTitle.setFont(UIUtils.FONT_HEADER);
        lblDeptTitle.setForeground(UIUtils.TEXT_DARK);

        String[] deptCols = {"Department", "Students", "Average Score", "Pass Rate"};
        deptTableModel = new DefaultTableModel(deptCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        deptTable = new JTable(deptTableModel);
        deptTable.setFont(UIUtils.FONT_REGULAR);
        deptTable.setRowHeight(32);
        deptTable.setShowGrid(false);
        deptTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        deptTable.getTableHeader().setBackground(UIUtils.TABLE_HEADER_BG);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        deptTable.getColumnModel().getColumn(1).setCellRenderer(centerR);
        deptTable.getColumnModel().getColumn(2).setCellRenderer(centerR);
        deptTable.getColumnModel().getColumn(3).setCellRenderer(centerR);

        JScrollPane deptSp = new JScrollPane(deptTable);
        deptSp.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1));

        JPanel deptContent = new JPanel(new BorderLayout());
        deptContent.setOpaque(false);
        deptContent.setBorder(new EmptyBorder(15, 0, 0, 0));
        deptContent.add(deptSp, BorderLayout.CENTER);

        deptCard.add(lblDeptTitle, BorderLayout.NORTH);
        deptCard.add(deptContent, BorderLayout.CENTER);

        // Right Card: Student Report Card Quick Generator
        JPanel reportCardPanel = UIUtils.createCardPanel();
        reportCardPanel.setLayout(new BorderLayout());
        reportCardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblGenTitle = new JLabel("Student Report Card Generator");
        lblGenTitle.setFont(UIUtils.FONT_HEADER);
        lblGenTitle.setForeground(UIUtils.TEXT_DARK);

        JPanel genBody = new JPanel();
        genBody.setOpaque(false);
        genBody.setLayout(new BoxLayout(genBody, BoxLayout.Y_AXIS));
        genBody.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblGenDesc = new JLabel("Select any student from the list to view and print their official report card:");
        lblGenDesc.setFont(UIUtils.FONT_REGULAR);
        lblGenDesc.setForeground(UIUtils.TEXT_MUTED);

        cmbSelectStudent = UIUtils.createStyledComboBox(new String[]{"No Students Available"});
        cmbSelectStudent.setMaximumSize(new Dimension(400, 38));

        btnGenerateReport = UIUtils.createStyledButton("Generate Report Card", UIUtils.PRIMARY, Color.WHITE);
        btnGenerateReport.setPreferredSize(new Dimension(200, 42));

        btnGenerateReport.addActionListener(e -> {
            int idx = cmbSelectStudent.getSelectedIndex();
            List<Student> all = studentService.getAllStudents();
            if (idx >= 0 && idx < all.size()) {
                Student selected = all.get(idx);
                ReportCardDialog dialog = new ReportCardDialog(SwingUtilities.getWindowAncestor(this), selected);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid student record.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        genBody.add(lblGenDesc);
        genBody.add(Box.createRigidArea(new Dimension(0, 15)));
        genBody.add(cmbSelectStudent);
        genBody.add(Box.createRigidArea(new Dimension(0, 20)));
        genBody.add(btnGenerateReport);

        reportCardPanel.add(lblGenTitle, BorderLayout.NORTH);
        reportCardPanel.add(genBody, BorderLayout.CENTER);

        centerSplit.add(deptCard);
        centerSplit.add(reportCardPanel);

        // --- Layout Assembly ---
        JPanel topBox = new JPanel(new BorderLayout(0, 20));
        topBox.setOpaque(false);
        topBox.add(headerPanel, BorderLayout.NORTH);
        topBox.add(spotlightsGrid, BorderLayout.SOUTH);

        add(topBox, BorderLayout.NORTH);
        add(centerSplit, BorderLayout.CENTER);
    }

    public void refreshReports() {
        Student highest = studentService.getHighestScoringStudent();
        if (highest != null) {
            lblHighestStudentVal.setText(highest.getName() + " (" + highest.getStudentId() + ")");
            lblHighestScoreVal.setText(String.format("%.1f%% - Grade %s", highest.getAverageMarks(), highest.getLetterGrade()));
        } else {
            lblHighestStudentVal.setText("N/A");
            lblHighestScoreVal.setText("0.0%");
        }

        Student lowest = studentService.getLowestScoringStudent();
        if (lowest != null) {
            lblLowestStudentVal.setText(lowest.getName() + " (" + lowest.getStudentId() + ")");
            lblLowestScoreVal.setText(String.format("%.1f%% - Grade %s", lowest.getAverageMarks(), lowest.getLetterGrade()));
        } else {
            lblLowestStudentVal.setText("N/A");
            lblLowestScoreVal.setText("0.0%");
        }

        long pass = studentService.getPassCount();
        long fail = studentService.getFailCount();
        double passPct = studentService.getPassPercentage();

        lblPassCountVal.setText(pass + " Students Passed");
        lblFailCountVal.setText(fail + " Students Failed");
        lblPassPercentVal.setText(String.format("%.1f%% Overall Pass Rate", passPct));

        // Department breakdown table
        deptTableModel.setRowCount(0);
        List<Student> allStudents = studentService.getAllStudents();
        Map<String, List<Student>> deptGroup = allStudents.stream().collect(Collectors.groupingBy(Student::getDepartment));

        for (Map.Entry<String, List<Student>> entry : deptGroup.entrySet()) {
            String deptName = entry.getKey();
            List<Student> sList = entry.getValue();
            int count = sList.size();
            double avg = sList.stream().mapToDouble(Student::getAverageMarks).average().orElse(0.0);
            long passed = sList.stream().filter(Student::isPass).count();
            double pRate = (passed * 100.0) / count;

            deptTableModel.addRow(new Object[]{
                    deptName,
                    count,
                    String.format("%.1f%%", avg),
                    String.format("%.1f%%", pRate)
            });
        }

        // Student selector drop-down
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        for (Student s : allStudents) {
            comboModel.addElement(s.getStudentId() + " - " + s.getName() + " (" + s.getDepartment() + ")");
        }
        cmbSelectStudent.setModel(comboModel);
    }

    private JPanel createSpotlightCard(String title, JLabel nameLbl, JLabel scoreLbl, Color color) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel(title);
        t.setFont(UIUtils.FONT_SMALL);
        t.setForeground(UIUtils.TEXT_MUTED);

        nameLbl.setFont(UIUtils.FONT_HEADER);
        nameLbl.setForeground(UIUtils.TEXT_DARK);

        scoreLbl.setFont(UIUtils.FONT_BOLD);
        scoreLbl.setForeground(color);

        card.add(t);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(nameLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(scoreLbl);

        return card;
    }

    private JPanel createPassFailCard(String title, JLabel passLbl, JLabel failLbl, JLabel rateLbl, Color color) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel(title);
        t.setFont(UIUtils.FONT_SMALL);
        t.setForeground(UIUtils.TEXT_MUTED);

        passLbl.setFont(UIUtils.FONT_BOLD);
        passLbl.setForeground(UIUtils.SUCCESS);

        failLbl.setFont(UIUtils.FONT_BOLD);
        failLbl.setForeground(UIUtils.DANGER);

        rateLbl.setFont(UIUtils.FONT_SMALL);
        rateLbl.setForeground(UIUtils.TEXT_MUTED);

        card.add(t);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel pfRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pfRow.setOpaque(false);
        pfRow.add(passLbl);
        pfRow.add(failLbl);

        card.add(pfRow);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(rateLbl);

        return card;
    }
}
