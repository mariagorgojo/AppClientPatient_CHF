/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

// should implement Serializable

public class SignalsBitalino implements Serializable{
    
    private Integer id;
    private Patient patient; //en las tablas de la db estará el id del patient (foreign key)
    private Type type;
    private Integer duration;
    private Date date;
    private String signal_path;
    private ArrayList<Integer> data;
    private ArrayList<Symptom> symptoms;
    
    
    // constructors
    public SignalsBitalino(){    
        super();
        data = new ArrayList<>();
        symptoms = new ArrayList<>();
    }
    
    //constructor wout automatic id
    public SignalsBitalino(Patient patient, Type type, Integer duration, Date date, String signal_path){    
        super();
        this.patient = patient;
        this.type = type;
        this.duration = duration;
        this.date = date;
        this.signal_path = signal_path;
        data = new ArrayList<>();
        symptoms = new ArrayList<>();
    }
    
    //constructor w everything
    public SignalsBitalino(Integer id, Patient patient, Type type, Integer duration, Date date, String signal_path){    
        super();
        this.id = id;
        this.patient = patient;
        this.type = type;
        this.duration = duration;
        this.date = date;
        this.signal_path = signal_path;
        data = new ArrayList<>();
        symptoms = new ArrayList<>();
    }

    //getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id){
        this.id=id;
    }
    
    public Patient getPatientId(){
        return patient;
    }
    
    public void setPatientId(Patient patient_id){
        this.patient=patient;
    }
    
    public Type getType(){
        return type;
    }
    
    public void setType(Type type){
        this.type=type;
    }
    
    public Integer getDuration(){
        return duration;
    }
    
    public void setDuration(Integer duration){
        this.duration=duration;
    }
    
    public Date getDate(){
        return date;
    }
    
    public void setDate(Date date){
        this.date=date;
    }
    
       public String getSignal_path() {
        return signal_path;
    }

    public void setSignal_path(String signal_path) {
        this.signal_path = signal_path;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }
    
        public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }
    
    // HACER TOSTRING !!!!!!!
}
