/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.util.ArrayList;

public class Doctor {
    
    // no estan todos los atributos RW
        private Integer id;
    	private final String dni;
        private String password;
	private String name;
        private String surname;
	private Integer telephone;
        private String email;
        private ArrayList<Patient> patients;

    
    public Doctor(String dni, String password, String name, String surname, Integer telephone, String email) {
        this.dni = dni;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        patients = new ArrayList<>();
    }
    
        public Doctor(Integer id, String dni, String password, String name, String surname, Integer telephone, String email) {
        this.id= id;
        this.dni = dni;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        patients = new ArrayList<>();
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }
    
    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s", dni, name, surname, String.valueOf(telephone), email);
    }
  
    
}
