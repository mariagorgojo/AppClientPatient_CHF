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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Disease;
import pojos.Doctor;
import pojos.Episode;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Recording;
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
            socket = new Socket("localhost", 9001); // Cambia localhost y puerto según sea necesario
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    // Método para cerrar la conexión al servidor
    private static void closeConnection() {
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
            printWriter.println(patient.getDni());
            printWriter.println(password); // Enviar la contraseña
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

            if (parts.length == 7) {

                patient = new Patient();
                patient.setDni(parts[0]);
                patient.setName(parts[1]);
                patient.setSurname(parts[2]);
                patient.setEmail(parts[3]);
                patient.setGender(Gender.valueOf(parts[4].toUpperCase()));
                patient.setPhoneNumber(Integer.parseInt(parts[5])); // Convertir a entero
                patient.setDob(LocalDate.parse(parts[6], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                /*String[] doctorParts = patientPart
                /[7].split(","); // Dividir directamente por comas

                    Doctor doctor = new Doctor();
                    doctor.setDni(doctorParts[0]);
                    doctor.setName(doctorParts[1]);
                    doctor.setSurname(doctorParts[2]);
                    doctor.setTelephone(Integer.parseInt(doctorParts[3])); // Convertir teléfono a entero
                    doctor.setEmail(doctorParts[4]);
                    patient.setDoctor(doctor);*/
                return patient;
            } else {
                System.out.println("Invalid data format received from server.");
                return null;
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerrar la conexión al servidor
        }
        return null;
    }

    public static Episode viewPatientEpisode(Integer episode_id) {
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
                         */
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
}
