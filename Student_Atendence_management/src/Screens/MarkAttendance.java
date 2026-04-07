package Screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MarkAttendance extends JFrame {

    JComboBox<String> sectionCombo;
    JTable table;
    DefaultTableModel model;

    JButton saveButton, backButton;
    JSpinner dateSpinner;

    Connection con;

    int teacherId;
    String department;

    public MarkAttendance(int teacherId, String department) {

        

        this.teacherId = teacherId;
        this.department = department;

        //  Change App Icon
        ImageIcon icon = new ImageIcon(
                TeacherDashboard.class.getResource("/Images/Appicon.png"));
        setIconImage(icon.getImage());

        


        setTitle("Mark Attendance");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Section Selection)
        JPanel topPanel = new JPanel();

        topPanel.add(new JLabel("Select Section:"));

        sectionCombo = new JComboBox<>();
        topPanel.add(sectionCombo);

        add(topPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new Object[]{"Roll No", "Name", "Present"}, 0) {

            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        
        topPanel.add(new JLabel("Select Date:"));

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);

        topPanel.add(dateSpinner);

        // Bottom Panel
        JPanel bottom = new JPanel();

        saveButton = new JButton("Save");
        backButton = new JButton("Back");

        bottom.add(saveButton);
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);

        
       

        connectDB();
        loadSections();

        sectionCombo.addActionListener(e -> loadStudents());

        saveButton.addActionListener(e -> saveAttendance());

        backButton.addActionListener(e -> {
            new TeacherDashboard(teacherId, department);
            dispose();
        });

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
            e.printStackTrace();
        }
    }

    void loadSections() {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT section FROM teacher_sections WHERE teacher_id=?");

            ps.setInt(1, teacherId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sectionCombo.addItem(rs.getString("section"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadStudents() {

        model.setRowCount(0);

        try {

            String section = (String) sectionCombo.getSelectedItem();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT roll_no, name FROM students WHERE department=? AND section=?");

            ps.setString(1, department);
            ps.setString(2, section);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        false
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveAttendance() {

    try {

        java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        for (int i = 0; i < model.getRowCount(); i++) {

            String rollNo = model.getValueAt(i, 0).toString();
            Boolean present = (Boolean) model.getValueAt(i, 2);

            String status = present ? "Present" : "Absent";

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO attendance (roll_no, date, status) VALUES (?, ?, ?)");

            ps.setString(1, rollNo);
            ps.setDate(2, sqlDate);
            ps.setString(3, status);

            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Attendance Saved Successfully ✅");

    } catch (SQLIntegrityConstraintViolationException ex) {

        JOptionPane.showMessageDialog(this,
                "Attendance already marked for this date ❌");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}