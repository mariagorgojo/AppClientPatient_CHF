/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Executable;
/*
import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.BitalinoDemo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Recording;*/

/**
 *
 * @author maria
 */



public class BitalinoTest {
/*
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
           // MAC MARIA 98:D3:51:FD:9C:72
           
             try {
          

                System.out.println("Recording Type (ECG/EMG, or type 'done' to finish): ");
                String typeInput = scanner.nextLine();
            
                Recording.Type signalType = Recording.Type.valueOf(typeInput.toUpperCase());

                int[] channelsToAcquire = BitalinoDemo.configureChannels(signalType);
                System.out.println("channel patient menu "+ channelsToAcquire);
                BITalino bitalino ;

                bitalino = new BITalino();
                int sample_rate = 1000;

                bitalino.open("98:D3:51:FD:9C:72", sample_rate);

                // date of the recording
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String recordingDate = LocalDateTime.now().format(formatter);
                LocalDateTime parserecordingDate = LocalDateTime.parse(recordingDate, formatter);

                // Empezamos la grabación:
                bitalino.start(channelsToAcquire); 

                String fileName = BitalinoDemo.generateFileName(signalType, recordingDate, "TESTDNI");
                ArrayList<Integer> data = BitalinoDemo.recordAndSaveData(bitalino, signalType, fileName, recordingDate, "TESTDNI");

                if (data == null || data.isEmpty()) {
                    System.out.println("Error: No data was captured. Please ensure the device is working properly.");
               
                } else {
                    
                    Recording recording = new Recording(signalType, parserecordingDate, fileName, data);
                    System.out.println("The recording has successfully ended.");                    
                }
                
            } catch (BITalinoException ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    */
    }
    



