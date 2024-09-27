package hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Billing extends JFrame {
    private JTextField patientNameField;
    private JTextField amountField;

    public Billing() {
        initComponents();
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen

        JLabel label1 = new JLabel("Patient Name:");
        JLabel label2 = new JLabel("Amount:");

        patientNameField = new JTextField(50);
        amountField = new JTextField(30);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitBilling();
            }
        });

        // Add "View Billing" button to view billing data
        JButton viewButton = new JButton("View Billing");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewBilling window when the button is clicked
                new ViewBilling().setVisible(true);
            }
        });

        // Add "Back" button to go back to the welcome page
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new welcome().setVisible(true); // Open the welcome page
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
                .addComponent(amountField)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(submitButton)
                    .addComponent(viewButton)
                    .addComponent(backButton)) // Add the "Back" button here
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label1)
                .addComponent(patientNameField)
                .addComponent(label2)
                .addComponent(amountField)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(viewButton)
                    .addComponent(backButton)) // Align the buttons horizontally
        );

        pack();
    }

    private void submitBilling() {
        String patientName = patientNameField.getText();
        String amount = amountField.getText();

        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password

        String query = "INSERT INTO billing (patient_name, amount, date) VALUES (?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patientName);
            stmt.setBigDecimal(2, new java.math.BigDecimal(amount));
            stmt.executeUpdate();

            // Create a panel to hold the message and increase height
            JPanel panel = new JPanel();
            JLabel message = new JLabel("Billing recorded successfully!");
            panel.add(message);
            panel.setPreferredSize(new java.awt.Dimension(200, 100)); // Increase height here

            // Display the customized dialog
            JOptionPane.showMessageDialog(this, panel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error recording billing: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Billing().setVisible(true);
    }
}
