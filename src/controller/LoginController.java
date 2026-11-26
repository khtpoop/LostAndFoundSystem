
package controller;

import model.User;
import model.UserDAO;
import view.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private MainFrame mainFrame;
    private UserDAO userDAO;

    public LoginController(MainFrame view) {

        this.mainFrame = view;
        this.userDAO = new UserDAO();

        mainFrame.getLoginButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String email = mainFrame.getEmail().trim();
        String password = mainFrame.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            mainFrame.showMessage(
                "Please enter your email and password!"
            );
            return;
        }

        User user = userDAO.login(email, password);

        if (user != null) {

            mainFrame.showMessage("Login Successful!");

            mainFrame.openDashboard(user);

        } else {

            mainFrame.showMessage(
                "Invalid email or password!"
            );
        }
    }
}
