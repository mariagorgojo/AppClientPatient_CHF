/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mariagorgojo
 */
public class Surgery implements Serializable{
    
        private int id;
        private String surgery;

	public Surgery() {
		super();
	}

	public Surgery(int id, String surgery) {
		super();
		this.id = id;
		this.surgery = surgery;
	}
	
	public Surgery(String surgery) {
		super();
		this.surgery = surgery;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return surgery;
	}

	public void setType(String surgery) {
		this.surgery = surgery;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, surgery);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Surgery other = (Surgery) obj;
		return id == other.id && Objects.equals(surgery, other.surgery);
	}

	@Override
	public String toString() {
		return "Surgery [id=" + id + ", surgery=" + surgery + "]";
	}
    
    
    
}
