package studentmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login() {
        setTitle("Staff Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("Staff Login");
        titleLabel.setFont(new Font("Arial",Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 25));
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBackground(Color.YELLOW);
        loginButton.setForeground(Color.BLACK);
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Database connection and query
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff WHERE email = ? AND password = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + rs.getString("first_name"));
                // Proceed to the home page
                openHomePage();
                this.dispose(); // Close the login form
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHomePage() {
        // Here you would instantiate and open the homepage window.
        JOptionPane.showMessageDialog(this, "Welcome to the Home Page...");
        HomePage home = new HomePage();
        home.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
    }
}
