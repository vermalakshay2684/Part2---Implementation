package view;

import model.Referral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ReferralTableFrame (VIEW)
 * - Displays referrals in JTable
 * - Create referral form
 * - Exposes inputs + buttons for Controller (Singleton use happens in controller/service)
 */
public class ReferralTableFrame extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JButton refreshButton;
    private final JButton createButton;

    private final JLabel statusLabel;

    // Form fields
    private final JTextField patientIdField = new JTextField(10);
    private final JTextField referringClinicianField = new JTextField(10);
    private final JTextField referredToClinicianField = new JTextField(10);
    private final JTextField referringFacilityField = new JTextField(10);
    private final JTextField referredToFacilityField = new JTextField(10);

    private final JTextField urgencyField = new JTextField(8);
    private final JTextField reasonField = new JTextField(18);
    private final JTextField summaryField = new JTextField(18);
    private final JTextField investigationsField = new JTextField(18);
    private final JTextField notesField = new JTextField(18);

    public ReferralTableFrame() {
        setTitle("Referrals (Singleton Managed)");
        setSize(1300, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Referral ID", "Patient ID", "From Clinician", "To Clinician",
                        "From Facility", "To Facility", "Date", "Urgency", "Status", "Reason"
                },
                0
        );

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton = new JButton("Refresh");
        createButton = new JButton("Create Referral (Singleton)");

        statusLabel = new JLabel("Ready");

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtons.add(refreshButton);

        JPanel form = new JPanel(new GridLayout(4, 6, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Create Referral"));

        form.add(new JLabel("Patient ID"));
        form.add(patientIdField);
        form.add(new JLabel("Referring Clinician ID"));
        form.add(referringClinicianField);
        form.add(new JLabel("Referred To Clinician ID"));
        form.add(referredToClinicianField);

        form.add(new JLabel("Referring Facility ID"));
        form.add(referringFacilityField);
        form.add(new JLabel("Referred To Facility ID"));
        form.add(referredToFacilityField);
        form.add(new JLabel("Urgency (Routine/Urgent)"));
        form.add(urgencyField);

        form.add(new JLabel("Referral Reason"));
        form.add(reasonField);
        form.add(new JLabel("Clinical Summary"));
        form.add(summaryField);
        form.add(new JLabel("Investigations"));
        form.add(investigationsField);

        form.add(new JLabel("Notes"));
        form.add(notesField);
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

    public void setReferrals(List<Referral> referrals) {
        tableModel.setRowCount(0);

        for (Referral r : referrals) {
            tableModel.addRow(new Object[]{
                    r.getReferralId(),
                    r.getPatientId(),
                    r.getReferringClinicianId(),
                    r.getReferredToClinicianId(),
                    r.getReferringFacilityId(),
                    r.getReferredToFacilityId(),
                    r.getReferralDate(),
                    r.getUrgencyLevel(),
                    r.getStatus(),
                    r.getReferralReason()
            });
        }
    }

    public void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    // Inputs for controller
    public String getPatientIdInput() { return patientIdField.getText().trim(); }
    public String getReferringClinicianInput() { return referringClinicianField.getText().trim(); }
    public String getReferredToClinicianInput() { return referredToClinicianField.getText().trim(); }
    public String getReferringFacilityInput() { return referringFacilityField.getText().trim(); }
    public String getReferredToFacilityInput() { return referredToFacilityField.getText().trim(); }
    public String getUrgencyInput() { return urgencyField.getText().trim(); }
    public String getReasonInput() { return reasonField.getText().trim(); }
    public String getSummaryInput() { return summaryField.getText().trim(); }
    public String getInvestigationsInput() { return investigationsField.getText().trim(); }
    public String getNotesInput() { return notesField.getText().trim(); }

    public void clearForm() {
        patientIdField.setText("");
        referringClinicianField.setText("");
        referredToClinicianField.setText("");
        referringFacilityField.setText("");
        referredToFacilityField.setText("");
        urgencyField.setText("");
        reasonField.setText("");
        summaryField.setText("");
        investigationsField.setText("");
        notesField.setText("");
    }

    public JButton getRefreshButton() { return refreshButton; }
    public JButton getCreateButton() { return createButton; }
}
