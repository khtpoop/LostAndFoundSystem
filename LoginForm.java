
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import controller.LoginController;
import model.User;

public class LoginForm extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {

        setTitle("Lost and Found Management System");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(232, 237, 227));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(213, 224, 204));
                g2.fillOval(-60, -70, 200, 200);
                g2.fillOval(420, 370, 180, 180);

                g2.setColor(new Color(250, 249, 245));
                g2.fill(new RoundRectangle2D.Float(
                    75, 45, 400, 390, 25, 25
                ));

                g2.dispose();
            }
        };

        JLabel title = new JLabel("LOST & FOUND", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(52, 70, 56));
        title.setBounds(100, 75, 350, 45);
        panel.add(title);

        JLabel subtitle = new JLabel(
            "MANAGEMENT SYSTEM", JLabel.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));
        subtitle.setBounds(100, 115, 350, 25);
        panel.add(subtitle);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Serif", Font.BOLD, 22));
        welcome.setForeground(new Color(52, 70, 56));
        welcome.setBounds(125, 165, 300, 30);
        panel.add(welcome);

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setBounds(125, 210, 300, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(125, 238, 300, 35);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(125, 285, 300, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(125, 313, 300, 35);
        panel.add(passwordField);

        loginButton = new JButton("LOGIN");
        loginButton.setBounds(125, 370, 300, 40);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(138, 154, 123));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(loginButton);

        add(panel);

                panel.add(loginButton);

        add(panel);


    
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
    
    

    