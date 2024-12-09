/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionPatient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Disease;
import pojos.Doctor;
import pojos.Episode;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Recording;
import pojos.Recording.Type;
import pojos.Surgery;
import pojos.Symptom;

/**
 *
 * @author martaguzman
 */
public class ConnectionPatient {

    private static Socket socket;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    // Método para establecer la conexión al servidor
    public static void connectToServer(String ip_address) throws IOException {
        if (socket == null || socket.isClosed()) {

            System.out.println("Connecting to server...");
            socket = new Socket(ip_address, 9090); // Cambiar localhost y puerto según sea necesario
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    // Método para cerrar la conexión al servidor
    public static void closeConnection() {

        try {
            if (printWriter != null) {
                // Notificar al servidor que el cliente está haciendo logout
                printWriter.println("LOGOUT");
                printWriter.flush(); // Asegurarse de que el mensaje se envíe inmediatamente
            }

            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean checkAvailableDoctors() throws IOException {
        printWriter.println("CHECK_DOCTORS");
        String response = bufferedReader.readLine();
        if (response.equals("NO_DOCTORS")) {
            return false;
        } else if (response.equals("AVAILABLE_DOCTORS")) {
            return true;

        }

        return true;
    }

    public static boolean sendRegisterServer(Patient patient, String encryptedPassword, List<String> previousDiseases) {
        try {
            // System.out.println("Sending patient registration information...");
            printWriter.println("REGISTER_PATIENT");
            printWriter.println(patient.getDNI());
            printWriter.println(encryptedPassword); // Enviar la contraseña
            printWriter.println(patient.getName());
            printWriter.println(patient.getSurname());
            printWriter.println(patient.getPhoneNumber().toString());
            printWriter.println(patient.getEmail());
            printWriter.println(patient.getDob().toString()); // mandamos todo como String
            printWriter.println(patient.getGender().toString());

            for (String disease : previousDiseases) {
                printWriter.println("DISEASE|" + disease);
            }
            printWriter.println("END_OF_PREVIOUS_DISEASES");

            String serverResponse = bufferedReader.readLine();
            if ("VALID".equals(serverResponse)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionPatient.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }

    // Método para validar el login del paciente
    public static boolean validateLogin(String dni, String password) {
        try {

            printWriter.println("LOGIN_PATIENT");
            printWriter.println(dni);
            printWriter.println(password);

            // Esperamos la respuesta del servidor
            String serverResponse = bufferedReader.readLine();

            if ("VALID".equals(serverResponse)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return false;
            }

        } catch (IOException e) {
            Logger.getLogger(ConnectionPatient.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }

    public static Patient viewPatientInformation(String dni) throws IOException {
        ArrayList<Disease> previousDiseases = new ArrayList<>();
        Patient patient = null;
        try {
            //  connectToServer();
            printWriter.println("VIEW_PATIENT_INFORMATION");
            printWriter.println(dni);

            // Read previous diseases
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_DISEASES"))) {
                String[] partsDisease = dataString.split(";");

                if (partsDisease.length >= 2 && partsDisease[0].equals("DISEASES")) {

                    Disease disease = new Disease();
                    disease.setDisease(partsDisease[1]);
                    previousDiseases.add(disease);

                }
            }
            dataString = bufferedReader.readLine(); // Read line that contains patient data
            String[] parts = dataString.split(";");

            // int length = parts.length;
            if (parts.length == 8) {

                // System.out.println("estoy dentro del parts.length == 7 -> le devuleve correct el paciente el server.");
                // System.out.println("le devolcio el server: " + dataString);
                patient = new Patient();
                patient.setId(Integer.parseInt(parts[0]));
                patient.setDni(parts[1]);
                patient.setName(parts[2]);
                patient.setSurname(parts[3]);
                patient.setEmail(parts[4]);
                patient.setGender(Gender.valueOf(parts[5].toUpperCase()));
                patient.setPhoneNumber(Integer.parseInt(parts[6])); // Convertir a entero
                patient.setDob(LocalDate.parse(parts[7], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                patient.setPreviousDiseases(previousDiseases);
                return patient;
            } else {
                System.out.println("Invalid data format received from server.");
                return null;
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
            /*} finally {
            closeConnection(); // Cerrar la conexión al servidor
             */
        }

        return null;
    }

    public static ArrayList<Episode> getPatientEpisodes(String patientDni) throws IOException {
        ArrayList<Episode> episodes = new ArrayList<>();

        try {

            printWriter.println("VIEW_PATIENT_EPISODES");
            printWriter.println(patientDni); // Enviar el DNI del paciente

            // Leer la lista de episodios desde el servidor
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_LIST"))) {
                // System.out.println("Data received from server: " + dataString);

                String[] parts = dataString.split(";");
                if (parts.length == 2) { // Validar que los datos contengan ID y Fecha
                    Episode episode = new Episode();
                    episode.setId(Integer.parseInt(parts[0])); // ID del episodio
                    episode.setDate(LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_DATE_TIME));

                    episodes.add(episode);
                } else {
                    System.err.println("Error seeing episodes");
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving episodes: " + e.getMessage());

        }
        return episodes;
    }

    public static Episode getEpisodeDetails(int episodeId, String patient_dni) throws IOException {
        Episode episode = new Episode();

        try {
            // Conectar al servidor
            // connectToServer();
            printWriter.println("VIEW_EPISODE_DETAILS");
            printWriter.println(String.valueOf(episodeId)); // Enviar el ID del episodio
            printWriter.println(patient_dni);

            // Leer los detalles del episodio desde el servidor
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_DETAILS"))) {
                String[] parts = dataString.split(";");

                if (parts.length >= 2) {
                    switch (parts[0]) {
                        case "SURGERIES":
                            Surgery surgery = new Surgery();
                            surgery.setSurgery(parts[1]);
                            episode.getSurgeries().add(surgery);
                            break;

                        case "SYMPTOMS":
                            Symptom symptom = new Symptom();
                            symptom.setSymptom(parts[1]);
                            episode.getSymptoms().add(symptom);
                            break;

                        case "DISEASES":
                            Disease disease = new Disease();
                            disease.setDisease(parts[1]);
                            episode.getDiseases().add(disease);
                            break;

                        case "RECORDINGS":
                            //   System.out.println("In recordings connect patient");

                            if (parts.length == 3) { // Asegúrate de que haya suficiente información para un Recording
                                Recording recording = new Recording();
                                recording.setId(Integer.parseInt(parts[1])); // Asignar ID de la grabación

                                recording.setSignal_path(parts[2]); // Asignar ruta de la señal
                                episode.getRecordings().add(recording);
                            } else {
                                System.err.println("Invalid RECORDINGS format: " + String.join(";", parts));
                            }
                            break;

                        default:
                            System.err.println("Unknown detail type received: " + parts[0]);
                    }
                }
            }

            //   System.out.println(episode);
            return episode;
        } catch (IOException e) {
            System.err.println("Error retrieving episode details: " + e.getMessage());
        }

        return episode;
    }

    public static List<Symptom> getAvailableSymptoms() {
        List<Symptom> symptoms = new ArrayList<>();

        try {
            // connectToServer(); // Establecer conexión con el servidor

            printWriter.println("AVAILABLE_SYMPTOMS"); // Comando para el servidor
            printWriter.flush();

            String symptomData;
            while (!(symptomData = bufferedReader.readLine()).equals("END_OF_LIST")) {
                Symptom symptom = new Symptom();
                symptom.setSymptom(symptomData);
                symptoms.add(symptom);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving symptoms: " + e.getMessage());
        }
        /*finally {
            closeConnection(); // Cerrar la conexión al servidor
        }*/

        return symptoms;
    }

    public static int insertEpisode(Episode episode, List<String> symptoms, List<Recording> recordings) {
        try {
            printWriter.println("INSERT_EPISODE");
            printWriter.println("CREATE_EPISODE");
            printWriter.println(episode.getPatient_id());
            printWriter.println(episode.getDate().toString());

            for (String symptom : symptoms) {
                printWriter.println("SYMPTOM|" + symptom);
            }

            for (Recording recording : recordings) {
                StringBuilder dataBuilder = new StringBuilder();
                for (Integer dataPoint : recording.getData()) {
                    dataBuilder.append(dataPoint).append(",");
                }
                if (dataBuilder.length() > 0) {
                    dataBuilder.setLength(dataBuilder.length() - 1);
                }
                String data = dataBuilder.toString();
                printWriter.println("RECORDING|" + recording.getType().toString() + "|"
                        + recording.getDate().toString() + "|"
                        + recording.getSignal_path()
                        + "|" + data);
            }

            printWriter.println("END_OF_EPISODE");
            printWriter.flush();

            // Leer el ID del episodio del servidor
            String response = bufferedReader.readLine();
            return Integer.parseInt(response);
        } catch (IOException e) {
            System.err.println("Error al insertar el episodio: " + e.getMessage());
            return -1;
        }
    }

    /*finally {
            closeConnection();
        }*/
    public static List<Disease> getAvailableDiseases() {
        List<Disease> diseases = new ArrayList<>();

        try {
            // connectToServer(); // Establecer conexión con el servidor

            printWriter.println("AVAILABLE_DISEASES"); // Comando para el servidor
            printWriter.flush();

            String diseaseData;
            while (!(diseaseData = bufferedReader.readLine()).equals("END_OF_LIST")) {
                Disease disease = new Disease();
                disease.setDisease(diseaseData);
                diseases.add(disease);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving diseases: " + e.getMessage());
        }
        /*finally {
            closeConnection(); // Cerrar la conexión al servidor
        }*/

        return diseases;
    }

}
