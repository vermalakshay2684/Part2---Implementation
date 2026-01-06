package repository;

import model.Patient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientRepository:
 * - Reads patients.csv
 * - Converts rows into Patient objects
 * - Returns List<Patient>
 *
 * MVC note: Repositories handle data access only. No GUI code here.
 */
public class PatientRepository {

    private final Path patientsCsvPath;

    public PatientRepository(Path patientsCsvPath) {
        this.patientsCsvPath = patientsCsvPath;
    }

    public List<Patient> loadAll() throws IOException {
        List<String[]> rows = CsvUtil.readAll(patientsCsvPath);

        List<Patient> patients = new ArrayList<>();

        // rows[0] is the header. Start from 1.
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);

            // Defensive check: skip malformed rows
            if (r.length < 14) continue;

            Patient p = new Patient(
                    r[0], r[1], r[2], r[3],
                    r[4], r[5], r[6], r[7],
                    r[8], r[9], r[10], r[11],
                    r[12], r[13]
            );
            patients.add(p);
        }

        return patients;
    }
}
