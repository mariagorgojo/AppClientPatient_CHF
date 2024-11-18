/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Executable;
import java.util.Scanner;
import Utilities.Utilities;
import pojos.Patient;
import ConnectionPatient.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import pojos.Patient.Gender;


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
        System.out.println("-- Welcome to the Patient App --");
        while (true) {
           
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            
            System.out.println("\n Please select an option to get started:"); 
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerPatient();
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
    boolean loginSuccess = false;

    do {
        System.out.print("Enter DNI: ");
        dni = scanner.nextLine();

        while (!Utilities.validateDNI(dni)) { // Valida el formato del DNI
            System.out.println("Invalid DNI format. Please try again.");
            System.out.print("Enter DNI: ");
            dni = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        password = scanner.nextLine();

        // Validar credenciales
        if (ConnectionPatient.validateLogin(dni, password)) { 
            System.out.println("Patient login successful!");
            loginSuccess = true; 
            patientMenu(dni);    
        } else {
            System.out.println("Something went wrong. Make sure to introduce your DNI and password correctly.");
        }

    } while (!loginSuccess); // Repite hasta que el login sea exitoso
}

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
    
     if ( ConnectionPatient.sendRegisterServer(currentPatient, password)) { 
            System.out.println("User registered with DNI: " + dni);
            mainMenu();
        } else {
            System.out.println("DNI: " + dni + " is already registered. Try to login to access your account.");    
             mainMenu();
         }
   
    }
   

    private static void patientMenu(String patientDni) { // CHECK CON CARMEN
        while (true) {
            System.out.println("=== Patient Menu ===");
            System.out.println("1. View my details");
            System.out.println("2. View my health records");
            System.out.println("0. Log out");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    getPatientById(patientDni);
                    break;
                case 2:
                    viewHealthRecordsMenu(patientDni);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void getPatientById(String dni) { // VOLVER
        
        // VOLVER -> DO. 
       //HABRIA Q BUSCARLO EN LA BASE D DTS XQ LUEGO EN LOS THREADS VAMOS A TENER VARIOS PACIENTES 
      // O SINO toString() +VARAIBLE GLOBAL 
    
}

    private static void viewHealthRecordsMenu(String patientDni) {
        while (true) {
            System.out.println("=== Health Records ===");
            System.out.println("1. View health record details");
            System.out.println("0. Go back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    selectHealthRecordById(patientDni);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void selectHealthRecordById(String patientDni) {
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
    }
    
}


    

