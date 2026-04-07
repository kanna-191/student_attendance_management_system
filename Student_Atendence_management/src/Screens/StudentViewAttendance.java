package Screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentViewAttendance extends JFrame {

    public StudentViewAttendance(String rollNo, String studentName) {


         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        setTitle("My Attendance");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Top Header
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 102, 187));
        topPanel.setPreferredSize(new Dimension(550, 60));

        JLabel heading = new JLabel("MY ATTENDANCE");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(heading);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 10, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 30, 80));

        JLabel totalLabel = new JLabel();
        JLabel presentLabel = new JLabel();
        JLabel percentLabel = new JLabel();

        Font font = new Font("Arial", Font.PLAIN, 16);

        totalLabel.setFont(font);
        presentLabel.setFont(font);
        percentLabel.setFont(font);

        centerPanel.add(new JLabel("Total Classes:"));
        centerPanel.add(totalLabel);

        centerPanel.add(new JLabel("Present:"));
        centerPanel.add(presentLabel);

        centerPanel.add(new JLabel("Attendance %:"));
        centerPanel.add(percentLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 🔹 Bottom Panel (Back Button)
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🔹 Load Attendance Data
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root");

            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) AS total_count, " +
                    "COALESCE(SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END),0) AS present_count " +
                    "FROM attendance WHERE roll_no=?");

            ps.setString(1, rollNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int total = rs.getInt("total_count");
                int present = rs.getInt("present_count");

                double percentage = 0;
                if (total > 0) {
                    percentage = (present * 100.0) / total;
                }

                totalLabel.setText(String.valueOf(total));
                presentLabel.setText(String.valueOf(present));
                percentLabel.setText(String.format("%.2f", percentage) + "%");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 Back Action
        backButton.addActionListener(e -> {
            new StudentDashboard(rollNo, studentName);
            dispose();
        });

        setVisible(true);
    }
}