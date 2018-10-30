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
        String sql = "SELECT * FROM Person where PersonID=?";
        Person person = new Person();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, personID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                person.setPersonID(rs.getString("PersonID"));
                person.setDescendant(rs.getString("Descendant"));
                person.setfName(rs.getString("FirstName"));
                person.setlName(rs.getString("LastName"));
                person.setGender(rs.getString("Gender").charAt(0));
                person.setFatherID(rs.getString("FatherID"));
                person.setMotherID(rs.getString("MotherID"));
                person.setSpouseID(rs.getString("SpouseID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return person;
    }

    public void addPerson(Person person){
        if (getPerson(person.getPersonID()) != null) return;
        String sql = "INSERT INTO Person values (?,?,?,?,?,?,?,?)";
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getfName());
            stmt.setString(4, person.getlName());
            stmt.setString(5, Character.toString(person.getGender()));
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
