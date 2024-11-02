package pojos;

import java.time.LocalDate;
import java.util.ArrayList;


public class Episodes {
    private Integer id;
    private Integer patient_id; 
    private LocalDate date; // last modification 
    
    // 1-n relations
    private ArrayList <Surgery> surgeries;
    private ArrayList <Disease> diseases;
    private ArrayList<Recording> recordings; 
    private ArrayList<Symptom> symptoms; 

    public Episodes() {
        this.surgeries = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.recordings = new ArrayList<>();
        this.symptoms = new ArrayList<>();
    }
        public Episodes(Integer id) {
        this.id=id;
        this.surgeries = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.recordings = new ArrayList<>();
        this.symptoms = new ArrayList<>();
    }
        public Episodes(Integer patient_id, LocalDate date) {
        this.patient_id=patient_id;
        this.date=date;
        this.surgeries = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.recordings = new ArrayList<>();
        this.symptoms = new ArrayList<>();
    }

      public Episodes(Integer id, LocalDate date,Integer patient_id) {
        this.id = id;
        this.date = date;
        this.patient_id = patient_id;
        this.surgeries = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.recordings = new ArrayList<>();
        this.symptoms = new ArrayList<>();
    }
    
        public Episodes( LocalDate date,Integer patient_id) {
       
        this.date = date;
        this.patient_id = patient_id;
        this.surgeries = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.recordings = new ArrayList<>();
        this.symptoms = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Surgery> getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(ArrayList<Surgery> surgeries) {
        this.surgeries = surgeries;
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    public ArrayList<Recording> getRecording() {
        return recordings;
    }

    public void setRecording(ArrayList<Recording> recordings) {
        this.recordings = recordings;
    }

    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "Episodes{" + "id=" + id + ", patient_id=" + patient_id + 
                ", date=" + date + ", surgeries=" + surgeries + ", diseases=" 
                + diseases + ", Bitalino Recordings=" + recordings + 
                ", symptoms=" + symptoms + '}';
    }
    
   
    
    
}
