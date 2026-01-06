package app;

import repository.CsvUtil;

import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.util.List;

/**
 * Step 3 test:
 * - Reads data/patients.csv using CsvUtil
 * - Prints the header and first data row to the console
 */
public class Main {

    public static void main(String[] args) {

        // Keep Swing thread available (not used yet), but this confirms app still runs
        SwingUtilities.invokeLater(() -> {
            try {
                Path patientsCsv = Path.of("data", "patients.csv");

                List<String[]> rows = CsvUtil.readAll(patientsCsv);

                System.out.println("Total rows (including header): " + rows.size());
                System.out.println("Header columns: " + rows.get(0).length);
                System.out.println("Header[0..3]: " + rows.get(0)[0] + ", " + rows.get(0)[1] + ", " + rows.get(0)[2] + ", " + rows.get(0)[3]);

                if (rows.size() > 1) {
                    System.out.println("First data row[0..3]: " + rows.get(1)[0] + ", " + rows.get(1)[1] + ", " + rows.get(1)[2] + ", " + rows.get(1)[3]);
                } else {
                    System.out.println("No data rows found (only header).");
                }

            } catch (Exception e) {
                // Print full error so we can diagnose path/JDK issues quickly
                e.printStackTrace();
            }
        });
    }
}

