package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewBilling extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public ViewBilling() {
        initComponents();
        loadBillingData(); // Load billing data when the window is opened
    }

    private void initComponents() {
        setTitle("Billing Records");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Billing Records");

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Patient Name", "Amount", "Date", "Action"});
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the "Action" column is editable
            }
        };

        // Set the custom renderer and editor for the button column
        TableColumn actionColumn = table.getColumn("Action");
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);

        // Add the back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to Billing.java
                dispose(); // Close the current window
                new Billing().setVisible(true); // Open the Billing window
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(label)
                .addComponent(scrollPane)
                .addComponent(backButton, GroupLayout.Alignment.CENTER)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(scrollPane)
                .addComponent(backButton)
        );

        pack();
    }

    private void loadBillingData() {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "SELECT * FROM billing";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                String amount = rs.getString("amount");
                String date = rs.getString("date");
                tableModel.addRow(new Object[]{patientName, amount, date, "Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading billing data: " + e.getMessage());
        }
    }

    private void deleteBillingRecord(String patientName, String amount, String date) {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "DELETE FROM billing WHERE patient_name = ? AND amount = ? AND date = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patientName);
            stmt.setString(2, amount);
            stmt.setString(3, date);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Billing record deleted successfully!");
                loadBillingData(); // Refresh the table after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Record not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting billing record: " + e.getMessage());
        }
    }

    // Custom renderer for buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }

    // Button editor for delete action
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String patientName;
        private String amount;
        private String date;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isPushed) {
                        // Confirm deletion
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to delete this record?\n" +
                                        "Patient: " + patientName + "\nAmount: " + amount + "\nDate: " + date,
                                "Confirm Deletion",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete the record
                            deleteBillingRecord(patientName, amount, date);
                        }
                    }
                    isPushed = false;
                    fireEditingStopped(); // Ensure editing is stopped after the button is clicked
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            patientName = table.getValueAt(row, 0).toString();
            amount = table.getValueAt(row, 1).toString();
            date = table.getValueAt(row, 2).toString();
            button.setText((value == null) ? "Delete" : value.toString());
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            // Avoid calling this multiple times
            if (isPushed) {
                super.fireEditingStopped();
                isPushed = false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewBilling().setVisible(true)); // Ensure GUI creation is on the EDT
    }
}
