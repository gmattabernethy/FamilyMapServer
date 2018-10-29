package DAO;

import model.Person;
import model.User;

import java.sql.*;

public class PersonDAO {
    private String connectionUrl;

    public PersonDAO(){
        connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=FamilyMap Database Schema;>";
    }

    public Person getPerson(String personID){
        /*
        select * from person
        where PersonID = personID

         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "select * from Person where PersonID = " + personID;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                Person person = new Person(rs.getString(0),rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4).charAt(0));
                person.setFatherID(rs.getString(5));
                person.setMotherID(rs.getString(6));
                person.setSpouseID(rs.getString(7));
                return person;

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPerson(Person person){
        /*
        insert into Person
        values (person.getPersonID(), person.getDescendant(), person.getfName(), person.getlname(), person.getGender(),
        person.getFatherID(), person.getMotherID(), person.getSpouseID());
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "insert into Person values (" + person.getID() + "," + person.getDescendant() +  "," + person.getfName() + "," + person.getlName() +
                    "," + person.getGender() +  "," + person.getFatherID() +  "," + person.getMotherID() +  "," + person.getSpouseID() + ")";
            stmt.executeQuery(SQL);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
