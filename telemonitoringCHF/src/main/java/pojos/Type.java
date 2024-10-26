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
public class Type implements Serializable{
		
	private int id;
	private String type; //ECG or EMG
	
	public Type() {
		super();
	}

	public Type(int id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	
	public Type(String type) {
		super();
		this.type = type;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		return id == other.id && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Type: " + type;
	}
	
}

