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
     * @param eventID the event's ID
     * @param descendant the username of the user to which the event belongs
     * @param personID the ID of the person the event is associated with
     * @param latitude the latitude of the location the event occurred
     * @param longitude the longitude of the location the event occurred
     * @param country the country in which the event occurred
     * @param city the city in which the event occurred
     * @param eventType what the event was: wedding, birth, baptism, etc.
     * @param year the year when the event occurred
     */

    public Event(String eventID, String descendant, String personID, double latitude, double longitude, String country, String city, String eventType, int year){
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

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
}

