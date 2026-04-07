package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateAttendance extends JFrame implements ActionListener {

    JTextField searchRollField;
    JTextField rollField, nameField, emailField, passwordField;
    JTextField deptField, sectionField, mobileField;
    JTextField presentField, totalField;

    JButton searchButton, updateButton, backButton;

    Connection con;
    int teacherId;
    String department;

    public UpdateAttendance(int teacherId, String department) {

        setTitle("Update Student Details");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // App Icon
        ImageIcon icon = new ImageIcon(
                UpdateAttendance.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        JLabel title = new JLabel("Update Student Details");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(180, 20, 350, 30);
        add(title);

        // 🔹 Search Roll No
        JLabel searchLabel = new JLabel("Enter Roll No:");
        searchLabel.setBounds(50, 80, 150, 30);
        add(searchLabel);

        searchRollField = new JTextField();
        searchRollField.setBounds(200, 80, 200, 30);
        add(searchRollField);

        searchButton = new JButton("Search");
        searchButton.setBounds(420, 80, 100, 30);
        add(searchButton);

        searchButton.addActionListener(e -> loadStudentDetails());

        // 🔹 Student Details Fields
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

        updateButton = new JButton("Update");
        updateButton.setBounds(200, 500, 120, 40);
        add(updateButton);

        backButton = new JButton("Back");
        backButton.setBounds(350, 500, 120, 40);
        add(backButton);

        updateButton.addActionListener(this);

        backButton.addActionListener(e -> {
           new TeacherDashboard(teacherId, department);
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
            JOptionPane.showMessageDialog(this, "Database Connection Failed");
        }
    }

    void loadStudentDetails() {

        String rollNo = searchRollField.getText().trim();

        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Roll No First");
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

        try {
            String mobile = mobileField.getText().trim();

        // ✅ Mobile Validation
        if (!mobile.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be exactly 10 digits ❌");
            return;
        }

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE students SET name=?, email=?, password=?, department=?, section=?, mobile=?, present_days=?, total_days=? WHERE roll_no=?");

            ps.setString(1, nameField.getText().trim());
            ps.setString(2, emailField.getText().trim());
            ps.setString(3, passwordField.getText().trim());
            ps.setString(4, deptField.getText().trim());
            ps.setString(5, sectionField.getText().trim());
            ps.setString(6, mobileField.getText().trim());
            ps.setInt(7, Integer.parseInt(presentField.getText().trim()));
            ps.setInt(8, Integer.parseInt(totalField.getText().trim()));
            ps.setString(9, rollField.getText());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student Updated Successfully ✅");
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed ❌");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update Error ❌");
            ex.printStackTrace();
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
}