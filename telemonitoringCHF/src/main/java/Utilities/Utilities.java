/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import pojos.Patient;

public class Utilities {

    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    // Read console inputs 
    public static int readInteger() {
        int num = 0;
        boolean ok = false;
        do {
            System.out.println("Introduce a number: ");
            try {
                num = Integer.parseInt(r.readLine());
                if (num < 0) {
                    ok = false;
                    System.out.println("You didn't type a valid number.");
                } else {
                    ok = true;
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (NumberFormatException nfe) {
                System.out.println("You didn't type a valid number!");
            }
        } while (!ok);

        return num;
    }

    public static String readString() {
        String text = null;
        boolean ok = false;
        do {
            try {
                text = r.readLine();
                if (!text.isEmpty()) {
                    ok = true;
                } else {
                    System.out.println("Empty input, please try again:");
                }
            } catch (IOException e) {

            }
        } while (!ok);

        return text;
    }

    // Validates the format of the DNI
    public static boolean validateDNI(String id) {

        boolean ok = true;

        if (id.length() != 9) {
            // System.out.println("Invalid DNI, try again");
            ok = false;

            return ok;
        }

        for (int i = 0; i < 8; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                ok = false;
                //System.out.println("Invalid DNI, try again");
                return ok;
            }
        }
        String num = id.substring(0, 8);

        String validLeters = "TRWAGMYFPDXBNJZSQVHLCKE";
        int indexLeter = Integer.parseInt(num) % 23;
        char valid = validLeters.charAt(indexLeter);

        if (id.toUpperCase().charAt(8) != valid) {
            //  System.out.println("Invalid DNI, try again");
            ok = false;
            return ok;
        }

        return ok;
    }

    public static boolean validateEmail(String email) {

        String emailpattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Check if the email matches the pattern
        return email != null && email.matches(emailpattern);
    }

    // Verifies that the IP direction exists and has the correct format
    public static boolean valid_ipAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false; // IP direction is empty (null)
        }

        // Format IPv4
        if (!isValidFormat(ipAddress)) {
            return false; // Not valid
        }

        // verify if responds to the network connection
        return isReachable(ipAddress);
    }

    // IPv4 format validation
    private static boolean isValidFormat(String ipAddress) {
        String IPv4_PATTERN
                = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ipAddress.matches(IPv4_PATTERN);
    }

    // Checks if the IP address is reachable (if the host at the given
    // IP address responds to a ping
    private static boolean isReachable(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress); // An InetAddress object is created using the ip string
            return inet.isReachable(3000); // Timeout to 3000 ms (3 sec). If the host does not respond, the IP address is 
                                            // considered unreachable
        } catch (IOException e) {
            return false; // Error 
        }
    }

    // Ask the user a valid IP address
    public static String getValidIPAddress() {
        Scanner scanner = new Scanner(System.in);
        String ipAddress;

        while (true) {
            System.out.println("\nPor favor, introduce una dirección IP válida: ");
            ipAddress = scanner.nextLine();

            if (valid_ipAddress(ipAddress)) {
                System.out.println("\nDirección IP válida: " + ipAddress);
                break;
            } else {
                System.out.println("La dirección IP no es válida o no responde. Inténtalo de nuevo.");
            }
        }

        return ipAddress; 
    }

    public static void showPatientDetails(Patient patient) {

        System.out.println("\nPatient Details:");
        System.out.println("DNI: " + patient.getDNI());
        System.out.println("Name: " + patient.getName());
        System.out.println("Surname: " + patient.getSurname());
        System.out.println("Date of Birth: " + patient.getDob());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Telephone: " + patient.getPhoneNumber());
        System.out.println("Email: " + patient.getEmail());
        ArrayList prevDiseases=patient.getPreviousDiseases();
        if (!prevDiseases.isEmpty()){
        System.out.println("Previous Diseases: " + patient.getPreviousDiseases());
        } else{
            System.out.println("The patient did not register any previous diseases");
        }
    }

    // Method to validate input within a range 
    public static int getValidInput(int min, int max) {
        while (true) {
            try {
                String input = r.readLine(); // Read input as a string
                int choice = Integer.parseInt(input); // Convert to integer
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (IOException | NumberFormatException e) {
                // Ignore and prompt again
            }
            System.out.print("Invalid input. Please try again: ");
        }

    }

}
