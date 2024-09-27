package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewInventory extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewInventory() {
        initComponents();
        loadInventoryData(); // Load inventory data when the window is opened
    }

    private void initComponents() {
        setTitle("Inventory Records");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Inventory Records");
        JButton backButton = new JButton("Back");

        // Action listener for the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the inventory page
                new Inventory().setVisible(true); // Replace Inventory with your actual inventory page class
                dispose(); // Close the current window
            }
        });

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Item Name", "Quantity", "Date Added", "Action"});
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

    private void loadInventoryData() {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "SELECT item_name, quantity, date_added FROM inventory";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                String dateAdded = rs.getString("date_added");
                tableModel.addRow(new Object[]{itemName, quantity, dateAdded, "Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading inventory data: " + e.getMessage());
        }
    }

    private void deleteInventoryItem(String itemName, int quantity, String dateAdded) {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "DELETE FROM inventory WHERE item_name = ? AND quantity = ? AND date_added = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setString(3, dateAdded);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Inventory item deleted successfully!");
                loadInventoryData(); // Refresh the table after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Item not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting inventory item: " + e.getMessage());
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
        private String itemName;
        private int quantity;
        private String dateAdded;
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
                                "Are you sure you want to delete this item?\n" +
                                        "Item: " + itemName + "\nQuantity: " + quantity + "\nDate Added: " + dateAdded,
                                "Confirm Deletion",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete the item
                            deleteInventoryItem(itemName, quantity, dateAdded);
                        }
                    }
                    isPushed = false;
                    fireEditingStopped(); // Ensure editing is stopped after the button is clicked
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            itemName = table.getValueAt(row, 0).toString();
            quantity = (int) table.getValueAt(row, 1);
            dateAdded = table.getValueAt(row, 2).toString();
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
        SwingUtilities.invokeLater(() -> new ViewInventory().setVisible(true)); // Ensure GUI creation is on the EDT
    }
}
