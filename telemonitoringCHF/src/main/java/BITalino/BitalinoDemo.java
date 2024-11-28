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

    //   DNI --> NAME DEL FILE
    private static final String MAC_ADDRESS = "98:D3:51:FD:9C:72"; // cambiar luego idea
    private static final int SAMPLING_RATE = 100; // Tasa de muestreo

    public static void main(String[] args) {
        BITalino bitalino = null;

        try {
            // Solicitar el tipo de señal (ECG o EMG)
            // SignalType signalType = getSignalType();

            // Inicializar y conectar el dispositivo BITalino
            bitalino = new BITalino();
            bitalino.open(MAC_ADDRESS, SAMPLING_RATE);

            // Configurar los canales para el tipo de señal
            //int[] channelsToAcquire = configureChannels(signalType);

            // Iniciar la captura de datos
           // bitalino.start(channelsToAcquire);

            // Capturar datos durante un período y guardarlos en un archivo
          // ArrayList<Integer> data = recordAndSaveData(Bbitalino, signalType, );

            System.out.println("Proceso completado exitosamente.");
          //  System.out.println("Datos capturados: " + data);

        } catch (BITalinoException ex) {
            System.err.println("Error al comunicarse con BITalino: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Error: " + ex.getMessage());
       // } catch (IOException ex) {
           // System.err.println("Error al guardar los datos: " + ex.getMessage());
        } catch (Throwable ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    // Configurar los canales según el tipo de señal
    public static int[] configureChannels(Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return new int[]{1}; // Canal específico para ECG
            case EMG:
                return new int[]{0}; // Canal específico para EMG
            default:
                throw new IllegalArgumentException("No signal permitted: " + signalType);
        }
    }

    // Grabar datos y guardarlos en un archivo
    public static ArrayList<Integer> recordAndSaveData(BITalino bitalino, Recording.Type signalType, String recordingDate, String fileName) throws BITalinoException, IOException {
        ArrayList<Integer> data = recordData(bitalino, signalType);
        saveDataToFile(fileName, data);
        return data;
    }

    // Función para grabar datos durante un tiempo específico
    private static ArrayList<Integer> recordData(BITalino bitalino,  Recording.Type signalType) throws BITalinoException {
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
                System.err.println("Error de comunicación con el dispositivo: " + e.getMessage());
                break; // Detener la captura si se pierde la conexión
            }
        }

        // Verificación de si se capturaron datos
        if (data.isEmpty()) {
            System.out.println("No se capturaron datos. Asegúrese de que el dispositivo esté conectado correctamente.");
        } else {
            System.out.println("La grabación ha finalizado con éxito.");
        }

        return data;
    }

// Función que captura los datos y los agrega a la lista según el tipo de señal
    private static void captureAndAddData(BITalino bitalino,  Recording.Type signalType, ArrayList<Integer> data) throws BITalinoException {
        int blockSize = 1000; // Número de muestras por lectura

        System.out.println("Capturing Data...");
        Frame[] frames = bitalino.read(blockSize);

        // Recorrer los frames y agregar los valores correspondientes
        for (Frame frame : frames) {
            int value = getSignalValue(frame, signalType);
            data.add(value);
        }
    }

// Función que determina el valor de la señal según el tipo de señal
    private static int getSignalValue(Frame frame,  Recording.Type signalType) {
        switch (signalType) {
            case ECG:
                return frame.analog[1]; // Canal ECG
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
    public static String generateFileName( Recording.Type signalType, String recordingDate, String patientDni) {
        return patientDni+"_"+ signalType + "_" + recordingDate.replace(":", "-").replace(" ", "_") + ".txt";
    }

    // Guardar los datos grabados en un archivo
    private static void saveDataToFile(String fileName, ArrayList<Integer> data) throws IOException {
        try ( FileWriter writer = new FileWriter(fileName)) {
            for (Integer value : data) {
                writer.write(value + "\n");
            }
            System.out.println("Data saved in file: " + fileName);
            System.out.println("File saved in: " + new java.io.File(fileName).getAbsolutePath());
        }
    }

}
