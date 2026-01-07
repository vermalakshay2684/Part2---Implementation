package view;

import model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * PrescriptionTableFrame (VIEW)
 * - Displays prescriptions in JTable
 * - Create form for new prescription
 * - Exposes getters + buttons for Controller
 */
public class PrescriptionTableFrame extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JButton refreshButton;
    private final JButton createButton;

    private final JLabel statusLabel;

    // Create form fields
    private final JTextField patientIdField = new JTextField(10);
    private final JTextField clinicianIdField = new JTextField(10);
    private final JTextField appointmentIdField = new JTextField(10); // optional

    private final JTextField medicationField = new JTextField(12);
    private final JTextField dosageField = new JTextField(10);
    private final JTextField frequencyField = new JTextField(10);

    private final JTextField durationDaysField = new JTextField(6);
    private final JTextField quantityField = new JTextField(6);

    private final JTextField instructionsField = new JTextField(18);
    private final JTextField pharmacyField = new JTextField(12);

    public PrescriptionTableFrame() {
        setTitle("Prescriptions");
        setSize(1250, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table columns (keep it readable)
        tableModel = new DefaultTableModel(
                new Object[]{
                        "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                        "Medication", "Dosage", "Frequency", "Duration (days)", "Qty", "Status"
                },
                0
        );

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton = new JButton("Refresh");
        createButton = new JButton("Create Prescription");

        statusLabel = new JLabel("Ready");

        // Buttons row
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtons.add(refreshButton);

        // Form panel
        JPanel form = new JPanel(new GridLayout(4, 6, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Create Prescription"));

        form.add(new JLabel("Patient ID"));
        form.add(patientIdField);
        form.add(new JLabel("Clinician ID"));
        form.add(clinicianIdField);
        form.add(new JLabel("Appointment ID (optional)"));
        form.add(appointmentIdField);

        form.add(new JLabel("Medication"));
        form.add(medicationField);
        form.add(new JLabel("Dosage"));
        form.add(dosageField);
        form.add(new JLabel("Frequency"));
        form.add(frequencyField);

        form.add(new JLabel("Duration Days"));
        form.add(durationDaysField);
        form.add(new JLabel("Quantity"));
        form.add(quantityField);
        form.add(new JLabel("Pharmacy"));
        form.add(pharmacyField);

        form.add(new JLabel("Instructions"));
        form.add(instructionsField);
        form.add(new JLabel()); // filler
        form.add(new JLabel()); // filler
        form.add(new JLabel()); // filler
        form.add(new JLabel()); // filler

        JPanel formButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formButtons.add(createButton);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.add(topButtons, BorderLayout.NORTH);
        topArea.add(form, BorderLayout.CENTER);
        topArea.add(formButtons, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(statusLabel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topArea, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        tableModel.setRowCount(0);

        for (Prescription p : prescriptions) {
            tableModel.addRow(new Object[]{
                    p.getPrescriptionId(),
                    p.getPatientId(),
                    p.getClinicianId(),
                    p.getAppointmentId(),
                    p.getMedicationName(),
                    p.getDosage(),
                    p.getFrequency(),
                    p.getDurationDays(),
                    p.getQuantity(),
                    p.getStatus()
            });
        }
    }

    public void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    // Form getters (Controller reads these)
    public String getPatientIdInput() { return patientIdField.getText().trim(); }
    public String getClinicianIdInput() { return clinicianIdField.getText().trim(); }
    public String getAppointmentIdInput() { return appointmentIdField.getText().trim(); }

    public String getMedicationInput() { return medicationField.getText().trim(); }
    public String getDosageInput() { return dosageField.getText().trim(); }
    public String getFrequencyInput() { return frequencyField.getText().trim(); }

    public String getDurationDaysInput() { return durationDaysField.getText().trim(); }
    public String getQuantityInput() { return quantityField.getText().trim(); }

    public String getInstructionsInput() { return instructionsField.getText().trim(); }
    public String getPharmacyInput() { return pharmacyField.getText().trim(); }

    public void clearForm() {
        patientIdField.setText("");
        clinicianIdField.setText("");
        appointmentIdField.setText("");
        medicationField.setText("");
        dosageField.setText("");
        frequencyField.setText("");
        durationDaysField.setText("");
        quantityField.setText("");
        instructionsField.setText("");
        pharmacyField.setText("");
    }

    // Buttons exposed for Controller
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getCreateButton() { return createButton; }
}
