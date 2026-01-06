package controller;

import model.Appointment;
import repository.AppointmentRepository;
import view.AppointmentTableFrame;
import service.AppointmentService;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * AppointmentController:
 * - Loads appointments into view
 * - Handles Refresh + Cancel Selected
 * - Handles Create Appointment
 * - Handles Load Selected -> Form
 * - Handles Update Selected
 */
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentTableFrame appointmentView;

    // Business rules
    private final AppointmentService appointmentService = new AppointmentService();

    public AppointmentController(AppointmentRepository appointmentRepository, AppointmentTableFrame appointmentView) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentView = appointmentView;
        wireActions();
    }

    private void wireActions() {
        appointmentView.getRefreshButton().addActionListener(e -> refresh());
        appointmentView.getCancelButton().addActionListener(e -> cancelSelected());
        appointmentView.getCreateButton().addActionListener(e -> createAppointmentFromForm());

        // NEW
        appointmentView.getLoadSelectedButton().addActionListener(e -> loadSelectedIntoForm());
        appointmentView.getUpdateSelectedButton().addActionListener(e -> updateSelectedFromForm());
    }

    public void start() {
        refresh();
        appointmentView.setVisible(true);
    }

    private void refresh() {
        try {
            appointmentView.setAppointments(appointmentRepository.loadAll());
            appointmentView.setStatus("Loaded appointments from CSV");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Failed to load appointments.csv:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cancelSelected() {
        String id = appointmentView.getSelectedAppointmentId();
        if (id == null) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Please select an appointment row first.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                appointmentView,
                "Cancel appointment " + id + "?",
                "Confirm cancel",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = appointmentRepository.cancelAppointment(id);
            if (ok) {
                appointmentView.setStatus("Cancelled appointment: " + id);
                refresh();
            } else {
                appointmentView.setStatus("Cancel failed: appointment not found (" + id + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Failed to cancel appointment:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createAppointmentFromForm() {
        try {
            Appointment candidate = buildCandidateAppointmentFromForm("TEMP");

            // Validate (service layer)
            String err = validateCandidateForCreate(candidate);
            if (err != null) {
                JOptionPane.showMessageDialog(appointmentView, err, "Validation error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newId = appointmentRepository.createAppointment(candidate);

            appointmentView.setStatus("Created appointment: " + newId);
            appointmentView.clearCreateForm();
            refresh();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Failed to create appointment:\n" + ex.getMessage() +
                            "\n\nDate must be yyyy-MM-dd, Time must be HH:mm.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * NEW: Load selected row values into form for editing.
     * Notes are not in the table, so we set notes empty by default.
     */
    private void loadSelectedIntoForm() {
        String[] row = appointmentView.getSelectedRowValues();
        if (row == null) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Please select an appointment row first.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // row mapping from view:
        // [0]=appointmentId [1]=patientId [2]=clinicianId [3]=facilityId
        // [4]=date [5]=time [6]=duration [7]=type [8]=status [9]=reason
        appointmentView.setFormFields(
                row[1], // patientId
                row[2], // clinicianId
                row[3], // facilityId
                row[4], // date
                row[5], // time
                row[6], // duration
                row[7], // type
                row[9], // reason
                ""      // notes (not in table)
        );

        appointmentView.setStatus("Loaded selected appointment into form. Now edit and click Update Selected.");
    }

    /**
     * NEW: Update appointment row in CSV using form values + selected appointment_id.
     */
    private void updateSelectedFromForm() {
        try {
            String appointmentId = appointmentView.getSelectedAppointmentId();
            if (appointmentId == null) {
                JOptionPane.showMessageDialog(
                        appointmentView,
                        "Select the appointment row you want to update.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Build candidate using SAME appointment_id (so repository updates correct row)
            Appointment candidate = buildCandidateAppointmentFromForm(appointmentId);

            // Validate (service layer)
            String err = validateCandidateForUpdate(candidate, appointmentId);
            if (err != null) {
                JOptionPane.showMessageDialog(appointmentView, err, "Validation error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = appointmentRepository.updateAppointment(candidate);
            if (ok) {
                appointmentView.setStatus("Updated appointment: " + appointmentId);
                appointmentView.clearCreateForm();
                refresh();
            } else {
                appointmentView.setStatus("Update failed: appointment not found (" + appointmentId + ")");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    appointmentView,
                    "Failed to update appointment:\n" + ex.getMessage() +
                            "\n\nDate must be yyyy-MM-dd, Time must be HH:mm.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Builds an Appointment from form inputs.
     * appointmentId is injected by caller (TEMP for create, real id for update).
     */
    private Appointment buildCandidateAppointmentFromForm(String appointmentId) {
        String patientId = appointmentView.getPatientIdInput();
        String clinicianId = appointmentView.getClinicianIdInput();
        String facilityId = appointmentView.getFacilityIdInput();
        String dateStr = appointmentView.getDateInput();
        String timeStr = appointmentView.getTimeInput();
        String durationStr = appointmentView.getDurationInput();
        String type = appointmentView.getTypeInput();
        String reason = appointmentView.getReasonInput();
        String notes = appointmentView.getNotesInput();

        // Basic required checks
        if (patientId.isEmpty() || clinicianId.isEmpty() || facilityId.isEmpty()
                || dateStr.isEmpty() || timeStr.isEmpty() || durationStr.isEmpty()
                || type.isEmpty() || reason.isEmpty()) {
            throw new IllegalArgumentException("Missing required fields (notes can be empty).");
        }

        // Parse date/time to enforce format
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);

        int duration = Integer.parseInt(durationStr);
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive.");

        LocalDate today = LocalDate.now();

        // For updates: we keep status Scheduled unless user cancelled
        // (Later you can load current status from repository if needed)
        String status = "Scheduled";

        return new Appointment(
                appointmentId,
                patientId,
                clinicianId,
                facilityId,
                date.toString(),
                time.toString(),
                String.valueOf(duration),
                type,
                status,
                reason,
                notes,
                today.toString(),
                today.toString()
        );
    }

    /**
     * Validation for create:
     * - not in past
     * - no clinician double-booking
     */
    private String validateCandidateForCreate(Appointment candidate) throws Exception {
        LocalDate date = LocalDate.parse(candidate.getAppointmentDate());
        LocalTime time = LocalTime.parse(candidate.getAppointmentTime());

        String err1 = appointmentService.validateNotInPast(date, time);
        if (err1 != null) return err1;

        var existing = appointmentRepository.loadAll();
        return appointmentService.validateNoClinicianDoubleBooking(existing, candidate);
    }

    /**
     * Validation for update:
     * - not in past
     * - no clinician double-booking EXCEPT itself (same appointment_id)
     */
    private String validateCandidateForUpdate(Appointment candidate, String appointmentId) throws Exception {
        LocalDate date = LocalDate.parse(candidate.getAppointmentDate());
        LocalTime time = LocalTime.parse(candidate.getAppointmentTime());

        String err1 = appointmentService.validateNotInPast(date, time);
        if (err1 != null) return err1;

        var existing = appointmentRepository.loadAll();

        // Double-booking check but ignore the appointment being updated
        for (Appointment a : existing) {
            if (a.getAppointmentId().equals(appointmentId)) continue;

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
