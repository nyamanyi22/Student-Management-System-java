package studentmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class StudentRecord extends JFrame {
    private JTextField firstNameField, lastNameField, ageField, studentIdField, emailField, phoneNumberField;
    private JComboBox<String> classComboBox;
    private JButton addButton, updateButton, deleteButton, generateReportButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton btnHome; // HOME button

    public StudentRecord() {
        setTitle("Student Record Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow window to be resized
       // setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.YELLOW);

        // Top panel with title and HOME button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.YELLOW);

        // HOME button
        btnHome = new JButton("HOME");
        btnHome.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Returning to the Home Page...");
            HomePage home = new HomePage();
            home.setVisible(true);
            this.setVisible(false);
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnHome);

        JLabel titleLabel = new JLabel("Student Registration Center", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Age:"), gbc);
        ageField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Student ID:"), gbc);
        studentIdField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Class:"), gbc);
        classComboBox = new JComboBox<>(new String[] { "Engineering", "Computer Science", "Teaching" });
        gbc.gridx = 1;
        formPanel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        add(formPanel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel();
        

        addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE); // Make the text white
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);

        updateButton = new JButton("Update");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(Color.GREEN);
        updateButton.setForeground(Color.WHITE);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);

        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.setFont(new Font("Arial", Font.BOLD, 14));
        generateReportButton.setBackground(Color.GRAY);
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.setOpaque(true);
        generateReportButton.setBorderPainted(false);


        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateReportButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Table panel
        tableModel = new DefaultTableModel(
                new String[] { "ID", "First Name", "Last Name", "Age", "Class", "Email", "Phone" }, 0);
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 200));

        add(tableScrollPane, BorderLayout.SOUTH);

        // Load Data
        loadData();

        // Button actions
        addButton.addActionListener(e -> addStudent());
        generateReportButton.addActionListener(e -> generateReport());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
    }

    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("class"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateReport() {
        StringBuilder reportData = new StringBuilder();
        reportData.append("Student Report\n");
        reportData.append("---------------\n");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                reportData.append(String.format("%-10d %-15s %-15s %-5d %-20s %-25s %-15s\n",
                        rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getInt("age"), rs.getString("class"), rs.getString("email"), rs.getString("phone_number")));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("student_report.txt"))) {
                writer.write(reportData.toString());
                JOptionPane.showMessageDialog(this, "Report saved to student_report.txt");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        // Add student to databaseprivate void addStudent() {
        // Check if any required field is empty
        if (studentIdField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return; // Stop execution if any field is blank
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO students (student_id, first_name, last_name, age, class, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(studentIdField.getText()));
            stmt.setString(2, firstNameField.getText());
            stmt.setString(3, lastNameField.getText());
            stmt.setInt(4, Integer.parseInt(ageField.getText()));
            stmt.setString(5, classComboBox.getSelectedItem().toString());
            stmt.setString(6, emailField.getText());
            stmt.setString(7, phoneNumberField.getText());
            stmt.executeUpdate();

            // Add the data to the JTable
            tableModel.addRow(new Object[] {
                    studentIdField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    ageField.getText(),
                    classComboBox.getSelectedItem(),
                    emailField.getText(),
                    phoneNumberField.getText()
            });

            // Show success message
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the text fields after successful insertion
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to clear all input fields
    private void clearFields() {
        studentIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        emailField.setText("");
        phoneNumberField.setText("");
        classComboBox.setSelectedIndex(0); // Reset to the first option
    }



    
        // Update student in database
        private void updateStudent() {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    // Validate input fields before proceeding
                    int age, studentId;
                    try {
                        age = Integer.parseInt(ageField.getText());
                        studentId = Integer.parseInt(studentIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Age and Student ID.",
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Database update
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "UPDATE students SET first_name=?, last_name=?, age=?, class=?, email=?, phone_number=? WHERE student_id=?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, firstNameField.getText());
                        stmt.setString(2, lastNameField.getText());
                        stmt.setInt(3, age);
                        stmt.setString(4, classComboBox.getSelectedItem().toString());
                        stmt.setString(5, emailField.getText());
                        stmt.setString(6, phoneNumberField.getText());
                        stmt.setInt(7, studentId);
                        stmt.executeUpdate();

                        // Update JTable
                        tableModel.setValueAt(firstNameField.getText(), selectedRow, 1);
                        tableModel.setValueAt(lastNameField.getText(), selectedRow, 2);
                        tableModel.setValueAt(String.valueOf(age), selectedRow, 3);
                        tableModel.setValueAt(classComboBox.getSelectedItem(), selectedRow, 4);
                        tableModel.setValueAt(emailField.getText(), selectedRow, 5);
                        tableModel.setValueAt(phoneNumberField.getText(), selectedRow, 6);

                        // Show success message
                        JOptionPane.showMessageDialog(this, "Student updated successfully!", "Update Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        }


       // Delete student from database
       private void deleteStudent() {
           int selectedRow = studentTable.getSelectedRow();
           if (selectedRow != -1) {
               // Get the student_id from the selected row
               int studentId = (int) tableModel.getValueAt(selectedRow, 0);

               // Show a confirmation dialog
               int confirm = JOptionPane.showConfirmDialog(this,
                       "Are you sure you want to delete this record?",
                       "Confirm Deletion",
                       JOptionPane.YES_NO_OPTION);

               // If the user confirms deletion
               if (confirm == JOptionPane.YES_OPTION) {
                   try (Connection conn = DatabaseConnection.getConnection()) {
                       String query = "DELETE FROM students WHERE student_id=?";
                       PreparedStatement stmt = conn.prepareStatement(query);
                       stmt.setInt(1, studentId);
                       stmt.executeUpdate();

                       // Remove the row from the JTable
                       tableModel.removeRow(selectedRow);
                       // Show success message
                       JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Deletion Success",
                               JOptionPane.INFORMATION_MESSAGE);
                   } catch (SQLException e) {
                       e.printStackTrace();
                       JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error",
                               JOptionPane.ERROR_MESSAGE);
                   }
               }
           } else {
               JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection",
                       JOptionPane.WARNING_MESSAGE);
           }
       }
       

   /*
    * private void deleteStudentFromDatabase(int studentId) {
    * String query = "DELETE FROM students WHERE student_id=?";
    * try (Connection conn = DatabaseConnection.getConnection();
    * PreparedStatement stmt = conn.prepareStatement(query)) {
    * stmt.setInt(1, studentId);
    * stmt.executeUpdate();
    * } catch (SQLException e) {
    * e.printStackTrace();
    * JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
    * "Database Error",
    * JOptionPane.ERROR_MESSAGE);
    * }
    * }
    */

    public static class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/utukufutechnical";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecord().setVisible(true));
    }
}
