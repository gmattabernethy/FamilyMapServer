package db;

import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public EventDAO(){}

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

    public List<Event> getAllEvents(String username){
        String sql = "SELECT *  FROM Event where Descendant=?";
        List<Event> events = new ArrayList<>();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Event event = new Event();

                event.setEventID(rs.getString("EventID"));
                event.setDescendant(rs.getString("Descendant"));
                event.setPersonID(rs.getString("PersonID"));
                event.setLatitude(rs.getDouble("Latitude"));
                event.setLongitude(rs.getDouble("Longitude"));
                event.setCountry(rs.getString("Country"));
                event.setCity(rs.getString("City"));
                event.setEventType(rs.getString("EventType"));
                event.setYear(rs.getInt("Year"));

                events.add(event);
            }
            conn.close();
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
