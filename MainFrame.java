/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package view;

import controller.LoginController;
import controller.RegisterController;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import model.User;
import controller.CrudController;
import model.User;

public class MainFrame extends javax.swing.JFrame {
    private CrudController crudController = new CrudController();
    private User loggedInUser;

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(MainFrame.class.getName());

    private final String DB_URL =
            "jdbc:mysql://localhost:3306/lost_and_found_db";

    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private CardLayout contentCardLayout;
    private JPanel contentCards;

    private JPanel usersPanel;
    private JPanel itemsPanel;
    private JPanel locationPanel;
    private JPanel claimsPanel;

    private final Map<String, CrudPage> crudPages =
            new HashMap<>();

    
private static class CrudPage {

    String tableName;
    String idColumn;
    String[] fields;
    String[] requiredFields;
    String[] displayColumns;

    JTable table;

    Map<String, JTextField> inputs =
            new LinkedHashMap<>();

    Map<String, javax.swing.JComboBox<String>> dropdowns =
            new LinkedHashMap<>();

    CrudPage(
            String tableName,
            String idColumn,
            String[] fields,
            String[] requiredFields,
            String[] displayColumns) {

        this.tableName = tableName;
        this.idColumn = idColumn;
        this.fields = fields;
        this.requiredFields = requiredFields;
        this.displayColumns = displayColumns;
    }
}


    public MainFrame() {

    initComponents();

       txtEmail.setText("Enter your email address");

    txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (txtEmail.getText().equals("Enter your email address")) {
                txtEmail.setText("");
            }
        }
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (txtEmail.getText().trim().isEmpty()) {
                txtEmail.setText("Enter your email address");
            }
        }
    });

    // PASSWORD PLACEHOLDER
    jPasswordField1.setEchoChar((char) 0);
    jPasswordField1.setText("Your password");

    jPasswordField1.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (new String(jPasswordField1.getPassword()).equals("Your password")) {
                jPasswordField1.setText("");
                jPasswordField1.setEchoChar('•');
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (jPasswordField1.getPassword().length == 0) {
                jPasswordField1.setEchoChar((char) 0);
                jPasswordField1.setText("Your password");
            }
        }
    });

    setupContentCards();
    setupSidebarNavigation();

    new LoginController(this);
    new RegisterController(this);
}

    public javax.swing.JButton getLoginButton() {
        return btnLoginSubmit;
    }

    public String getEmail() {
        return txtEmail.getText().trim();
    }

    public String getPassword() {
    return new String(jPasswordField1.getPassword());
}

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    public void openDashboard(User user) {

        this.loggedInUser = user;
        

for (String key : new String[]{"ITEMS", "CLAIMS"}) {

    CrudPage page = crudPages.get(key);

    if (page != null && page.inputs.get("user_id") != null) {

        page.inputs.get("user_id").setText(
                String.valueOf(user.getUserId())
        );
    }
}
    contentCardLayout.show(contentCards, "WELCOME");

    btnLogin.setVisible(false);
    btnRegister.setVisible(false);

    btnUsers.setVisible(true);
    btnItems.setVisible(true);
    btnLocation.setVisible(true);
    btnClaims.setVisible(true);
    btnLogout.setVisible(true);

    revalidate();
    repaint();
}

    public javax.swing.JButton getRegisterButton() {
        return btnRegisterSubmit;
    }

    public String getFirstName() {
        return txtFirstName.getText().trim();
    }

    public String getLastName() {
        return txtLastName.getText().trim();
    }

    public String getRegisterEmail() {
        return txtRegisterEmail.getText().trim();
    }

    public String getContactNo() {
        return txtContactNo.getText().trim();
    }

    public String getRegisterPassword() {
        return new String(txtRegisterPassword.getPassword());
    }

    public String getUserType() {
        return cmbUserType.getSelectedItem().toString();
    }

    private void setupContentCards() {

        contentCards = new JPanel(new CardLayout());

        contentCards.add(pnContent, "WELCOME");

        usersPanel = createCrudPage(
                "USERS MANAGEMENT",
                "USERS",
                "users",
                "user_id",
                new String[]{
                    "first_name",
                    "last_name",
                    "email",
                    "contact_number",
                    "user_type",
                    "password"
                },
                new String[]{
                    "first_name",
                    "last_name",
                    "email",
                    "user_type"
                },
                new String[]{
                    "user_id",
                    "first_name",
                    "last_name",
                    "email",
                    "contact_number",
                    "user_type"
                }
        );

        itemsPanel = createCrudPage(
                "ITEMS MANAGEMENT",
                "ITEMS",
                "items",
                "item_id",
                new String[]{
                    "user_id",
                    "location_id",
                    "item_name",
                    "category",
                    "description",
                    "date_reported",
                    "item_type",
                    "item_status"
                },
                new String[]{
                    "user_id",
                    "location_id",
                    "item_name",
                    "date_reported",
                    "item_type",
                    "item_status"
                },
                new String[]{
                    "item_id",
                    "user_id",
                    "location_id",
                    "item_name",
                    "category",
                    "description",
                    "date_reported",
                    "item_type",
                    "item_status"
                }
        );

        locationPanel = createCrudPage(
                "LOCATIONS MANAGEMENT",
                "LOCATION",
                "locations",
                "location_id",
                new String[]{
                    "location_name",
                    "building",
                    "description"
                },
                new String[]{
                    "location_name"
                },
                new String[]{
                    "location_id",
                    "location_name",
                    "building",
                    "description"
                }
        );

        
claimsPanel = createCrudPage(
        "CLAIMS MANAGEMENT",
        "CLAIMS",
        "claims",
        "claim_id",
        new String[]{
            "item_id",
            "user_id",
            "location_id",
            "claim_date",
            "proof_description",
            "claim_status"
        },
        new String[]{
            "item_id",
            "user_id",
            "location_id",
            "claim_date"
        },
        new String[]{
            "claim_id",
            "item_id",
            "user_id",
            "location_id",
            "claim_date",
            "proof_description",
            "claim_status"
        }
);

        contentCards.add(usersPanel, "USERS");
        contentCards.add(itemsPanel, "ITEMS");
        contentCards.add(locationPanel, "LOCATION");
        contentCards.add(claimsPanel, "CLAIMS");

        contentCardLayout =
                (CardLayout) contentCards.getLayout();

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(
                pnlSidebar,
                BorderLayout.WEST
        );

        getContentPane().add(
                contentCards,
                BorderLayout.CENTER
        );

        revalidate();
        repaint();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createCrudPage(
            String title,
            String pageKey,
            String tableName,
            String idColumn,
            String[] fields,
            String[] requiredFields,
            String[] displayColumns) {

        CrudPage page = new CrudPage(
                tableName,
                idColumn,
                fields,
                requiredFields,
                displayColumns
        );

        crudPages.put(pageKey, page);

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);

        JLabel heading = new JLabel(title);

        heading.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 10, 15
                )
        );

        JPanel form =
                new JPanel(new GridLayout(0, 2, 10, 8));

        form.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 15
                )
        );

        

