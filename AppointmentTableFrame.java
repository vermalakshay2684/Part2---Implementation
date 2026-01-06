package view;

import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AppointmentTableFrame (VIEW)
 * - Table of appointments
 * - Controls: Refresh, Cancel Selected
 * - Create form (also reused for Update)
 */
public class AppointmentTableFrame extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JButton refreshButton;
    private final JButton cancelButton;

    private final JButton createButton;
    private final JButton loadSelectedButton;
    private final JButton updateSelectedButton;

    private final JLabel statusLabel;

    // Create/Update form fields
    private final JTextField patientIdField = new JTextField(10);
    private final JTextField clinicianIdField = new JTextField(10);
    private final JTextField facilityIdField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);     // yyyy-MM-dd
    private final JTextField timeField = new JTextField(5);      // HH:mm
    private final JTextField durationField = new JTextField(5);  // minutes
    private final JTextField typeField = new JTextField(8);      // GP / Specialist etc.
    private final JTextField reasonField = new JTextField(18);
    private final JTextField notesField = new JTextField(18);
    private final JButton backButton = new JButton("← Back to Dashboard");

    public AppointmentTableFrame() {
        setTitle("Appointments");
        setSize(1250, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table model
        tableModel = new DefaultTableModel(
                new Object[]{
                        "Appointment ID", "Patient ID", "Clinician ID", "Facility ID",
                        "Date", "Time", "Duration", "Type", "Status", "Reason"
                },
                0
        );

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        refreshButton = new JButton("Refresh");
        cancelButton = new JButton("Cancel Selected");

        createButton = new JButton("Create Appointment");
        loadSelectedButton = new JButton("Load Selected → Form");
        updateSelectedButton = new JButton("Update Selected");

        // Status bar
        statusLabel = new JLabel("Ready");

        // Top button bar
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtons.add(refreshButton);
        topButtons.add(cancelButton);


        // Create/Update form panel (grid)
        JPanel form = new JPanel(new GridLayout(3, 6, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Create / Update Appointment"));

        form.add(new JLabel("Patient ID"));
        form.add(patientIdField);
        form.add(new JLabel("Clinician ID"));
        form.add(clinicianIdField);
        form.add(new JLabel("Facility ID"));
        form.add(facilityIdField);

        form.add(new JLabel("Date (yyyy-MM-dd)"));
        form.add(dateField);
        form.add(new JLabel("Time (HH:mm)"));
        form.add(timeField);
        form.add(new JLabel("Duration (min)"));
        form.add(durationField);

        form.add(new JLabel("Type"));
        form.add(typeField);
        form.add(new JLabel("Reason"));
        form.add(reasonField);
        form.add(new JLabel("Notes"));
        form.add(notesField);

        JPanel formButtonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formButtonRow.add(createButton);
        formButtonRow.add(loadSelectedButton);
        formButtonRow.add(updateSelectedButton);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.add(topButtons, BorderLayout.NORTH);
        topArea.add(form, BorderLayout.CENTER);
        topArea.add(formButtonRow, BorderLayout.SOUTH);

        // Bottom panel for status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        // Main layout
        setLayout(new BorderLayout());
        add(topArea, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setAppointments(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getDurationMinutes(),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit()
            });
        }
    }

    /** appointment_id from selected row */
    public String getSelectedAppointmentId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        Object id = tableModel.getValueAt(row, 0);
        return id == null ? null : id.toString();
    }

    /**
     * Get selected row values needed to populate the form.
     * Returns null if no selection.
     */
    public String[] getSelectedRowValues() {
        int row = table.getSelectedRow();
        if (row == -1) return null;

        // Map table columns to the form fields we have
        String appointmentId = String.valueOf(tableModel.getValueAt(row, 0));
        String patientId = String.valueOf(tableModel.getValueAt(row, 1));
        String clinicianId = String.valueOf(tableModel.getValueAt(row, 2));
        String facilityId = String.valueOf(tableModel.getValueAt(row, 3));
        String date = String.valueOf(tableModel.getValueAt(row, 4));
        String time = String.valueOf(tableModel.getValueAt(row, 5));
        String duration = String.valueOf(tableModel.getValueAt(row, 6));
        String type = String.valueOf(tableModel.getValueAt(row, 7));
        String status = String.valueOf(tableModel.getValueAt(row, 8));
        String reason = String.valueOf(tableModel.getValueAt(row, 9));

        return new String[]{appointmentId, patientId, clinicianId, facilityId, date, time, duration, type, status, reason};
    }

    /** Controller uses this to show messages */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    // Form getters
    public String getPatientIdInput() { return patientIdField.getText().trim(); }
    public String getClinicianIdInput() { return clinicianIdField.getText().trim(); }
    public String getFacilityIdInput() { return facilityIdField.getText().trim(); }
    public String getDateInput() { return dateField.getText().trim(); }
    public String getTimeInput() { return timeField.getText().trim(); }
    public String getDurationInput() { return durationField.getText().trim(); }
    public String getTypeInput() { return typeField.getText().trim(); }
    public String getReasonInput() { return reasonField.getText().trim(); }
    public String getNotesInput() { return notesField.getText().trim(); }

    public void clearCreateForm() {
        patientIdField.setText("");
        clinicianIdField.setText("");
        facilityIdField.setText("");
        dateField.setText("");
        timeField.setText("");
        durationField.setText("");
        typeField.setText("");
        reasonField.setText("");
        notesField.setText("");
    }

    /** Controller fills the form from selected row */
    public void setFormFields(String patientId, String clinicianId, String facilityId,
                              String date, String time, String duration, String type,
                              String reason, String notes) {
        patientIdField.setText(patientId);
        clinicianIdField.setText(clinicianId);
        facilityIdField.setText(facilityId);
        dateField.setText(date);
        timeField.setText(time);
        durationField.setText(duration);
        typeField.setText(type);
        reasonField.setText(reason);
        notesField.setText(notes);
    }

    // Buttons exposed for Controller
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getCreateButton() { return createButton; }
    public JButton getLoadSelectedButton() { return loadSelectedButton; }
    public JButton getUpdateSelectedButton() { return updateSelectedButton; }
}
