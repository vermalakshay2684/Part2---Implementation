package app;

import controller.DashboardController;
import view.DashboardFrame;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Path patientsCsv = Path.of("data", "patients.csv");
            Path appointmentsCsv = Path.of("data", "appointments.csv");
            Path prescriptionsCsv = Path.of("data", "prescriptions.csv");
            Path referralsCsv = Path.of("data", "referrals.csv");
            Path outDir = Path.of("data", "out");

            DashboardFrame dashboard = new DashboardFrame();
            DashboardController controller = new DashboardController(
                    dashboard,
                    patientsCsv,
                    appointmentsCsv,
                    prescriptionsCsv,
                    referralsCsv,
                    outDir
            );

            controller.start();
        });
    }
}
