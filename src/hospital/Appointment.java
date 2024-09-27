package hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Appointment extends JFrame implements Runnable {
    private JTextField patientNameField;
    private JTextField doctorNameField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextArea reasonField;

    public Appointment() {
        initComponents();
        showDate(); // Show current date
        Thread t = new Thread(this);
        t.start(); // Start thread to update time
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen

        JLabel label1 = new JLabel("Patient Name:");
        JLabel label2 = new JLabel("Doctor Name:");
        JLabel label3 = new JLabel("Date (YYYY-MM-DD):");
        JLabel label4 = new JLabel("Time (HH:MM:SS):");
        JLabel label5 = new JLabel("Reason:");

        patientNameField = new JTextField(50);
        doctorNameField = new JTextField(20);
        dateField = new JTextField(10);
        timeField = new JTextField(8);
        timeField.setEditable(false); // Make timeField non-editable
        reasonField = new JTextArea(5, 20);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAppointment();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new welcome().setVisible(true); // Show the welcome page
            }
        });

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewAppointments().setVisible(true); // Open the ViewAppointments window
                dispose(); // Close the current window
            }
        });

        // Layout settings
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(label1)
                .addComponent(patientNameField)
                .addComponent(label2)
                .addComponent(doctorNameField)
                .addComponent(label3)
                .addComponent(dateField)
                .addComponent(label4)
                .addComponent(timeField)
                .addComponent(label5)
                .addComponent(reasonField)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(backButton)
                    .addComponent(viewButton)
                    .addComponent(submitButton))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label1)
                .addComponent(patientNameField)
                .addComponent(label2)
                .addComponent(doctorNameField)
                .addComponent(label3)
                .addComponent(dateField)
                .addComponent(label4)
                .addComponent(timeField)
                .addComponent(label5)
                .addComponent(reasonField)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(viewButton)
                    .addComponent(submitButton))
        );

        pack();
    }

    private void showDate() {
        // Get current date
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateField.setText(sdf.format(currentDate)); // Set current date
    }

    private void submitAppointment() {
        String patientName = patientNameField.getText();
        String doctorName = doctorNameField.getText();
        String date = dateField.getText();
        String time = timeField.getText();
        String reason = reasonField.getText();

        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password

        String query = "INSERT INTO appointment (patient_name, doctor_name, date, time, reason) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patientName);
            stmt.setString(2, doctorName);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, reason);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
            // Remove the following line to keep the form open
            // dispose(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error scheduling appointment: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            Calendar cal = Calendar.getInstance();
            // Format the time to HH:MM:SS
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = cal.getTime();
            String time24 = sdf24.format(currentTime);
            timeField.setText(time24); // Update timeField with current time

            try {
                Thread.sleep(1000); // Update every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Appointment().setVisible(true);
    }
}
