package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddStudent extends JFrame implements ActionListener {

    JTextField rollField, nameField, emailField, deptField, sectionField, mobileField;
    JPasswordField passwordField;
    JButton saveButton, backButton;

    Connection con;

    int teacherId;
    String department;

public AddStudent(int teacherId, String department) {
    this.teacherId = teacherId;
    this.department = department;
    
        setTitle("Add Student");
        setSize(550, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // App Icon
        ImageIcon icon = new ImageIcon(
                AddStudent.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        JLabel title = new JLabel("Add Student Details");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // Roll No
        addLabel("Roll No:", 80);
        rollField = addTextField(80);

        // Name
        addLabel("Name:", 120);
        nameField = addTextField(120);

        // Email
        addLabel("Email:", 160);
        emailField = addTextField(160);

        // Password
        addLabel("Password:", 200);
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 200, 250, 30);
        add(passwordField);

        // Department
        addLabel("Department:", 240);
        deptField = addTextField(240);

        // Section
        addLabel("Section:", 280);
        sectionField = addTextField(280);

        // Mobile
        addLabel("Mobile:", 320);
        mobileField = addTextField(320);

        saveButton = new JButton("Save");
        saveButton.setBounds(150, 380, 100, 40);
        add(saveButton);

        backButton = new JButton("Back");
        backButton.setBounds(300, 380, 100, 40);
        add(backButton);

        saveButton.addActionListener(this);

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        connectDB();
        setVisible(true);
    }

    void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(50, y, 120, 30);
        add(label);
    }

    JTextField addTextField(int y) {
        JTextField tf = new JTextField();
        tf.setBounds(200, y, 250, 30);
        add(tf);
        return tf;
    }

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed");
        }
    }

    public void actionPerformed(ActionEvent e) {

    try {

        String mobile = mobileField.getText().trim();

        // ✅ Mobile Validation
        if (!mobile.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be exactly 10 digits ❌");
            return;
        }

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO students(roll_no,name,email,password,department,section,mobile,present_days,total_days) VALUES(?,?,?,?,?,?,?,?,?)");

        ps.setString(1, rollField.getText().trim());
        ps.setString(2, nameField.getText().trim());
        ps.setString(3, emailField.getText().trim());
        ps.setString(4, String.valueOf(passwordField.getPassword()).trim());
        ps.setString(5, deptField.getText().trim());
        ps.setString(6, sectionField.getText().trim());
        ps.setString(7, mobile);
        ps.setInt(8, 0);
        ps.setInt(9, 0);

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Student Added Successfully ✅");

    } catch (SQLIntegrityConstraintViolationException ex) {
        JOptionPane.showMessageDialog(this, "Roll No already exists ❌");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error Adding Student ❌");
        ex.printStackTrace();
    }
}
}