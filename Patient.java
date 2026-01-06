package model;

/**
 * Patient model.
 * Mirrors patients.csv columns exactly (as Strings).
 * We keep them as Strings for now to avoid date parsing errors early on.
 */
public class Patient {

    private final String patientId;
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String nhsNumber;
    private final String gender;
    private final String phoneNumber;
    private final String email;
    private final String address;
    private final String postcode;
    private final String emergencyContactName;
    private final String emergencyContactPhone;
    private final String registrationDate;
    private final String gpSurgeryId;

    public Patient(
            String patientId,
            String firstName,
            String lastName,
            String dateOfBirth,
            String nhsNumber,
            String gender,
            String phoneNumber,
            String email,
            String address,
            String postcode,
            String emergencyContactName,
            String emergencyContactPhone,
            String registrationDate,
            String gpSurgeryId
    ) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.registrationDate = registrationDate;
        this.gpSurgeryId = gpSurgeryId;
    }

    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getNhsNumber() { return nhsNumber; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public String getRegistrationDate() { return registrationDate; }
    public String getGpSurgeryId() { return gpSurgeryId; }

    /** Helpful for debugging */
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
