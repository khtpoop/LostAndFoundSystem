
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */

package model;

import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CrudDAO {

    // LOAD RECORDS
    public List<Object[]> getAllRecords(
            String tableName,
            String idColumn,
            String[] displayColumns,
            boolean hideClaimedItems) throws SQLException {

        List<Object[]> records = new ArrayList<>();

        String sql = "SELECT "
                + String.join(", ", displayColumns)
                + " FROM " + tableName;

        if (hideClaimedItems && tableName.equals("items")) {
            sql += " WHERE item_status IS NULL "
                    + "OR LOWER(item_status) <> 'claimed'";
        }

        sql += " ORDER BY " + idColumn + " ASC";

        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection connection = con;
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[displayColumns.length];

                for (int i = 0; i < displayColumns.length; i++) {
                    row[i] = rs.getObject(displayColumns[i]);
                }

                records.add(row);
            }
        }

        return records;
    }

    // ADD RECORD
    public void addRecord(
            String tableName,
            String[] fields,
            String[] values) throws SQLException {

        String sql = "INSERT INTO " + tableName
                + " (" + String.join(", ", fields) + ")"
                + " VALUES ("
                + String.join(", ", Collections.nCopies(fields.length, "?"))
                + ")";

        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection connection = con;
             PreparedStatement pst = connection.prepareStatement(sql)) {

            for (int i = 0; i < fields.length; i++) {
                setValue(pst, i + 1, fields[i], values[i]);
            }

            pst.executeUpdate();
        }
    }

    // UPDATE RECORD
    public void updateRecord(
            String tableName,
            String idColumn,
            int id,
            List<String> fields,
            List<String> values) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "UPDATE " + tableName + " SET ");

        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }

            sql.append(fields.get(i)).append(" = ?");
        }

        sql.append(" WHERE ").append(idColumn).append(" = ?");

        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection connection = con;
             PreparedStatement pst = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < fields.size(); i++) {
                setValue(pst, i + 1, fields.get(i), values.get(i));
            }

            pst.setInt(fields.size() + 1, id);
            pst.executeUpdate();
        }
    }

    // DELETE RECORD
    public void deleteRecord(
            String tableName,
            String idColumn,
            int id) throws SQLException {

        String sql = "DELETE FROM " + tableName
                + " WHERE " + idColumn + " = ?";

        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection connection = con;
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // MARK ITEM AS CLAIMED
    public void markItemAsClaimed(int itemId) throws SQLException {

        String sql = "UPDATE items SET item_status = 'Claimed' "
                + "WHERE item_id = ?";

        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection connection = con;
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, itemId);
            pst.executeUpdate();
        }
    }

    // SET VALUES
    private void setValue(
            PreparedStatement pst,
            int parameter,
            String field,
            String value) throws SQLException {

        if (value == null || value.trim().isEmpty()) {
            if (field.equals("date_reported")
                    || field.equals("claim_date")) {
                pst.setNull(parameter, Types.DATE);
            } else {
                pst.setNull(parameter, Types.VARCHAR);
            }
            return;
        }

        if (field.equals("date_reported")
                || field.equals("claim_date")) {
            pst.setDate(parameter, Date.valueOf(value));
        } else {
            pst.setString(parameter, value);
        }
    }
}
