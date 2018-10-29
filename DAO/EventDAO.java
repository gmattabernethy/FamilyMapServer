package DAO;

import model.Event;
import model.Person;

import java.sql.*;

public class EventDAO {
    private String connectionUrl;
    public EventDAO(){
        connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=FamilyMap Database Schema;>";
    }

    public Event getEvent(String eventID){
        /*
        select * from Event
        where EventID = eventID
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "select * from Event where EventID = " + eventID;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                return new Event(rs.getString(0),rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3)),
                        Integer.parseInt(rs.getString(4)), rs.getString(5), rs.getString(6), rs.getString(7), Integer.parseInt(rs.getString(8)));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addEvent(Event event){
        /*
        insert into Event
        values (event.getEventID(), event.getDescendant(), event.getPersonID(), event.getLatitude(), event.getlongitude(),
        event.getCountry(), event.getCity(), event.getEventType(), event.getYear());
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "insert into Event values (" + event.getEventID() + "," + event.getDescendant() +  "," + event.getPersonID() + "," + event.getLatitude() +
                    "," + event.getLongitude() +  "," + event.getCountry() +  "," + event.getCity() +  "," + event.getEventType() + "," + event.getYear() + ")";
            stmt.executeQuery(SQL);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Event getByPersonID(String personID){
        /*
        select * from Event
        where PersonID = personID
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "select * from Event where PersonID = " + personID;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                return new Event(rs.getString(0),rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3)),
                        Integer.parseInt(rs.getString(4)), rs.getString(5), rs.getString(6), rs.getString(7), Integer.parseInt(rs.getString(8)));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
