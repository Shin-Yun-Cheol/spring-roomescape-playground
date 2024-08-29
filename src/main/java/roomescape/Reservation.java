package roomescape;

public class Reservation {
    private long id;
    private String name;
    private String date;
    private Time time; // Time 객체로 수정

    // Constructor
    public Reservation(long id, String name, String date, Time time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    // Default constructor
    public Reservation() {
    }

    // Static factory method
    public static Reservation toEntity(Reservation reservation, long id) {
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Time getTime() { // Return Time object instead of String
        return time;
    }

    public void setTime(Time time) { // Accept Time object instead of String
        this.time = time;
    }
}
