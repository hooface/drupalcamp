package be.drupalcamp.leuven;

public class Session {

    int id;
    String title;
    String description = "";
    int special;
    int startDate;
    int endDate;
    int level = 0;
    int day = 0;
    // @todo tracks

    // Empty constructor.
    public Session() {

    }

    // Full constructor.
    public Session(int id, String title, String description, int special, int startDate, int endDate, int level, int day) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.special = special;
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
