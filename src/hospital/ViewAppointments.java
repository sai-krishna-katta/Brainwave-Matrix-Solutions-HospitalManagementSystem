package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewAppointments extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;

    public ViewAppointments() {
        initComponents();
        loadAppointments();
    }

    private void initComponents() {
        setSize(800, 600); // Set the desired width and height
        setLocationRelativeTo(null); // Center the window on the screen

        setTitle("View Appointments");

        // Create the table model and set column names
        tableModel = new DefaultTableModel(new Object[]{"Patient Name", "Doctor Name", "Date", "Time", "Reason", "Action"}, 0);
        appointmentsTable = new JTable(tableModel);

        // Set the custom renderer for the "Delete" button in each row
        appointmentsTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        appointmentsTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Add a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose(); // Close the current window
            new Appointment().setVisible(true); // Show the appointment page
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadAppointments() {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String query = "SELECT * FROM appointment";

        // Clear table before reloading appointments
        tableModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                String doctorName = rs.getString("doctor_name");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String reason = rs.getString("reason");

                // Add a row with appointment data and a "Delete" button
                tableModel.addRow(new Object[]{patientName, doctorName, date, time, reason, "Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
        }
    }

    private void deleteAppointment(String patientName, String doctorName, String date, String time) {
        String url = "jdbc:mysql://localhost:3306/hms"; // Update with your database info
        String user = "root"; // Your DB username
        String password = "root"; // Your DB password
        String deleteQuery = "DELETE FROM appointment WHERE patient_name = ? AND doctor_name = ? AND date = ? AND time = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setString(1, patientName);
            stmt.setString(2, doctorName);
            stmt.setString(3, date);
            stmt.setString(4, time);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Appointment could not be deleted.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting appointment: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAppointments().setVisible(true));
    }

    // ButtonRenderer for rendering buttons in the JTable
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // ButtonEditor for handling button clicks in the JTable
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isPushed = true;

                    // Retrieve data from the table model
                    String patientName = (String) tableModel.getValueAt(row, 0);
                    String doctorName = (String) tableModel.getValueAt(row, 1);
                    String date = (String) tableModel.getValueAt(row, 2);
                    String time = (String) tableModel.getValueAt(row, 3);

                    // Delete appointment from the database
                    deleteAppointment(patientName, doctorName, date, time);

                    // Remove the row from the table without reloading the entire table
                    tableModel.removeRow(row);
                }
            });
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
