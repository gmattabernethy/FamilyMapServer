package DAO;

import model.Event;
import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private String connectionUrl;
    public EventDAO(){
        connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=FamilyMap Database Schema;>";
    }

    public Event getEvent(String eventID){
        String sql = "SELECT *  FROM Event where EventID=?";
        Event event = new Event();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                event.setEventID(rs.getString("EventID"));
                event.setDescendant(rs.getString("Descendant"));
                event.setPersonID(rs.getString("PersonID"));
                event.setLatitude(rs.getDouble("Latitude"));
                event.setLongitude(rs.getDouble("Longitude"));
                event.setCountry(rs.getString("Country"));
                event.setCity(rs.getString("City"));
                event.setEventType(rs.getString("EventType"));
                event.setYear(rs.getInt("Year"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return event;
    }

    public Event getEvent(String personID, String eventType){
        String sql = "SELECT *  FROM Event where PersonID=? AND EventType =?";
        Event event = new Event();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, personID);
            stmt.setString(2, eventType);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                event.setEventID(rs.getString("EventID"));
                event.setDescendant(rs.getString("Descendant"));
                event.setPersonID(rs.getString("PersonID"));
                event.setLatitude(rs.getDouble("Latitude"));
                event.setLongitude(rs.getDouble("Longitude"));
                event.setCountry(rs.getString("Country"));
                event.setCity(rs.getString("City"));
                event.setEventType(rs.getString("EventType"));
                event.setYear(rs.getInt("Year"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return event;
    }

    public List<Event> getAllEvents(String personID){
        String sql = "SELECT *  FROM Event where PersonID=?";
        List<Event> events = new ArrayList<>();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, personID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                Array eventIDs = rs.getArray("EventID");
                Array descendants = rs.getArray("Descendant");
                Array personIDs = rs.getArray("PersonID");
                Array latitudes = rs.getArray("Latitude");
                Array longitudes= rs.getArray("Longitude");
                Array countries = rs.getArray("Country");
                Array cities = rs.getArray("City");
                Array eventTypes = rs.getArray("EventType");
                Array years = rs.getArray("Year");

                for(int i = 0; i < ((String[])eventIDs.getArray()).length; i++){
                    Event event = new Event();

                    event.setEventID(((String[])eventIDs.getArray())[i]);
                    event.setDescendant(((String[])descendants.getArray())[i]);
                    event.setPersonID(((String[])personIDs.getArray())[i]);
                    event.setLatitude(((double[])latitudes.getArray())[i]);
                    event.setLongitude(((double[])longitudes.getArray())[i]);
                    event.setCountry(((String[])countries.getArray())[i]);
                    event.setCity(((String[])cities.getArray())[i]);
                    event.setEventType(((String[])eventTypes.getArray())[i]);
                    event.setYear(((int[])years.getArray())[i]);

                    events.add(event);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }

    public void addEvent(Event event){
        if (getEvent(event.getEventID()) != null) return;
        String sql = "INSERT INTO Event values (?,?,?,?,?,?,?,?,?)";
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
