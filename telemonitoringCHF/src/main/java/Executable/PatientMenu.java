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
import pojos.Episode;
import pojos.Patient.Gender;
import pojos.Recording;

/**
 *
 * @author martaguzman
 */
public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        System.out.println("\n-- Welcome to the Patient App --");
        while (true) {

            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");

            System.out.println("\n Please select an option to get started:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
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
            System.out.print("Enter DNI: ");
            dni = Utilities.readString();

            if (!Utilities.validateDNI(dni)) { // Valida el formato del DNI
                System.out.println("Invalid DNI format. Please try again.");
            }
        } while (!Utilities.validateDNI(dni));

        System.out.print("Enter password: ");
        password = Utilities.readString();
        try {
            // Valida login
            if (ConnectionPatient.validateLogin(dni, password)) {
                System.out.println("\n Patient login successful!");
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
            System.out.print("DNI: ");
            dni = scanner.nextLine();
        } while (!Utilities.validateDNI(dni));

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("First name: ");
        String name = scanner.nextLine();

        System.out.print("Last name: ");
        String surname = scanner.nextLine();

        System.out.print("Phone: ");
        Integer telephone = scanner.nextInt();
        scanner.nextLine();

        String email;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine();
        } while (!Utilities.validateEmail(email));

        // Solicitar fecha de nacimiento
        LocalDate dateOfBirth;
        while (true) {
            System.out.print("Date of Birth (yyyy-MM-dd): ");
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
            System.out.print("Gender (MALE, FEMALE): ");
            String genderInput = scanner.nextLine().trim().toUpperCase();
            try {
                gender = Gender.valueOf(genderInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender. Please enter MALE, FEMALE.");
            }
        } while (gender == null);

        // Crear objeto Patient con los nuevos datos
        Patient currentPatient = new Patient(dni, name, surname, email, gender, telephone, dateOfBirth);

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
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Patient patient = ConnectionPatient.viewPatientInformation(patientDni);
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

                    Episode selectedEpisode = ConnectionPatient.getEpisodeDetails(episodeId);
                    if (selectedEpisode != null) {
                        System.out.println("\n=== Episode Details ===");
                        System.out.println("Surgeries: " + selectedEpisode.getSurgeries());
                        System.out.println("Symptoms: " + selectedEpisode.getSymptoms());
                        System.out.println("Diseases: " + selectedEpisode.getDiseases());
                        System.out.println("Recordings: ");
                        
                    for (int i = 0; i < selectedEpisode.getRecordings().size(); i++) {
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
                    } else {
                        System.out.println("Episode details could not be retrieved.");
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
}

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
