package BITalino;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Recording;

public class BitalinoDemo {

    // Configurar los canales según el tipo de señal
    public static int[] configureChannels(Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return new int[]{1}; // Canal específico para ECG
            case EMG:
                return new int[]{0}; // Canal específico para EMG
            default:
                throw new IllegalArgumentException("Not signal permitted: " + signalType);
        }
    }

    // Grabar datos y guardarlos en un archivo
    public static ArrayList<Integer> recordAndSaveData(BITalino bitalino, Recording.Type signalType, String fileName, String recordingDate, String patientDni) throws BITalinoException, IOException {
        ArrayList<Integer> data = new ArrayList<>();
        //copiado de alberto:
        /*Frame[] frame;
        
                   //Read in total 10000000 times
            for (int j = 0; j < 10000000; j++) {

                //Each time read a block of 10 samples 
                int block_size=10;
                frame = bitalino.read(block_size);

                System.out.println("size block: " + frame.length);

                //Print the samples
                for (int i = 0; i < frame.length; i++) {
                    int value = getSignalValue(frame[i], signalType);

                    data.add(value); // Almacenar el valor 

                    System.out.println((j * block_size + i) + " seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " "
                     //       + frame[i].analog[1] + " "
                    //  + frame[i].analog[2] + " "
                    //  + frame[i].analog[3] + " "
                    //  + frame[i].analog[4] + " "
                    //  + frame[i].analog[5]
                    );

                }
            }*/
        int blockSize = 10; // Tamaño del bloque de muestras
        int totalBlocks = 1000; // Número total de bloques que queremos capturar

        System.out.println("Starting recording...");

        for (int blockIndex = 0; blockIndex < totalBlocks; blockIndex++) {
            try {
                // Leer un bloque de datos
                Frame[] frames = bitalino.read(blockSize);
                //System.out.println("Bloque " + blockIndex + " leído. Tamaño: " + frames.length);

                // Procesar y guardar los datos del bloque
                for (int i = 0; i < frames.length; i++) {
                    int sequence = frames[i].seq;
                    int value = getSignalValue(frames[i], signalType);

                    data.add(value); // Almacenar el valor 

                    // borrar luego
                    System.out.println("Bloque: " + blockIndex + " | Muestra: " + i
                            + " | Secuencia: " + sequence
                            + " | Valor: " + value);
                }
            } catch (BITalinoException e) {
                System.err.println("Error:" + e.getMessage());
                break; // Detener captura si ocurre un error crítico
            }
        }

        // Guardar los datos capturados en un archivo
        generateFileName(signalType, recordingDate, patientDni);
        saveDataToFile(fileName, data);

        return data;
    }

    /*// Función para grabar datos durante un tiempo específico
    private static ArrayList<Integer> recordData(BITalino bitalino, Recording.Type signalType) throws BITalinoException {
        ArrayList<Integer> data = new ArrayList<>();
        int duration = 60; // Duración de la grabación en segundos
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (duration * 1000);

        // Captura los datos mientras la conexión esté activa
        while (System.currentTimeMillis() < endTime) {
            try {
                // Intentamos capturar datos
                captureAndAddData(bitalino, signalType, data);
            } catch (BITalinoException e) {
                System.err.println("Communication error with the device " + e.getMessage());
                break; // Detener la captura si se pierde la conexión
            }
        }

        return data;
    }
/*
// Función que captura los datos y los agrega a la lista según el tipo de señal
    private static void captureAndAddData(BITalino bitalino, Recording.Type signalType, ArrayList<Integer> data) throws BITalinoException {
        int blockSize = 1000; // Número de muestras por lectura

        System.out.println("Capturing Data...");
        Frame[] frames = bitalino.read(blockSize);

        // Recorrer los frames y agregar los valores correspondientes
        for (Frame frame : frames) {
            int value = getSignalValue(frame, signalType);
            data.add(value);
        }
    }*/
// Función que determina el valor de la señal según el tipo de señal
    private static int getSignalValue(Frame frame, Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return frame.analog[0]; // Canal ECG
            case EMG:
                return frame.analog[0]; // Canal EMG
            default:
                throw new IllegalArgumentException("Signal type not valid: " + signalType);
        }
    }

    // Método para validar una dirección MAC
    public static boolean isValidMacAddress(String macAddress) {
        // Expresión regular para formato de dirección MAC
        String macPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        return macAddress != null && macAddress.matches(macPattern);
    }

    // Generar el nombre del archivo con la señal y la fecha
    public static String generateFileName(Recording.Type signalType, String recordingDate, String patientDni) {
        return patientDni + "_" + signalType + "_" + recordingDate.replace(":", "-").replace(" ", "_") + ".txt";
    }

    // Guardar los datos grabados en un archivo
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
