package com.grade.ui;

import com.grade.service.AuthService;
import com.grade.service.StudentService;
import com.grade.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Modern Login Screen with split-hero layout, custom input fields,
 * password toggle, and default credentials hint.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private final StudentService studentService;

    public LoginFrame(StudentService studentService) {
        this.studentService = studentService;
        initUI();
    }

    private void initUI() {
        setTitle("Student Grade Management System - Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainContainer = new JPanel(new GridLayout(1, 2));

        // --- Left Hero Panel ---
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, UIUtils.NAV_BG,
                        getWidth(), getHeight(), new Color(20, 24, 40)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative background circles
                g2.setColor(new Color(54, 153, 255, 30));
                g2.fillOval(-50, -50, 250, 250);
                g2.setColor(new Color(27, 197, 189, 25));
                g2.fillOval(getWidth() - 150, getHeight() - 180, 220, 220);
                g2.dispose();
            }
        };
        heroPanel.setLayout(new BorderLayout());
        heroPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel heroContent = new JPanel();
        heroContent.setOpaque(false);
        heroContent.setLayout(new BoxLayout(heroContent, BoxLayout.Y_AXIS));

        JLabel lblBadge = new JLabel("ACADEMIC PORTAL");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setForeground(UIUtils.SUCCESS);

        JLabel lblTitle = new JLabel("<html>Student Grade<br>Management System</html>");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("<html>Comprehensive academic record tracking, grade calculation, GPA analytics, and report card generation.</html>");
        lblDesc.setFont(UIUtils.FONT_REGULAR);
        lblDesc.setForeground(UIUtils.TEXT_LIGHT);

        heroContent.add(lblBadge);
        heroContent.add(Box.createRigidArea(new Dimension(0, 15)));
        heroContent.add(lblTitle);
        heroContent.add(Box.createRigidArea(new Dimension(0, 20)));
        heroContent.add(lblDesc);

        heroPanel.add(heroContent, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("© 2026 Academic Systems • v2.5");
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.TEXT_MUTED);
        heroPanel.add(lblFooter, BorderLayout.SOUTH);

        // --- Right Login Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIUtils.CANVAS_BG);

        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 35, 35, 35));
        card.setPreferredSize(new Dimension(340, 410));

        JLabel lblHeader = new JLabel("System Sign In");
        lblHeader.setFont(UIUtils.FONT_TITLE);
        lblHeader.setForeground(UIUtils.TEXT_DARK);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubHeader = new JLabel("Enter your administrator credentials");
        lblSubHeader.setFont(UIUtils.FONT_REGULAR);
        lblSubHeader.setForeground(UIUtils.TEXT_MUTED);
        lblSubHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Error message label
        lblError = new JLabel(" ");
        lblError.setFont(UIUtils.FONT_SMALL);
        lblError.setForeground(UIUtils.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fields
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(UIUtils.FONT_BOLD);
        lblUser.setForeground(UIUtils.TEXT_DARK);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = UIUtils.createStyledTextField(18);
        txtUsername.setText("Priyadharshini");
        txtUsername.setMaximumSize(new Dimension(280, 38));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UIUtils.FONT_BOLD);
        lblPass.setForeground(UIUtils.TEXT_DARK);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = UIUtils.createStyledPasswordField(18);
        txtPassword.setText("admin123");
        txtPassword.setMaximumSize(new Dimension(280, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setFont(UIUtils.FONT_SMALL);
        chkShowPassword.setForeground(UIUtils.TEXT_MUTED);
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        btnLogin = UIUtils.createStyledButton("Sign In", UIUtils.PRIMARY, Color.WHITE);
        btnLogin.setMaximumSize(new Dimension(280, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Credential hint box
        JPanel hintBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        hintBox.setOpaque(false);
        hintBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblHint = new JLabel("Default: Priyadharshini / admin123");
        lblHint.setFont(UIUtils.FONT_SMALL);
        lblHint.setForeground(UIUtils.TEXT_MUTED);
        hintBox.add(lblHint);

        // Assemble Card
        card.add(lblHeader);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(lblSubHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblError);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblUser);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtUsername);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(lblPass);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(chkShowPassword);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnLogin);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(hintBox);

        formPanel.add(card);

        mainContainer.add(heroPanel);
        mainContainer.add(formPanel);

        add(mainContainer);

        // Event listeners
        btnLogin.addActionListener(e -> performLogin());

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterListener);
        txtPassword.addKeyListener(enterListener);
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please enter both username and password.");
            return;
        }

        if (AuthService.authenticate(username, password)) {
            lblError.setText(" ");
            // Open Main Dashboard Window
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(studentService);
                mainFrame.setVisible(true);
                dispose();
            });
        } else {
            lblError.setText("Invalid username or password!");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
