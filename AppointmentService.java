package service;

import model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * AppointmentService:
 * - Business rules (validation) for appointments
 * - Repository handles persistence; Service handles rules
 */
public class AppointmentService {

    /**
     * Rule 1: No appointments in the past.
     * Returns null if OK, otherwise an error message string.
     */
    public String validateNotInPast(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            return "Appointment date cannot be in the past.";
        }

        // If same date, time must not be earlier than now
        if (date.isEqual(today)) {
            LocalTime now = LocalTime.now();
            if (time.isBefore(now)) {
                return "Appointment time cannot be in the past (today).";
            }
        }

        return null;
    }

    /**
     * Rule 2: No double-booking for the same clinician at same date + time.
     * We consider an appointment conflicting if:
     * - same clinicianId
     * - same date
     * - same time
     * - status is not Cancelled
     */
    public String validateNoClinicianDoubleBooking(List<Appointment> existing, Appointment candidate) {
        for (Appointment a : existing) {
            if (a.getClinicianId().equals(candidate.getClinicianId())
                    && a.getAppointmentDate().equals(candidate.getAppointmentDate())
                    && a.getAppointmentTime().equals(candidate.getAppointmentTime())
                    && !a.getStatus().equalsIgnoreCase("Cancelled")) {
                return "Clinician is already booked at " + candidate.getAppointmentDate() +
                        " " + candidate.getAppointmentTime() + ".";
            }
        }
        return null;
    }
}
