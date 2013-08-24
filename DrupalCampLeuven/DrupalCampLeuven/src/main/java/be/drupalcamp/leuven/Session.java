package be.drupalcamp.leuven;

import java.util.List;

public class Session {

    int id;
    String title;
    String description = "";
    int special;
    int startDate;
    int endDate;
    int level = 0;
    int day = 0;
    List<Speaker> speakerList;

    // @todo tracks

    // Empty constructor.
    public Session() {

    }

    // Full constructor.
    public Session(int id, String title, String description, int special, int startDate, int endDate, int level, int day, List<Speaker> speakerList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.special = special;
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.day = day;
        this.speakerList = speakerList;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpecial() {
        return this.special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getStartDate() {
        return this.startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return this.endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<Speaker> getSpeakers() {
        return this.speakerList;
    }

}
