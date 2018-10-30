package model;

public class Event {
    /**
     * the event's ID
     */
    private String eventID;
    /**
     * the username of the user to which the event belongs
     */
    private String descendant;
    /**
     * the ID of the person the event is associated with
     */
    private String personID;
    /**
     * the latitude of the location the event occurred
     */
    private double latitude;
    /**
     * the longitude of the location the event occurred
     */
    private double longitude;
    /**
     * the country in which the event occurred
     */
    private String country;
    /**
     * the city in which the event occurred
     */
    private String city;
    /**
     * what the event was: wedding, birth, baptism, etc.
     */
    private String eventType;
    /**
     * the year when the event occurred
     */
    private int year;


    /**
     * Event constructor
     */

    public Event(){}

    public String getEventID() {
        return eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setYear(int year) {
        this.year = year;
    }

}

