package studentmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ClassRecord extends JFrame {

    // Database connection variables
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    // UI Components
    private JTextField classIdField, teacherField;
    private JComboBox<String> classNameComboBox;
    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JButton btnHome; // HOME button

    public ClassRecord() {
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
        setTitle("Class Record Form");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // Top panel for title and HOME button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.YELLOW);

        // HOME button, aligned left
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

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false); // Keeps yellow background visible
        leftPanel.add(btnHome);
        leftPanel.setBackground(Color.YELLOW);
        // Title label, centered
        JLabel titleLabel = new JLabel("Class Record Form", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);

        topPanel.add(leftPanel, BorderLayout.WEST); // Left side for HOME button
        topPanel.add(titleLabel, BorderLayout.CENTER); // Centered title

        // Add the top panel to the frame
        add(topPanel, BorderLayout.NORTH);

        // Center panel for form inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.YELLOW);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Class Name ComboBox
        JLabel classNameLabel = new JLabel("Class Name:");
        classNameComboBox = new JComboBox<>(new String[] { "Engineering", "Computer Science", "Teaching" });
        classNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        classNameComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(classNameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(classNameComboBox, gbc);

        // Class ID Input Field
        JLabel classIdLabel = new JLabel("Class ID:");
        classIdField = new JTextField(15);
        classIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        classIdField.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(classIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(classIdField, gbc);

        // Teacher in Charge Input Field
        JLabel teacherLabel = new JLabel("Teacher in Charge:");
        teacherField = new JTextField(15);
        teacherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teacherField.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(teacherLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(teacherField, gbc);

        // Add input panel to the top-center of the frame
        add(inputPanel, BorderLayout.CENTER);

        // Table panel to display class records
        String[] columns = { "Class ID", "Class Name", "Teacher in Charge" };
        tableModel = new DefaultTableModel(columns, 0);
        recordsTable = new JTable(tableModel);
        recordsTable.setRowHeight(30);
        recordsTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Table scroll pane to ensure the table fits well
        JScrollPane tableScroll = new JScrollPane(recordsTable);

        // Panel for action buttons and table (placed at the bottom of the frame)
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(tableScroll, BorderLayout.CENTER);

        // Add bottom panel containing buttons and table to the frame
        add(bottomPanel, BorderLayout.SOUTH);

        // Style other buttons
        styleButton(addButton, new Color(40, 167, 69)); // Bootstrap success color
        styleButton(updateButton, new Color(255, 193, 7)); // Bootstrap warning color
        styleButton(deleteButton, new Color(220, 53, 69)); // Bootstrap danger color

        // Database connection setup
        connectDatabase();

        // Button action listeners
        addButton.addActionListener(e -> addRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/utukufutechnical", "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addRecord() {
        String className = classNameComboBox.getSelectedItem().toString();
        String classId = classIdField.getText().trim();
        String teacher = teacherField.getText().trim();

        if (classId.isEmpty() || teacher.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Class ID and Teacher fields cannot be empty!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "INSERT INTO class_records (class_id, class_name, teacher_in_charge) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, classId);
            pst.setString(2, className);
            pst.setString(3, teacher);
            pst.executeUpdate();
            loadRecordsFromDatabase();
            classIdField.setText("");
            teacherField.setText("");
            classNameComboBox.setSelectedIndex(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRecord() {
        {
            String className = classNameComboBox.getSelectedItem().toString();
            String classId = classIdField.getText().trim();
            String teacher = teacherField.getText().trim();

            // Check if any field is empty
            if (classId.isEmpty() || teacher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Class ID and Teacher fields cannot be empty!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String query = "UPDATE class_records SET class_name = ?, teacher_in_charge = ? WHERE class_id = ?";
                pst = conn.prepareStatement(query);
                pst.setString(1, className);
                pst.setString(2, teacher);
                pst.setString(3, classId);
                pst.executeUpdate();

                // Refresh the JTable after updating
                loadRecordsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void deleteRecord() {
        // Get selected row index
        int selectedRow = recordsTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get class ID from the selected row
        String classId = (String) recordsTable.getValueAt(selectedRow, 0); // First column (Class ID)

        try {
            String query = "DELETE FROM class_records WHERE class_id = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, classId);
            pst.executeUpdate();

            // Refresh the JTable after deleting
            loadRecordsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRecordsFromDatabase() {
        try {
            String query = "SELECT * FROM class_records";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            // Clear existing rows
            tableModel.setRowCount(0);

            // Add rows from the result set
            while (rs.next()) {
                String classId = rs.getString("class_id");
                String className = rs.getString("class_name");
                String teacher = rs.getString("teacher_in_charge");
                tableModel.addRow(new Object[] { classId, className, teacher });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClassRecord form = new ClassRecord();
            form.setVisible(true);
        });
    }
}
