package com.grade.ui;

import com.grade.model.Student;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Modern Modal Dialog displaying a complete, official-style Student Report Card
 * with individual subject breakdown, total scores, GPA, pass/fail status, and export option.
 */
public class ReportCardDialog extends JDialog {

    private final Student student;

    public ReportCardDialog(Window owner, Student student) {
        super(owner, "Official Student Report Card - " + student.getName(), ModalityType.APPLICATION_MODAL);
        this.student = student;
        initUI();
    }

    private void initUI() {
        setSize(650, 720);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UIUtils.CANVAS_BG);
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Card Container ---
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- Top Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblInstitution = new JLabel("ACADEMIC EVALUATION SYSTEM");
        lblInstitution.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblInstitution.setForeground(UIUtils.PRIMARY);

        JLabel lblTitle = new JLabel("Official Student Report Card");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_DARK);

        JLabel lblDate = new JLabel("Term: 2026 Academic Year");
        lblDate.setFont(UIUtils.FONT_SMALL);
        lblDate.setForeground(UIUtils.TEXT_MUTED);

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.add(lblInstitution);
        headerLeft.add(Box.createRigidArea(new Dimension(0, 4)));
        headerLeft.add(lblTitle);
        headerLeft.add(Box.createRigidArea(new Dimension(0, 2)));
        headerLeft.add(lblDate);

        // Status Badge (PASS / FAIL)
        JLabel lblStatus = new JLabel(student.getStatus()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(student.isPass() ? UIUtils.SUCCESS : UIUtils.DANGER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setPreferredSize(new Dimension(90, 34));

        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(lblStatus, BorderLayout.EAST);

        // --- Student Demographics Grid ---
        JPanel demoPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        demoPanel.setOpaque(false);
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, UIUtils.BORDER_COLOR),
                new EmptyBorder(15, 0, 15, 0)
        ));

        addDemoItem(demoPanel, "Student ID", student.getStudentId());
        addDemoItem(demoPanel, "Full Name", student.getName());
        addDemoItem(demoPanel, "Department", student.getDepartment());
        addDemoItem(demoPanel, "Academic Year", student.getYear());
        addDemoItem(demoPanel, "Section", student.getSection());
        addDemoItem(demoPanel, "Overall Grade", student.getLetterGrade() + " (GPA: " + String.format("%.2f", student.getGpa()) + ")");

        // --- Subject Breakdown Table ---
        String[] columns = {"Subject Name", "Max Marks", "Marks Obtained", "Subject Grade", "Remark"};
        Object[][] data = {
                {"Mathematics", 100, student.getMathMarks(), getSubjectGrade(student.getMathMarks()), getRemark(student.getMathMarks())},
                {"Science / Physics", 100, student.getScienceMarks(), getSubjectGrade(student.getScienceMarks()), getRemark(student.getScienceMarks())},
                {"English Literature", 100, student.getEnglishMarks(), getSubjectGrade(student.getEnglishMarks()), getRemark(student.getEnglishMarks())},
                {"History & Civics", 100, student.getHistoryMarks(), getSubjectGrade(student.getHistoryMarks()), getRemark(student.getHistoryMarks())},
                {"Computer Science", 100, student.getComputerMarks(), getSubjectGrade(student.getComputerMarks()), getRemark(student.getComputerMarks())}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(UIUtils.FONT_REGULAR);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.getTableHeader().setFont(UIUtils.FONT_BOLD);
        table.getTableHeader().setBackground(UIUtils.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(UIUtils.TEXT_DARK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1));
        scrollPane.setPreferredSize(new Dimension(550, 190));

        // --- Metrics Summary Cards Grid ---
        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 12, 0));
        metricsGrid.setOpaque(false);
        metricsGrid.setBorder(new EmptyBorder(15, 0, 15, 0));

        metricsGrid.add(createMiniStatCard("Total Marks", student.getTotalMarks() + " / 500", UIUtils.PRIMARY));
        metricsGrid.add(createMiniStatCard("Average", String.format("%.1f%%", student.getAverageMarks()), UIUtils.SUCCESS));
        metricsGrid.add(createMiniStatCard("Letter Grade", student.getLetterGrade(), UIUtils.INFO));
        metricsGrid.add(createMiniStatCard("GPA Score", String.format("%.2f", student.getGpa()), UIUtils.WARNING));

        // --- Center Content Assembly ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(demoPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(metricsGrid);

        // --- Footer Action Buttons ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setOpaque(false);

        JButton btnExport = UIUtils.createStyledButton("Print / Export", UIUtils.PRIMARY, Color.WHITE);
        btnExport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Report Card for " + student.getName() + " (" + student.getStudentId() + ")\nhas been sent to printer / PDF exporter successfully!",
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnClose = UIUtils.createStyledButton("Close", new Color(225, 227, 234), UIUtils.TEXT_DARK);
        btnClose.addActionListener(e -> dispose());

        footerPanel.add(btnExport);
        footerPanel.add(btnClose);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);

        rootPanel.add(card, BorderLayout.CENTER);
        add(rootPanel);
    }

    private void addDemoItem(JPanel parent, String label, String value) {
        JPanel item = new JPanel();
        item.setOpaque(false);
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(UIUtils.FONT_SMALL);
        lbl.setForeground(UIUtils.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(UIUtils.FONT_BOLD);
        val.setForeground(UIUtils.TEXT_DARK);

        item.add(lbl);
        item.add(val);
        parent.add(item);
    }

    private JPanel createMiniStatCard(String title, String valText, Color color) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(color);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel t = new JLabel(title);
        t.setFont(UIUtils.FONT_SMALL);
        t.setForeground(UIUtils.TEXT_MUTED);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel v = new JLabel(valText);
        v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        v.setForeground(color);
        v.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(t);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(v);

        return p;
    }

    private String getSubjectGrade(int mark) {
        if (mark >= 90) return "A+";
        if (mark >= 80) return "A";
        if (mark >= 70) return "B";
        if (mark >= 60) return "C";
        if (mark >= 50) return "D";
        return "F";
    }

    private String getRemark(int mark) {
        if (mark >= 90) return "Outstanding";
        if (mark >= 80) return "Excellent";
        if (mark >= 70) return "Good";
        if (mark >= 60) return "Satisfactory";
        if (mark >= 50) return "Average";
        return "Needs Attention";
    }
}
