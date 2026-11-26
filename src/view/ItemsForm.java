
package view;

import config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ItemsForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private final int userId;

    private final Color backgroundColor = new Color(232, 237, 227);
    private final Color sageGreen = new Color(138, 154, 123);
    private final Color darkGreen = new Color(52, 70, 56);
    private final Color cream = new Color(250, 249, 245);

    public ItemsForm(int userId) {

        this.userId = userId;

        setTitle("Items Management");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("ITEMS MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel("Manage lost and found items", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
            "Item ID", "User ID", "Location ID", "Item Name",
            "Category", "Description", "Date Reported",
            "Item Type", "Item Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setBackground(cream);
        table.setForeground(darkGreen);
        table.setSelectionBackground(new Color(213, 224, 204));
        table.setSelectionForeground(darkGreen);
        table.setGridColor(new Color(220, 225, 215));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setBackground(sageGreen);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(cream);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(cream);
        tableCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableCard, BorderLayout.CENTER);

        JButton addButton = createButton("ADD ITEM");
        JButton editButton = createButton("EDIT ITEM");
        JButton deleteButton = createButton("DELETE ITEM");
        JButton refreshButton = createButton("REFRESH");
        JButton backButton = createButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 10, 5)
        );
        buttonPanel.setOpaque(false);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addItem());
        editButton.addActionListener(e -> editItem());
        deleteButton.addActionListener(e -> deleteItem());
        refreshButton.addActionListener(e -> loadItems());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);
        loadItems();
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

    // LOAD ONLY THE LOGGED-IN USER'S ITEMS
    private void loadItems() {

        model.setRowCount(0);

        String sql = "SELECT * FROM items "
                   + "WHERE user_id = ? "
                   + "AND (item_status IS NULL OR item_status <> 'Claimed')";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, userId);

                try (ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {

                        model.addRow(new Object[]{
                            rs.getInt("item_id"),
                            rs.getInt("user_id"),
                            rs.getInt("location_id"),
                            rs.getString("item_name"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getDate("date_reported"),
                            rs.getString("item_type"),
                            rs.getString("item_status")
                        });
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading items: " + e.getMessage());
        }
    }

    // ADD ITEM
    private void addItem() {

        JTextField userIdField = new JTextField(String.valueOf(userId));
        userIdField.setEditable(false);

        JTextField locationId = new JTextField();
        JTextField itemName = new JTextField();
        JTextField category = new JTextField();
        JTextField description = new JTextField();
        JTextField dateReported = new JTextField();

        JComboBox<String> itemType = new JComboBox<>(
            new String[]{"Lost", "Found"}
        );

        JComboBox<String> itemStatus = new JComboBox<>(
            new String[]{"Pending", "Returned"}
        );

        Object[] fields = {
            "User ID:", userIdField,
            "Location ID:", locationId,
            "Item Name:", itemName,
            "Category:", category,
            "Description:", description,
            "Date Reported (YYYY-MM-DD):", dateReported,
            "Item Type:", itemType,
            "Item Status:", itemStatus
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "ADD NEW ITEM",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (locationId.getText().trim().isEmpty()
                || itemName.getText().trim().isEmpty()
                || dateReported.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please complete all required fields!");
            return;
        }

        String sql = "INSERT INTO items "
            + "(user_id, location_id, item_name, category, description, "
            + "date_reported, item_type, item_status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                // Automatically use the logged-in user's ID
                pst.setInt(1, userId);
                pst.setInt(2, Integer.parseInt(locationId.getText().trim()));
                pst.setString(3, itemName.getText().trim());
                pst.setString(4, category.getText().trim());
                pst.setString(5, description.getText().trim());
                pst.setDate(6, java.sql.Date.valueOf(dateReported.getText().trim()));
                pst.setString(7, itemType.getSelectedItem().toString());
                pst.setString(8, itemStatus.getSelectedItem().toString());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Item added successfully!");

                loadItems();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Location ID must be a number!");

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date! Use YYYY-MM-DD.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error adding item: " + e.getMessage());
        }
    }

    // EDIT ITEM
    private void editItem() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to edit!");
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int itemId = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        JTextField userIdField = new JTextField(
            model.getValueAt(row, 1).toString()
        );
        userIdField.setEditable(false);

        JTextField locationId = new JTextField(
            model.getValueAt(row, 2).toString()
        );

        JTextField itemName = new JTextField(
            model.getValueAt(row, 3).toString()
        );

        JTextField category = new JTextField(
            model.getValueAt(row, 4) == null ? "" :
            model.getValueAt(row, 4).toString()
        );

        JTextField description = new JTextField(
            model.getValueAt(row, 5) == null ? "" :
            model.getValueAt(row, 5).toString()
        );

        JTextField dateReported = new JTextField(
            model.getValueAt(row, 6) == null ? "" :
            model.getValueAt(row, 6).toString()
        );

        JComboBox<String> itemType = new JComboBox<>(
            new String[]{"Lost", "Found"}
        );
        itemType.setSelectedItem(model.getValueAt(row, 7).toString());

        JComboBox<String> itemStatus = new JComboBox<>(
            new String[]{"Pending", "Returned"}
        );
        itemStatus.setSelectedItem(model.getValueAt(row, 8) == null
            ? "Pending" : model.getValueAt(row, 8).toString());

        Object[] fields = {
            "User ID:", userIdField,
            "Location ID:", locationId,
            "Item Name:", itemName,
            "Category:", category,
            "Description:", description,
            "Date Reported (YYYY-MM-DD):", dateReported,
            "Item Type:", itemType,
            "Item Status:", itemStatus
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "EDIT ITEM",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (locationId.getText().trim().isEmpty()
                || itemName.getText().trim().isEmpty()
                || dateReported.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please complete all required fields!");
            return;
        }

        // Update only if the item belongs to the logged-in user
        String sql = "UPDATE items SET location_id=?, "
            + "item_name=?, category=?, description=?, date_reported=?, "
            + "item_type=?, item_status=? "
            + "WHERE item_id=? AND user_id=?";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, Integer.parseInt(locationId.getText().trim()));
                pst.setString(2, itemName.getText().trim());
                pst.setString(3, category.getText().trim());
                pst.setString(4, description.getText().trim());
                pst.setDate(5, java.sql.Date.valueOf(dateReported.getText().trim()));
                pst.setString(6, itemType.getSelectedItem().toString());
                pst.setString(7, itemStatus.getSelectedItem().toString());
                pst.setInt(8, itemId);
                pst.setInt(9, userId);

                int updated = pst.executeUpdate();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Item updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "You cannot edit this item!");
                }

                loadItems();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Location ID must be a number!");

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date! Use YYYY-MM-DD.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error updating item: " + e.getMessage());
        }
    }

    // DELETE ITEM
    private void deleteItem() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to delete!");
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int itemId = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this item?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Delete only if the item belongs to the logged-in user
        String sql = "DELETE FROM items WHERE item_id = ? AND user_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Database connection failed!");
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, itemId);
                pst.setInt(2, userId);

                int deleted = pst.executeUpdate();

                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Item deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "You cannot delete this item!");
                }

                loadItems();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Unable to delete item. It may have existing claims.\n"
                + e.getMessage());
        }
    }
}
