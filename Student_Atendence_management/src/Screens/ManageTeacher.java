package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageTeacher extends JFrame implements ActionListener {

    JTextField searchIdField;
    JTextField idField, nameField, emailField, passwordField, deptField;

    JButton searchButton, updateButton, deleteButton, backButton;

    Connection con;

    public ManageTeacher() {


         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        setTitle("Manage Teachers");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Manage Teachers");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(180, 20, 300, 30);
        add(title);

        // 🔍 Search
        addLabel("Enter ID:", 80);
        searchIdField = addTextField(80);

        searchButton = new JButton("Search");
        searchButton.setBounds(400, 80, 100, 30);
        add(searchButton);
        searchButton.addActionListener(e -> loadTeacher());

        // Fields
        addLabel("ID:", 130);
        idField = addTextField(130);
        idField.setEditable(false);

        addLabel("Name:", 170);
        nameField = addTextField(170);

        addLabel("Email:", 210);
        emailField = addTextField(210);

        addLabel("Password:", 250);
        passwordField = addTextField(250);

        addLabel("Department:", 290);
        deptField = addTextField(290);

        // Buttons
        updateButton = new JButton("Update");
        updateButton.setBounds(120, 360, 100, 40);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(250, 360, 100, 40);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        add(deleteButton);

        backButton = new JButton("Back");
        backButton.setBounds(380, 360, 100, 40);
        add(backButton);

        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        connectDB();
        setVisible(true);
    }

    // 🔗 DB Connection
    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed ❌");
            e.printStackTrace();
        }
    }

    void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(50, y, 150, 30);
        add(label);
    }

    JTextField addTextField(int y) {
        JTextField tf = new JTextField();
        tf.setBounds(200, y, 250, 30);
        add(tf);
        return tf;
    }

    // 🔍 Load Teacher
    void loadTeacher() {

    String idText = searchIdField.getText().trim();

    if (idText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter ID ❌");
        return;
    }

    try {

        int id = Integer.parseInt(idText); // convert to int

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM teachers WHERE id=?");

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            idField.setText(String.valueOf(rs.getInt("id")));
            nameField.setText(rs.getString("name"));
            emailField.setText(rs.getString("email"));
            passwordField.setText(rs.getString("password"));
            deptField.setText(rs.getString("department"));

        } else {
            JOptionPane.showMessageDialog(this, "Teacher Not Found ❌");

            // clear fields
            idField.setText("");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            deptField.setText("");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID must be a number ❌");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading teacher ❌");
        e.printStackTrace();
    }
}
    public void actionPerformed(ActionEvent e) {

        try {

            // ================= UPDATE =================
            if (e.getSource() == updateButton) {

                String email = emailField.getText().trim();

                // ✅ Email validation only for update
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(this, "Invalid Email Format ❌");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE teachers SET name=?, email=?, password=?, department=? WHERE id=?");

                ps.setString(1, nameField.getText().trim());
                ps.setString(2, email);
                ps.setString(3, passwordField.getText().trim());
                ps.setString(4, deptField.getText().trim());
                ps.setInt(5, Integer.parseInt(idField.getText()));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Updated Successfully ✅");
                } else {
                    JOptionPane.showMessageDialog(this, "Update Failed ❌");
                }
            }

            // ================= DELETE =================
           else if (e.getSource() == deleteButton) {

    if (idField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Search a teacher first ❌");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure to delete this teacher?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {

        int id = Integer.parseInt(idField.getText());

        // 🔥 STEP 1: Delete from child table FIRST
        PreparedStatement ps1 = con.prepareStatement(
                "DELETE FROM teacher_sections WHERE teacher_id=?");
        ps1.setInt(1, id);
        ps1.executeUpdate();

        // 🔥 STEP 2: Delete from teachers
        PreparedStatement ps2 = con.prepareStatement(
                "DELETE FROM teachers WHERE id=?");
        ps2.setInt(1, id);

        int rows = ps2.executeUpdate();

        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Deleted Successfully ✅");

            idField.setText("");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            deptField.setText("");

        } else {
            JOptionPane.showMessageDialog(this, "No record found ❌");
        }
    }
}

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Operation Failed ❌");
            ex.printStackTrace(); // VERY IMPORTANT for debugging
        }
    }
}