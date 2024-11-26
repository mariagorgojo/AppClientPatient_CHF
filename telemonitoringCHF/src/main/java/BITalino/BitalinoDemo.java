package BITalino;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BitalinoDemo {

    public enum Type {
        ECG, EMG
    } // Tipos de señal admitidos

    public static void main(String[] args) {
        BITalino bitalino = null;

        try {
            // Dirección MAC fija del BITalino
            String macAddress = "83:BA:20:5E:FD:76";

            // Solicitar el tipo de señal (ECG o EMG)
            Type signalType = getSignalType();

            // Inicializar y conectar el dispositivo BITalino
            bitalino = new BITalino();
            int samplingRate = 10; // Tasa de muestreo
            bitalino.open(macAddress, samplingRate);

            // Configurar los canales para el tipo de señal
            int[] channelsToAcquire = configureChannels(signalType);

            // Iniciar la captura de datos
            try {
                bitalino.start(channelsToAcquire); // Captura la señal
            } catch (Throwable ex) {
                System.out.println("Error al iniciar la captura de datos");
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

            // Capturar datos durante un período y guardarlos en un archivo
            ArrayList<Integer> data = recordAndSaveData(bitalino, signalType);

            System.out.println("Proceso completado exitosamente.");

        } catch (BITalinoException ex) {
            System.err.println("Error al comunicarse con BITalino: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error al guardar los datos: " + ex.getMessage());
        } finally {
            // Detener y cerrar la conexión con BITalino
            try {
                if (bitalino != null) {
                    bitalino.stop();
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                System.err.println("Error al cerrar el BITalino: " + ex.getMessage());
            }
        }
    }

    // Solicitar el tipo de señal (ECG o EMG)
    private static Type getSignalType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona el tipo de señal (ECG o EMG): ");
        return Type.valueOf(scanner.nextLine().toUpperCase());
    }

    // Configurar los canales según el tipo de señal
    private static int[] configureChannels(Type signalType) {
        if (signalType == Type.ECG) {
            return new int[]{2}; // Canal para ECG
        } else {
            return new int[]{1}; // Canal para EMG
        }
    }

    // Grabar datos y guardarlos en un archivo
    private static ArrayList<Integer> recordAndSaveData(BITalino bitalino, Type signalType) throws BITalinoException, IOException {
        // Iniciar grabación durante un tiempo específico
        ArrayList<Integer> data = recordData(bitalino, signalType);

        // Generar el nombre del archivo
        String timestamp = getTimestamp();
        String fileName = generateFileName(signalType, timestamp);

        // Guardar los datos en un archivo
        saveDataToFile(fileName, data);

        return data;
    }

    // Función para grabar datos durante un tiempo específico
    private static ArrayList<Integer> recordData(BITalino bitalino, Type signalType) throws BITalinoException {
        ArrayList<Integer> data = new ArrayList<>();
        int duration = 10; // Duración de la grabación en segundos

        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < duration * 1000) {
            int blockSize = 10; // Leer bloques de 10 muestras
            Frame[] frames = bitalino.read(blockSize);

            for (Frame frame : frames) {
                int value;
                if (signalType == Type.ECG) {
                    value = frame.analog[1]; // Captura ECG
                } else {
                    value = frame.analog[0]; // Captura EMG
                }
                data.add(value);
            }
        }

        System.out.println("La grabación ha finalizado.");
        return data;
    }

    // Obtener la fecha y hora actual en formato legible
    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    // Generar el nombre del archivo con la señal y la fecha
    private static String generateFileName(Type signalType, String timestamp) {
        return signalType + "_" + timestamp.replace(":", "-").replace(" ", "_") + ".txt";
    }

    // Guardar los datos grabados en un archivo
    private static void saveDataToFile(String fileName, ArrayList<Integer> data) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Integer value : data) {
                writer.write(value + "\n");
            }
            System.out.println("Datos guardados en el archivo: " + fileName);
        }
    }
}