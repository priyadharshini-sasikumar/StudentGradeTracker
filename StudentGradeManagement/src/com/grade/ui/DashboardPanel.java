package com.grade.ui;

import com.grade.model.Student;
import com.grade.service.StudentService;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Dashboard View displaying summary cards (Total Students, Class Average,
 * Highest Score, Pass Rate), a custom painted Grade Distribution Bar Chart,
 * and a Top Performers table.
 */
public class DashboardPanel extends JPanel {

    private final StudentService studentService;

    // Stat card labels
    private JLabel lblTotalStudentsVal;
    private JLabel lblClassAvgVal;
    private JLabel lblHighestScoreVal;
    private JLabel lblPassRateVal;

    private GradeChartPanel chartPanel;
    private JTable topPerformersTable;
    private DefaultTableModel tableModel;

    public DashboardPanel(StudentService studentService) {
        this.studentService = studentService;
        initUI();
        refreshDashboardData();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.CANVAS_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- Top Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Academic Dashboard");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_DARK);

        JLabel lblSubtitle = new JLabel("Real-time summary of student performance, grade distribution, and statistics");
        lblSubtitle.setFont(UIUtils.FONT_REGULAR);
        lblSubtitle.setForeground(UIUtils.TEXT_MUTED);

        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.add(lblTitle);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        titleContainer.add(lblSubtitle);

        JButton btnRefresh = UIUtils.createStyledButton("Refresh Data", UIUtils.PRIMARY, Color.WHITE);
        btnRefresh.addActionListener(e -> refreshDashboardData());

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        // --- 4 Summary Cards Grid ---
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 18, 0));
        cardsGrid.setOpaque(false);

        lblTotalStudentsVal = new JLabel("0");
        lblClassAvgVal = new JLabel("0.0%");
        lblHighestScoreVal = new JLabel("0.0%");
        lblPassRateVal = new JLabel("0.0%");

        cardsGrid.add(createMetricCard("TOTAL STUDENTS", lblTotalStudentsVal, "Enrolled Records", UIUtils.PRIMARY));
        cardsGrid.add(createMetricCard("CLASS AVERAGE", lblClassAvgVal, "Overall Mean", UIUtils.SUCCESS));
        cardsGrid.add(createMetricCard("HIGHEST SCORE", lblHighestScoreVal, "Top Average", UIUtils.INFO));
        cardsGrid.add(createMetricCard("PASS RATE", lblPassRateVal, "Passing Threshold", UIUtils.WARNING));

        // --- Center Content Split (Chart on Left, Top Performers on Right) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        // Left: Grade Distribution Chart Card
        JPanel chartCard = UIUtils.createCardPanel();
        chartCard.setLayout(new BorderLayout());
        chartCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblChartTitle = new JLabel("Grade Distribution");
        lblChartTitle.setFont(UIUtils.FONT_HEADER);
        lblChartTitle.setForeground(UIUtils.TEXT_DARK);
        chartCard.add(lblChartTitle, BorderLayout.NORTH);

        chartPanel = new GradeChartPanel();
        chartCard.add(chartPanel, BorderLayout.CENTER);

        // Right: Top Performers Table Card
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTableTitle = new JLabel("Top Performing Students");
        lblTableTitle.setFont(UIUtils.FONT_HEADER);
        lblTableTitle.setForeground(UIUtils.TEXT_DARK);
        tableCard.add(lblTableTitle, BorderLayout.NORTH);

        String[] cols = {"Rank", "ID", "Name", "Department", "Average", "Grade"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        topPerformersTable = new JTable(tableModel);
        topPerformersTable.setFont(UIUtils.FONT_REGULAR);
        topPerformersTable.setRowHeight(34);
        topPerformersTable.setShowGrid(false);
        topPerformersTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        topPerformersTable.getTableHeader().setBackground(UIUtils.TABLE_HEADER_BG);
        topPerformersTable.getTableHeader().setForeground(UIUtils.TEXT_DARK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        topPerformersTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        topPerformersTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        topPerformersTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        topPerformersTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane sp = new JScrollPane(topPerformersTable);
        sp.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(15, 0, 0, 0));
        tableContainer.add(sp, BorderLayout.CENTER);

        tableCard.add(tableContainer, BorderLayout.CENTER);

        centerPanel.add(chartCard);
        centerPanel.add(tableCard);

        // --- Main Layout Assembly ---
        JPanel topContainer = new JPanel(new BorderLayout(0, 20));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(cardsGrid, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void refreshDashboardData() {
        int total = studentService.getTotalStudentsCount();
        double avg = studentService.getClassAverage();
        double highest = studentService.getHighestScore();
        double passRate = studentService.getPassPercentage();

        lblTotalStudentsVal.setText(String.valueOf(total));
        lblClassAvgVal.setText(String.format("%.1f%%", avg));
        lblHighestScoreVal.setText(String.format("%.1f%%", highest));
        lblPassRateVal.setText(String.format("%.1f%%", passRate));

        // Update Grade Chart
        chartPanel.setGradeData(studentService.getGradeDistribution(), total);

        // Update Top Performers Table
        tableModel.setRowCount(0);
        List<Student> sorted = studentService.sortStudentsByAverage(true);
        int limit = Math.min(sorted.size(), 6);
        for (int i = 0; i < limit; i++) {
            Student s = sorted.get(i);
            tableModel.addRow(new Object[]{
                    "#" + (i + 1),
                    s.getStudentId(),
                    s.getName(),
                    s.getDepartment(),
                    String.format("%.2f%%", s.getAverageMarks()),
                    s.getLetterGrade()
            });
        }
    }

    private JPanel createMetricCard(String title, JLabel valLabel, String subtext, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIUtils.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                // Accent top border line
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, 6, 14, 14);

                g2.setColor(UIUtils.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.FONT_SMALL);
        lblTitle.setForeground(UIUtils.TEXT_MUTED);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLabel.setForeground(UIUtils.TEXT_DARK);

        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(UIUtils.FONT_SMALL);
        lblSub.setForeground(UIUtils.TEXT_MUTED);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(lblSub);

        return card;
    }

    /**
     * Custom component to draw Grade Distribution visual bar chart.
     */
    private static class GradeChartPanel extends JPanel {
        private Map<String, Integer> gradeMap;
        private int totalCount;

        public GradeChartPanel() {
            setOpaque(false);
        }

        public void setGradeData(Map<String, Integer> gradeMap, int totalCount) {
            this.gradeMap = gradeMap;
            this.totalCount = totalCount;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gradeMap == null || gradeMap.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int padding = 40;
            int chartWidth = width - (padding * 2);
            int chartHeight = height - (padding * 2);

            String[] grades = {"A+", "A", "B", "C", "D", "F"};
            Color[] colors = {
                    new Color(27, 197, 189), // A+ Emerald
                    new Color(54, 153, 255), // A Blue
                    new Color(137, 80, 252), // B Purple
                    new Color(255, 168, 0),  // C Amber
                    new Color(246, 120, 78), // D Orange
                    new Color(246, 78, 96)   // F Red
            };

            int barCount = grades.length;
            int barWidth = (chartWidth / barCount) - 20;

            int maxVal = gradeMap.values().stream().max(Integer::compare).orElse(1);
            if (maxVal == 0) maxVal = 1;

            int startX = padding + 10;
            int baselineY = height - padding;

            // Draw horizontal reference line
            g2.setColor(UIUtils.BORDER_COLOR);
            g2.drawLine(padding, baselineY, width - padding, baselineY);

            for (int i = 0; i < barCount; i++) {
                String gName = grades[i];
                int count = gradeMap.getOrDefault(gName, 0);

                int barHeight = (int) (((double) count / maxVal) * (chartHeight - 30));
                if (count > 0 && barHeight < 10) barHeight = 10;

                int x = startX + i * (barWidth + 20);
                int y = baselineY - barHeight;

                // Draw Bar with rounded top
                g2.setColor(colors[i]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                // Value Label above bar
                g2.setColor(UIUtils.TEXT_DARK);
                g2.setFont(UIUtils.FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String countStr = String.valueOf(count);
                g2.drawString(countStr, x + (barWidth - fm.stringWidth(countStr)) / 2, y - 6);

                // Category Label below bar
                g2.setColor(UIUtils.TEXT_MUTED);
                g2.setFont(UIUtils.FONT_BOLD);
                FontMetrics fmLabel = g2.getFontMetrics();
                g2.drawString(gName, x + (barWidth - fmLabel.stringWidth(gName)) / 2, baselineY + 20);
            }
            g2.dispose();
        }
    }
}
