/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Executable;

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

/**
 *
 * @author martaguzman
 */
public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try{
        mainMenu();
        //nuevo
        }finally{
            ConnectionPatient.closeConnection(); // Cierra la conexión al finalizar
        }
    }

    private static void mainMenu() {
        System.out.println("\n-- Welcome to the Patient App --");
        while (true) {

            System.out.println("1. Register");
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
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loginMenu() {
        String dni;
        String password;
        //boolean loginSuccess = false;

        //do {
        do {
            System.out.println("\nEnter DNI: ");
            dni = Utilities.readString();

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
            }

        } catch (Exception e) {
            System.out.println("ERROR. Make sure you entered your DNI and password correctly.");
            System.out.println("If you're not registered, please do it first. \n");
            //loginSuccess = true; 
            mainMenu();
            //System.out.println(e);

        }
    } //while (!loginSuccess); // Repite hasta que el login sea exitoso
//}

    private static void registerPatient() {
        System.out.println("Enter patient details to register");
        System.out.flush();

        String dni;
        do {
            System.out.println("DNI: ");
            dni = scanner.nextLine();
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
            System.out.println("4. View a specific medical detail");
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
                    System.out.println("PATIENT MENU DNI!!!: "+ patientDni);
                    Episode selectedEpisode = ConnectionPatient.getEpisodeDetails(episodeId, patientDni);
                    if (selectedEpisode != null) {
                        ArrayList<Surgery> insertedSurgeries = selectedEpisode.getSurgeries();
                        ArrayList<Symptom> insertedSymptoms = selectedEpisode.getSymptoms();
                        ArrayList<Disease> insertedDiseases = selectedEpisode.getDiseases();
                        ArrayList<Recording> insertedRecordings = selectedEpisode.getRecordings();
                        
                        if(!insertedSurgeries.isEmpty() ||!insertedSymptoms.isEmpty() || !insertedDiseases.isEmpty()
                                || !insertedRecordings.isEmpty()){
                        System.out.println("\n=== Episode Details ===");
                        
                        System.out.println("Surgeries: " + insertedSurgeries);                        
                        System.out.println("Symptoms: " + insertedSymptoms);
                        System.out.println("Diseases: " + insertedDiseases);
                        System.out.println("Recordings: ");
                        for (int i = 0; i < insertedRecordings.size(); i++) {
                            Recording rec = selectedEpisode.getRecordings().get(i);
                            System.out.println("ID: " + rec.getId() + ", Path: " + rec.getSignal_path());
                        }
                        

                        System.out.println("Enter the ID of the recording you want to view details for:");
                        int recordingId = scanner.nextInt();
                        scanner.nextLine();

                        Recording recordingDetails = ConnectionPatient.getRecordingDetails(recordingId);
                        if (recordingDetails != null) {
                            System.out.println("\n=== Recording Details ===");
                            System.out.println("ID: " + recordingDetails.getId());
                            System.out.println("Type: " + recordingDetails.getType());
                            System.out.println("Duration: " + recordingDetails.getDuration() + " seconds");
                            System.out.println("Date: " + recordingDetails.getDate());
                            System.out.println("Signal Path: " + recordingDetails.getSignal_path());
                            System.out.println("Episode ID: " + recordingDetails.getEpisode_id());
                        } else {
                            System.out.println("Recording details could not be retrieved.");
                        }
                        
                        } else{
                            System.out.println("There is nothing inserted on the episode "+ episodeId);
                        }
                    } else {
                        System.out.println("Episode details could not be retrieved.");
                    }
                    break;

                case 3:
                    // Crear episodio
                    // System.out.println("i am in inside case 3");
                    Episode episode = new Episode();

                    patient = ConnectionPatient.viewPatientInformation(patientDni);
                    //System.out.println("patient return: "+patient);
                    int patientId = patient.getId();
                    //System.out.println(" patientId: "+ patientId);

                    LocalDateTime episodeDate = LocalDateTime.now(); // Obtiene la fecha y hora actuale
                    episode.setDate(episodeDate);

                    episode.setPatient_id(patientId);

                    // Pasar por cada paso del flujo
                    List<String> diseases = selectDiseases();
                    List<String> symptoms = selectSymptoms();
                    List<String> surgeries = selectSurgeries();
                    List<Recording> recordings = addRecordings();

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
            System.out.println((availableDiseases.size() + 2) + ". Skip to next step");

            System.out.println("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

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
            System.out.println((availableSymptoms.size() + 2) + ". Skip to next step");

            System.out.println("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
            System.out.println((availableSurgeries.size() + 2) + ". Skip to next step");

            System.out.println("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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

    private static List<Recording> addRecordings() { // graba la señal
        ArrayList<Recording> recordings = new ArrayList<>();
        System.out.println("=== Add Recordings ===");

        while (true) {
            System.out.println("Recording Type (ECG/EMG, or type 'done' to finish): ");
            String typeInput = scanner.nextLine();
            if (typeInput.equalsIgnoreCase("done")) {
                break;
            }
            Recording.Type type = Recording.Type.valueOf(typeInput.toUpperCase());

            System.out.println("Duration (in seconds): ");
            int duration = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("Recording Date (YYYY-MM-DD): ");
            LocalDate recordingDate = LocalDate.parse(scanner.nextLine());

            System.out.println("Signal Path: ");
            String signalPath = scanner.nextLine();

            ArrayList<Integer> data = new ArrayList<>();
            System.out.println("Enter signal data (integers, type 'done' when finished):");
            while (true) {
                String dataInput = scanner.nextLine();
                if (dataInput.equalsIgnoreCase("done")) {
                    break;
                }
                data.add(Integer.parseInt(dataInput));
            }

            recordings.add(new Recording(type, duration, recordingDate, signalPath, data));
            System.out.println("Recording added.");
        }

        return recordings;
    }
}

/* System.out.println("\n=== Insert New Episode ===");

                    // Leer la fecha del episodio
                    System.out.println("Enter the episode date (yyyy-MM-dd):");
                    String dateInput = scanner.nextLine();
                    LocalDate episodeDate;
                    try {
                        episodeDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Please try again.");
                        break;
                    }

                    // Seleccionar enfermedad
                    System.out.println("\n=== Diseases ===");
                    List<Disease> diseases = ConnectionPatient.getAvailableDiseases();
                    for (int i = 0; i < diseases.size(); i++) {
                        System.out.println((i + 1) + ". " + diseases.get(i).getDisease());
                    }
                    System.out.println("Select a disease by number (or enter 0 to add a new one):");
                    int diseaseSelection = scanner.nextInt();
                    scanner.nextLine();
                    String diseaseName;
                    if (diseaseSelection == 0) {
                        System.out.println("Enter the name of the new disease:");
                        diseaseName = scanner.nextLine();
                        boolean diseaseAdded = ConnectionPatient.insertNewDisease(diseaseName);
                        if (diseaseAdded) {
                            System.out.println("New disease added successfully.");
                        } else {
                            System.out.println("Error adding the new disease.");
                        }
                    } else {
                        diseaseName = diseases.get(diseaseSelection - 1).getDisease();
                    }                    
                    
                    // Seleccionar síntoma
                    System.out.println("\n=== Symptoms ===");
                    List<Symptom> symptoms = ConnectionPatient.getAvailableSymptoms();
                    for (int i = 0; i < symptoms.size(); i++) {
                        System.out.println((i + 1) + ". " + symptoms.get(i).getType());
                    }
                    System.out.println("Select a symptom by number (or enter 0 to add a new one):");
                    int symptomSelection = scanner.nextInt();
                    scanner.nextLine();
                    String symptomName;
                    if (symptomSelection == 0) {
                        System.out.println("Enter the name of the new symptom:");
                        symptomName = scanner.nextLine();
                        boolean symptomAdded = ConnectionPatient.insertNewSymptom(symptomName);
                        if (symptomAdded) {
                            System.out.println("New symptom added successfully.");
                        } else {
                            System.out.println("Error adding the new symptom.");
                        }
                    } else {
                        symptomName = symptoms.get(symptomSelection - 1).getType();
                    }

                    // Seleccionar cirugía
                    System.out.println("\n=== Surgeries ===");
                    List<Surgery> surgeries = ConnectionPatient.getAvailableSurgeries();
                    for (int i = 0; i < surgeries.size(); i++) {
                        System.out.println((i + 1) + ". " + surgeries.get(i).getType());
                    }
                    System.out.println("Select a surgery by number (or enter 0 to add a new one):");
                    int surgerySelection = scanner.nextInt();
                    scanner.nextLine();
                    String surgeryName;
                    if (surgerySelection == 0) {
                        System.out.println("Enter the name of the new surgery:");
                        surgeryName = scanner.nextLine();
                        boolean surgeryAdded = ConnectionPatient.insertNewSurgery(surgeryName);
                        if (surgeryAdded) {
                            System.out.println("New surgery added successfully.");
                        } else {
                            System.out.println("Error adding the new surgery.");
                        }
                    } else {
                        surgeryName = surgeries.get(surgerySelection - 1).getType();
                    }

                    // Preguntar si se desea grabar una señal
                    System.out.println("Do you want to record a signal with BITalino? (1 = Yes, 0 = No):");
                    int recordChoice = scanner.nextInt();
                    scanner.nextLine();

                    Recording recording = null;
                    if (recordChoice == 1) {
                        System.out.println("Starting BITalino recording...");
                        recording = ConnectionPatient.recordDataWithBitalino();
                        if (recording != null) {
                            System.out.println("Recording completed successfully.");
                        } else {
                            System.out.println("Recording failed. Proceeding without recording.");
                        }
                    } else {
                        System.out.println("Skipping recording.");
                    }

                    // Enviar todos los datos al servidor
                    boolean success = ConnectionPatient.insertEpisodeWithRecording(patientDni, episodeDate, diseaseName, symptomName, surgeryName, recording);
                    if (success) {
                        System.out.println("Episode successfully added.");
                    } else {
                        System.out.println("Error adding episode. Please try again.");
                    }*/
 /*private static void viewEpisodesMenu(Patient patient) {
        ArrayList<Episode> episodes = patient.getEpisodes();

        if (episodes.isEmpty()) {
            System.out.println("\nNo episodes found for this patient.");
            return;
        } else {

            // Imprime la lista de episodios por fecha
            for (int i = 0; i < episodes.size(); i++) {
                System.out.println((i + 1) + " Date: " + episodes.get(i).getDate());
            }
            System.out.println("Select a specific episode.");
            // elegir un episodio
            int option = Utilities.getValidInput(1, episodes.size());
            Episode selectedEpisode = episodes.get(option - 1);
            // ver todo lo que tiene un episodio 
            Episode episode = ConnectionPatient.viewPatientEpisode(selectedEpisode.getId());
            System.out.println(episode.toString());

        }
    }
}*/
 /*private static void selectHealthRecordById(String patientDni) {
        System.out.print("Please select the health record you wish to view from the list: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Displaying health record details...");

        while (true) {
            System.out.println("=== Health Record Menu ===");
            System.out.println("1. View specific health data");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewHealthDataByRecord();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewHealthDataByRecord() {
        System.out.println("Displaying specific health data...");
        // Implementation to display specific health data
    }*/
