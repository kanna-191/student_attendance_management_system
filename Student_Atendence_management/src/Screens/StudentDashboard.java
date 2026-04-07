package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDashboard extends JFrame implements ActionListener {

    String rollNo;
    String studentName;

    JButton viewAttendanceBtn, detailsBtn, logoutBtn;

    public StudentDashboard(String rollNo, String studentName) {

        this.rollNo = rollNo;
        this.studentName = studentName;

        // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        setTitle("Student Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Top Header Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 102, 187));
        topPanel.setPreferredSize(new Dimension(800, 70));

        JLabel heading = new JLabel("WELCOME, " + studentName.toUpperCase());
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        topPanel.add(heading);
        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);

        viewAttendanceBtn = new JButton("View Attendance");
        viewAttendanceBtn.setBounds(250, 80, 300, 50);
        styleButton(viewAttendanceBtn);
        centerPanel.add(viewAttendanceBtn);

        detailsBtn = new JButton("Student Details");
        detailsBtn.setBounds(250, 160, 300, 50);
        styleButton(detailsBtn);
        centerPanel.add(detailsBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(250, 240, 300, 50);
        styleButton(logoutBtn);
        centerPanel.add(logoutBtn);

        add(centerPanel, BorderLayout.CENTER);

        // Button Actions
        viewAttendanceBtn.addActionListener(this);
        detailsBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        setVisible(true);
    }

    void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(200, 210, 220));
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == viewAttendanceBtn) {
            new StudentViewAttendance(rollNo,studentName);
            dispose();
        }

        if (e.getSource() == detailsBtn) {
            new StudentDetails(rollNo);
            dispose();
        }

        if (e.getSource() == logoutBtn) {
            new LoginPage();
            dispose();
        }
    }
}