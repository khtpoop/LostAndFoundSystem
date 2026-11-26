/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package view;

import config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class LocationsForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private final Color backgroundColor = new Color(232, 237, 227);
    private final Color sageGreen = new Color(138, 154, 123);
    private final Color darkGreen = new Color(52, 70, 56);
    private final Color cream = new Color(250, 249, 245);

    public LocationsForm() {
        setTitle("Locations Management");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("LOCATIONS MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel("Manage reported item locations", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
            "Location ID", "Location Name", "Building", "Description"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
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
        tableCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableCard, BorderLayout.CENTER);

        JButton addButton = createButton("ADD LOCATION");
        JButton refreshButton = createButton("REFRESH");
        JButton backButton = createButton("BACK");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addLocation());
        refreshButton.addActionListener(e -> loadLocations());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);
        loadLocations();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(160, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(sageGreen);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadLocations() {
        model.setRowCount(0);

        String sql = "SELECT * FROM locations";

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
                        rs.getInt("location_id"),
                        rs.getString("location_name"),
                        rs.getString("building"),
                        rs.getString("description")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading locations: " + e.getMessage());
        }
    }

    private void addLocation() {

        JTextField locationName = new JTextField();
        JTextField building = new JTextField();
        JTextField description = new JTextField();

        Object[] fields = {
            "Location Name:", locationName,
            "Building:", building,
            "Description:", description
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "ADD NEW LOCATION",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (locationName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a location name!");
            return;
        }

        String sql = "INSERT INTO locations "
            + "(location_name, building, description) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, locationName.getText().trim());
                pst.setString(2, building.getText().trim());
                pst.setString(3, description.getText().trim());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Location added successfully!");

                loadLocations();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error adding location: " + e.getMessage());
        }
    }
}
