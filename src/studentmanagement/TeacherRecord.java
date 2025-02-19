package studentmanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class TeacherRecord extends JFrame {
    // Declare your components here
    private JTextField txtFirst;
    private JTextField txtLast;
    private JTextField txtId;
    private JTextField txtAge;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfrm;
    private JButton btnRegister;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnHome; // HOME button
    private JLabel titleLabel;

    // Declare JTable and JScrollPane
    private JTable table;
    private JScrollPane scrollPane;
    
    // Declare JComboBox for course selection
    private JComboBox<String> cmbCourse;

    public TeacherRecord() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize components
        initComponents();
    }

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Statement stmt;

    private void initComponents() {
        // Initialize other components
        txtFirst = new JTextField();
        txtLast = new JTextField();
        txtId = new JTextField();
        txtAge = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfrm = new JPasswordField();

        // Set up the frame
        setTitle("Teacher Registration Form");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // HOME button
        btnHome = new JButton("HOME");
        btnHome.setFont(new Font("Arial", Font.BOLD, 14));
        btnHome.setBackground(new Color(0, 123, 255)); // Bootstrap primary color
        btnHome.setForeground(Color.WHITE);
        btnHome.setOpaque(true);
        btnHome.setBorderPainted(false);
        btnHome.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Returning to the Home Page...");
            HomePage home = new HomePage();
            home.setVisible(true);
            this.setVisible(false);
        });

        // Create a panel for the HOME button and add it to the NORTH position
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(btnHome);
        add(northPanel, BorderLayout.NORTH);

        // Create a panel for the form components and set its layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnRegister = new JButton("Register");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        // Style other buttons
        styleButton(btnRegister, new Color(40, 167, 69)); // Bootstrap success color
        styleButton(btnUpdate, new Color(255, 193, 7)); // Bootstrap warning color
        styleButton(btnDelete, new Color(220, 53, 69)); // Bootstrap danger color

        // Create JComboBox for Course with items
        cmbCourse = new JComboBox<>(new String[] { "Engineering", "Computer Science", "Teaching" });

        // Create and style the title label
        titleLabel = new JLabel(" Teacher Registration Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Set preferred size for text fields
        Dimension textFieldSize = new Dimension(100, 20);
        txtFirst.setPreferredSize(textFieldSize);
        txtLast.setPreferredSize(textFieldSize);
        txtId.setPreferredSize(textFieldSize);
        txtAge.setPreferredSize(textFieldSize);
        txtEmail.setPreferredSize(textFieldSize);
        txtPassword.setPreferredSize(textFieldSize);
        txtConfrm.setPreferredSize(textFieldSize);

        // Initialize JTable and JScrollPane
        table = new JTable();
        scrollPane = new JScrollPane(table);

        // Add the title label to the form panel
        gbc.gridx = 1; // Move the title label to the next column
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        formPanel.add(titleLabel, gbc);

        // Add components to the form panel
        addComponent(formPanel, new JLabel("First Name:"), 2, 0, gbc);
        addComponent(formPanel, txtFirst, 2, 1, gbc);
        addComponent(formPanel, new JLabel("Last Name:"), 3, 0, gbc);
        addComponent(formPanel, txtLast, 3, 1, gbc);
        addComponent(formPanel, new JLabel("ID Number:"), 4, 0, gbc);
        addComponent(formPanel, txtId, 4, 1, gbc);

        // Use JComboBox for Course
        addComponent(formPanel, new JLabel("Course:"), 5, 0, gbc);
        addComponent(formPanel, cmbCourse, 5, 1, gbc);

        addComponent(formPanel, new JLabel("Age:"), 6, 0, gbc);
        addComponent(formPanel, txtAge, 6, 1, gbc);
        addComponent(formPanel, new JLabel("Email:"), 7, 0, gbc);
        addComponent(formPanel, txtEmail, 7, 1, gbc);
        addComponent(formPanel, new JLabel("Password:"), 8, 0, gbc);
        addComponent(formPanel, txtPassword, 8, 1, gbc);
        addComponent(formPanel, new JLabel("Confirm Password:"), 9, 0, gbc);
        addComponent(formPanel, txtConfrm, 9, 1, gbc);
        addComponent(formPanel, btnRegister, 10, 0, gbc);
        addComponent(formPanel, btnUpdate, 10, 1, gbc);
        addComponent(formPanel, btnDelete, 10, 2, gbc);

        // Add the form panel to the center of the frame
        add(formPanel, BorderLayout.CENTER);

        // Add the JScrollPane (with JTable) to the frame
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(scrollPane, gbc);

        // Add action listeners
        btnRegister.addActionListener(this::btnRegisterActionPerformed);
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        btnDelete.addActionListener(e -> deleteTeacher());
        // Initially load the data
        table.setModel(fetchData());
    }

    private void addComponent(JPanel panel, Component component, int row, int col, GridBagConstraints gbc) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = 1; // Reset gridwidth to 1 for other components
        panel.add(component, gbc);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    // Inside your TeacherRecord class

    // ActionListener for Update button
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = table.getSelectedRow(); // Get selected row in the JTable
        if (selectedRow != -1) {
            // Get values from the table for the selected row
            String firstName = table.getValueAt(selectedRow, 0).toString();
            String lastName = table.getValueAt(selectedRow, 1).toString();
            String idNumber = table.getValueAt(selectedRow, 2).toString();
            String course = table.getValueAt(selectedRow, 3).toString();
            String age = table.getValueAt(selectedRow, 4).toString();
            String email = table.getValueAt(selectedRow, 5).toString();

            // Populate the form fields with the selected row data
            txtFirst.setText(firstName);
            txtLast.setText(lastName);
            txtId.setText(idNumber);
            cmbCourse.setSelectedItem(course);
            txtAge.setText(age);
            txtEmail.setText(email);

            // Allow user to edit the fields and update the teacher's record
            btnRegister.setText("Save Changes"); // Change button text to indicate save action

            // Modify the action listener for saving changes
            btnRegister.removeActionListener(this::btnRegisterActionPerformed);
            btnRegister.addActionListener(e -> {
                String updatedFirstName = txtFirst.getText();
                String updatedLastName = txtLast.getText();
                String updatedIdNumber = txtId.getText();
                String updatedCourse = (String) cmbCourse.getSelectedItem();
                String updatedAge = txtAge.getText();
                String updatedEmail = txtEmail.getText();
                String updatedPassword = new String(txtPassword.getPassword());

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
                    pst = con.prepareStatement(
                            "UPDATE teacher SET First_name = ?, Last_Name = ?, Course = ?, Age = ?, Email = ?, Password = ? WHERE Id_Number = ?");
                    pst.setString(1, updatedFirstName);
                    pst.setString(2, updatedLastName);
                    pst.setString(3, updatedCourse);
                    pst.setString(4, updatedAge);
                    pst.setString(5, updatedEmail);
                    pst.setString(6, updatedPassword);
                    pst.setString(7, updatedIdNumber);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Teacher record updated successfully!");

                    // Refresh the table data
                    table.setModel(fetchData());

                    // Reset the form and button
                    txtFirst.setText("");
                    txtLast.setText("");
                    txtId.setText("");
                    txtAge.setText("");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfrm.setText("");
                    btnRegister.setText("Register");

                    // Reset action listener back to original register functionality
                    btnRegister.removeActionListener(ev -> {
                    });
                    btnRegister.addActionListener(this::btnRegisterActionPerformed);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(TeacherRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a record to update.");
        }
    }

    // ActionListener for Delete button
    private void deleteTeacher() {
        int selectedRow = table.getSelectedRow(); // Get selected row in the JTable
        if (selectedRow != -1) {
            String idNumber = table.getValueAt(selectedRow, 2).toString(); // Get the ID of the selected teacher

            // Debugging: Print selected ID number
            System.out.println("Selected ID Number: " + idNumber);

            // Confirm the deletion
            int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                try {
                    // Establish database connection
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
                    pst = con.prepareStatement("DELETE FROM teacher WHERE Id_Number = ?");
                    pst.setString(1, idNumber); // Set the ID to delete
                    pst.executeUpdate(); // Execute the delete query

                    // Show success message
                    JOptionPane.showMessageDialog(null, "Teacher record deleted successfully!");

                    // Refresh the table data after deletion
                    table.setModel(fetchData());

                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(TeacherRecord.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error deleting record: " + ex.getMessage());
                }
            }
        } else {
            // No row selected, show an alert
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
        }
    }

    // Fetch data from the database and populate the JTable
    private DefaultTableModel fetchData() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "First Name", "Last Name", "ID Number", "Course", "Age", "Email" }, 0);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT First_name, Last_Name, Id_Number, Course, Age, Email FROM teacher");
            while (rs.next()) {
                String firstName = rs.getString("First_name");
                String lastName = rs.getString("Last_Name");
                String idNumber = rs.getString("Id_Number");
                String course = rs.getString("Course");
                String age = rs.getString("Age");
                String email = rs.getString("Email");

                model.addRow(new Object[] { firstName, lastName, idNumber, course, age, email });
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TeacherRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtFirst.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your first name");
        } else if (txtLast.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your last name");
        } else if (txtId.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your ID number");
        } else if (cmbCourse.getSelectedItem() == null) { // Check if a course is selected
            JOptionPane.showMessageDialog(this, "Please select a course");
        } else if (txtAge.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your age");
        } else if (txtEmail.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your email");
        } else if (txtPassword.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your password");
        } else if (!txtPassword.getText().equals(txtConfrm.getText())) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
        } else {
            try {
                // Registration logic here
                String firstName = txtFirst.getText();
                String lastName = txtLast.getText();
                String idNumber = txtId.getText();
                String course = (String) cmbCourse.getSelectedItem(); // Get selected course from combo box
                String age = txtAge.getText();
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
                pst = con.prepareStatement(
                        "INSERT INTO teacher (First_name, Last_Name, Id_Number, Course, Age, Email, Password) VALUES (?, ?, ?, ?, ?, ?, ?)");
                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setString(3, idNumber);
                pst.setString(4, course);
                pst.setString(5, age);
                pst.setString(6, email);
                pst.setString(7, password);
                pst.executeUpdate();

                // Show success message
                JOptionPane.showMessageDialog(null, "Teacher registered successfully!");

                // Clear fields after successful registration
                txtFirst.setText("");
                txtLast.setText("");
                txtId.setText("");
                cmbCourse.setSelectedIndex(0); // Reset the JComboBox to the first item
                txtAge.setText("");
                txtEmail.setText("");
                txtPassword.setText("");
                txtConfrm.setText("");

                // Refresh the table data
                table.setModel(fetchData());
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TeacherRecord.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(() -> new TeacherRecord().setVisible(true));
    }
}
