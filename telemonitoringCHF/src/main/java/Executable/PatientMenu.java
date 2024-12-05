/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Executable;

import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.BitalinoDemo;
import java.util.Scanner;
import Utilities.Utilities;
import pojos.Patient;
import ConnectionPatient.*;
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

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            mainMenu();
            //nuevo
        } finally {
            ConnectionPatient.closeConnection(); // Cierra la conexión al finalizar
        }
    }

    private static void mainMenu() {
        System.out.println("\n-- Welcome to the Patient App --");
        while (true) {

            System.out.println("\n1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");
            System.out.println("\nPlease select an option to get started:");

            int option = scanner.nextInt();
            scanner.nextLine();

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
            System.out.println("\nEnter DNI: ");
            dni = Utilities.readString();
            dni = dni.toUpperCase();

            if (!Utilities.validateDNI(dni)) { // Valida el formato del DNI
                System.out.println("Invalid DNI format. Please try again.");
            }
        } while (!Utilities.validateDNI(dni));

        System.out.println("Enter password: ");
        password = Utilities.readString();
        try {
            // Valida login
            if (ConnectionPatient.validateLogin(dni, password)) {
                System.out.println("\nPatient login successful!");
                // loginSuccess = true; 
                patientMenu(dni); // Redirige al menú del doctor
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
            dni = scanner.nextLine();
            dni = dni.toUpperCase();

        } while (!Utilities.validateDNI(dni));

        System.out.println("Password: ");
        String password = scanner.nextLine();

        System.out.println("First name: ");
        String name = scanner.nextLine();

        System.out.println("Last name: ");
        String surname = scanner.nextLine();

        System.out.println("Phone: ");
        Integer telephone = scanner.nextInt();
        scanner.nextLine();

        String email;
        do {
            System.out.println("Email: ");
            email = scanner.nextLine();
        } while (!Utilities.validateEmail(email));

        // Solicitar fecha de nacimiento
        LocalDate dateOfBirth;
        while (true) {
            System.out.println("Date of Birth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine();
            try {
                dateOfBirth = LocalDate.parse(dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }

        // Solicitar género
        Gender gender = null;
        do {
            System.out.println("Gender (MALE, FEMALE): ");
            String genderInput = scanner.nextLine().trim().toUpperCase();
            try {
                gender = Gender.valueOf(genderInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender. Please enter MALE, FEMALE.");
            }
        } while (gender == null);

        // Crear objeto Patient con los nuevos datos
        Patient currentPatient = new Patient(dni, password, name, surname, email, gender, telephone, dateOfBirth);

        if (ConnectionPatient.sendRegisterServer(currentPatient, password)) {
            System.out.println("User registered with DNI: " + dni);
            mainMenu();
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
            // System.out.println("4. View a specific medical detail");
            System.out.println("0. Log out");
            System.out.println("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
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

                    System.out.println("\n=== Episodes ===");
                    for (Episode episode : episodes) {
                        System.out.println("ID: " + episode.getId() + ", Date: " + episode.getDate());
                    }

                    System.out.println("Enter the ID of the episode you want to view details for:");
                    int episodeId = scanner.nextInt();
                    scanner.nextLine();
                    Episode selectedEpisode = ConnectionPatient.getEpisodeDetails(episodeId, patientDni);
                    if (selectedEpisode != null) {
                        ArrayList<Surgery> insertedSurgeries = selectedEpisode.getSurgeries();
                        ArrayList<Symptom> insertedSymptoms = selectedEpisode.getSymptoms();
                        ArrayList<Disease> insertedDiseases = selectedEpisode.getDiseases();
                        ArrayList<Recording> insertedRecordings = selectedEpisode.getRecordings();

                        if (!insertedSurgeries.isEmpty() || !insertedSymptoms.isEmpty() || !insertedDiseases.isEmpty()
                                || !insertedRecordings.isEmpty()) {
                            System.out.println("\n=== Episode Details ===");

                            System.out.println("Surgeries: " + insertedSurgeries);
                            System.out.println("Symptoms: " + insertedSymptoms);
                            System.out.println("Diseases: " + insertedDiseases);
                            System.out.println("Recordings: ");
                            for (int i = 0; i < insertedRecordings.size(); i++) {
                                Recording rec = selectedEpisode.getRecordings().get(i);
                                System.out.println("ID: " + rec.getId() + ", Path: " + rec.getSignal_path());
                            }

                        } else {
                            System.out.println("There is nothing inserted on the episode " + episodeId);
                        }
                    } else {
                        System.out.println("Episode details could not be retrieved.");
                    }
                    break;

                case 3:

                    Episode episode = new Episode();

                    patient = ConnectionPatient.viewPatientInformation(patientDni);
                    int patientId = patient.getId();

                    LocalDateTime episodeDate = LocalDateTime.now();
                    episode.setDate(episodeDate);

                    episode.setPatient_id(patientId);

                    List<String> diseases = selectDiseases();
                    List<String> symptoms = selectSymptoms();
                    List<String> surgeries = selectSurgeries();
                    List<Recording> recordings = new ArrayList();

                    boolean recLoop = true;
                    while (recLoop) {
                        System.out.println("Do you want to insert a recording?: [YES/NO]: ");
                        String res = scanner.nextLine().toUpperCase();
                        if (res.equals("YES")) {
                            recLoop = true;

                            recordings = addRecordings(patientDni);
                            System.out.println(recordings);

                            break;
                        } else if (res.equals("NO")) {
                            recLoop = false;
                            break;
                        } else {
                            System.out.println("Not valid response");

                        }
                    }

                    // Enviar episodio al servidor
                    boolean success = ConnectionPatient.insertEpisode(episode, diseases, symptoms, surgeries, recordings);

                    if (success) {
                        System.out.println("Episode inserted successfully!");
                        System.out.println("Diseases: " + diseases + "Symptoms: " + symptoms + " "
                                + "Surgeries introduced: " + surgeries + "Number of recordings: " + recordings.size());
                    } else {
                        System.err.println("Failed to insert episode. Please try again.");
                    }
                    break;

                case 0:
                    System.out.println("Logging out...");
                    ConnectionPatient.closeConnection();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static List<String> selectDiseases() {

        List<Disease> availableDiseases = ConnectionPatient.getAvailableDiseases();
        List<String> selectedDiseases = new ArrayList<>();
        int option;

        System.out.println("=== Disease Selection ===");
        do {
            System.out.println("\nAvailable Diseases:");
            for (int i = 0; i < availableDiseases.size(); i++) {
                System.out.println((i + 1) + ". " + availableDiseases.get(i).getDisease());
            }

            System.out.println((availableDiseases.size() + 1) + ". Add new Disease");
            System.out.println((availableDiseases.size() + 2) + ". Skip to next step -> Introduce Symptom: ");

           
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
                String newDisease = scanner.nextLine();
                selectedDiseases.add(newDisease);
                System.out.println("Disease \"" + newDisease + "\" added.");
            } else if (option != availableDiseases.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableDiseases.size() + 2);

        return selectedDiseases;
    }

    private static List<String> selectSymptoms() {
        List<Symptom> availableSymptoms = ConnectionPatient.getAvailableSymptoms();
        List<String> selectedSymptoms = new ArrayList<>();
        int option;

        System.out.println("=== Symptom Selection ===");
        do {
            System.out.println("\nAvailable Symptoms:");
            for (int i = 0; i < availableSymptoms.size(); i++) {
                System.out.println((i + 1) + ". " + availableSymptoms.get(i).getSymptom());
            }

            System.out.println((availableSymptoms.size() + 1) + ". Add new Symptom");
            System.out.println((availableSymptoms.size() + 2) + ". Skip to next step -> Introduce Surguries");

            option = Utilities.readInteger();
          

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
                String newSymptom = scanner.nextLine();
                selectedSymptoms.add(newSymptom);
                System.out.println("Symptom \"" + newSymptom + "\" added.");
            } else if (option != availableSymptoms.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableSymptoms.size() + 2);

        return selectedSymptoms;
    }

    private static List<String> selectSurgeries() {
        List<Surgery> availableSurgeries = ConnectionPatient.getAvailableSurgeries();
        List<String> selectedSurgeries = new ArrayList<>();
        int option;

        System.out.println("=== Surgery Selection ===");
        do {
            System.out.println("\nAvailable Surgeries:");
            for (int i = 0; i < availableSurgeries.size(); i++) {
                System.out.println((i + 1) + ". " + availableSurgeries.get(i).getSurgery());
            }

            System.out.println((availableSurgeries.size() + 1) + ". Add new Surgery");
            System.out.println((availableSurgeries.size() + 2) + ". Skip to next step -> Introduce Recording");

            
            option = Utilities.readInteger();

          
            if (option > 0 && option <= availableSurgeries.size()) {
                String selectedSurgery = availableSurgeries.get(option - 1).getSurgery();
                if (!selectedSurgeries.contains(selectedSurgery)) {
                    selectedSurgeries.add(selectedSurgery);
                    System.out.println("Surgery \"" + selectedSurgery + "\" added to your selection.");
                } else {
                    System.out.println("You already selected \"" + selectedSurgery + "\".");
                }
            } else if (option == availableSurgeries.size() + 1) {
                System.out.println("Enter new Surgery: ");
                String newSurgery = scanner.nextLine();
                selectedSurgeries.add(newSurgery);
                System.out.println("Surgery \"" + newSurgery + "\" added.");
            } else if (option != availableSurgeries.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableSurgeries.size() + 2);

        return selectedSurgeries;
    }

    // Llama a los metodos de las clase Bitalino para poder grabar la señal
    private static List<Recording> addRecordings(String patientDni) {
        ArrayList<Recording> recordings = new ArrayList<>();
        System.out.println("=== Add Recordings ===");
        boolean response = true;

        String macAddress;
        while (true) {
            System.out.println("Introduce a valid MAC address (format: XX:XX:XX:XX:XX:XX): ");
            macAddress = scanner.nextLine();

            if (BitalinoDemo.isValidMacAddress(macAddress)) {
                break; 
            } else {
                System.out.println("MAC address invalid. Try again.");
            }
        }

        while (response) {

            try {

                System.out.println("Recording Type (ECG/EMG, or type 'done' to finish): ");
                String typeInput = scanner.nextLine();
                if (typeInput.equalsIgnoreCase("done")) {
                    break;
                }
                Recording.Type signalType = Recording.Type.valueOf(typeInput.toUpperCase());

                int[] channelsToAcquire = BitalinoDemo.configureChannels(signalType);
                BITalino bitalino = null;

                bitalino = new BITalino();
                int sample_rate = 1000;

                bitalino.open(macAddress, sample_rate);

                // date of the recording
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String recordingDate = LocalDateTime.now().format(formatter);
                LocalDateTime parserecordingDate = LocalDateTime.parse(recordingDate, formatter);

                // Empezamos la grabación:
                bitalino.start(channelsToAcquire);

                String fileName = BitalinoDemo.generateFileName(signalType, recordingDate, patientDni);
                ArrayList<Integer> data = BitalinoDemo.recordAndSaveData(bitalino, signalType, fileName, recordingDate, patientDni);

                if (data == null || data.isEmpty()) {
                    System.out.println("Error: No data was captured. Please ensure the device is working properly.");
                    
                    patientMenu(patientDni); 
                } else {

                    Recording recording = new Recording(signalType, parserecordingDate, fileName, data);
                    recordings.add(recording);
                    System.out.println("The recording has successfully ended.");
                }
                String res = "";
                while (true) {
                    System.out.println("Do you want to add another recording?: [YES/NO]: ");
                    res = scanner.nextLine().toUpperCase();
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

            } catch (BITalinoException ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return recordings;
    }
}