for (String field : fields) {

    form.add(new JLabel(
            field.replace("_", " ").toUpperCase()
    ));

    // AUTOMATIC LOGGED-IN USER ID
    if (field.equals("user_id")
            && (tableName.equals("items")
            || tableName.equals("claims"))) {

        JTextField input = new JTextField();
        input.setEditable(false);

        if (loggedInUser != null) {
            input.setText(
                    String.valueOf(loggedInUser.getUserId())
            );
        }

        page.inputs.put(field, input);
        form.add(input);

    }

    // LOCATION DROPDOWN
    else if (field.equals("location_id")
            && (tableName.equals("items")
            || tableName.equals("claims"))) {

        javax.swing.JComboBox<String> dropdown =
                new javax.swing.JComboBox<>();

        loadLocationOptions(dropdown);

        page.dropdowns.put(field, dropdown);
        form.add(dropdown);

    }

    // ITEM TYPE DROPDOWN
    else if (field.equals("item_type")
            && tableName.equals("items")) {

        javax.swing.JComboBox<String> dropdown =
                new javax.swing.JComboBox<>(
                        new String[]{"Lost", "Found"}
                );

        page.dropdowns.put(field, dropdown);
        form.add(dropdown);

    }

    // ITEM STATUS DROPDOWN
    else if (field.equals("item_status")
            && tableName.equals("items")) {

        javax.swing.JComboBox<String> dropdown =
                new javax.swing.JComboBox<>(
                        new String[]{"Missing", "Found", "Claimed"}
                );

        page.dropdowns.put(field, dropdown);
        form.add(dropdown);

    }

    // CLAIM STATUS DROPDOWN
    else if (field.equals("claim_status")
            && tableName.equals("claims")) {

        javax.swing.JComboBox<String> dropdown =
                new javax.swing.JComboBox<>(
                        new String[]{"Pending", "Approved", "Rejected"}
                );

        page.dropdowns.put(field, dropdown);
        form.add(dropdown);

    }

    // OTHER TEXT FIELDS
    else {

        JTextField input;

        if (field.equals("password")) {
            input = new JPasswordField();
        } else {
            input = new JTextField();
        }

        page.inputs.put(field, input);
        form.add(input);
    }
}


        JButton add = new JButton("ADD");
        JButton update = new JButton("UPDATE");
        JButton delete = new JButton("DELETE");
        JButton clear = new JButton("CLEAR");
        JButton claimItem = new JButton("CLAIM ITEM");

