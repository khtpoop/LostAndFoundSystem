
package view;

import view.LoginForm;
import config.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class RegisterForm extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField contactField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JComboBox<String> userTypeBox;

    public RegisterForm() {

        setTitle("Lost and Found Management System");
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                // Background
                g2.setColor(new Color(232, 237, 227));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(213, 224, 204));
                g2.fillOval(-60, -70, 200, 200);
                g2.fillOval(420, 650, 180, 180);

                // Cream card
                g2.setColor(new Color(250, 249, 245));
                g2.fill(new RoundRectangle2D.Float(
                    55, 20, 440, 690, 25, 25
                ));

                g2.dispose();
            }
        };

        // Title
        JLabel title = new JLabel("LOST & FOUND", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(52, 70, 56));
        title.setBounds(100, 35, 350, 45);
        panel.add(title);

        JLabel subtitle = new JLabel(
            "MANAGEMENT SYSTEM", JLabel.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));
        subtitle.setBounds(100, 75, 350, 25);
        panel.add(subtitle);

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Serif", Font.BOLD, 22));
        heading.setForeground(new Color(52, 70, 56));
        heading.setBounds(125, 110, 300, 30);
        panel.add(heading);

        // First Name
        addLabel(panel, "First Name", 125, 150);
        firstNameField = addTextField(panel, 125, 175);

        // Last Name
        addLabel(panel, "Last Name", 125, 215);
        lastNameField = addTextField(panel, 125, 240);

        // Email
        addLabel(panel, "Email Address", 125, 280);
        emailField = addTextField(panel, 125, 305);

        // Contact
        addLabel(panel, "Contact Number", 125, 345);
        contactField = addTextField(panel, 125, 370);

        // User Type
        addLabel(panel, "User Type", 125, 410);

        userTypeBox = new JComboBox<>(
            new String[]{"Student", "Staff", "Admin"}
        );
        userTypeBox.setBounds(125, 435, 300, 35);
        panel.add(userTypeBox);

        // Password
        addLabel(panel, "Password", 125, 475);
        passwordField = new JPasswordField();
        passwordField.setBounds(125, 500, 300, 35);
        panel.add(passwordField);

        // Confirm Password
        addLabel(panel, "Confirm Password", 125, 540);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(125, 565, 300, 35);
        panel.add(confirmPasswordField);

        // Register Button
        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(125, 615, 300, 38);
        styleButton(registerButton);
        panel.add(registerButton);

        registerButton.addActionListener(e -> register());

        // Back to Login
        JButton backButton = new JButton("BACK TO LOGIN");
        backButton.setBounds(125, 660, 300, 30);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setForeground(new Color(52, 70, 56));
        backButton.setBackground(new Color(250, 249, 245));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        panel.add(backButton);

        backButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(52, 70, 56));
        label.setBounds(x, y, 300, 22);
        panel.add(label);
    }

    private JTextField addTextField(
            JPanel panel, int x, int y) {

        JTextField field = new JTextField();
        field.setBounds(x, y, 300, 35);
        panel.add(field);
        return field;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(138, 154, 123));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void register() {

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String userType = userTypeBox.getSelectedItem().toString();

        String password = new String(passwordField.getPassword());
        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || contact.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please complete all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address!");
            return;
        }

        String sql = "INSERT INTO users "
                + "(first_name, last_name, email, contact_number, user_type, password) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setString(3, email);
                pst.setString(4, contact);
                pst.setString(5, userType);
                pst.setString(6, password);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Registration Successful!");

                new LoginForm().setVisible(true);
                dispose();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Registration Failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
    }
}
