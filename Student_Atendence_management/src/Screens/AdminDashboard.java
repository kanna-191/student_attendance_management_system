package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {

    JButton addStudentBtn, addTeacherBtn;
    JButton manageStudentBtn, manageTeacherBtn;
    JButton manageSectionBtn, logoutBtn;

    public AdminDashboard() {

         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());


        setTitle("Admin Dashboard");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false); // optional UI improvement

        // Title
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(180, 30, 300, 30);
        add(title);

        // Buttons
        addStudentBtn = createButton("Add Student", 100);
        addTeacherBtn = createButton("Add Teacher", 160);
        manageStudentBtn = createButton("Manage Students", 220);
        manageTeacherBtn = createButton("Manage Teachers", 280);
        manageSectionBtn = createButton("Manage Teacher Sections", 340);
        logoutBtn = createButton("Logout", 400);

        // Add buttons
        add(addStudentBtn);
        add(addTeacherBtn);
        add(manageStudentBtn);
        add(manageTeacherBtn);
        add(manageSectionBtn);
        add(logoutBtn);

        // Actions
        addStudentBtn.addActionListener(this);
        addTeacherBtn.addActionListener(this);
        manageStudentBtn.addActionListener(this);
        manageTeacherBtn.addActionListener(this);
        manageSectionBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        setVisible(true);
    }

    JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(180, y, 220, 40);
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addStudentBtn) {
            new AddStudent(0, "");
            dispose();
        }

        else if (e.getSource() == addTeacherBtn) {
            new AddTeacher();
            dispose();
        }

        else if (e.getSource() == manageStudentBtn) {
            new ManageStudent();
            dispose();
        }

        else if (e.getSource() == manageTeacherBtn) {
            new ManageTeacher();
            dispose();
        }

        else if (e.getSource() == manageSectionBtn) {
            new ManageTeacherSection();
            dispose();
        }

        else if (e.getSource() == logoutBtn) {
            new LoginPage();
            dispose();
        }
    }
    public static void main(String[] args) {
    new AdminDashboard();
}
}