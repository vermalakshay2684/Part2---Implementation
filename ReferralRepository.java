package repository;

import model.Referral;
import service.IdGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ReferralRepository:
 * - Loads referrals from referrals.csv
 * - Appends new referrals (no DB)
 *
 * Singleton requirement is NOT here. This is pure persistence.
 */
public class ReferralRepository {

    private final Path referralsCsvPath;

    public ReferralRepository(Path referralsCsvPath) {
        this.referralsCsvPath = referralsCsvPath;
    }

    public List<Referral> loadAll() throws IOException {
        List<String[]> rows = CsvUtil.readAll(referralsCsvPath);

        List<Referral> referrals = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 16) continue;

            Referral ref = new Referral(
                    r[0], r[1], r[2], r[3],
                    r[4], r[5], r[6], r[7],
                    r[8], r[9], r[10], r[11],
                    r[12], r[13], r[14], r[15]
            );

            referrals.add(ref);
        }

        return referrals;
    }

    /**
     * Appends a new referral to referrals.csv, generating referral_id.
     * Returns generated ID.
     */
    public String createReferral(Referral newReferral) throws IOException {
        List<String[]> rows = CsvUtil.readAll(referralsCsvPath);

        List<String> ids = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).length > 0) ids.add(rows.get(i)[0]);
        }

        String nextId = IdGenerator.nextId(ids);

        String today = LocalDate.now().toString();

        Referral toSave = new Referral(
                nextId,
                newReferral.getPatientId(),
                newReferral.getReferringClinicianId(),
                newReferral.getReferredToClinicianId(),
                newReferral.getReferringFacilityId(),
                newReferral.getReferredToFacilityId(),
                blankIfEmpty(newReferral.getReferralDate(), today),
                blankIfEmpty(newReferral.getUrgencyLevel(), "Routine"),
                newReferral.getReferralReason(),
                newReferral.getClinicalSummary(),
                newReferral.getRequestedInvestigations(),
                blankIfEmpty(newReferral.getStatus(), "Pending"),
                blankIfEmpty(newReferral.getAppointmentId(), ""),
                blankIfEmpty(newReferral.getNotes(), ""),
                today,
                today
        );

        CsvUtil.appendRow(referralsCsvPath, toCsvRow(toSave));
        return nextId;
    }

    private String blankIfEmpty(String value, String fallback) {
        if (value == null) return fallback;
        String v = value.trim();
        return v.isEmpty() ? fallback : v;
    }

    private String[] toCsvRow(Referral r) {
        return new String[]{
                r.getReferralId(),
                r.getPatientId(),
                r.getReferringClinicianId(),
                r.getReferredToClinicianId(),
                r.getReferringFacilityId(),
                r.getReferredToFacilityId(),
                r.getReferralDate(),
                r.getUrgencyLevel(),
                r.getReferralReason(),
                r.getClinicalSummary(),
                r.getRequestedInvestigations(),
                r.getStatus(),
                r.getAppointmentId(),
                r.getNotes(),
                r.getCreatedDate(),
                r.getLastUpdated()
        };
    }
}
