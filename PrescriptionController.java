package controller;

import model.Prescription;
import repository.PrescriptionRepository;
import view.PrescriptionTableFrame;

import javax.swing.JOptionPane;

/**
 * PrescriptionController (MVC)
 * - Refresh: loads prescriptions from CSV and shows them in the table
 * - Create: reads form -> appends to CSV via repository -> refreshes table
 */
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionTableFrame prescriptionView;

    public PrescriptionController(PrescriptionRepository prescriptionRepository, PrescriptionTableFrame prescriptionView) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionView = prescriptionView;
        wireActions();
    }

    private void wireActions() {
        prescriptionView.getRefreshButton().addActionListener(e -> refresh());
        prescriptionView.getCreateButton().addActionListener(e -> createFromForm());
    }

    public void start() {
        refresh();
        prescriptionView.setVisible(true);
    }

    private void refresh() {
        try {
            prescriptionView.setPrescriptions(prescriptionRepository.loadAll());
            prescriptionView.setStatus("Loaded prescriptions from CSV");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    prescriptionView,
                    "Failed to load prescriptions.csv:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createFromForm() {
        try {
            String patientId = prescriptionView.getPatientIdInput();
            String clinicianId = prescriptionView.getClinicianIdInput();
            String appointmentId = prescriptionView.getAppointmentIdInput(); // optional

            String medication = prescriptionView.getMedicationInput();
            String dosage = prescriptionView.getDosageInput();
            String frequency = prescriptionView.getFrequencyInput();

            String durationDays = prescriptionView.getDurationDaysInput();
            String quantity = prescriptionView.getQuantityInput();

            String instructions = prescriptionView.getInstructionsInput();
            String pharmacy = prescriptionView.getPharmacyInput();

            // Basic required checks (keep it strict)
            if (patientId.isEmpty() || clinicianId.isEmpty()
                    || medication.isEmpty() || dosage.isEmpty() || frequency.isEmpty()
                    || durationDays.isEmpty() || quantity.isEmpty()
                    || instructions.isEmpty() || pharmacy.isEmpty()) {

                JOptionPane.showMessageDialog(
                        prescriptionView,
                        "Fill all required fields. Appointment ID is optional.",
                        "Missing fields",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Basic numeric checks
            int d = Integer.parseInt(durationDays);
            int q = Integer.parseInt(quantity);
            if (d <= 0 || q <= 0) {
                JOptionPane.showMessageDialog(
                        prescriptionView,
                        "Duration days and Quantity must be positive numbers.",
                        "Invalid values",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Repository will generate prescription_id and set dates if blank
            Prescription candidate = new Prescription(
                    "TEMP",
                    patientId,
                    clinicianId,
                    appointmentId,   // can be blank
                    "",              // prescription_date blank -> repo sets today
                    medication,
                    dosage,
                    frequency,
                    String.valueOf(d),
                    String.valueOf(q),
                    instructions,
                    pharmacy,
                    "Issued",
                    "",              // issue_date blank -> repo sets today
                    ""               // collection_date optional
            );

            String newId = prescriptionRepository.createPrescription(candidate);

            prescriptionView.setStatus("Created prescription: " + newId);
            prescriptionView.clearForm();
            refresh();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(
                    prescriptionView,
                    "Duration Days and Quantity must be valid integers.",
                    "Invalid number",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    prescriptionView,
                    "Failed to create prescription:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
