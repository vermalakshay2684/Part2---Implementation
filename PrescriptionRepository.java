package repository;

import model.Prescription;
import service.IdGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PrescriptionRepository:
 * - Loads prescriptions from prescriptions.csv
 * - Appends new prescription records (no DB allowed)
 *
 * IDs are generated using IdGenerator based on existing prescription_id values.
 */
public class PrescriptionRepository {

    private final Path prescriptionsCsvPath;

    public PrescriptionRepository(Path prescriptionsCsvPath) {
        this.prescriptionsCsvPath = prescriptionsCsvPath;
    }

    public List<Prescription> loadAll() throws IOException {
        List<String[]> rows = CsvUtil.readAll(prescriptionsCsvPath);

        List<Prescription> prescriptions = new ArrayList<>();

        // Skip header
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 15) continue;

            Prescription p = new Prescription(
                    r[0], r[1], r[2], r[3], r[4],
                    r[5], r[6], r[7], r[8], r[9],
                    r[10], r[11], r[12], r[13], r[14]
            );

            prescriptions.add(p);
        }

        return prescriptions;
    }

    /**
     * Appends a new prescription to the CSV, generating a new prescription_id.
     * Returns the generated ID.
     */
    public String createPrescription(Prescription newPrescription) throws IOException {
        List<String[]> rows = CsvUtil.readAll(prescriptionsCsvPath);

        // Collect existing IDs
        List<String> ids = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).length > 0) ids.add(rows.get(i)[0]);
        }

        String nextId = IdGenerator.nextId(ids);

        // Set dates consistently (simple approach)
        String today = LocalDate.now().toString();

        // Force the generated ID into an immutable object
        Prescription toSave = new Prescription(
                nextId,
                newPrescription.getPatientId(),
                newPrescription.getClinicianId(),
                newPrescription.getAppointmentId(),           // can be blank
                blankIfEmpty(newPrescription.getPrescriptionDate(), today),
                newPrescription.getMedicationName(),
                newPrescription.getDosage(),
                newPrescription.getFrequency(),
                newPrescription.getDurationDays(),
                newPrescription.getQuantity(),
                newPrescription.getInstructions(),
                newPrescription.getPharmacyName(),
                blankIfEmpty(newPrescription.getStatus(), "Issued"),
                blankIfEmpty(newPrescription.getIssueDate(), today),
                blankIfEmpty(newPrescription.getCollectionDate(), "") // can be empty
        );

        CsvUtil.appendRow(prescriptionsCsvPath, toCsvRow(toSave));
        return nextId;
    }

    private String blankIfEmpty(String value, String fallback) {
        if (value == null) return fallback;
        String v = value.trim();
        return v.isEmpty() ? fallback : v;
    }

    private String[] toCsvRow(Prescription p) {
        return new String[]{
                p.getPrescriptionId(),
                p.getPatientId(),
                p.getClinicianId(),
                p.getAppointmentId(),
                p.getPrescriptionDate(),
                p.getMedicationName(),
                p.getDosage(),
                p.getFrequency(),
                p.getDurationDays(),
                p.getQuantity(),
                p.getInstructions(),
                p.getPharmacyName(),
                p.getStatus(),
                p.getIssueDate(),
                p.getCollectionDate()
        };
    }
}
