package pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Recording {

    private int id;
    private Type type;
    private final int duration = 60;
    private LocalDateTime date;
    private String signal_path;
    private ArrayList<Integer> data;
    private Integer episode_id;

    // Default constructor
    public Recording() {
        this.data = new ArrayList<>();
    }

    // Complete constructor
    public Recording(Integer id, Type type, LocalDateTime date, String signal_path, ArrayList<Integer> data, Integer episode_id) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.signal_path = signal_path;
        this.data = data != null ? data : new ArrayList<>();
        this.episode_id = episode_id;
    }

    // Constructor without ID
    public Recording(Type type, LocalDateTime date, String signal_path, ArrayList<Integer> data, Integer episode_id) {
        this.type = type;
        this.date = date;
        this.signal_path = signal_path;
        this.data = data != null ? data : new ArrayList<>();
        this.episode_id = episode_id;
    }

    // Constructor add recording
    public Recording(Type type, LocalDateTime date, String signal_path, ArrayList<Integer> data) {
        this.type = type;
        this.date = date;
        this.signal_path = signal_path;
        this.data = data != null ? data : new ArrayList<>();
    }

    // Getters and setters
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
        System.out.println("Setting data: " + (data != null ? data.toString() : "null"));
        this.data = data != null ? data : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +
                ", type=" + type +
                ", duration=" + duration +
                ", date=" + date +
                ", signal_path='" + signal_path + '\'' +
                ", data=" + data +
                ", episode_id=" + episode_id +
                '}';
    }

    // Enum recording types
    public enum Type {
        ECG,
        EMG
    }
}