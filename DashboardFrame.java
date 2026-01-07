package view;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardFrame:
 * - Single entry point GUI for the system
 * - Buttons open module windows (Patients/Appointments/Prescriptions/Referrals)
 *
 * No persistence or business logic here.
 */
public class DashboardFrame extends JFrame {

    private final JButton patientsButton = new JButton("Patients");
    private final JButton appointmentsButton = new JButton("Appointments");
    private final JButton prescriptionsButton = new JButton("Prescriptions");
    private final JButton referralsButton = new JButton("Referrals (Singleton)");

    public DashboardFrame() {
        setTitle("Healthcare Management System - Dashboard");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Healthcare Management System", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));

        JPanel buttons = new JPanel(new GridLayout(2, 2, 12, 12));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttons.add(patientsButton);
        buttons.add(appointmentsButton);
        buttons.add(prescriptionsButton);
        buttons.add(referralsButton);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
    }

    public JButton getPatientsButton() { return patientsButton; }
    public JButton getAppointmentsButton() { return appointmentsButton; }
    public JButton getPrescriptionsButton() { return prescriptionsButton; }
    public JButton getReferralsButton() { return referralsButton; }
}
