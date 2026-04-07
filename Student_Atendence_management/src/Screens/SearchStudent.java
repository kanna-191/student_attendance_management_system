package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchStudent extends JFrame implements ActionListener {

    JTextField rollField;
    JTextArea resultArea;
    JButton searchButton, backButton;

    Connection con;
    int teacherId;
    String department;

    public SearchStudent(int teacherId, String department) {

        setTitle("Search Student");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // App Icon
        ImageIcon icon = new ImageIcon(
                SearchStudent.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        JLabel title = new JLabel("Search Student By Roll No");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(120, 20, 350, 30);
        add(title);

        JLabel rollLabel = new JLabel("Enter Roll No:");
        rollLabel.setBounds(60, 90, 120, 30);
        add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(200, 90, 200, 30);
        add(rollField);

        searchButton = new JButton("Search");
        searchButton.setBounds(180, 140, 120, 35);
        add(searchButton);

        resultArea = new JTextArea();
        resultArea.setBounds(60, 200, 420, 200);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(resultArea);

        backButton = new JButton("Back");
        backButton.setBounds(220, 420, 100, 35);
        add(backButton);

        searchButton.addActionListener(this);

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

    public void actionPerformed(ActionEvent e) {

        String rollNo = rollField.getText().trim();

        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Roll No");
            return;
        }

        try {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM students WHERE roll_no=?");

            ps.setString(1, rollNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // 🔹 Calculate Attendance From attendance Table

PreparedStatement attPs = con.prepareStatement(
        "SELECT " +
        "COUNT(*) AS total_count, " +
        "SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) AS present_count " +
        "FROM attendance WHERE roll_no=?");

attPs.setString(1, rollNo);

ResultSet attRs = attPs.executeQuery();

int total = 0;
int present = 0;
double percentage = 0;

if (attRs.next()) {
    total = attRs.getInt("total_count");
    present = attRs.getInt("present_count");

    if (total > 0) {
        percentage = (present * 100.0) / total;
    }
}
                String result = "Roll No: " + rs.getString("roll_no") +
        "\nName: " + rs.getString("name") +
        "\nEmail: " + rs.getString("email") +
        "\nDepartment: " + rs.getString("department") +
        "\nSection: " + rs.getString("section") +
        "\nMobile: " + rs.getString("mobile") +
        "\n---------------------------" +
        "\nTotal Classes: " + total +
        "\nPresent: " + present +
        "\nAttendance %: " + String.format("%.2f", percentage) + "%";

                resultArea.setText(result);

            } else {
                resultArea.setText("Student Not Found ❌");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Search Error ❌");
        }
    }
}