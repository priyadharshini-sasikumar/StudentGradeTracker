package com.grade.ui;

import com.grade.service.StudentService;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Main Application Dashboard Frame containing the dark sidebar navigation,
 * header panel, CardLayout view switcher, and global keyboard shortcuts.
 */
public class MainFrame extends JFrame {

    private final StudentService studentService;
    private CardLayout cardLayout;
    private JPanel contentContainer;

    private DashboardPanel dashboardPanel;
    private StudentManagementPanel studentManagementPanel;
    private ReportsPanel reportsPanel;

    private JButton btnNavDashboard;
    private JButton btnNavStudents;
    private JButton btnNavReports;

    public MainFrame(StudentService studentService) {
        this.studentService = studentService;
        initUI();
        setupShortcuts();
    }

    private void initUI() {
        setTitle("Student Grade Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);

        JPanel rootContainer = new JPanel(new BorderLayout());
        rootContainer.setBackground(UIUtils.CANVAS_BG);

        // ================= LEFT SIDEBAR NAVIGATION =================
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIUtils.NAV_BG);
        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        // Sidebar Brand Header
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblLogoText = new JLabel("STUDENT GRADE");
        lblLogoText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogoText.setForeground(UIUtils.PRIMARY);

        JLabel lblLogoSub = new JLabel("Management Portal");
        lblLogoSub.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblLogoSub.setForeground(Color.WHITE);

        brandPanel.add(lblLogoText);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        brandPanel.add(lblLogoSub);

        // Sidebar Navigation Buttons
        JPanel navMenu = new JPanel();
        navMenu.setOpaque(false);
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setBorder(new EmptyBorder(10, 15, 10, 15));

        btnNavDashboard = createNavButton(" Dashboard", "D");
        btnNavStudents = createNavButton(" Student Management", "M");
        btnNavReports = createNavButton(" Reports & Analytics", "R");

        navMenu.add(btnNavDashboard);
        navMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        navMenu.add(btnNavStudents);
        navMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        navMenu.add(btnNavReports);

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navMenu, BorderLayout.CENTER);

        // Sidebar User Profile Footer
        JPanel userFooter = new JPanel(new BorderLayout());
        userFooter.setOpaque(false);
        userFooter.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lblUser = new JLabel("<html><b>Priyadharshini</b><br><font color='#7E8299'>Administrator</font></html>");
        lblUser.setFont(UIUtils.FONT_REGULAR);
        lblUser.setForeground(Color.WHITE);

        JButton btnLogout = UIUtils.createStyledButton("Logout", UIUtils.DANGER, Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(80, 32));
        btnLogout.addActionListener(e -> performLogout());

        userFooter.add(lblUser, BorderLayout.WEST);
        userFooter.add(btnLogout, BorderLayout.EAST);

        sidebar.add(userFooter, BorderLayout.SOUTH);

        // ================= CENTER CONTENT AREA =================
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setBackground(UIUtils.CANVAS_BG);

        dashboardPanel = new DashboardPanel(studentService);
        studentManagementPanel = new StudentManagementPanel(studentService);
        reportsPanel = new ReportsPanel(studentService);

        contentContainer.add(dashboardPanel, "DASHBOARD");
        contentContainer.add(studentManagementPanel, "STUDENTS");
        contentContainer.add(reportsPanel, "REPORTS");

        rootContainer.add(sidebar, BorderLayout.WEST);
        rootContainer.add(contentContainer, BorderLayout.CENTER);

        add(rootContainer);

        // Navigation Button Listeners
        btnNavDashboard.addActionListener(e -> showView("DASHBOARD"));
        btnNavStudents.addActionListener(e -> showView("STUDENTS"));
        btnNavReports.addActionListener(e -> showView("REPORTS"));

        // Default initial view
        showView("DASHBOARD");
    }

    private void showView(String cardName) {
        cardLayout.show(contentContainer, cardName);

        // Update button active highlights
        btnNavDashboard.setBackground(cardName.equals("DASHBOARD") ? UIUtils.NAV_ITEM_ACTIVE : UIUtils.NAV_BG);
        btnNavStudents.setBackground(cardName.equals("STUDENTS") ? UIUtils.NAV_ITEM_ACTIVE : UIUtils.NAV_BG);
        btnNavReports.setBackground(cardName.equals("REPORTS") ? UIUtils.NAV_ITEM_ACTIVE : UIUtils.NAV_BG);

        // Refresh views dynamically when switched
        if (cardName.equals("DASHBOARD")) {
            dashboardPanel.refreshDashboardData();
        } else if (cardName.equals("REPORTS")) {
            reportsPanel.refreshReports();
        }
    }

    private JButton createNavButton(String text, String shortcutKey) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setFont(getFont());
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = 16;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                // Draw shortcut hint
                g2.setColor(new Color(255, 255, 255, 120));
                g2.setFont(UIUtils.FONT_SMALL);
                String hint = "Ctrl+" + shortcutKey;
                FontMetrics fmHint = g2.getFontMetrics();
                g2.drawString(hint, getWidth() - fmHint.stringWidth(hint) - 15, y);

                g2.dispose();
            }
        };

        btn.setFont(UIUtils.FONT_BOLD);
        btn.setBackground(UIUtils.NAV_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(210, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private void setupShortcuts() {
        JRootPane root = getRootPane();

        // Ctrl + D -> Dashboard
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "navDash");
        root.getActionMap().put("navDash", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showView("DASHBOARD");
            }
        });

        // Ctrl + M -> Student Management
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK), "navStudents");
        root.getActionMap().put("navStudents", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showView("STUDENTS");
            }
        });

        // Ctrl + R -> Reports
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), "navReports");
        root.getActionMap().put("navReports", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showView("REPORTS");
            }
        });

        // Ctrl + Q -> Logout
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), "logout");
        root.getActionMap().put("logout", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            studentService.saveChanges();
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame(studentService);
                login.setVisible(true);
            });
        }
    }
}
