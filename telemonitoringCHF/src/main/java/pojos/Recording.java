/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

// should implement Serializable?

public class Recording {
    
    private int id;   
    private Type type;
    private final int duration=60;
    private LocalDateTime date;
    private String signal_path;
    private ArrayList<Integer> data;
    private Integer episode_id;
    
    // constructors
    public Recording(){            
        data = new ArrayList<>();
    }     
    
    //constructor everything
    public Recording(Integer id, Type type, LocalDateTime date, 
            String signal_path, ArrayList<Integer> data, Integer episode_id) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.episode_id=episode_id;
        this.signal_path = signal_path;
        this.data = data;
    }
    
    public Recording(Type type, LocalDateTime date, String signal_path, ArrayList<Integer> data, Integer episode_id) {
        this.type = type;
        this.date = date;
        this.signal_path = signal_path;
        this.data = data;
        this.episode_id = episode_id;
    }

      public Recording( Type type, LocalDateTime date, 
            String signal_path, ArrayList<Integer> data) {
        
        this.type = type;
        this.date = date;
        this.signal_path = signal_path;
        this.data = data;
    }
    public Recording(Integer id) {
        this.id = id;
    }
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(Integer episode_id) {
        this.episode_id = episode_id;
    }

    
    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "Recording{" + "type=" + type + ", duration=" + duration + 
                ", date=" + date + ", signal_path=" + signal_path + ", data=" 
                + data + '}';
    }

    
    public enum Type {
    ECG,
    EMG
}
}
