package view;

import model.Patient;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * PatientTableFrame (VIEW only)
 * - Builds UI
 * - Exposes setPatients() for Controller to inject data
 * - Does NOT read files or apply business rules
 */
public class PatientTableFrame extends JFrame {

    private final DefaultTableModel tableModel;


    public PatientTableFrame() {
        setTitle("Patients");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "First Name", "Last Name", "DOB", "NHS", "Phone"},
                0
        );

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table));
    }

    /** Controller calls this to push data into the view */
    public void setPatients(List<Patient> patients) {
        tableModel.setRowCount(0);

        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getPatientId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getDateOfBirth(),
                    p.getNhsNumber(),
                    p.getPhoneNumber()
            });
        }
    }
}
