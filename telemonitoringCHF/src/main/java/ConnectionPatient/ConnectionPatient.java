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
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Patient;


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
        } /*finally {
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
        } /*finally {
            printWriter.println("STOP");
            closeConnection(); // Cerramos la conexión
        }*/
    }


    // CAMBIAR PATIENT
    
public static Patient viewPatientDetails(String doctorDni) {
        try {
            connectToServer();
            printWriter.println("VIEW_DOCTOR_DETAILS");
            printWriter.println(doctorDni);

            String doctorString = bufferedReader.readLine();

            String[] parts = doctorString.split(",");

            if (parts.length == 5) {
                // INFORMACION PATIENT
                /*Doctor doctor = new Doctor();
                doctor.setDni(parts[0]);
                doctor.setName(parts[1]);
                doctor.setSurname(parts[2]);
                doctor.setTelephone(Integer.parseInt(parts[3]));
                doctor.setEmail(parts[4]);

                return doctor;*/
            } else {
                System.out.println("Invalid data format received from server.");
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionPatient.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // printWriter.println("STOP");
            closeConnection(); // correct?
        }
        return null;
    }


}

    