claimItem.setVisible(tableName.equals("items"));

        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);
        buttons.add(claimItem);

        JPanel top = new JPanel(new BorderLayout());

        top.add(heading, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        String[] headings =
                new String[displayColumns.length];

        for (int i = 0; i < displayColumns.length; i++) {

            headings[i] = displayColumns[i]
                    .replace("_", " ")
                    .toUpperCase();
        }

        DefaultTableModel model =
                new DefaultTableModel(headings, 0) {

            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        page.table = new JTable(model);

        page.table.setRowHeight(28);

        page.table.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        page.table.getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && page.table.getSelectedRow() != -1) {

                selectCrudRow(page);
            }
        });

        add.addActionListener(e ->
                addCrudRecord(page));

        update.addActionListener(e ->
                updateCrudRecord(page));

        delete.addActionListener(e ->
                deleteCrudRecord(page));

        clear.addActionListener(e ->
                clearCrudFields(page));
        

claimItem.addActionListener(e -> {

    if (!tableName.equals("items")) {
        return;
    }

    int selectedRow = page.table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select an item first."
        );
        return;
    }

    CrudPage claimsPage = crudPages.get("CLAIMS");

    if (claimsPage == null) {
        return;
    }

    int itemIdColumn = Arrays.asList(
            page.displayColumns
    ).indexOf("item_id");

    int locationColumn = Arrays.asList(
            page.displayColumns
    ).indexOf("location_id");

    if (itemIdColumn < 0 || locationColumn < 0) {
        JOptionPane.showMessageDialog(
                this,
                "Item ID or Location ID column not found."
        );
        return;
    }

    String itemId = page.table.getValueAt(
            selectedRow, itemIdColumn
    ).toString();

    String locationId = page.table.getValueAt(
            selectedRow, locationColumn
    ).toString();

    claimsPage.inputs.get("item_id").setText(itemId);

    if (loggedInUser != null) {
        claimsPage.inputs.get("user_id").setText(
                String.valueOf(loggedInUser.getUserId())
        );
    }

    claimsPage.inputs.get("claim_date").setText(
            java.time.LocalDate.now().toString()
    );

    javax.swing.JComboBox<String> locationDropdown =
            claimsPage.dropdowns.get("location_id");

    for (int i = 0; i < locationDropdown.getItemCount(); i++) {

        String option = locationDropdown.getItemAt(i);

        if (option.startsWith(locationId + " - ")) {
            locationDropdown.setSelectedIndex(i);
            break;
        }
    }

    javax.swing.JComboBox<String> statusDropdown =
            claimsPage.dropdowns.get("claim_status");

    statusDropdown.setSelectedItem("Pending");

    claimsPage.inputs.get("proof_description").setText("");

    contentCardLayout.show(contentCards, "CLAIMS");

});

        panel.add(top, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(page.table),
                BorderLayout.CENTER
        );

        return panel;
    }

    private String getCrudInputValue(JTextField input) {

        if (input instanceof JPasswordField) {

            return new String(
                    ((JPasswordField) input).getPassword()
            ).trim();
        }

        return input.getText().trim();
    }

