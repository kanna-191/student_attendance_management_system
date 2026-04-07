package Screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageTeacherSection extends JFrame implements ActionListener {

    JTextField teacherIdField, deptField, sectionField;
    JTextField nameField, emailField;

    JButton assignButton, deleteButton, backButton;

    JTable sectionTable;
    DefaultTableModel tableModel;

    Connection con;

    public ManageTeacherSection() {
         // 🔹 Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());


        setTitle("Manage Teacher Sections");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Assign Sections to Teacher");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // Teacher ID
        addLabel("Teacher ID:", 70);
        teacherIdField = addTextField(70);

        // Name
        addLabel("Name:", 110);
        nameField = addTextField(110);
        nameField.setEditable(false);

        // Email
        addLabel("Email:", 150);
        emailField = addTextField(150);
        emailField.setEditable(false);

        // Department
        addLabel("Department:", 190);
        deptField = addTextField(190);

        // Section
        addLabel("Section:", 230);
        sectionField = addTextField(230);

        // Buttons
        assignButton = new JButton("Assign");
        assignButton.setBounds(80, 270, 100, 35);
        add(assignButton);

        deleteButton = new JButton("Remove");
        deleteButton.setBounds(200, 270, 100, 35);
        add(deleteButton);

        backButton = new JButton("Back");
        backButton.setBounds(320, 270, 100, 35);
        add(backButton);

        assignButton.addActionListener(this);
        deleteButton.addActionListener(this);

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Department");
        tableModel.addColumn("Section");

        sectionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(sectionTable);
        scrollPane.setBounds(100, 330, 350, 150);
        add(scrollPane);

        // Auto fetch when typing teacher ID
        teacherIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadTeacherDetails();
            }
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
        tf.setBounds(180, y, 250, 30);
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
            JOptionPane.showMessageDialog(this, "DB Connection Failed ❌");
        }
    }

    // 🔹 Load Teacher Details
    void loadTeacherDetails() {

        String id = teacherIdField.getText().trim();

        if (id.isEmpty()) return;

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM teachers WHERE id=?");

            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                deptField.setText(rs.getString("department"));

                loadSections(Integer.parseInt(id)); // load sections

            } else {
                nameField.setText("");
                emailField.setText("");
                deptField.setText("");
                tableModel.setRowCount(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Load Sections into JTable
    void loadSections(int teacherId) {

        try {

            tableModel.setRowCount(0);

            PreparedStatement ps = con.prepareStatement(
                    "SELECT department, section FROM teacher_sections WHERE teacher_id=?");

            ps.setInt(1, teacherId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("department"),
                        rs.getString("section")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        String teacherId = teacherIdField.getText().trim();
        String dept = deptField.getText().trim();
        String section = sectionField.getText().trim();

        if (teacherId.isEmpty() || dept.isEmpty() || section.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required ❌");
            return;
        }

        try {

            if (e.getSource() == assignButton) {

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO teacher_sections(teacher_id, department, section) VALUES(?,?,?)");

                ps.setInt(1, Integer.parseInt(teacherId));
                ps.setString(2, dept);
                ps.setString(3, section);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Section Assigned ✅");

                loadSections(Integer.parseInt(teacherId)); // refresh

            }

            else if (e.getSource() == deleteButton) {

                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM teacher_sections WHERE teacher_id=? AND department=? AND section=?");

                ps.setInt(1, Integer.parseInt(teacherId));
                ps.setString(2, dept);
                ps.setString(3, section);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Section Removed ✅");
                } else {
                    JOptionPane.showMessageDialog(this, "Not Found ❌");
                }

                loadSections(Integer.parseInt(teacherId)); // refresh
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Operation Failed ❌");
            ex.printStackTrace();
        }
    }
}