package studentmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    private JButton btnStudentRecord;
    private JButton btnTeacher;
    private JButton btnClasses;

    public HomePage() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Utukufu University");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        btnStudentRecord = new JButton("Student Record");
        btnTeacher = new JButton("Teacher");
        btnClasses = new JButton("Classes");

        // Set button colors
        btnStudentRecord.setBackground(Color.GREEN);
        btnStudentRecord.setForeground(Color.WHITE);
        btnTeacher.setBackground(Color.GREEN);
        btnTeacher.setForeground(Color.WHITE);
        btnClasses.setBackground(Color.GREEN);
        btnClasses.setForeground(Color.WHITE);

        // Set button size
        Dimension buttonSize = new Dimension(150, 40);
        btnStudentRecord.setPreferredSize(buttonSize);
        btnTeacher.setPreferredSize(buttonSize);
        btnClasses.setPreferredSize(buttonSize);

        btnStudentRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openStudentRecord(evt);
            }
        });

        btnTeacher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openTeacher(evt);
            }
        });

        btnClasses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openClasses(evt);
            }
        });

        // Add title label
        JLabel titleLabel = new JLabel("Utukufu University", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(titleLabel, gbc);

        // Add buttons
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(btnStudentRecord, gbc);

        gbc.gridy = 2;
        add(btnTeacher, gbc);

        gbc.gridy = 3;
        add(btnClasses, gbc);

        pack();
        setLocationRelativeTo(null); // Center the frame
    }

    private void openStudentRecord(ActionEvent evt) {
        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setVisible(true);
        this.setVisible(false);
        // new StudentRecord().setVisible(true);
    }

    private void openTeacher(ActionEvent evt) {
        TeacherRecord teacherRecord = new TeacherRecord();
        teacherRecord.setVisible(true);
        this.setVisible(false);

        // new Teacher().setVisible(true);
    }

    private void openClasses(ActionEvent evt) {
        ClassRecord classes = new ClassRecord();
        classes.setVisible(true);
        this.setVisible(false);
        // new Classes().setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }
}
