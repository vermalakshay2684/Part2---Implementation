package controller;

import model.Patient;
import repository.PatientRepository;
import view.PatientTableFrame;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;

/**
 * MainController (CONTROLLER)
 * - Calls repository to fetch data
 * - Updates view
 * - Handles user-facing error messages
 */
public class MainController {

    private final PatientRepository patientRepository;
    private final PatientTableFrame patientView;

    public MainController(PatientRepository patientRepository, PatientTableFrame patientView) {
        this.patientRepository = patientRepository;
        this.patientView = patientView;
    }

    public void start() {
        try {
            List<Patient> patients = patientRepository.loadAll();
            patientView.setPatients(patients);
            patientView.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load patients.csv:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
