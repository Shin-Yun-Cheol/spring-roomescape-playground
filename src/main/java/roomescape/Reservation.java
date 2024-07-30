package roomescape;

public class Reservation {
    private final Long id;
    private String name;
    private String date;
    private String time;

    public Reservation(Long id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public static Reservation toEntity(Reservation member, Long id) {
        return new Reservation(id, member.name, member.date, member.time);
    }

    public void update(Reservation newMember) {
        this.name = newMember.name;
        this.date = newMember.date;
        this.time = newMember.time;
    }
}

