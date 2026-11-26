/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import model.User;
import model.UserDAO;
import view.MainFrame;

public class RegisterController {

    private MainFrame view;
    private UserDAO userDAO;

    public RegisterController(MainFrame view) {
        this.view = view;
        this.userDAO = new UserDAO();

        view.getRegisterButton().addActionListener(e -> registerUser());
    }

    private void registerUser() {

       String firstName = view.getFirstName().trim();
String lastName = view.getLastName().trim();
String email = view.getRegisterEmail().trim();
String contactNo = view.getContactNo().trim();
        String password = view.getRegisterPassword();
        String userType = view.getUserType();

        if (firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || contactNo.isEmpty()
                || password.isEmpty()) {

            view.showMessage("Please complete all fields!");
            return;
        }

        User user = new User(
            0,
            firstName,
            lastName,
            email,
            contactNo,
            userType
        );

        if (userDAO.addUser(user, password)) {
            view.showMessage("Registration Successful!");
        } else {
            view.showMessage("Registration Failed! Please check your information.");
        }
    }
}
