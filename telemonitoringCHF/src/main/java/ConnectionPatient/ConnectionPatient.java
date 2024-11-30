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
    private static void connectToServer() throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 9090); // Cambia localhost y puerto según sea necesario
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    // Método para cerrar la conexión al servidor
    public static void closeConnection() {
        try {
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

    // Método para registrar un paciente en el servidor
    public static boolean sendRegisterServer(Patient patient, String password) {
        try {
            connectToServer(); // Establecemos la conexión

            // Enviamos los datos del paciente al servidor
            System.out.println("Sending patient registration information...");
            printWriter.println("REGISTER_PATIENT");
            printWriter.println(patient.getDNI());
            printWriter.println(patient.getPassword()); // Enviar la contraseña
            printWriter.println(patient.getName());
            printWriter.println(patient.getSurname());
            printWriter.println(patient.getPhoneNumber().toString());
            printWriter.println(patient.getEmail());
            printWriter.println(patient.getDob().toString()); // mandamos todo como String
            printWriter.println(patient.getGender().toString());

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
        /*finally {
            // Enviar "STOP" para indicar el fin de los datos
            printWriter.println("STOP");
            closeConnection(); // Cerramos la conexión
        }*/

    }

    // Método para validar el login del paciente
    public static boolean validateLogin(String dni, String password) {
        try {
            connectToServer(); // Establecemos la conexión

            // Enviamos las credenciales para validación
            System.out.println("Sending patient login information...");

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
        /*finally {
            printWriter.println("STOP");
            closeConnection(); // Cerramos la conexión
        }*/
    }

    // CAMBIAR PATIENT
    public static Patient viewPatientInformation(String dni) throws IOException {
        Patient patient = null;
        try {
            connectToServer();
            printWriter.println("VIEW_PATIENT_INFORMATION");
            printWriter.println(dni);

            String dataString = bufferedReader.readLine();
            String[] parts = dataString.split(",");

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

                /*String[] doctorParts = patientPart
                /[7].split(","); // Dividir directamente por comas

                    Doctor doctor = new Doctor();
                    doctor.setDni(doctorParts[0]);
                    doctor.setName(doctorParts[1]);
                    doctor.setSurname(doctorParts[2]);
                    doctor.setTelephone(Integer.parseInt(doctorParts[3])); // Convertir teléfono a entero
                    doctor.setEmail(doctorParts[4]);
                    patient.setDoctor(doctor);*/
                //System.out.println("el objeto patient es: " + patient);
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
            // Conectar al servidor
            connectToServer();
            System.out.println("CONECTED TO THE SERVER");
            printWriter.println("VIEW_PATIENT_EPISODES");
            printWriter.println(patientDni); // Enviar el DNI del paciente

            // Leer la lista de episodios desde el servidor
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_LIST"))) {
                System.out.println("Data received from server: " + dataString);

                String[] parts = dataString.split(",");
                if (parts.length == 2) { // Validar que los datos contengan ID y Fecha
                    Episode episode = new Episode();
                    episode.setId(Integer.parseInt(parts[0])); // ID del episodio
                    //episode.setDate(LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS"))); // Fecha
                    episode.setDate(LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_DATE_TIME));

                    episodes.add(episode);
                } else {
                    System.err.println("Error seeing episodes");
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving episodes: " + e.getMessage());

            // No se debería cerrar la conexión, cerrar SOLO al final de las operaciones
            // cerrar en el menu del paciente
            /* } finally {
            // Asegurar que la conexión al servidor se cierra
            closeConnection();
             */
        }
        return episodes;
    }

    public static Episode getEpisodeDetails(int episodeId, String patient_dni) throws IOException {
        Episode episode = new Episode();
        //System.out.println("CONNECTION PATIENT!!!: " + patient_dni);

        try {
            // Conectar al servidor
            connectToServer();
            printWriter.println("VIEW_EPISODE_DETAILS");
            printWriter.println(String.valueOf(episodeId)); // Enviar el ID del episodio
            printWriter.println(patient_dni);

            // Leer los detalles del episodio desde el servidor
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_DETAILS"))) {
                String[] parts = dataString.split(",");

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
                            System.out.println("In recordings connect patient");

                            if (parts.length == 3) { // Asegúrate de que haya suficiente información para un Recording
                                Recording recording = new Recording();
                                recording.setId(Integer.parseInt(parts[1])); // Asignar ID de la grabación

                                recording.setSignal_path(parts[2]); // Asignar ruta de la señal
                                episode.getRecordings().add(recording);
                            } else {
                                System.err.println("Invalid RECORDINGS format: " + String.join(",", parts));
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
        /*finally {
            // Asegurar que la conexión al servidor se cierra
            closeConnection();
        }*/
        // System.out.println(episode);
        return episode;
    }

    public static List<Disease> getAvailableDiseases() {
        List<Disease> diseases = new ArrayList<>();

        try {
            connectToServer(); // Establecer conexión con el servidor

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

    /* public static boolean insertNewDisease(String diseaseName) {
        try {
            connectToServer();
            printWriter.println("INSERT_NEW_DISEASE");
            printWriter.println(diseaseName);
            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error inserting new disease: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }
     */
    public static List<Symptom> getAvailableSymptoms() {
        List<Symptom> symptoms = new ArrayList<>();

        try {
            connectToServer(); // Establecer conexión con el servidor

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

    /*  public static boolean insertNewSymptom(String symptomName) {
        try {
            connectToServer();
            printWriter.println("INSERT_NEW_SYMPTOM");
            printWriter.println(symptomName);
            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error inserting new symptomName: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }*/
    public static List<Surgery> getAvailableSurgeries() {
        List<Surgery> surgeries = new ArrayList<>();

        try {
            connectToServer(); // Establecer conexión con el servidor

            printWriter.println("AVAILABLE_SURGERIES"); // Comando para el servidor
            printWriter.flush();
            String surgeryData;
            while (!(surgeryData = bufferedReader.readLine()).equals("END_OF_LIST")) {
                Surgery surgery = new Surgery();
                surgery.setSurgery(surgeryData);
                surgeries.add(surgery);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving surgeries: " + e.getMessage());
        }
        /*finally {
            closeConnection(); // Cerrar la conexión al servidor
        }*/

        return surgeries;
    }

    /*
    public static boolean insertNewSurgery(String surgeryName) {
        try {
            connectToServer();
            printWriter.println("INSERT_NEW_SURGERY");
            printWriter.println(surgeryName);
            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error inserting new surgeryName: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
     */
    public static boolean insertRecording(Recording recording, int episodeId) {
        try {
            connectToServer();
            printWriter.println("INSERT_RECORDING"); // Comando para el servidor

            // Enviar datos básicos de la grabación
            printWriter.println(episodeId); // Asociar grabación con el episodio
            printWriter.println(recording.getType().name()); // Tipo de grabación (ECG/EMG)
            printWriter.println(recording.getDuration()); // Duración en segundos
            printWriter.println(recording.getDate().toString()); // Fecha de grabación
            printWriter.println(recording.getSignal_path()); // Ruta del archivo de la señal

            // Enviar datos de la señal (array de enteros)
            for (Integer dataPoint : recording.getData()) {
                printWriter.println(dataPoint);
            }
            printWriter.println("END_OF_RECORDING_DATA"); // Señal de fin de datos de la grabación

            // Leer la respuesta del servidor
            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error inserting recording: " + e.getMessage());
            return false;
        }
        /*finally {
            closeConnection();
        }*/
    }

    public static boolean insertEpisode(Episode episode, List<String> diseases, List<String> symptoms, List<String> surgeries, List<Recording> recordings) {
        try {
            connectToServer();
            // Enviar comando al servidor
            printWriter.println("INSERT_EPISODE");
            // printWriter.flush();

            // Paso 1: Enviar datos del episodio
            printWriter.println(episode.getPatient_id());

            //System.out.println("connect patient--> episode.getDate().toString())"+ episode.getDate().toString());            
            printWriter.println(episode.getDate().toString());

            // Paso 2: Enviar enfermedades asociadas
            for (String disease : diseases) {

                printWriter.println("DISEASE|" + disease);
            }

            // Paso 3: Enviar síntomas asociados
            for (String symptom : symptoms) {
                //System.out.println(symptoms);
                printWriter.println("SYMPTOM|" + symptom);
            }

            // Paso 4: Enviar cirugías asociadas
            for (String surgery : surgeries) {
                printWriter.println("SURGERY|" + surgery);
            }

            // Paso 5: Enviar grabaciones asociadas
            for (Recording recording : recordings) {
                StringBuilder dataBuilder = new StringBuilder();
                for (Integer dataPoint : recording.getData()) {
                    dataBuilder.append(dataPoint).append(","); // Agrega cada dato seguido de una coma
                }
                // Elimina la última coma si existe
                if (dataBuilder.length() > 0) {
                    dataBuilder.setLength(dataBuilder.length() - 1);
                }
                String data = dataBuilder.toString(); // Convierte StringBuilder en cadena

                printWriter.println("RECORDING|" + recording.getType().toString() + "|"
                        + recording.getDate().toString() + "|"
                        + recording.getSignal_path()
                        + "|" + data);

                // Enviar datos de la señal
                /*for (Integer dataPoint : recording.getData()) {
                    printWriter.println(String.valueOf(dataPoint));
                }
                printWriter.println("END_OF_RECORDING_DATA");*/
                printWriter.flush();
            }
            // Indicar fin del episodio
            printWriter.println("END_OF_EPISODE");
            printWriter.flush();
            // Leer la respuesta del servidor            
            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error handling episode: " + e.getMessage());
            return false;
        }
        /*finally {
            closeConnection();
        }*/
    }
}
/*public static Episode viewPatientEpisode(Integer episode_id) {
        List<Surgery> surgeries = new ArrayList<>();
        List<Symptom> symptoms = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        List<Recording> recordings = new ArrayList<>();
        // PENSAR COMO MOSTRAR RECORDING SIGNALPATH??
        Episode episode = null;

        try {
            connectToServer();
            printWriter.println("VIEW_PATIENT_EPISODE");
            printWriter.println(String.valueOf(episode_id));

            String dataString;
            while (!(dataString = bufferedReader.readLine()).equals("END_OF_LIST")) {
                String[] parts = dataString.split(",");

                if (parts.length == 2) {
                    String type = parts[0];
                    String data = parts[1]; // Tipo de dato: SURGERY, SYMPTOM, DISEASE
                    episode = new Episode();

                    switch (type) {
                        case "SURGERY":
                            Surgery surgery = new Surgery();
                            surgery.setType(data);
                            surgeries.add(surgery);
                            episode.setSurgeries((ArrayList<Surgery>) surgeries);
                            break;

                        case "SYMPTOM":
                            Symptom symptom = new Symptom();
                            symptom.setType(data);
                            symptoms.add(symptom);
                            episode.setSymptoms((ArrayList<Symptom>) symptoms);
                            break;

                        case "DISEASE":
                            Disease disease = new Disease();
                            disease.setDisease(data);
                            diseases.add(disease);
                            episode.setDiseases((ArrayList<Disease>) diseases);
                            break;
                        /* case "RECORDING":
                            // SignalPath: NameSurname_Date(hour)_Type
                            Recording recording = new Recording();
                            recording.setSignal_path(data);
                            recordings.add(recording);
                            episode.setRecordings((ArrayList<Recording>) recordings);
                            break;
                        default:
                            System.out.println("Unknown data type: " + type);
                            break;
                    }

                    return episode;
                } else {
                    System.out.println("Invalid data format received: " + dataString);
                    return null;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerrar la conexión al servidor
        }
        return null;
    }
 */
