package studentmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StaffRegistration extends JFrame {
    private JTextField firstNameField, lastNameField, staffIdField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton addButton;
    private JButton loginButton;
    private JButton homeButton;

    public StaffRegistration() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize components
        initComponents();
    }

    private void initComponents() {
        // Set up the frame
        setTitle("Staff Registration");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Create the home button and add it to the top left
        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Going to the Home Page...");
            // Implement navigation to the home page
        });
        topPanel.add(homeButton, BorderLayout.WEST);

        // Create the title label and set it centered
        JLabel titleLabel = new JLabel("Staff Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().setBackground(Color.YELLOW);
        formPanel.setBackground(Color.YELLOW);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        // Staff ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Staff ID:"), gbc);
        staffIdField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(staffIdField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Add button
        addButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Already have an account?"), gbc);

        // login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Going to the Login Page...");
            Login in = new Login();
            in.setVisible(true);
            this.setVisible(false);
        });

        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        add(formPanel, BorderLayout.CENTER);

        // Style buttons
        styleButton(addButton, new Color(40, 167, 69)); // Bootstrap success color
        styleButton(loginButton, new Color(0, 123, 255)); // Bootstrap primary color

        // Add action listener for the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaffToDatabase();
            }
        });
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    // Database connection and insertion

    private void addStaffToDatabase() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String staffId = staffIdField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hash the password before storing it
        // Database connection and insertion
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO staff (first_name, last_name, staff_id, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, staffId);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, password);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Staff registered successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the fields after successful insertion
            firstNameField.setText("");
            lastNameField.setText("");
            staffIdField.setText("");
            emailField.setText("");
            phoneField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to hash passwords

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StaffRegistration frame = new StaffRegistration();
            frame.setVisible(true);
        });
    }
}
