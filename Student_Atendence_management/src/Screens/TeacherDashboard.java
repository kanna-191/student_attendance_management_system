package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherDashboard extends JFrame implements ActionListener {

    JButton updateAttendance, searchStudent, markAttendance, exit;
    int teacherId;
String department;

    public TeacherDashboard(int teacherId, String department) {
    this.teacherId = teacherId;
    this.department = department;

        setTitle("Teacher Dashboard");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        setLayout(new BorderLayout());

        // 🔹 Top Title Panel
        JLabel title = new JLabel("STUDENT ATTENDANCE MANAGEMENT SYSTEM",
                JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setPreferredSize(new Dimension(600, 60));
        add(title, BorderLayout.NORTH);

        // 🔹 Center Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        //addStudent = new JButton("Add Student");
        updateAttendance = new JButton("Update Attendance");
        searchStudent = new JButton("Search Student");
        markAttendance = new JButton("Mark Attendance");
        exit = new JButton("Exit");

       // panel.add(addStudent);
        panel.add(updateAttendance);
        panel.add(searchStudent);
        panel.add(markAttendance);
        panel.add(exit);

        add(panel, BorderLayout.CENTER);


         

           


            updateAttendance.addActionListener(e -> {
                new UpdateAttendance(teacherId, department);
                dispose();
            });

             searchStudent.addActionListener(e -> {
                    new SearchStudent(teacherId, department);
                    dispose();
                });

                markAttendance.addActionListener(e -> {
                    new MarkAttendance(teacherId, department);
                    dispose();
                });

           exit.addActionListener(e -> {
                new LoginPage();   // Open Login Page
                dispose();         // Close Dashboard
           });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Future functionality
    }
}