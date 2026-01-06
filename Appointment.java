package model;

/**
 * Appointment model.
 * Mirrors appointments.csv columns exactly (kept as Strings for now).
 *
 * appointments.csv columns:
 * appointment_id, patient_id, clinician_id, facility_id, appointment_date, appointment_time,
 * duration_minutes, appointment_type, status, reason_for_visit, notes, created_date, last_modified
 */
public class Appointment {

    private final String appointmentId;
    private final String patientId;
    private final String clinicianId;
    private final String facilityId;
    private final String appointmentDate;   // e.g., 2025-01-10
    private final String appointmentTime;   // e.g., 09:30
    private final String durationMinutes;
    private final String appointmentType;
    private final String status;            // Scheduled / Cancelled etc.
    private final String reasonForVisit;
    private final String notes;
    private final String createdDate;
    private final String lastModified;

    public Appointment(
            String appointmentId,
            String patientId,
            String clinicianId,
            String facilityId,
            String appointmentDate,
            String appointmentTime,
            String durationMinutes,
            String appointmentType,
            String status,
            String reasonForVisit,
            String notes,
            String createdDate,
            String lastModified
    ) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastModified = lastModified;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getFacilityId() { return facilityId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getDurationMinutes() { return durationMinutes; }
    public String getAppointmentType() { return appointmentType; }
    public String getStatus() { return status; }
    public String getReasonForVisit() { return reasonForVisit; }
    public String getNotes() { return notes; }
    public String getCreatedDate() { return createdDate; }
    public String getLastModified() { return lastModified; }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", clinicianId='" + clinicianId + '\'' +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
