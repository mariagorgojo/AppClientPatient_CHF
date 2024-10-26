/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.time.LocalDate;
import java.util.ArrayList;

// should implement Serializable

class SignalsBitalino {
    
    private Integer id;
    private String patient_id;
    private Type type;
    private String duration;
    private LocalDate date;
    private String signal_path;
    private ArrayList<Integer> data;
    private ArrayList<Symptoms> symptoms;
    
    
    // constructors
    public SignalsBitalino(){
        
    }
    
    //getters and setters
    
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id=id;
    }
    
    public String getPatientId(){
        return patient_id;
    }
    
    public void setPatientId(String patient_id){
        this.patient_id=patient_id;
    }
    
    public Type getType(){
        return type;
    }
    
    public void setType(Type type){
        this.type=type;
    }
    
    public String getDuration(){
        return duration;
    }
    
    public void setDuration(String duration){
        this.duration=duration;
    }
    
    public LocalDate getDate(){
        return date;
    }
    
    public void setDate(LocalDate date){
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
    
        public ArrayList<Symptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }
}
