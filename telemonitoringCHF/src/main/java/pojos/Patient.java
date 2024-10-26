/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.time.LocalDate;
import java.util.ArrayList;


// should implement Serializable to establish the conncection with the server
public class Patient {
    
    private String id;
    private String name;
    private String email;
    private Gender gender;
    private Integer telephone; 
    private LocalDate dob;
    private Doctor doctor; //??
    private MedHistory history; 
    private ArrayList <SignalsBitalino> signals;
    
    
    // constructors
    public Patient(){
        
    }
    
    // constructor when asking registration
    public Patient(String id, String name, String email,
            Gender gender, Integer telephone, LocalDate dob) {
		this.id = id;
		this.name = name;
		this.email = email;
                this.gender = gender;
                this.telephone = telephone;
		this.dob = dob;	
		this.history = history;	
		this.signals = new ArrayList<SignalsBitalino>();
    }
    
    public String getId() {
	return id;
    }


    public void setId(String id) {
	this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    

    public Gender getGender() {
        return gender;
    }


    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public Integer getTelephone() {
            return telephone;
    }


    public void setTelephone(Integer telephone) {
            this.telephone = telephone;
    }
    
    public LocalDate getDob() {
            return dob;
    }

    public void setDob(LocalDate dob) {
            this.dob = dob;
    }
    
    public Doctor getDoctor() {
            return doctor;
    }


    public void setDoctor(Doctor doctor) {
            this.doctor = doctor;
    }



    public MedHistory getMedHistory() {
            return history;
    }


    public void setMedHistory(MedHistory history) {
            this.history = history;
    }


    public ArrayList<SignalsBitalino> getSignalsBitalino() {
            return signals;
    }


    public void setSignalsBitalino(ArrayList<SignalsBitalino> signals) {
            this.signals = signals;
    }



         
  
    
}
