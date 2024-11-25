package BITalino;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitalinoDemo {

    public enum Type {
        ECG, EMG
    } // Tipos de señal admitidos

    public static void main(String[] args) {
        BITalino bitalino = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Solicitar la dirección MAC del dispositivo
            String macAddress = getMacAddress(scanner);

            // Validar el formato de la dirección MAC
            if (!isValidMacAddress(macAddress)) {
                throw new IllegalArgumentException("La dirección MAC no tiene un formato válido.");
            }

            // Solicitar el tipo de señal
            Type signalType = getSignalType(scanner);

            // Inicializar el dispositivo BITalino
            bitalino = new BITalino();
            int samplingRate = 10; // Tasa de muestreo (puede ser 10, 100 o 1000)
            bitalino.open(macAddress, samplingRate);

            // Configurar los canales según el tipo de señal
            int[] channelsToAcquire = configureChannels(signalType);

            // Intentar iniciar la adquisición de datos
            try {
                bitalino.start(channelsToAcquire); // Captura la señal
            } catch (Throwable ex) {
                System.out.println("Error al iniciar la captura de datos");
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Crear un formato de fecha y hora legible
            String timestamp = getTimestamp();

            // Nombre del archivo de salida
            String fileName = generateFileName(signalType, timestamp);

            // Iniciar grabación durante un tiempo específico
            ArrayList<Integer> data = recordData(bitalino, signalType);

            // Guardar los datos en un archivo
            saveDataToFile(fileName, data);

        } catch (BITalinoException ex) {
            System.err.println("Error al comunicarse con BITalino: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error al guardar los datos: " + ex.getMessage());
        } finally {
            // Cerrar la conexión
            try {
                if (bitalino != null) {
                    bitalino.stop();
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                System.err.println("Error al cerrar el BITalino: " + ex.getMessage());
            }
        }

        scanner.close();
    }

    // Solicitar la dirección MAC al usuario -> cambiar a Utilities
    private static String getMacAddress(Scanner scanner) {
        System.out.println("Introduce la dirección MAC del BITalino: ");
        return scanner.nextLine();
    }

    // Validar el formato de la dirección MAC
    private static boolean isValidMacAddress(String macAddress) {
        String macPattern = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$";
        Pattern pattern = Pattern.compile(macPattern);
        Matcher matcher = pattern.matcher(macAddress);
        return matcher.matches();
    }

    // Solicitar el tipo de señal (ECG o EMG) al usuario -> cambiar a Utilities
    private static Type getSignalType(Scanner scanner) {
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

    // Obtener la fecha y hora actual en formato legible
    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    // Generar el nombre del archivo con la señal y la fecha
    private static String generateFileName(Type signalType, String timestamp) {
        return signalType + "_" + timestamp.replace(":", "-").replace(" ", "_") + ".txt";
    }

    // Función para grabar datos durante un tiempo específico
    private static ArrayList<Integer> recordData(BITalino bitalino, Type signalType) throws BITalinoException {
        ArrayList<Integer> data = new ArrayList<>();
        
        // cambiar duracion sino mas adelante 
        int duration = 2; // Duración de la grabación en segundos (por ejemplo, 30 segundos)

        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < duration * 1000) {
            int blockSize = 10; // Leer bloques de 10 muestras
            Frame[] frames = bitalino.read(blockSize);

            // Guardar los datos de la señal
            for (Frame frame : frames) {
                int value;

                // Dependiendo del tipo de señal, capturamos el valor correspondiente
                if (signalType == Type.ECG) {
                    value = frame.analog[1]; // Captura el canal 0 para ECG
                } else {
                    value = frame.analog[0]; // Captura el canal 1 para EMG
                }

                // Añadir el valor al ArrayList de datos
                data.add(value);
            }
        }

        // Notificar al usuario que la grabación ha finalizado
        System.out.println("La grabación ha finalizado.");

        return data;
    }

    // Guardar los datos grabados en un archivo
    private static void saveDataToFile(String fileName, ArrayList<Integer> data) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (Integer value : data) {
            writer.write(value + "\n");
        }
        writer.close();
        System.out.println("Datos guardados en el archivo: " + fileName);
    }
}
