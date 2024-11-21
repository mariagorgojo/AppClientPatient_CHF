/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.time.LocalDate;
import java.util.ArrayList;


// should implement Serializable to establish the conncection with the server
public class Patient {
    
    private Integer id; //el automatico de la db
    private String dni;  
    private String name;
    private String surname;
    private String email;
    private Gender gender;
    private Integer phoneNumber; 
    private LocalDate dob;
    private Doctor doctor;
    private ArrayList <Episode> episodes;
    
    
    // constructors
    
    public Patient() {
	episodes = new ArrayList<>();
    }
    

    //constructor for registration

    public Patient(String dni, String name, String surname, String email, 
            Gender gender, Integer phoneNumber, LocalDate dob) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
	episodes = new ArrayList<>();
    }
    
    
  

    public Patient(Integer id) {
        this.id = id;
        episodes = new ArrayList<>();
    }

    public Patient(String dni) {
        this.dni = dni;
        episodes = new ArrayList<>();
    }
    
   /* public Patient(String email) {
   this.email = email;
    }
    
    public Patient(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    */

    public Patient(String name, String surname) {
    this.name = name;
    this.surname = surname;
    }
    

    //constructor with everything
    public Patient(Integer id, String dni, String name, String surname, 
            String email, Gender gender, Integer phoneNumber, LocalDate dob, 
            Doctor doctor, Episode episodes) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.doctor = doctor;
        this.episodes = new ArrayList<>();
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



    public ArrayList<Episode> getEpisodes() {
            return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
            this.episodes = episodes;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
public String toString() {
    return String.format("Patient Details:\n" +
                         "----------------------------\n" +
                         "DNI: %s\n" +
                         "Name: %s %s\n" +
                         "Email: %s\n" +
                         "Gender: %s\n" +
                         "Phone Number: %d\n" +
                         "Date of Birth: %s\n" +
                         "Assigned Doctor: %s\n" +
                         "----------------------------", 
                         dni, name, surname, email, gender, phoneNumber, dob, doctor);
}

    public String getTelephone() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

         
    public enum Gender {
    MALE,
    FEMALE
}
}
