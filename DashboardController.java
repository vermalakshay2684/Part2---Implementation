package controller;

import repository.AppointmentRepository;
import repository.PatientRepository;
import repository.PrescriptionRepository;
import repository.ReferralRepository;
import view.*;

import java.nio.file.Path;

/**
 * DashboardController:
 * - Opens each module window when a dashboard button is clicked.
 * - Keeps the app coherent as one system.
 */
public class DashboardController {

    private final DashboardFrame dashboard;

    // Paths
    private final Path patientsCsv;
    private final Path appointmentsCsv;
    private final Path prescriptionsCsv;
    private final Path referralsCsv;
    private final Path outDir;

    public DashboardController(DashboardFrame dashboard,
                               Path patientsCsv,
                               Path appointmentsCsv,
                               Path prescriptionsCsv,
                               Path referralsCsv,
                               Path outDir) {
        this.dashboard = dashboard;
        this.patientsCsv = patientsCsv;
        this.appointmentsCsv = appointmentsCsv;
        this.prescriptionsCsv = prescriptionsCsv;
        this.referralsCsv = referralsCsv;
        this.outDir = outDir;

        wireActions();
    }

    private void wireActions() {

        dashboard.getPatientsButton().addActionListener(e -> {
            PatientRepository repo = new PatientRepository(patientsCsv);
            PatientTableFrame view = new PatientTableFrame();
            MainController controller = new MainController(repo, view);
            controller.start();
        });

        dashboard.getAppointmentsButton().addActionListener(e -> {
            try {
                AppointmentRepository repo = new AppointmentRepository(appointmentsCsv);
                AppointmentTableFrame view = new AppointmentTableFrame();
                AppointmentController controller = new AppointmentController(repo, view);
                controller.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(
                        dashboard,
                        "Failed to open Appointments:\n" + ex.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        });


        dashboard.getPrescriptionsButton().addActionListener(e -> {
            PrescriptionRepository repo = new PrescriptionRepository(prescriptionsCsv);
            PrescriptionTableFrame view = new PrescriptionTableFrame();
            PrescriptionController controller = new PrescriptionController(repo, view);
            controller.start();
        });

        dashboard.getReferralsButton().addActionListener(e -> {
            try {
                ReferralRepository repo = new ReferralRepository(referralsCsv);
                ReferralTableFrame view = new ReferralTableFrame();
                ReferralController controller = new ReferralController(repo, view, outDir);
                controller.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void start() {
        dashboard.setVisible(true);
    }
}
