package roomescape;

public class Time {
    private long id;
    private String time;

    // Constructor
    public Time(long id, String time) {
        this.id = id;
        this.time = time;
    }

    // Default constructor for deserialization
    public Time() {
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
