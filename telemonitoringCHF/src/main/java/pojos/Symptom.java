/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

/**
 *
 * @author maria
 */
public class Symptom {

    private int id;
    private String symptom;

    public Symptom() {
        super();
    }

    public Symptom(int id, String symptom) {
        super();
        this.id = id;
        this.symptom = symptom;
    }

    public Symptom(String symptom) {
        super();
        this.symptom = symptom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Symptom{ '" + symptom + "'}";
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

}
