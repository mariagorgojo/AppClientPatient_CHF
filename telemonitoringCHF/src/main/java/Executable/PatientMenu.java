/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Executable;

import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.BitalinoDemo;
import Utilities.Utilities;
import pojos.Patient;
import ConnectionPatient.*;
import Utilities.Encryption;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import pojos.Disease;
import pojos.Episode;
import pojos.Patient.Gender;
import pojos.Recording;
import pojos.Surgery;
import pojos.Symptom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martaguzman
 */
public class PatientMenu {

    public static void main(String[] args) {
        String ip_address_valid = null;

        try {
            System.out.println("\nPlease enter a valid IP address: ");
            ip_address_valid = Utilities.getValidIPAddress();
            try {
                ConnectionPatient.connectToServer(ip_address_valid);
            } catch (IOException ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);

            }
            mainMenu();
        } finally {
            ConnectionPatient.closeConnection(); // Close the connection at the end
        }
    }

    private static void mainMenu() {

        System.out.println("\n-- Welcome to the Patient App --");

        while (true) {

            System.out.println("\n1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");
            System.out.println("\nPlease select an option to get started:");

            int option = Utilities.readInteger();

            switch (option) {
                case 1:

                    registerPatient();
                    break;
                case 2:

                    loginMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    ConnectionPatient.closeConnection();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }

    }

    private static void loginMenu() {
        String dni;
        String password;

        do {
            System.out.println("\nEnter DNI to login (-1 to go back): ");
            dni = Utilities.readString();
            if (dni.equals("-1")) {
                return; // Go back to the previous menu
            }
            dni = dni.toUpperCase();

            if (!Utilities.validateDNI(dni)) { // Validate DNI format
                System.out.println("Invalid DNI format. Please try again.");
            }
        } while (!Utilities.validateDNI(dni));

        System.out.println("Enter password (-1 to go back): ");
        password = Utilities.readString();
        if (password.equals("-1")) {
            return; // Go back to the previous menu
        }

        String encryptedPassword = Encryption.encryptPasswordMD5(password);

        try {
            // Validate login
            if (ConnectionPatient.validateLogin(dni, encryptedPassword)) {
                System.out.println("\nPatient login successful!");
                patientMenu(dni); // Patien´s main menu
            } else {

                System.out.println("ERROR. Make sure you entered your DNI and password correctly.");
                System.out.println("If you're not registered, please do it first. \n");
                mainMenu();

            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please try again.");

            mainMenu();

        }
    }

    private static void registerPatient() {
        System.out.println("Enter patient details to register");
        System.out.flush();

        String dni;
        do {
            System.out.println("DNI: ");
            dni = Utilities.readString();
            dni = dni.toUpperCase();

        } while (!Utilities.validateDNI(dni));

        System.out.println("Password: ");
        String password = Utilities.readString();
        String encryptedPassword = Encryption.encryptPasswordMD5(password);

        System.out.println("First name: ");
        String name = Utilities.readString();

        System.out.println("Last name: ");
        String surname = Utilities.readString();

        System.out.println("Phone: ");
        Integer telephone = Utilities.readInteger();

        String email;
        do {
            System.out.println("Email: ");
            email = Utilities.readString();
        } while (!Utilities.validateEmail(email));
        // Request date of birth

        LocalDate dateOfBirth;
        while (true) {
            System.out.println("Date of Birth (yyyy-MM-dd): ");
            String dobInput = Utilities.readString();
            try {
                dateOfBirth = LocalDate.parse(dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }

        // Request gender
        Gender gender = null;
        do {
            System.out.println("Gender (MALE, FEMALE): ");
            String genderInput = Utilities.readString().trim().toUpperCase();
            try {
                gender = Gender.valueOf(genderInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender. Please enter MALE, FEMALE.");
            }
        } while (gender == null);

        // Request previous diseases
        List<String> selectedDiseases = selectPreviousDiseases();

        // Create Patient object with the new data
        Patient currentPatient = new Patient(dni, encryptedPassword, name, surname, email, gender, telephone, dateOfBirth);

        if (ConnectionPatient.sendRegisterServer(currentPatient, encryptedPassword, selectedDiseases)) {
            System.out.println("User registered with DNI: " + dni);
            loginMenu();
        } else {
            System.out.println("DNI: " + dni + " is already registered. Try to login to access your account.");
            mainMenu();
        }

    }

    private static void patientMenu(String patientDni) throws IOException {
        while (true) {
            System.out.println("\n=== Patient Menu ===");
            System.out.println("1. View my personal information"); // Name, surname, phone, etc.
            System.out.println("2. View episodes"); // Episodes list -> Surgery, Disease, Symptom, etc.
            System.out.println("3. Introduce episode");
            System.out.println("0. Log out");
            System.out.println("Choose an option: ");

            int choice = Utilities.readInteger();
            Patient patient = null;

            switch (choice) {
                case 1:
                    patient = ConnectionPatient.viewPatientInformation(patientDni);
                    if (patient != null) {
                        Utilities.showPatientDetails(patient);
                    } else {
                        System.out.println("Patient information could not be retrieved.");
                    }
                    break;

                case 2:
                    ArrayList<Episode> episodes = ConnectionPatient.getPatientEpisodes(patientDni);
                    if (episodes.isEmpty()) {
                        System.out.println("No episodes found for this patient.");

                        break;
                    }

                    // Valid IDs
                    ArrayList<Integer> validEpisodeIds = new ArrayList<>();

                    System.out.println("\n=== Episodes ===");
                    for (Episode episode : episodes) {
                        System.out.println("ID: " + episode.getId() + ", Date: " + episode.getDate());
                        validEpisodeIds.add(episode.getId());
                    }
                    boolean validEp = true;

                    do {

                        System.out.println("Enter the ID of the episode you want to view details for:");
                        int episodeId = Utilities.readInteger();

                        if (!validEpisodeIds.contains(episodeId)) {
                            System.out.println("The entered ID is not valid. Please select one of the listed episodes.");
                        } else {
                            validEp = false;
                            Episode selectedEpisode = ConnectionPatient.getEpisodeDetails(episodeId, patientDni);
                            if (selectedEpisode != null) {
                                ArrayList<Surgery> insertedSurgeries = selectedEpisode.getSurgeries();
                                ArrayList<Symptom> insertedSymptoms = selectedEpisode.getSymptoms();
                                ArrayList<Disease> insertedDiseases = selectedEpisode.getDiseases();
                                ArrayList<Recording> insertedRecordings = selectedEpisode.getRecordings();

                                System.out.println("\n=== Episode Details ===");

                                // Surgeries
                                if (!insertedSurgeries.isEmpty()) {
                                    System.out.println("Surgeries: " + insertedSurgeries);
                                } else {
                                    System.out.println("Surgeries: No surgeries recommended by the doctor.");
                                }// Symptoms
                                if (!insertedSymptoms.isEmpty()) {
                                    System.out.println("Symptoms: " + insertedSymptoms);
                                } else {
                                    System.out.println("Symptoms: No symptoms reported in this episode.");
                                }
                                // Diseases
                                if (!insertedDiseases.isEmpty()) {
                                    System.out.println("Diseases: " + insertedDiseases);
                                } else {
                                    System.out.println("Diseases: No diseases recommended by the doctor.");
                                }
                                // Recordings
                                if (!insertedRecordings.isEmpty()) {
                                    System.out.println("Recordings: " + insertedRecordings.size());
                                    for (int i = 0; i < insertedRecordings.size(); i++) {
                                        Recording rec = insertedRecordings.get(i);
                                        System.out.println("ID: " + rec.getId() + ", Path: " + rec.getSignal_path());
                                    }
                                } else {
                                    System.out.println("Recordings: No recordings available.");
                                }

                            } else {
                                System.out.println("Episode details could not be retrieved.");
                            }
                        }
                    } while (validEp);
                    break;

                case 3:

                    Episode episode = new Episode();

                    patient = ConnectionPatient.viewPatientInformation(patientDni);
                    int patientId = patient.getId();

                    LocalDateTime episodeDate = LocalDateTime.now();
                    episode.setDate(episodeDate);
                    episode.setPatient_id(patientId);

                    List<String> symptoms = selectSymptoms(patient.getDNI());

                    List<Recording> recordings = new ArrayList<>();

                    boolean recLoop = true;
                    while (recLoop) {
                        System.out.println("Do you want to insert a recording? [YES/NO]: ");
                        String res = Utilities.readString().toUpperCase();
                        if (res.equals("YES")) {
                            recordings = addRecordings(patientDni); // Método para añadir grabaciones
                            break;
                        } else if (res.equals("NO")) {
                            recLoop = false;
                            break;
                        } else {
                            System.out.println("Invalid response. Please type 'YES' or 'NO'.");
                        }
                    }

                    int episodeId = ConnectionPatient.insertEpisode(episode, symptoms, recordings);

                    if (episodeId > 0) {
                        System.out.println("Episode created successfully with ID: " + episodeId);
                        System.out.println("Symptoms: " + symptoms);
                        System.out.println("Number of recordings: " + recordings.size());
                        System.out.println("The doctor can now associate diseases and surgeries with this episode.");
                    } else {
                        System.err.println("Failed to create episode. Please try again.");
                    }
                    break;
                case 0:
                    System.out.println("Logging out...");
                    ConnectionPatient.closeConnection();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static List<String> selectSymptoms(String patientDni) throws IOException {
    List<Symptom> availableSymptoms = ConnectionPatient.getAvailableSymptoms();
    List<String> selectedSymptoms = new ArrayList<>();
    int option = 0;
    System.out.println("=== Symptom Selection ===");
    do {
        System.out.println("\nAvailable Symptoms:");
        for (int i = 0; i < availableSymptoms.size(); i++) {
            System.out.println((i + 1) + ". " + availableSymptoms.get(i).getSymptom());
        }
        System.out.println("");
        System.out.println((availableSymptoms.size() + 1) + ". Add new Symptom");
        System.out.println("");
        System.out.println((availableSymptoms.size() + 2) + ". Skip to next step (-1 to go back to login menu)");
        
        String input = Utilities.readString(); // Use readString to capture text input
        try {
            option = Integer.parseInt(input); // Try to parse the input to an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            continue;
        }
        
        if (option == -1) {
            System.out.println("Going back to the login menu...");
            patientMenu(patientDni); // Call the login menu
            return new ArrayList<>(); // Return an empty list to exit the symptom selection
        }
        
        if (option > 0 && option <= availableSymptoms.size()) {
            String selectedSymptom = availableSymptoms.get(option - 1).getSymptom();
            if (!selectedSymptoms.contains(selectedSymptom)) {
                selectedSymptoms.add(selectedSymptom);
                System.out.println("Symptom \"" + selectedSymptom + "\" added to your selection.");
            } else {
                System.out.println("You already selected \"" + selectedSymptom + "\".");
            }
        } else if (option == availableSymptoms.size() + 1) {
            System.out.println("Enter new Symptom: ");
            String newSymptom = Utilities.readString();
            selectedSymptoms.add(newSymptom);
            System.out.println("Symptom \"" + newSymptom + "\" added.");
        } else if (option != availableSymptoms.size() + 2) {
            System.out.println("Invalid choice. Please try again.");
        }
    } while (option != availableSymptoms.size() + 2);
    return selectedSymptoms;
    }

    private static List<String> selectPreviousDiseases() {
        List<Disease> availableDiseases = ConnectionPatient.getAvailableDiseases();
        List<String> selectedDiseases = new ArrayList<>();
        int option;

        System.out.println("=== Disease Selection ===");
        do {
            System.out.println("\nAvailable Diseases:");
            for (int i = 0; i < availableDiseases.size(); i++) {
                System.out.println((i + 1) + ". " + availableDiseases.get(i).getDisease());
            }
            System.out.println("");
            System.out.println((availableDiseases.size() + 1) + ". Add new Disease");
            System.out.println("");
            System.out.println((availableDiseases.size() + 2) + ". Skip to next step");

            option = Utilities.readInteger();

            if (option > 0 && option <= availableDiseases.size()) {
                String selectedDisease = availableDiseases.get(option - 1).getDisease();
                if (!selectedDiseases.contains(selectedDisease)) {
                    selectedDiseases.add(selectedDisease);
                    System.out.println("Disease \"" + selectedDisease + "\" added to your selection.");
                } else {
                    System.out.println("You already selected \"" + selectedDisease + "\".");
                }
            } else if (option == availableDiseases.size() + 1) {
                System.out.println("Enter new Disease: ");
                String newDisease = Utilities.readString();
                selectedDiseases.add(newDisease);
                System.out.println("Disease \"" + newDisease + "\" added.");
            } else if (option != availableDiseases.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableDiseases.size() + 2);

        return selectedDiseases;
    }

    // Call the methods of the BITalino class to record the signal
    private static List<Recording> addRecordings(String patientDni) {
        ArrayList<Recording> recordings = new ArrayList<>();
        System.out.println("=== Add Recordings ===");
        boolean response = true;

        String macAddress;
        // Validate mac address
        while (true) {
            System.out.println("Introduce a valid MAC address (format: XX:XX:XX:XX:XX:XX): ");
            macAddress = Utilities.readString();

            if (BitalinoDemo.isValidMacAddress(macAddress)) {
                break;
            } else {
                System.out.println("MAC address invalid. Try again.");
            }
        }

        while (response) {

            try {

                System.out.println("Recording Type (ECG/EMG, or type 'done' to finish): ");
                String typeInput = Utilities.readString();

                // validate recording type
                if (typeInput.equalsIgnoreCase("done")) {
                    break;
                }
                Recording.Type signalType;
                try {
                    signalType = Recording.Type.valueOf(typeInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid recording type. Please choose ECG or EMG.");
                    continue;
                }
                if (signalType == Recording.Type.ECG) {
                    System.out.println("\n=== ECG Recording Instructions ===");
                    System.out.println("1. Ensure the electrodes are placed correctly:");
                    System.out.println("   - Electrode 1 (Red): Below the right collarbone (chest area).");
                    System.out.println("   - Electrode 2 (Black): Below the left collarbone (chest area).");
                    System.out.println("   - Electrode 3 (White - Ground): Lower left side of the chest.");
                    System.out.println("2. Stay still during the recording.");
                    System.out.println("\n The recording will last 60 seconds.");

                } else if (signalType == Recording.Type.EMG) {
                    System.out.println("\n=== EMG Recording Instructions ===");
                    System.out.println("1. Place the electrodes on the quadriceps muscle:");
                    System.out.println("   - Electrode 1 (Red): Center of the quadriceps muscle.");
                    System.out.println("   - Electrode 2 (Black): 2-3 cm above Electrode 1, along the muscle line.");
                    System.out.println("   - Electrode 3 (White - Ground): Over the patella.");
                    System.out.println("2. Stay relaxed during the setup and follow the contraction instructions:");
                    System.out.println("   - Alternate between pressing (contracting) and relaxing the muscle.");
                    System.out.println("3. Avoid unnecessary external movements to ensure a clean signal.");
                    System.out.println("\n The recording will last 60 seconds.");

                }

                int[] channelsToAcquire = BitalinoDemo.configureChannels(signalType);
                BITalino bitalino = new BITalino();
                int sample_rate = 1000;

                try {
                    bitalino.open(macAddress, sample_rate);
                    // countdown before starting the recording
                    System.out.println("\nStarting recording in...");
                    for (int i = 3; i > 0; i--) {
                        System.out.println(i);
                        Thread.sleep(1000); // Pause for 1 second
                    }
                    System.out.println("Start Recording!");

                    // date of the recording
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String recordingDate = LocalDateTime.now().format(formatter);
                    LocalDateTime parserecordingDate = LocalDateTime.parse(recordingDate, formatter);

                    // Start reccording
                    bitalino.start(channelsToAcquire);

                    String fileName = BitalinoDemo.generateFileName(signalType, recordingDate, patientDni);
                    ArrayList<Integer> data = BitalinoDemo.recordAndSaveData(bitalino, signalType, fileName, recordingDate, patientDni);

                    if (data == null || data.isEmpty()) {
                        System.out.println("Error: No data was captured. Please ensure the device is working properly.");
                        continue;
                    }

                    Recording recording = new Recording(signalType, parserecordingDate, fileName, data);
                    recordings.add(recording);
                    System.out.println("The recording has successfully ended.");
                } catch (BITalinoException | IOException e) {
                    System.err.println("Error during connection or recording: " + e.getMessage());
                } catch (Throwable ex) {
                    Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    // Ensure the device is properly closed
                    try {
                        bitalino.stop();
                        bitalino.close();
                    } catch (BITalinoException ex) {
                        System.err.println("Error closing BITalino device: " + ex.getMessage());
                    }
                }
                // Ask if the user wants to add another recording

                while (true) {
                    System.out.println("Do you want to add another recording?: [YES/NO]: ");
                    String res = Utilities.readString().toUpperCase();
                    if (res.equals("YES")) {
                        response = true;
                        break;
                    } else if (res.equals("NO")) {
                        response = false;
                        break;
                    } else {
                        System.out.println("Not valid response");

                    }
                }

            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
        return recordings;
    }

}
