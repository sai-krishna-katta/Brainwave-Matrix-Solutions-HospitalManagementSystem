package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewStaff extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewStaff() {
        initComponents();
        loadStaffData(); // Load staff data when the window is opened
    }

    private void initComponents() {
        setTitle("Staff Records");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Staff Records");
        JButton backButton = new JButton("Back");

        // Action listener for the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the Staff page
                new Staff().setVisible(true); // Replace Staff with your actual staff page class
                dispose(); // Close the current window
            }
        });

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Staff Name", "Role", "Date Joined", "Action"});
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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(label)
                .addComponent(scrollPane)
                .addComponent(backButton) // Add the back button to the layout
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(scrollPane)
                .addComponent(backButton) // Add the back button to the layout
        );

        pack();
    }

    private void loadStaffData() {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "SELECT staff_name, role, date_joined FROM staff";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                String staffName = rs.getString("staff_name");
                String role = rs.getString("role");
                String dateJoined = rs.getString("date_joined");
                tableModel.addRow(new Object[]{staffName, role, dateJoined, "Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + e.getMessage());
        }
    }

    private void deleteStaffMember(String staffName, String role, String dateJoined) {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "DELETE FROM staff WHERE staff_name = ? AND role = ? AND date_joined = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffName);
            stmt.setString(2, role);
            stmt.setString(3, dateJoined);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Staff member deleted successfully!");
                loadStaffData(); // Refresh the table after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Member not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting staff member: " + e.getMessage());
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
        private String staffName;
        private String role;
        private String dateJoined;
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
                                "Are you sure you want to delete this staff member?\n" +
                                        "Staff: " + staffName + "\nRole: " + role + "\nDate Joined: " + dateJoined,
                                "Confirm Deletion",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete the record
                            deleteStaffMember(staffName, role, dateJoined);
                        }
                    }
                    isPushed = false;
                    fireEditingStopped(); // Ensure editing is stopped after the button is clicked
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            staffName = table.getValueAt(row, 0).toString();
            role = table.getValueAt(row, 1).toString();
            dateJoined = table.getValueAt(row, 2).toString();
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
        SwingUtilities.invokeLater(() -> new ViewStaff().setVisible(true)); // Ensure GUI creation is on the EDT
    }
}
