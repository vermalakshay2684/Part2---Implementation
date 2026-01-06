package model;

/**
 * Referral model.
 * Mirrors referrals.csv columns exactly (kept as Strings).
 *
 * referrals.csv columns:
 * referral_id, patient_id, referring_clinician_id, referred_to_clinician_id,
 * referring_facility_id, referred_to_facility_id, referral_date, urgency_level,
 * referral_reason, clinical_summary, requested_investigations, status, appointment_id,
 * notes, created_date, last_updated
 *
 * Note: appointment_id can be blank depending on workflow.
 */
public class Referral {

    private final String referralId;
    private final String patientId;
    private final String referringClinicianId;
    private final String referredToClinicianId;
    private final String referringFacilityId;
    private final String referredToFacilityId;
    private final String referralDate;
    private final String urgencyLevel;
    private final String referralReason;
    private final String clinicalSummary;
    private final String requestedInvestigations;
    private final String status;
    private final String appointmentId;      // can be empty
    private final String notes;
    private final String createdDate;
    private final String lastUpdated;

    public Referral(
            String referralId,
            String patientId,
            String referringClinicianId,
            String referredToClinicianId,
            String referringFacilityId,
            String referredToFacilityId,
            String referralDate,
            String urgencyLevel,
            String referralReason,
            String clinicalSummary,
            String requestedInvestigations,
            String status,
            String appointmentId,
            String notes,
            String createdDate,
            String lastUpdated
    ) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredToClinicianId = referredToClinicianId;
        this.referringFacilityId = referringFacilityId;
        this.referredToFacilityId = referredToFacilityId;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.referralReason = referralReason;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.status = status;
        this.appointmentId = appointmentId;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    public String getReferralId() { return referralId; }
    public String getPatientId() { return patientId; }
    public String getReferringClinicianId() { return referringClinicianId; }
    public String getReferredToClinicianId() { return referredToClinicianId; }
    public String getReferringFacilityId() { return referringFacilityId; }
    public String getReferredToFacilityId() { return referredToFacilityId; }
    public String getReferralDate() { return referralDate; }
    public String getUrgencyLevel() { return urgencyLevel; }
    public String getReferralReason() { return referralReason; }
    public String getClinicalSummary() { return clinicalSummary; }
    public String getRequestedInvestigations() { return requestedInvestigations; }
    public String getStatus() { return status; }
    public String getAppointmentId() { return appointmentId; }
    public String getNotes() { return notes; }
    public String getCreatedDate() { return createdDate; }
    public String getLastUpdated() { return lastUpdated; }

    @Override
    public String toString() {
        return "Referral{" +
                "referralId='" + referralId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", referringClinicianId='" + referringClinicianId + '\'' +
                ", referredToClinicianId='" + referredToClinicianId + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
