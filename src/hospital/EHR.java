package hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EHR extends JFrame {
    private JTextField patientNameField;
    private JTextArea recordField;

    public EHR() {
        initComponents();
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen

        JLabel label1 = new JLabel("Patient Name:");
        JLabel label2 = new JLabel("Record:");

        patientNameField = new JTextField(50);
        recordField = new JTextArea(10, 20);

        JButton submitButton = new JButton("Submit");
        JButton viewButton = new JButton("View Records"); // View button
        JButton backButton = new JButton("Back to Welcome"); // Back button

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRecord(); // Submit the record and keep the dialog open
            }
        });

        // ActionListener to open the ViewEHR page
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewEHR().setVisible(true); // Open the EHR records viewer
                dispose(); // Close the current window
            }
        });

        // ActionListener to go back to the Welcome page
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new welcome().setVisible(true); // Navigate to the Welcome page
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
                .addComponent(recordField)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(submitButton)
                    .addComponent(viewButton) // View button in the layout
                    .addComponent(backButton) // Back button in the layout
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label1)
                .addComponent(patientNameField)
                .addComponent(label2)
                .addComponent(recordField)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(viewButton) // View button in the layout
                    .addComponent(backButton) // Back button in the layout
                )
        );

        pack();
    }

    // This method submits the record and keeps the window open
    private void submitRecord() {
        String patientName = patientNameField.getText();
        String record = recordField.getText();

        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password

        String query = "INSERT INTO ehr (patient_name, record, date) VALUES (?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patientName);
            stmt.setString(2, record);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record added successfully!");
            // No dispose() here; the dialog will stay open after submission
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new EHR().setVisible(true);
    }
}
