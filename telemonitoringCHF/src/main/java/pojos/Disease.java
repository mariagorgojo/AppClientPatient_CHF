/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;


public class Disease {
    
    
    private int id; 
    private String disease; 

    
    public Disease(int id, String disease) {
        this.id = id;
        this.disease = disease;
    }
        public Disease( String disease) {        
        this.disease = disease;
    }

    public Disease() {
    }
    
        
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    
    @Override
    public String toString() {
        return "Disease{ '" + disease + "'}";
    }
}
    
