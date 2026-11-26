
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JPanel {

    private int userId;
    private String firstName;
    private String userType;

    private final Color backgroundColor = new Color(232, 237, 227);
    private final Color greenColor = new Color(104, 128, 94);
    private final Color darkColor = new Color(52, 70, 56);

    public Dashboard(int userId, String firstName,
                     String userType, Runnable onLogout) {

        this.userId = userId;
        this.firstName = firstName;
        this.userType = userType;

        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // MAIN CONTENT
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(35, 40, 35, 40));

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(darkColor);

        JLabel subtitle = new JLabel(
            "LOST AND FOUND MANAGEMENT SYSTEM"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 125, 100));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        logoutButton.setBackground(greenColor);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(110, 40));

        logoutButton.addActionListener(e -> onLogout.run());

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // WELCOME SECTION
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(250, 249, 245));
        welcomePanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel welcomeLabel = new JLabel(
            "Welcome, " + firstName + "!"
        );
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        welcomeLabel.setForeground(darkColor);

        JLabel roleLabel = new JLabel(
            "Account Type: " + userType
        );
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        roleLabel.setForeground(new Color(110, 125, 100));

        JLabel messageLabel = new JLabel(
            "Manage lost and found records efficiently."
        );
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.DARK_GRAY);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(8));
        welcomePanel.add(roleLabel);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(messageLabel);

        // SUMMARY TITLE
        JLabel summaryTitle = new JLabel("SYSTEM MANAGEMENT");
        summaryTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        summaryTitle.setForeground(darkColor);

        // SUMMARY CARDS
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setOpaque(false);

        cardsPanel.add(createCard(
            "USERS",
            "Manage system accounts"
        ));

        cardsPanel.add(createCard(
            "ITEMS",
            "Manage lost and found items"
        ));

        cardsPanel.add(createCard(
            "LOCATIONS",
            "Manage reported locations"
        ));

        cardsPanel.add(createCard(
            "CLAIMS",
            "Manage item claims"
        ));

        // CENTER CONTENT
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        welcomePanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 150)
        );

        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(summaryTitle);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(cardsPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String description) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 249, 245));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel cardTitle = new JLabel(title);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        cardTitle.setForeground(greenColor);

        JLabel cardDescription = new JLabel(description);
        cardDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cardDescription.setForeground(Color.DARK_GRAY);

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(cardDescription);

        return card;
    }
}
