package hospital;

import javax.swing.*;

public class welcome extends javax.swing.JFrame {

    /** Creates new form welcome */
    public welcome() {
        initComponents();
    }

    /** This method is called from within the constructor to initialize the form. */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	setSize(800, 600); // Set the desired width and height
    	setLocationRelativeTo(null); // Center the window on the screen

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton(); // Button for Appointment
        jButton5 = new javax.swing.JButton(); // Button for EHR
        jButton6 = new javax.swing.JButton(); // Button for Billing
        jButton7 = new javax.swing.JButton(); // Button for Inventory
        jButton8 = new javax.swing.JButton(); // Button for Staff Management

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Welcome to North City Hospital");

        // Set preferred size for the window
        setPreferredSize(new java.awt.Dimension(600, 600)); // Width, Height

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed", 1, 24)); // NOI18N
        jLabel1.setText("Welcome to North City Hospital");

        jButton1.setText("Doctor's Record");
        jButton1.addActionListener(evt -> openDoctorsRecord());

        jButton2.setText("Patient's Record");
        jButton2.addActionListener(evt -> openPatientsRecord());

        jButton3.setText("LOGOUT");
        jButton3.addActionListener(evt -> logout());

        // New Buttons
        jButton4.setText("Appointment Management");
        jButton4.addActionListener(evt -> openAppointmentManagement());

        jButton5.setText("EHR Management");
        jButton5.addActionListener(evt -> openEHRManagement());

        jButton6.setText("Billing Management");
        jButton6.addActionListener(evt -> openBillingManagement());

        jButton7.setText("Inventory Management");
        jButton7.addActionListener(evt -> openInventoryManagement());

        jButton8.setText("Staff Management");
        jButton8.addActionListener(evt -> openStaffManagement());

        // Layout setup
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(205, 205, 205))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(294, 294, 294))
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(10, 10, 10)
                .addComponent(jButton5)
                .addGap(10, 10, 10)
                .addComponent(jButton6)
                .addGap(10, 10, 10)
                .addComponent(jButton7)
                .addGap(10, 10, 10)
                .addComponent(jButton8)
                .addGap(20, 20, 20)
                .addComponent(jButton3)
                .addGap(55, 55, 55))
        );

        pack();
        setLocationRelativeTo(null); // Center the frame on screen
    }// </editor-fold>//GEN-END:initComponents

    private void openDoctorsRecord() {
        DOCTORS obj = new DOCTORS();
        obj.setVisible(true);
        dispose();
    }

    private void openPatientsRecord() {
        PATIENT obj = new PATIENT();
        obj.setVisible(true);
        dispose();
    }

    private void logout() {
        LoginPage obj = new LoginPage();
        obj.setVisible(true);
        dispose();
    }

    private void openAppointmentManagement() {
        Appointment obj = new Appointment();
        obj.setVisible(true);
        dispose();
    }

    private void openEHRManagement() {
        EHR obj = new EHR();
        obj.setVisible(true);
        dispose();
    }

    private void openBillingManagement() {
        Billing obj = new Billing();
        obj.setVisible(true);
        dispose();
    }

    private void openInventoryManagement() {
        Inventory obj = new Inventory();
        obj.setVisible(true);
        dispose();
    }

    private void openStaffManagement() {
        Staff obj = new Staff();
        obj.setVisible(true);
        dispose();
    }

    /** @param args the command line arguments */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new welcome().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4; // Button for Appointment
    private javax.swing.JButton jButton5; // Button for EHR
    private javax.swing.JButton jButton6; // Button for Billing
    private javax.swing.JButton jButton7; // Button for Inventory
    private javax.swing.JButton jButton8; // Button for Staff Management
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
