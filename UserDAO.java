
package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import config.DatabaseConnection;

public class UserDAO {

    // LOGIN
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            if (con == null) return null;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, email);
                pst.setString(2, password);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("contact_number"),
                            rs.getString("user_type")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // DISPLAY ALL USERS
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("contact_number"),
                    rs.getString("user_type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // ADD USER
    public boolean addUser(User user, String password) {
        String sql = "INSERT INTO users "
                   + "(first_name, last_name, email, password, contact_number, user_type) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, password);
            pst.setString(5, user.getContactNumber());
            pst.setString(6, user.getUserType());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE USER
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, "
                   + "email=?, contact_number=?, user_type=? WHERE user_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getContactNumber());
            pst.setString(5, user.getUserType());
            pst.setInt(6, user.getUserId());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE USER
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, userId);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            
            
        }
    }
}
