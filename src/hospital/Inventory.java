package hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Inventory extends JFrame {
    private JTextField itemNameField;
    private JTextField quantityField;

    public Inventory() {
        initComponents();
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label1 = new JLabel("Item Name:");
        JLabel label2 = new JLabel("Quantity:");

        itemNameField = new JTextField(50);
        quantityField = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitInventory();
            }
        });

        JButton viewButton = new JButton("View Inventory");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewInventory().setVisible(true); // Open ViewInventory window
                dispose(); // Close the current window
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new welcome().setVisible(true); // Replace WelcomePage with your actual welcome page class
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
                .addComponent(itemNameField)
                .addComponent(label2)
                .addComponent(quantityField)
                .addComponent(submitButton)
                .addComponent(viewButton) // Add view button
                .addComponent(backButton) // Add back button
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label1)
                .addComponent(itemNameField)
                .addComponent(label2)
                .addComponent(quantityField)
                .addComponent(submitButton)
                .addComponent(viewButton) // Add view button
                .addComponent(backButton) // Add back button
        );

        pack();
    }

    private void submitInventory() {
        String itemName = itemNameField.getText();
        String quantity = quantityField.getText();

        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password

        String query = "INSERT INTO inventory (item_name, quantity, date_added) VALUES (?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, Integer.parseInt(quantity));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Inventory item added successfully!");
            itemNameField.setText(""); // Clear input fields after submission
            quantityField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding inventory item: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Inventory().setVisible(true);
    }
}