private void loadLocationOptions(
        javax.swing.JComboBox<String> dropdown) {

    String sql = "SELECT location_id, location_name FROM locations ORDER BY location_id ASC";

    try (
        Connection con = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD);

        PreparedStatement pst = con.prepareStatement(sql);

        ResultSet rs = pst.executeQuery()
    ) {

        dropdown.removeAllItems();

        while (rs.next()) {

            String location =
                    rs.getInt("location_id")
                    + " - "
                    + rs.getString("location_name");

            dropdown.addItem(location);
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to load locations: " + e.getMessage()
        );
    }
}
private String getCrudFieldValue(CrudPage page, String field) {

    if (page.dropdowns.containsKey(field)) {

        Object selected =
                page.dropdowns.get(field).getSelectedItem();

        if (selected == null) {
            return "";
        }

        return selected.toString().split(" - ", 2)[0].trim();
    }

    JTextField input = page.inputs.get(field);

    if (input == null) {
        return "";
    }

    return getCrudInputValue(input);
}

    private void setCrudValue(
            PreparedStatement pst,
            int parameter,
            String field,
            String value) throws SQLException {

        if (value.isEmpty()) {
            pst.setNull(parameter, Types.NULL);
            return;
        }

        if (field.equals("date_reported")
                || field.equals("claim_date")) {

            pst.setDate(
                    parameter,
                    Date.valueOf(value)
            );

        } else {
            pst.setString(parameter, value);
        }
    }

    // LOAD RECORDS
    // Claimed items will not appear in the Items table.

    private void loadCrudRecords(CrudPage page) {

        if (page == null) {
            return;
        }

        DefaultTableModel model =
                (DefaultTableModel) page.table.getModel();

        model.setRowCount(0);

        String sql =
                "SELECT "
                + String.join(", ", page.displayColumns)
                + " FROM " + page.tableName;

        if (page.tableName.equals("items")) {

            sql += " WHERE item_status IS NULL "
                    + "OR LOWER(item_status) <> 'claimed'";
        }

        sql += " ORDER BY " + page.idColumn + " ASC";

        try (
            Connection con = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement pst =
                    con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                Object[] row =
                        new Object[page.displayColumns.length];

                for (int i = 0;
                        i < page.displayColumns.length;
                        i++) {

                    row[i] = rs.getObject(
                            page.displayColumns[i]
                    );
                }

                model.addRow(row);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load records: "
                    + e.getMessage()
            );
        }
    }

    // ADD RECORD

    

private void addCrudRecord(CrudPage page) {

    try {

        // CHECK REQUIRED FIELDS
        for (String field : page.requiredFields) {

            if (getCrudFieldValue(page, field).isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in "
                        + field.replace("_", " ")
                );

                return;
            }
        }

        // GET FIELD VALUES
        String[] values = new String[page.fields.length];

        for (int i = 0; i < page.fields.length; i++) {

            String field = page.fields[i];

            String value = getCrudFieldValue(page, field);

            if (page.tableName.equals("claims")
                    && field.equals("claim_status")
                    && value.isEmpty()) {

                value = "Pending";
            }

            values[i] = value;
        }

        // SAVE RECORD
        crudController.addRecord(
                page.tableName,
                page.fields,
                values
        );

        // IF CLAIM IS APPROVED, MARK ITEM AS CLAIMED
        if (page.tableName.equals("claims")) {

            String status = "";

            for (int i = 0; i < page.fields.length; i++) {

                if (page.fields[i].equals("claim_status")) {
                    status = values[i];
                }
            }

            if (status.equalsIgnoreCase("Approved")) {

                String itemId = getCrudFieldValue(
                        page,
                        "item_id"
                );

                crudController.markItemAsClaimed(
                        Integer.parseInt(itemId)
                );
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Record added successfully!"
        );

        clearCrudFields(page);
        loadCrudRecords(page);

    } catch (IllegalArgumentException | SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to add record: " + e.getMessage()
        );
    }
}


    

    
private void updateCrudRecord(CrudPage page) {

    int selectedRow = page.table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a record from the table."
        );
        return;
    }

    try {

        // CHECK REQUIRED FIELDS
        for (String field : page.requiredFields) {

            if (getCrudFieldValue(page, field).isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in "
                        + field.replace("_", " ")
                );
                return;
            }
        }

        List<String> updateFields = new ArrayList<>();
        List<String> updateValues = new ArrayList<>();

        // GET VALUES FROM TEXT FIELDS AND DROPDOWNS
        for (String field : page.fields) {

            String value = getCrudFieldValue(page, field);

            if (field.equals("password") && value.isEmpty()) {
                continue;
            }

            updateFields.add(field);
            updateValues.add(value);
        }

        // GET SELECTED RECORD ID
        int id = Integer.parseInt(
                page.table.getValueAt(selectedRow, 0).toString()
        );

        // UPDATE DATABASE
        crudController.updateRecord(
                page.tableName,
                page.idColumn,
                id,
                updateFields,
                updateValues
        );

        // UPDATE ITEM STATUS IF CLAIM IS APPROVED
        if (page.tableName.equals("claims")) {

            String status = getCrudFieldValue(
                    page, "claim_status"
            );

            if (status.equalsIgnoreCase("Approved")) {

                String itemId = getCrudFieldValue(
                        page, "item_id"
                );

                if (!itemId.isEmpty()) {
                    crudController.markItemAsClaimed(
                            Integer.parseInt(itemId)
                    );
                }
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Record updated successfully!"
        );

        clearCrudFields(page);
        loadCrudRecords(page);

    } catch (IllegalArgumentException | SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to update record: " + e.getMessage()
        );
    }
}

    

    private void deleteCrudRecord(CrudPage page) {

        int selectedRow = page.table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a record from the table."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int id = Integer.parseInt(
                    page.table.getValueAt(selectedRow, 0).toString()
            );

            crudController.deleteRecord(
                    page.tableName,
                    page.idColumn,
                    id
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Record deleted successfully!"
            );

            clearCrudFields(page);
            loadCrudRecords(page);

        } catch (SQLException | NumberFormatException e) {

            if (e instanceof SQLException
                    && ((SQLException) e).getErrorCode() == 1451) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot delete this user because they have existing claim records.\n"
                        + "Please keep the user record to preserve claim history.",
                        "Delete Not Allowed",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to delete record: " + e.getMessage(),
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // MARK ITEM AS CLAIMED

    private void markItemAsClaimed(CrudPage page) {

        if (!page.tableName.equals("claims")) {
            return;
        }

        String status = getCrudInputValue(
                page.inputs.get("claim_status")
        );

        if (!status.equalsIgnoreCase("Approved")) {
            return;
        }

        String itemId = getCrudInputValue(
                page.inputs.get("item_id")
        );

        if (itemId.isEmpty()) {
            return;
        }

        try {

            crudController.markItemAsClaimed(
                    Integer.parseInt(itemId)
            );

        } catch (SQLException | NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update item status: " + e.getMessage()
            );
        }
    }

    // SELECT TABLE ROW

    
