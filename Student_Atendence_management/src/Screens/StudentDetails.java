package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDetails extends JFrame {

    public StudentDetails(String rollNo) {

         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        setTitle("Student Details");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Top Header
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 102, 187));
        topPanel.setPreferredSize(new Dimension(550, 60));

        JLabel heading = new JLabel("STUDENT DETAILS");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(heading);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(6, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel rollLabel = new JLabel();
        JLabel nameLabel = new JLabel();
        JLabel emailLabel = new JLabel();
        JLabel deptLabel = new JLabel();
        JLabel sectionLabel = new JLabel();
        JLabel mobileLabel = new JLabel();

        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        rollLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        deptLabel.setFont(labelFont);
        sectionLabel.setFont(labelFont);
        mobileLabel.setFont(labelFont);

        centerPanel.add(new JLabel("Roll No:"));
        centerPanel.add(rollLabel);

        centerPanel.add(new JLabel("Name:"));
        centerPanel.add(nameLabel);

        centerPanel.add(new JLabel("Email:"));
        centerPanel.add(emailLabel);

        centerPanel.add(new JLabel("Department:"));
        centerPanel.add(deptLabel);

        centerPanel.add(new JLabel("Section:"));
        centerPanel.add(sectionLabel);

        centerPanel.add(new JLabel("Mobile:"));
        centerPanel.add(mobileLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 🔹 Bottom Panel (Back Button)
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");

        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🔹 Load Data
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root");

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM students WHERE roll_no=?");

            ps.setString(1, rollNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rollLabel.setText(rs.getString("roll_no"));
                nameLabel.setText(rs.getString("name"));
                emailLabel.setText(rs.getString("email"));
                deptLabel.setText(rs.getString("department"));
                sectionLabel.setText(rs.getString("section"));
                mobileLabel.setText(rs.getString("mobile"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 Back Action
        backButton.addActionListener(e -> {
            new StudentDashboard(rollNo, nameLabel.getText());
            dispose();
        });

        setVisible(true);
    }
}