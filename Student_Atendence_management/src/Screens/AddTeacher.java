package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddTeacher extends JFrame implements ActionListener {

    JTextField nameField, emailField, deptField;
    JPasswordField passwordField;
    JButton saveButton, backButton;

    Connection con;

    public AddTeacher() {

         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());


        setTitle("Add Teacher");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Title
        JLabel title = new JLabel("Add Teacher Details");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // Name
        addLabel("Name:", 80);
        nameField = addTextField(80);

        // Email
        addLabel("Email:", 130);
        emailField = addTextField(130);

        // Password
        addLabel("Password:", 180);
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 180, 250, 30);
        add(passwordField);

        // Department
        addLabel("Department:", 230);
        deptField = addTextField(230);

        // Buttons
        saveButton = new JButton("Save");
        saveButton.setBounds(150, 300, 100, 40);
        add(saveButton);

        backButton = new JButton("Back");
        backButton.setBounds(300, 300, 100, 40);
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
            JOptionPane.showMessageDialog(this, "Database Connection Failed ❌");
        }
    }

    public void actionPerformed(ActionEvent e) {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String dept = deptField.getText().trim();

        // ✅ Basic Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required ❌");
            return;
        }

        // ✅ Email Format Validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email Format ❌");
            return;
        }

        try {

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO teachers(name,email,password,department) VALUES(?,?,?,?)");

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, dept);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Teacher Added Successfully ✅");

            // Clear fields
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            deptField.setText("");

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Email already exists ❌");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Adding Teacher ❌");
            ex.printStackTrace();
        }
    }
}