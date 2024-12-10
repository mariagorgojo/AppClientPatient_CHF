/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import BITalino.BitalinoDemo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import pojos.Patient;
import pojos.Recording;

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
        String ipAddress;

        while (true) {
            try {
                ipAddress = r.readLine();

                if (valid_ipAddress(ipAddress)) {
                    System.out.println("\nValid IP address: " + ipAddress);

                    break;
                } else {
                    System.out.println("The IP address is not valid or is not responding.");
                    System.out.println("\nPlease enter a valid IP address: ");

                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading input. Please try again.");
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
        ArrayList prevDiseases = patient.getPreviousDiseases();
        if (!prevDiseases.isEmpty()) {
            System.out.println("Previous Diseases: " + patient.getPreviousDiseases());
        } else {
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

    // new
    public static String getValidMacAddress() {
        String macAddress;
        while (true) {
            System.out.println("Introduce a valid MAC address (format: XX:XX:XX:XX:XX:XX): ");
            macAddress = readString();
            if (BitalinoDemo.isValidMacAddress(macAddress)) {
                break;
            }
            System.out.println("MAC address invalid. Try again.");
        }
        return macAddress;
    }

    public static Recording.Type getRecordingType() {
        while (true) {
            System.out.println("Recording Type (ECG/EMG, or type 'done' to finish): ");
            String typeInput = readString();

            if (typeInput.equalsIgnoreCase("done")) {
                return null;
            }
            try {
                return Recording.Type.valueOf(typeInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid recording type. Please choose ECG or EMG.");
            }
        }
    }

    public static boolean askToRetry(String message) {
        while (true) {
            System.out.println(message + " [YES/NO]: ");
            String retryResponse = readString().toUpperCase();
            if (retryResponse.equals("YES")) {
                return true;
            }
            if (retryResponse.equals("NO")) {
                return false;
            }
            System.out.println("Not a valid response. Please type YES or NO.");
        }
    }

    public static void displayRecordingInstructions(Recording.Type signalType) {
        System.out.println("\n=== " + signalType + " Recording Instructions ===");
        if (signalType == Recording.Type.ECG) {
            System.out.println("1. Ensure the electrodes are placed correctly:\n"
                    + "   - Electrode 1 (Red): Below the right collarbone (chest area).\n"
                    + "   - Electrode 2 (Black): Below the left collarbone (chest area).\n"
                    + "   - Electrode 3 (White - Ground): Lower left side of the chest.\n"
                    + "2. Stay still during the recording.");
        } else if (signalType == Recording.Type.EMG) {
            System.out.println("1. Place the electrodes on the quadriceps muscle:\n"
                    + "   - Electrode 1 (Red): Center of the quadriceps muscle.\n"
                    + "   - Electrode 2 (Black): 2-3 cm above Electrode 1, along the muscle line.\n"
                    + "   - Electrode 3 (White - Ground): Over the patella (knee).\n"
                    + "2. Alternate between contracting and relaxing the muscle.\n"
                    + "3. Avoid unnecessary external movements.");
        }
        System.out.println("\n The recording will last 60 seconds.");
    }

    public static void countdown(int seconds) throws InterruptedException {
        for (int i = seconds; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
    }
}

