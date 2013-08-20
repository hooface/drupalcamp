package be.drupalcamp.leuven;

public class Speaker {

    int id;
    int sessionId;
    String username;
    String firstName;
    String lastName;
    String organisation;
    String twitter;
    String avatar;

    public Speaker() {
    }

    public Speaker(int id, int sessionId, String username, String firstName, String lastName, String organisation, String twitter, String avatar) {
        this.id = id;
        this.sessionId = sessionId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.twitter = twitter;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