private void selectCrudRow(CrudPage page) {

    int row = page.table.getSelectedRow();

    if (row == -1) {
        return;
    }

    for (String field : page.fields) {

        int column = Arrays.asList(
                page.displayColumns
        ).indexOf(field);

        if (column < 0) {
            continue;
        }

        Object value = page.table.getValueAt(row, column);

        String savedValue = value == null
                ? ""
                : value.toString();

        // HANDLE DROPDOWN
        if (page.dropdowns.containsKey(field)) {

            javax.swing.JComboBox<String> dropdown =
                    page.dropdowns.get(field);

            for (int i = 0; i < dropdown.getItemCount(); i++) {

                String option = dropdown.getItemAt(i);

                if (option.startsWith(savedValue + " - ")) {
                    dropdown.setSelectedIndex(i);
                    break;
                }
            }

            continue;
        }

        // HANDLE TEXT FIELD
        JTextField input = page.inputs.get(field);

        if (input == null) {
            continue;
        }

        if (field.equals("password")) {
            input.setText("");
            continue;
        }

        input.setText(savedValue);
    }
}


    // CLEAR FIELDS

   private void clearCrudFields(CrudPage page) {

    for (Map.Entry<String, JTextField> entry
            : page.inputs.entrySet()) {

        String field = entry.getKey();
        JTextField input = entry.getValue();

        if (field.equals("user_id") && loggedInUser != null) {
            input.setText(
                    String.valueOf(loggedInUser.getUserId())
            );
        } else {
            input.setText("");
        }
    }

    for (javax.swing.JComboBox<String> dropdown
            : page.dropdowns.values()) {

        if (dropdown.getItemCount() > 0) {
            dropdown.setSelectedIndex(0);
        }
    }

    page.table.clearSelection();
}
    // SIDEBAR NAVIGATION

    private void setupSidebarNavigation() {

        btnUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                loadCrudRecords(crudPages.get("USERS"));

                contentCardLayout.show(
                        contentCards, "USERS"
                );
            }
        });

        btnItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                loadCrudRecords(crudPages.get("ITEMS"));

                contentCardLayout.show(
                        contentCards, "ITEMS"
                );
            }
        });
                // LOGOUT BUTTON
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int confirm = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    loggedInUser = null;

                    // Return to login and register screen
                    contentCardLayout.show(contentCards, "WELCOME");

                    btnLogin.setVisible(true);
                    btnRegister.setVisible(true);

                    btnUsers.setVisible(false);
                    btnItems.setVisible(false);
                    btnLocation.setVisible(false);
                    btnClaims.setVisible(false);
                    btnLogout.setVisible(false);

                    // Clear login fields
                    txtEmail.setText("Enter your email address");
                    jPasswordField1.setText("Your password");
                    jPasswordField1.setEchoChar((char) 0);

                    revalidate();
                    repaint();
                }
            }
        });

        btnLocation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                loadCrudRecords(crudPages.get("LOCATION"));

                contentCardLayout.show(
                        contentCards, "LOCATION"
                );
            }
        });

        btnClaims.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                loadCrudRecords(crudPages.get("CLAIMS"));

                contentCardLayout.show(
                        contentCards, "CLAIMS"
                );
            }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        pnlSidebar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnLogin = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnRegister = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnUsers = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnItems = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnLocation = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnClaims = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnLogout = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pnContent = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnLoginSubmit = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtRegisterEmail = new javax.swing.JTextField();
        txtContactNo = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        cmbUserType = new javax.swing.JComboBox<>();
        txtRegisterPassword = new javax.swing.JPasswordField();
        btnRegisterSubmit = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlSidebar.setBackground(new java.awt.Color(52, 70, 56));

        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogin.setText("LOGIN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addContainerGap())
        );

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("MANAGEMENT SYSTEM");

        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegister.setText("REGISTER");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnRegister)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(btnRegister)
                .addContainerGap())
        );

        btnUsers.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUsers.setText("USERS");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(btnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(btnUsers)
                .addContainerGap())
        );

        btnItems.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnItems.setText("ITEMS");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addComponent(btnItems, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnItems)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        btnLocation.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLocation.setText("LOCATION");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(btnLocation)
                .addContainerGap())
        );

        btnClaims.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClaims.setText("CLAIMS");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addComponent(btnClaims, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClaims, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LOST AND FOUND");

        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSidebarLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlSidebarLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        pnContent.setBackground(new java.awt.Color(250, 249, 245));

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("WELCOME TO  LOST AND FOUND MANAGEMENT SYSTEM");
        lblWelcome.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel2.setText("jLabel2");

        jPanel10.setBackground(new java.awt.Color(0, 102, 102));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" LOGIN YOUR ACCOUNT");

        txtEmail.setText("Email Address");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        btnLoginSubmit.setBackground(new java.awt.Color(0, 153, 153));
        btnLoginSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLoginSubmit.setText("LOGIN");
        btnLoginSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginSubmitActionPerformed(evt);
            }
        });

        jPasswordField1.setText("jPasswordField1");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLoginSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(49, 49, 49))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(53, 53, 53)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnLoginSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(0, 102, 102));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("REGISTER");

        txtFirstName.setText("First Name");
        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        txtRegisterEmail.setText("Email");
        txtRegisterEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRegisterEmailActionPerformed(evt);
            }
        });

        txtContactNo.setText("Contact No.");
        txtContactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactNoActionPerformed(evt);
            }
        });

        txtLastName.setText("Last Name");

        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Student", "Teacher", "Staff", "Admin" }));

        txtRegisterPassword.setText("jPasswordField2");

        btnRegisterSubmit.setBackground(new java.awt.Color(0, 153, 153));
        btnRegisterSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegisterSubmit.setText("REGISTER");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(102, 102, 102))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtRegisterEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                        .addComponent(txtContactNo, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                        .addComponent(txtLastName)
                        .addComponent(cmbUserType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRegisterPassword)
                        .addComponent(btnRegisterSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(44, 44, 44)
                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRegisterEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRegisterPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegisterSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnContentLayout = new javax.swing.GroupLayout(pnContent);
        pnContent.setLayout(pnContentLayout);
        pnContentLayout.setHorizontalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContentLayout.createSequentialGroup()
                .addGroup(pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnContentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnContentLayout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addGroup(pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 672, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnContentLayout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(81, 81, 81)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        pnContentLayout.setVerticalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContentLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblWelcome)
                .addGap(37, 37, 37)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void txtRegisterEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRegisterEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRegisterEmailActionPerformed

    private void txtContactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactNoActionPerformed

    private void btnLoginSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginSubmitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoginSubmitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnClaims;
    private javax.swing.JLabel btnItems;
    private javax.swing.JLabel btnLocation;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JButton btnLoginSubmit;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnRegister;
    private javax.swing.JButton btnRegisterSubmit;
    private javax.swing.JLabel btnUsers;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JTextField txtContactNo;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtRegisterEmail;
    private javax.swing.JPasswordField txtRegisterPassword;
    // End of variables declaration//GEN-END:variables
}
