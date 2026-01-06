package repository;

import model.Appointment;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentRepository:
 * - Reads appointments.csv into Appointment objects
 * - Updates/cancels appointments by overwriting the CSV (no database allowed)
 *
 * NOTE:
 * - We keep Appointment fields as String for now.
 * - Repository is responsible for persistence, not business validation.
 */
public class AppointmentRepository {

    private final Path appointmentsCsvPath;

    public AppointmentRepository(Path appointmentsCsvPath) {
        this.appointmentsCsvPath = appointmentsCsvPath;
    }

    public List<Appointment> loadAll() throws IOException {
        List<String[]> rows = CsvUtil.readAll(appointmentsCsvPath);

        List<Appointment> appointments = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 13) continue;

            Appointment a = new Appointment(
                    r[0], r[1], r[2], r[3],
                    r[4], r[5], r[6], r[7],
                    r[8], r[9], r[10], r[11], r[12]
            );

            appointments.add(a);
        }

        return appointments;
    }

    /**
     * Updates an existing appointment row matching appointmentId.
     * Returns true if updated, false if not found.
     */
    public boolean updateAppointment(Appointment updated) throws IOException {
        List<String[]> rows = CsvUtil.readAll(appointmentsCsvPath);

        // Header is at index 0
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 13) continue;

            if (r[0].equals(updated.getAppointmentId())) {
                // Overwrite all columns in correct order
                rows.set(i, toCsvRow(updated));
                CsvUtil.writeAll(appointmentsCsvPath, rows);
                return true;
            }
        }
        return false;
    }

    /**
     * Cancels an appointment by setting status=Cancelled and updating last_modified.
     */
    public boolean cancelAppointment(String appointmentId) throws IOException {
        List<String[]> rows = CsvUtil.readAll(appointmentsCsvPath);

        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 13) continue;

            if (r[0].equals(appointmentId)) {
                // Column indexes (based on header order)
                // 8 = status
                // 12 = last_modified
                r[8] = "Cancelled";
                r[12] = LocalDate.now().toString(); // simple date stamp

                rows.set(i, r);
                CsvUtil.writeAll(appointmentsCsvPath, rows);
                return true;
            }
        }
        return false;
    }


    /**
     * Creates a new appointment and appends it to appointments.csv.
     * Returns the generated appointment_id.
     *
     * Repository does not enforce business validation.
     * Validation will be done later in service/controller.
     */
    public String createAppointment(Appointment newAppointment) throws IOException {

        // Collect existing IDs from file
        List<String[]> rows = CsvUtil.readAll(appointmentsCsvPath);
        List<String> ids = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).length > 0) ids.add(rows.get(i)[0]);
        }

        String nextId = service.IdGenerator.nextId(ids);

        // Force-set ID into a new Appointment object (immutable fields)
        Appointment toSave = new Appointment(
                nextId,
                newAppointment.getPatientId(),
                newAppointment.getClinicianId(),
                newAppointment.getFacilityId(),
                newAppointment.getAppointmentDate(),
                newAppointment.getAppointmentTime(),
                newAppointment.getDurationMinutes(),
                newAppointment.getAppointmentType(),
                newAppointment.getStatus(),
                newAppointment.getReasonForVisit(),
                newAppointment.getNotes(),
                newAppointment.getCreatedDate(),
                newAppointment.getLastModified()
        );

        CsvUtil.appendRow(appointmentsCsvPath, toCsvRow(toSave));
        return nextId;
    }


    /**
     * Convert Appointment -> CSV row in the SAME column order as appointments.csv
     */
    private String[] toCsvRow(Appointment a) {
        return new String[]{
                a.getAppointmentId(),
                a.getPatientId(),
                a.getClinicianId(),
                a.getFacilityId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getDurationMinutes(),
                a.getAppointmentType(),
                a.getStatus(),
                a.getReasonForVisit(),
                a.getNotes(),
                a.getCreatedDate(),
                a.getLastModified()
        };
    }
}
