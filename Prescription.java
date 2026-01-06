package model;

/**
 * Prescription model.
 * Mirrors prescriptions.csv columns exactly (kept as Strings).
 *
 * prescriptions.csv columns:
 * prescription_id, patient_id, clinician_id, appointment_id, prescription_date,
 * medication_name, dosage, frequency, duration_days, quantity, instructions,
 * pharmacy_name, status, issue_date, collection_date
 *
 * Note: appointment_id may be blank in your provided dataset, so allow empty String.
 */
public class Prescription {

    private final String prescriptionId;
    private final String patientId;
    private final String clinicianId;
    private final String appointmentId;     // can be empty
    private final String prescriptionDate;

    private final String medicationName;
    private final String dosage;
    private final String frequency;
    private final String durationDays;
    private final String quantity;
    private final String instructions;

    private final String pharmacyName;
    private final String status;
    private final String issueDate;
    private final String collectionDate;

    public Prescription(
            String prescriptionId,
            String patientId,
            String clinicianId,
            String appointmentId,
            String prescriptionDate,
            String medicationName,
            String dosage,
            String frequency,
            String durationDays,
            String quantity,
            String instructions,
            String pharmacyName,
            String status,
            String issueDate,
            String collectionDate
    ) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getAppointmentId() { return appointmentId; }
    public String getPrescriptionDate() { return prescriptionDate; }
    public String getMedicationName() { return medicationName; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public String getDurationDays() { return durationDays; }
    public String getQuantity() { return quantity; }
    public String getInstructions() { return instructions; }
    public String getPharmacyName() { return pharmacyName; }
    public String getStatus() { return status; }
    public String getIssueDate() { return issueDate; }
    public String getCollectionDate() { return collectionDate; }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId='" + prescriptionId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", clinicianId='" + clinicianId + '\'' +
                ", medicationName='" + medicationName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
