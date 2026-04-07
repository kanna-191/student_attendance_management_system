package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageStudent extends JFrame implements ActionListener {

    JTextField searchRollField;
    JTextField rollField, nameField, emailField, passwordField;
    JTextField deptField, sectionField, mobileField;
    JTextField presentField, totalField;

    JButton searchButton, updateButton, deleteButton, backButton;

    Connection con;

    public ManageStudent() {

         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());


        setTitle("Manage Students");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Manage Students");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(200, 20, 300, 30);
        add(title);

        // 🔍 Search
        addLabel("Enter Roll No:", 80);
        searchRollField = addTextField(80);

        searchButton = new JButton("Search");
        searchButton.setBounds(420, 80, 100, 30);
        add(searchButton);
        searchButton.addActionListener(e -> loadStudent());

        // Fields
        addLabel("Roll No:", 130);
        rollField = addTextField(130);
        rollField.setEditable(false);

        addLabel("Name:", 170);
        nameField = addTextField(170);

        addLabel("Email:", 210);
        emailField = addTextField(210);

        addLabel("Password:", 250);
        passwordField = addTextField(250);

        addLabel("Department:", 290);
        deptField = addTextField(290);

        addLabel("Section:", 330);
        sectionField = addTextField(330);

        addLabel("Mobile:", 370);
        mobileField = addTextField(370);

        addLabel("Present Days:", 410);
        presentField = addTextField(410);

        addLabel("Total Days:", 450);
        totalField = addTextField(450);

        // Buttons
        updateButton = new JButton("Update");
        updateButton.setBounds(150, 500, 100, 40);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(270, 500, 100, 40);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        add(deleteButton);

        backButton = new JButton("Back");
        backButton.setBounds(390, 500, 100, 40);
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

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed ❌");
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

    // 🔍 Load student
    void loadStudent() {

        String rollNo = searchRollField.getText().trim();

        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Roll No ❌");
            return;
        }

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM students WHERE roll_no=?");

            ps.setString(1, rollNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                rollField.setText(rs.getString("roll_no"));
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                passwordField.setText(rs.getString("password"));
                deptField.setText(rs.getString("department"));
                sectionField.setText(rs.getString("section"));
                mobileField.setText(rs.getString("mobile"));
                presentField.setText(String.valueOf(rs.getInt("present_days")));
                totalField.setText(String.valueOf(rs.getInt("total_days")));

            } else {
                JOptionPane.showMessageDialog(this, "Student Not Found ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        String mobile = mobileField.getText().trim();

        // ✅ Mobile validation
        if (!mobile.isEmpty() && !mobile.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Mobile must be 10 digits ❌");
            return;
        }

        try {

            if (e.getSource() == updateButton) {

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE students SET name=?, email=?, password=?, department=?, section=?, mobile=?, present_days=?, total_days=? WHERE roll_no=?");

                ps.setString(1, nameField.getText().trim());
                ps.setString(2, emailField.getText().trim());
                ps.setString(3, passwordField.getText().trim());
                ps.setString(4, deptField.getText().trim());
                ps.setString(5, sectionField.getText().trim());
                ps.setString(6, mobile);
                ps.setInt(7, Integer.parseInt(presentField.getText().trim()));
                ps.setInt(8, Integer.parseInt(totalField.getText().trim()));
                ps.setString(9, rollField.getText());

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Updated Successfully ✅");
                } else {
                    JOptionPane.showMessageDialog(this, "Update Failed ❌");
                }
            }

            else if (e.getSource() == deleteButton) {

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure to delete this student?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {

                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM students WHERE roll_no=?");

                    ps.setString(1, rollField.getText());

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Deleted Successfully ✅");

                        // Clear fields
                        rollField.setText("");
                        nameField.setText("");
                        emailField.setText("");
                        passwordField.setText("");
                        deptField.setText("");
                        sectionField.setText("");
                        mobileField.setText("");
                        presentField.setText("");
                        totalField.setText("");
                    }
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Operation Failed ❌");
            ex.printStackTrace();
        }
    }
}