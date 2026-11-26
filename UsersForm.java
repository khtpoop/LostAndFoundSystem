
package view;

import config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class UsersForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private Color backgroundColor = new Color(232, 237, 227);
    private Color sageGreen = new Color(138, 154, 123);
    private Color darkGreen = new Color(52, 70, 56);
    private Color cream = new Color(250, 249, 245);

    public UsersForm() {

        setTitle("Users Management");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("USERS MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
                "Manage registered users", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                    "User ID", "First Name", "Last Name",
                    "Email", "Contact Number", "User Type"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setBackground(cream);
        table.setForeground(darkGreen);
        table.setSelectionBackground(new Color(213, 224, 204));
        table.setSelectionForeground(darkGreen);
        table.setGridColor(new Color(220, 225, 215));

        JTableHeader header = table.getTableHeader();
        header.setBackground(sageGreen);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(cream);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(cream);
        tableCard.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableCard, BorderLayout.CENTER);

        JButton addButton = createButton("ADD USER");
        JButton refreshButton = createButton("REFRESH");
        JButton backButton = createButton("BACK");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addUser());
        refreshButton.addActionListener(e -> loadUsers());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);

        loadUsers();
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(sageGreen);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void loadUsers() {

        model.setRowCount(0);

        String sql = "SELECT user_id, first_name, last_name, "
                + "email, contact_number, user_type FROM users";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed!");
                return;
            }

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {

                    model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("contact_number"),
                        rs.getString("user_type")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {

        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField email = new JTextField();
        JTextField contact = new JTextField();
        JPasswordField password = new JPasswordField();

        JComboBox<String> userType = new JComboBox<>(
                new String[]{"Student", "Teacher", "Staff", "Admin"});

        Object[] fields = {
            "First Name:", firstName,
            "Last Name:", lastName,
            "Email:", email,
            "Contact Number:", contact,
            "Password:", password,
            "User Type:", userType
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "ADD NEW USER",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (firstName.getText().trim().isEmpty()
                || lastName.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()
                || new String(password.getPassword()).isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please complete all required fields!");
            return;
        }

        String sql = "INSERT INTO users "
                + "(first_name, last_name, email, contact_number, "
                + "user_type, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, firstName.getText().trim());
                pst.setString(2, lastName.getText().trim());
                pst.setString(3, email.getText().trim());
                pst.setString(4, contact.getText().trim());
                pst.setString(5, userType.getSelectedItem().toString());
                pst.setString(6, new String(password.getPassword()));

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "User added successfully!");

                loadUsers();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding user: " + e.getMessage());
        }
    }
}
