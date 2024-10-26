/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


// should implement Serializable to establish the conncection with the server
public class Patient implements Serializable{
    
    private Integer id; //el automatico de la db
    
    //para iniciar sesion en la app
    private String dni;
    private String password;
    
    // DUDA: gender y dob final???
    
    private String name;
    private String surname;
    private String email;
    private Gender gender;
    private Integer phoneNumber; 
    private Date dob;
    private Doctor doctor;
    private MedHistory history; 
    private ArrayList <SignalsBitalino> signals;
    
    
    // constructors
    
    public Patient() {
        super();
	signals = new ArrayList<>();
    }

    //constructor for registration
    //no dni and password because they have already signed up
    public Patient(String name, String surname, String email, Gender gender, Integer phoneNumber, Date dob, Doctor doctor, MedHistory history) {
        super();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.doctor = doctor;
        this.history = history;
        signals = new ArrayList<>();
    }
    
    //everything but automatic id
    public Patient(String dni, String password, String name, String surname, String email, Gender gender, Integer phoneNumber, Date dob, Doctor doctor, MedHistory history) {
        super();
        this.dni = dni;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.doctor = doctor;
        this.history = history;
        signals = new ArrayList<>();
    }

    //constructor with everything
    public Patient(Integer id, String dni, String password, String name, String surname, String email, Gender gender, Integer phoneNumber, Date dob, Doctor doctor, MedHistory history) {
        super();
        this.id = id;
        this.dni = dni;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.doctor = doctor;
        this.history = history;
        signals = new ArrayList<>();
    }    
    
    public Integer getId() {
	return id;
    }


    public void setId(Integer id) {
	this.id = id;
    }

    public String getDNI() {
        return dni;
    }


    public void setDNI(String dni) {
        this.dni = dni;
    }
    
    public String getPassword() {
        return password;
    }


    public void setPassword(String psw) {
        this.password = psw;
    }
    
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname = surname;
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
    
    public Integer getPhoneNumber() {
            return phoneNumber;
    }


    public void setPhoneNumber(Integer phoneNumber) {
            this.phoneNumber = phoneNumber;
    }
    
    public Date getDob() {
            return dob;
    }

    public void setDob(Date dob) {
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

//sobra?
    public void setSignalsBitalino(ArrayList<SignalsBitalino> signals) {
            this.signals = signals;
    }



         // HACER TOSTRING PATIENT !!!!!!!
  
    
}
