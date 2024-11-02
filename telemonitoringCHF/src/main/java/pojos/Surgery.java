/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;


/**
 *
 * @author mariagorgojo
 */
public class Surgery {
    
        private int id;
        private String surgery;

	public Surgery() {
            
        }

	public Surgery(int id, String surgery) {
		this.id = id;
		this.surgery = surgery;
	}
	
	public Surgery(String surgery) {		
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
	public String toString() {
		return "Surgery: " + surgery + "]";
	}
    
    
    
}
