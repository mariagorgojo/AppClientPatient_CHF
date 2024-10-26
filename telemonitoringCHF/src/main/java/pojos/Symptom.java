/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author maria
 */

public class Symptom implements Serializable{
		
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

	public String getType() {
		return symptom;
	}

	public void setType(String symptom) {
		this.symptom = symptom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, symptom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symptom other = (Symptom) obj;
		return id == other.id && Objects.equals(symptom, other.symptom);
	}

	@Override
	public String toString() {
		return "Symptom: " + symptom;
	}
        
}

