package controller;

import model.Referral;
import repository.ReferralRepository;
import service.ReferralManager;
import view.ReferralTableFrame;

import javax.swing.JOptionPane;
import java.nio.file.Path;

/**
 * ReferralController:
 * - Refresh: loads referrals from CSV
 * - Create: calls ReferralManager Singleton to create + queue + write email/EHR/audit outputs
 *
 * This demonstrates the Singleton pattern in the referral workflow.
 */
public class ReferralController {

    private final ReferralRepository referralRepository;
    private final ReferralTableFrame referralView;

    // Singleton manager (single instance)
    private final ReferralManager referralManager;

    public ReferralController(ReferralRepository referralRepository, ReferralTableFrame referralView, Path outDir) throws Exception {
        this.referralRepository = referralRepository;
        this.referralView = referralView;

        // Create/get singleton instance
        this.referralManager = ReferralManager.getInstance(referralRepository, outDir);

        wireActions();
    }

    private void wireActions() {
        referralView.getRefreshButton().addActionListener(e -> refresh());
        referralView.getCreateButton().addActionListener(e -> createFromForm());
    }

    public void start() {
        refresh();
        referralView.setVisible(true);
    }

    private void refresh() {
        try {
            referralView.setReferrals(referralRepository.loadAll());
            referralView.setStatus("Loaded referrals from CSV");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    referralView,
                    "Failed to load referrals.csv:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createFromForm() {
        try {
            String patientId = referralView.getPatientIdInput();
            String fromClinician = referralView.getReferringClinicianInput();
            String toClinician = referralView.getReferredToClinicianInput();
            String fromFacility = referralView.getReferringFacilityInput();
            String toFacility = referralView.getReferredToFacilityInput();

            String urgency = referralView.getUrgencyInput();
            String reason = referralView.getReasonInput();
            String summary = referralView.getSummaryInput();
            String investigations = referralView.getInvestigationsInput();
            String notes = referralView.getNotesInput();

            // Basic required checks
            if (patientId.isEmpty() || fromClinician.isEmpty() || toClinician.isEmpty()
                    || fromFacility.isEmpty() || toFacility.isEmpty()
                    || reason.isEmpty() || summary.isEmpty() || investigations.isEmpty()) {

                JOptionPane.showMessageDialog(
                        referralView,
                        "Fill all required fields. Notes can be empty.",
                        "Missing fields",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (urgency.isEmpty()) urgency = "Routine";

            Referral candidate = new Referral(
                    "TEMP",
                    patientId,
                    fromClinician,
                    toClinician,
                    fromFacility,
                    toFacility,
                    "",             // referral_date -> repository sets today
                    urgency,
                    reason,
                    summary,
                    investigations,
                    "Pending",
                    "",             // appointment_id optional
                    notes,
                    "",             // created_date set in repository
                    ""              // last_updated set in repository
            );

            // IMPORTANT: create via Singleton manager (handles queue + outputs + audit)
            String newId = referralManager.createAndQueueReferral(candidate);

            referralView.setStatus("Created referral via Singleton: " + newId +
                    " | Queue size: " + referralManager.getQueueSize());

            referralView.clearForm();
            refresh();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    referralView,
                    "Failed to create referral:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
