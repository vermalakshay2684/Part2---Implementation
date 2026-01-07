package service;

import model.Referral;
import repository.ReferralRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ReferralManager (Singleton)
 * - Ensures ONE consistent referral processing component
 * - Manages referral queue + simulated email + EHR updates + audit trail
 *
 * This directly addresses coursework requirement:
 * "Singleton pattern to manage referral queues, email communications, and EHR updates"
 */
public final class ReferralManager {

    private static ReferralManager instance;

    private final ReferralRepository referralRepository;

    // In-memory queue of referrals awaiting processing
    private final Deque<Referral> referralQueue = new ArrayDeque<>();

    // Output files (simulated communications)
    private final Path emailOutPath;
    private final Path ehrOutPath;
    private final Path auditOutPath;

    /**
     * Private constructor: prevents outside instantiation.
     */
    private ReferralManager(ReferralRepository referralRepository, Path outDir) throws IOException {
        this.referralRepository = referralRepository;

        // Ensure output directory exists
        Files.createDirectories(outDir);

        this.emailOutPath = outDir.resolve("referral_emails.txt");
        this.ehrOutPath = outDir.resolve("ehr_updates.log");
        this.auditOutPath = outDir.resolve("audit.log");

        // Ensure files exist (create if missing)
        touch(emailOutPath);
        touch(ehrOutPath);
        touch(auditOutPath);
    }

    private void touch(Path file) throws IOException {
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    /**
     * Singleton accessor.
     * Thread-safe via synchronized.
     *
     * IMPORTANT: first call must supply repository + outDir.
     */
    public static synchronized ReferralManager getInstance(ReferralRepository referralRepository, Path outDir) throws IOException {
        if (instance == null) {
            instance = new ReferralManager(referralRepository, outDir);
        }
        return instance;
    }

    /**
     * Create referral + enqueue + persist communications + audit.
     * Returns generated referral ID.
     */
    public synchronized String createAndQueueReferral(Referral referral) throws IOException {

        // 1) Persist referral record into referrals.csv (generates ID)
        String newId = referralRepository.createReferral(referral);

        // 2) Enqueue (use a new Referral object with the generated ID for queue)
        Referral queued = new Referral(
                newId,
                referral.getPatientId(),
                referral.getReferringClinicianId(),
                referral.getReferredToClinicianId(),
                referral.getReferringFacilityId(),
                referral.getReferredToFacilityId(),
                referral.getReferralDate(),
                referral.getUrgencyLevel(),
                referral.getReferralReason(),
                referral.getClinicalSummary(),
                referral.getRequestedInvestigations(),
                referral.getStatus(),
                referral.getAppointmentId(),
                referral.getNotes(),
                referral.getCreatedDate(),
                referral.getLastUpdated()
        );

        referralQueue.addLast(queued);

        // 3) Simulate communications
        writeEmailSimulation(queued);
        writeEhrUpdateSimulation(queued);

        // 4) Audit
        audit("CREATED_AND_QUEUED", queued);

        return newId;
    }

    /**
     * Processes next referral in queue and records audit.
     * Returns the referral processed, or null if queue is empty.
     */
    public synchronized Referral processNextReferral() throws IOException {
        Referral r = referralQueue.pollFirst();
        if (r == null) return null;

        audit("PROCESSED", r);
        return r;
    }

    public synchronized int getQueueSize() {
        return referralQueue.size();
    }

    // ------------------- persistence outputs -------------------

    private void writeEmailSimulation(Referral r) throws IOException {
        String timestamp = LocalDateTime.now().toString();

        String emailBlock =
                "----- REFERRAL EMAIL (SIMULATED) -----\n" +
                        "Timestamp: " + timestamp + "\n" +
                        "To (Specialist Clinician ID): " + r.getReferredToClinicianId() + "\n" +
                        "From (Referring Clinician ID): " + r.getReferringClinicianId() + "\n" +
                        "Patient ID: " + r.getPatientId() + "\n" +
                        "Urgency: " + r.getUrgencyLevel() + "\n" +
                        "Reason: " + r.getReferralReason() + "\n" +
                        "Clinical Summary: " + r.getClinicalSummary() + "\n" +
                        "Requested Investigations: " + r.getRequestedInvestigations() + "\n" +
                        "Referral ID: " + r.getReferralId() + "\n" +
                        "-------------------------------------\n\n";

        appendText(emailOutPath, emailBlock);
    }

    private void writeEhrUpdateSimulation(Referral r) throws IOException {
        String line = LocalDateTime.now() +
                " | EHR_UPDATE | referral_id=" + r.getReferralId() +
                " | patient_id=" + r.getPatientId() +
                " | status=" + r.getStatus() +
                " | referred_to=" + r.getReferredToClinicianId() + "\n";

        appendText(ehrOutPath, line);
    }

    private void audit(String action, Referral r) throws IOException {
        String line = LocalDateTime.now() +
                " | " + action +
                " | referral_id=" + r.getReferralId() +
                " | patient_id=" + r.getPatientId() +
                " | from=" + r.getReferringClinicianId() +
                " | to=" + r.getReferredToClinicianId() +
                " | urgency=" + r.getUrgencyLevel() + "\n";

        appendText(auditOutPath, line);
    }

    private void appendText(Path file, String text) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(
                file,
                StandardOpenOption.APPEND
        )) {
            bw.write(text);
        }
    }
}
