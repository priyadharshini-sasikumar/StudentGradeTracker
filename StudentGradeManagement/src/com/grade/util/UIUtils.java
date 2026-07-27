package com.grade.util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UI Design System and Helper Utilities for creating modern, attractive Swing GUIs.
 * Provides curated color palettes, custom fonts, rounded components, custom button styling,
 * badge pill renderers, and shadow card effects.
 */
public class UIUtils {

    // Modern Color Palette
    public static final Color NAV_BG = new Color(30, 30, 45);         // #1E1E2D - Dark Sidebar
    public static final Color NAV_ITEM_HOVER = new Color(44, 44, 64);
    public static final Color NAV_ITEM_ACTIVE = new Color(54, 153, 255);
    
    public static final Color HEADER_BG = Color.WHITE;
    public static final Color CANVAS_BG = new Color(244, 246, 249);    // #F4F6F9 - Soft Gray
    public static final Color CARD_BG = Color.WHITE;

    public static final Color PRIMARY = new Color(54, 153, 255);       // #3699FF - Modern Blue
    public static final Color PRIMARY_HOVER = new Color(24, 125, 228);
    public static final Color SUCCESS = new Color(27, 197, 189);       // #1BC5BD - Emerald Green
    public static final Color SUCCESS_HOVER = new Color(10, 162, 154);
    public static final Color WARNING = new Color(255, 168, 0);        // #FFA800 - Amber
    public static final Color DANGER = new Color(246, 78, 96);         // #F64E60 - Vibrant Red
    public static final Color DANGER_HOVER = new Color(220, 50, 68);
    public static final Color INFO = new Color(137, 80, 252);          // #8950FC - Purple Accent

    public static final Color TEXT_DARK = new Color(24, 28, 50);       // #181C32 - Slate Charcoal
    public static final Color TEXT_MUTED = new Color(126, 130, 153);   // #7E8299 - Muted Gray
    public static final Color TEXT_LIGHT = new Color(185, 190, 210);

    public static final Color BORDER_COLOR = new Color(228, 230, 239); // #E4E6EF
    public static final Color TABLE_HEADER_BG = new Color(243, 246, 249);
    public static final Color TABLE_ROW_ALT = new Color(250, 251, 253);

    // Common Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Creates a modern rounded button with hover and press effects.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color background = getBackground();
                if (getModel().isPressed()) {
                    background = background.darker();
                } else if (getModel().isRollover()) {
                    background = background.brighter();
                }
                
                g2.setColor(background);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setFont(getFont());
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        button.setFont(FONT_BOLD);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 38));
        return button;
    }

    /**
     * Creates a styled text field with rounded border and padding.
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(FONT_REGULAR);
        textField.setForeground(TEXT_DARK);
        textField.setCaretColor(PRIMARY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, BORDER_COLOR),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }

    /**
     * Creates a styled password field with rounded border.
     */
    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setFont(FONT_REGULAR);
        passwordField.setForeground(TEXT_DARK);
        passwordField.setCaretColor(PRIMARY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, BORDER_COLOR),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return passwordField;
    }

    /**
     * Creates a styled JComboBox.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(FONT_REGULAR);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_DARK);
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, BORDER_COLOR),
                new EmptyBorder(4, 6, 4, 6)
        ));
        return combo;
    }

    /**
     * Creates a card panel with antialiased rounded background and subtle border.
     */
    public static JPanel createCardPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
    }

    /**
     * Custom Border implementation for clean rounded inputs.
     */
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius / 2;
            return insets;
        }
    }
}
