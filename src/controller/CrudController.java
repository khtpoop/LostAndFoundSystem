/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import model.CrudDAO;
import java.sql.SQLException;
import java.util.List;

public class CrudController {

    private final CrudDAO crudDAO;

    public CrudController() {
        crudDAO = new CrudDAO();
    }

    public List<Object[]> loadRecords(
            String tableName,
            String idColumn,
            String[] displayColumns,
            boolean hideClaimedItems) throws SQLException {

        return crudDAO.getAllRecords(
                tableName,
                idColumn,
                displayColumns,
                hideClaimedItems
        );
    }

    public void addRecord(
            String tableName,
            String[] fields,
            String[] values) throws SQLException {

        crudDAO.addRecord(tableName, fields, values);
    }

    public void updateRecord(
            String tableName,
            String idColumn,
            int id,
            List<String> fields,
            List<String> values) throws SQLException {

        crudDAO.updateRecord(
                tableName, idColumn, id, fields, values
        );
    }

    public void deleteRecord(
            String tableName,
            String idColumn,
            int id) throws SQLException {

        crudDAO.deleteRecord(tableName, idColumn, id);
    }

    public void markItemAsClaimed(int itemId) throws SQLException {
        crudDAO.markItemAsClaimed(itemId);
    }
}
