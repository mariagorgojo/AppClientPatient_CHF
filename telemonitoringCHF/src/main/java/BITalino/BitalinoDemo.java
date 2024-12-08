package BITalino;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import pojos.Recording;

public class BitalinoDemo {

    // Configure channels based on the signal type
    public static int[] configureChannels(Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return new int[]{1}; // Specific channel for ECG
            case EMG:
                return new int[]{0}; // Specific channel for EMG
            default:
                throw new IllegalArgumentException("Signal not permitted: " + signalType);
        }
    }

    // Record data and save it to a file
    public static ArrayList<Integer> recordAndSaveData(BITalino bitalino, Recording.Type signalType, String fileName, String recordingDate, String patientDni) throws BITalinoException, IOException {
        ArrayList<Integer> data = new ArrayList<>();

        int blockSize = 10; // Block size for samples
        int totalBlocks = 6000; // Total number of blocks to capture
                                // We want the duration to be 60 seconds
                                // duration = blockSize * totalBlocks / frequency
        System.out.println("Starting recording...");
        for (int blockIndex = 0; blockIndex < totalBlocks; blockIndex++) {
            try {
                // Read a block of data
                Frame[] frames = bitalino.read(blockSize);
                // Process and save the block data
                for (int i = 0; i < frames.length; i++) {
                    int sequence = frames[i].seq;
                    int value = getSignalValue(frames[i], signalType);
                    data.add(value); // Store the value
                }
            } catch (BITalinoException e) {
                System.err.println("Error:" + e.getMessage());
                break; // Stop capture if a critical error occurs
            }
        }
        // Save the captured data to a file
        generateFileName(signalType, recordingDate, patientDni);
        saveDataToFile(fileName, data);
        return data;
    }

   // Function to determine the signal value based on the signal type
    private static int getSignalValue(Frame frame, Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return frame.analog[0]; // ECG channel
            case EMG:
                return frame.analog[0]; // EMG channel
            default:
                throw new IllegalArgumentException("Signal type not valid: " + signalType);
        }
    }

    // Method to validate a MAC address
    public static boolean isValidMacAddress(String macAddress) {
        // Regular expression for MAC address format
        String macPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        return macAddress != null && macAddress.matches(macPattern);
    }

    // Generate the file name with the signal type and date
    public static String generateFileName(Recording.Type signalType, String recordingDate, String patientDni) {
        return patientDni + "_" + signalType + "_" + recordingDate.replace(":", "-").replace(" ", "_") + ".txt";
    }

    // Save the recorded data to a file
    private static void saveDataToFile(String fileName, ArrayList<Integer> data) throws IOException {
        try ( FileWriter writer = new FileWriter(fileName)) {
            for (Integer value : data) {
                writer.write(value + "\n");
            }
            System.out.println("Data saved with name: " + fileName);
            System.out.println("File saved in: " + new java.io.File(fileName).getAbsolutePath());
        }
    }
}
