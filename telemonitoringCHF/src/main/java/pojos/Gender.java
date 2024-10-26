/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.io.Serializable;
import java.util.Objects;

public class Gender implements Serializable{
    
    private Integer id; //automatic db id
    private String gender;

	public Gender() {
		super();
	}

	public Gender(int id, String gender) {
		super();
		this.id = id;
		this.gender = gender;
	}
	
	public Gender(String gender) {
		super();
		this.gender = gender;
	}    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(id, gender);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gender other = (Gender) obj;
		return id == other.id && Objects.equals(gender, other.gender);
	}    
        
        @Override
	public String toString() {
		return "Gender: " + gender;
	}
}
