package hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Staff extends JFrame {
    private JTextField staffNameField;
    private JTextField roleField;

    public Staff() {
        initComponents();
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen

        JLabel label1 = new JLabel("Staff Name:");
        JLabel label2 = new JLabel("Role:");
        
        staffNameField = new JTextField(50);
        roleField = new JTextField(20);
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitStaff();
            }
        });

        JButton viewButton = new JButton("View Staff");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewStaff().setVisible(true);
                dispose(); // Close the current window
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the welcome page
                new welcome().setVisible(true); // Replace Welcome with your actual welcome page class
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
                .addComponent(staffNameField)
                .addComponent(label2)
                .addComponent(roleField)
                .addComponent(submitButton)
                .addComponent(viewButton)
                .addComponent(backButton)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label1)
                .addComponent(staffNameField)
                .addComponent(label2)
                .addComponent(roleField)
                .addComponent(submitButton)
                .addComponent(viewButton)
                .addComponent(backButton)
        );

        pack();
    }

    private void submitStaff() {
        String staffName = staffNameField.getText();
        String role = roleField.getText();

        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password

        String query = "INSERT INTO staff (staff_name, role, date_joined) VALUES (?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffName);
            stmt.setString(2, role);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Staff added successfully!");
            staffNameField.setText(""); // Clear input fields after submission
            roleField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Staff().setVisible(true)); // Ensure GUI creation is on the EDT
    }
}
