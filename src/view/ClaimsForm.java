
package view;

import config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ClaimsForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private int userId;
    private String userType;

    private final Color backgroundColor = new Color(232, 237, 227);
    private final Color sageGreen = new Color(138, 154, 123);
    private final Color darkGreen = new Color(52, 70, 56);
    private final Color cream = new Color(250, 249, 245);

    public ClaimsForm(int userId, String userType) {

        this.userId = userId;
        this.userType = userType;

        setTitle("Claims Management");
        setSize(1100, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel title = new JLabel(
            "CLAIMS MANAGEMENT", JLabel.CENTER
        );
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
            "Manage item claims and requests", JLabel.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{
                "Claim ID",
                "Item ID",
                "User ID",
                "Claim Date",
                "Proof Description",
                "Claim Status"
            }, 0
        ) {
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
        tableCard.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableCard, BorderLayout.CENTER);

        JButton addButton = createButton("ADD CLAIM");
        JButton approveButton = createButton("APPROVE CLAIM");
        JButton refreshButton = createButton("REFRESH");
        JButton backButton = createButton("BACK");

        // Set button permissions
        addButton.setEnabled(!isAdmin());
        approveButton.setEnabled(isAdmin());

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 5)
        );
        buttonPanel.setOpaque(false);

        buttonPanel.add(addButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addClaim());
        approveButton.addActionListener(e -> approveClaim());
        refreshButton.addActionListener(e -> loadClaims());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);

        loadClaims();
    }

    private boolean isAdmin() {
        return userType != null
                && userType.trim().equalsIgnoreCase("Admin");
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(175, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(sageGreen);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // LOAD CLAIMS
    private void loadClaims() {

        model.setRowCount(0);

        String sql;

        if (isAdmin()) {
            sql = "SELECT * FROM claims ORDER BY claim_id DESC";
        } else {
            sql = "SELECT * FROM claims WHERE user_id = ? "
                    + "ORDER BY claim_id DESC";
        }

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(
                    this, "Database connection failed!"
                );
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                if (!isAdmin()) {
                    pst.setInt(1, userId);
                }

                try (ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {

                        model.addRow(new Object[]{
                            rs.getInt("claim_id"),
                            rs.getInt("item_id"),
                            rs.getInt("user_id"),
                            rs.getDate("claim_date"),
                            rs.getString("proof_description"),
                            rs.getString("claim_status")
                        });
                    }
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this, "Error loading claims: " + e.getMessage()
            );
        }
    }

    // ADD CLAIM
    private void addClaim() {

        if (isAdmin()) {
            JOptionPane.showMessageDialog(
                this, "Admin accounts cannot submit claims."
            );
            return;
        }

        JTextField itemId = new JTextField();
        JTextField claimDate = new JTextField(
            java.time.LocalDate.now().toString()
        );
        JTextField proofDescription = new JTextField();

        Object[] fields = {
            "Item ID:", itemId,
            "Claim Date (YYYY-MM-DD):", claimDate,
            "Proof Description:", proofDescription
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "ADD NEW CLAIM",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (itemId.getText().trim().isEmpty()
                || claimDate.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Please complete all required fields!"
            );
            return;
        }

        String checkItem =
            "SELECT user_id, item_type, item_status "
            + "FROM items WHERE item_id = ? FOR UPDATE";

        String checkClaim =
            "SELECT claim_id FROM claims "
            + "WHERE item_id = ? AND claim_status = 'Pending'";

        String insertClaim =
            "INSERT INTO claims "
            + "(item_id, user_id, claim_date, proof_description, claim_status) "
            + "VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(
                    this, "Database connection failed!"
                );
                return;
            }

            con.setAutoCommit(false);

            try {

                int id = Integer.parseInt(itemId.getText().trim());

                // Check if the item exists
                try (PreparedStatement pst =
                        con.prepareStatement(checkItem)) {

                    pst.setInt(1, id);

                    try (ResultSet rs = pst.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Item not found!");
                        }

                        int ownerId = rs.getInt("user_id");
                        String type = rs.getString("item_type");
                        String status = rs.getString("item_status");

                        // Only Found items can be claimed
                        if (type == null
                                || !type.equalsIgnoreCase("Found")) {

                            throw new SQLException(
                                "You can only claim Found items!"
                            );
                        }

                        // Users cannot claim their own items
                        if (ownerId == userId) {
                            throw new SQLException(
                                "You cannot claim your own item!"
                            );
                        }

                        // Check if already claimed
                        if (status != null
                                && status.equalsIgnoreCase("Claimed")) {

                            throw new SQLException(
                                "This item has already been claimed!"
                            );
                        }
                    }
                }

                // Prevent multiple pending claims for one item
                try (PreparedStatement pst =
                        con.prepareStatement(checkClaim)) {

                    pst.setInt(1, id);

                    try (ResultSet rs = pst.executeQuery()) {

                        if (rs.next()) {
                            throw new SQLException(
                                "This item already has a pending claim!"
                            );
                        }
                    }
                }

                // Insert claim using the logged-in user's ID
                try (PreparedStatement pst =
                        con.prepareStatement(insertClaim)) {

                    pst.setInt(1, id);
                    pst.setInt(2, userId);
                    pst.setDate(3, java.sql.Date.valueOf(
                        claimDate.getText().trim()
                    ));
                    pst.setString(4,
                        proofDescription.getText().trim()
                    );

                    pst.executeUpdate();
                }

                con.commit();

                JOptionPane.showMessageDialog(
                    this,
                    "Claim submitted successfully!\n"
                    + "Your claim is now Pending."
                );

                loadClaims();

            } catch (Exception e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Item ID must be a number!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                this, "Invalid date! Use YYYY-MM-DD."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this, "Error adding claim: " + e.getMessage()
            );
        }
    }

    // APPROVE CLAIM
    private void approveClaim() {

        if (!isAdmin()) {
            JOptionPane.showMessageDialog(
                this, "Only Admin can approve claims!"
            );
            return;
        }

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a claim first!"
            );
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int claimId = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int itemId = Integer.parseInt(
            model.getValueAt(row, 1).toString()
        );

        String status = model.getValueAt(row, 5).toString();

        if (!status.equalsIgnoreCase("Pending")) {
            JOptionPane.showMessageDialog(
                this, "Only Pending claims can be approved!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Approve this claim?\n"
            + "The item status will become Claimed.\n"
            + "The item and claim records will be kept.",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String updateClaim =
            "UPDATE claims SET claim_status = 'Approved' "
            + "WHERE claim_id = ? AND claim_status = 'Pending'";

        String updateItem =
            "UPDATE items SET item_status = 'Claimed' "
            + "WHERE item_id = ? AND item_status <> 'Claimed'";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(
                    this, "Database connection failed!"
                );
                return;
            }

            con.setAutoCommit(false);

            try {

                // Update item status
                try (PreparedStatement pst =
                        con.prepareStatement(updateItem)) {

                    pst.setInt(1, itemId);

                    int updatedItem = pst.executeUpdate();

                    if (updatedItem == 0) {
                        throw new SQLException(
                            "Item was not found or is already claimed."
                        );
                    }
                }

                // Approve selected claim
                try (PreparedStatement pst =
                        con.prepareStatement(updateClaim)) {

                    pst.setInt(1, claimId);

                    int updatedClaim = pst.executeUpdate();

                    if (updatedClaim == 0) {
                        throw new SQLException(
                            "Claim was not found or is no longer Pending."
                        );
                    }
                }

                con.commit();

                JOptionPane.showMessageDialog(
                    this,
                    "Claim approved successfully!\n"
                    + "Item status is now Claimed.\n"
                    + "Records have been kept."
                );

                loadClaims();

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this, "Error approving claim: " + e.getMessage()
            );
        }
    }
}
