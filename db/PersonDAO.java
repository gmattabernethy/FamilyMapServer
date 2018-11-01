package db;

import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    public PersonDAO(){}

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
                person.setFirstName(rs.getString("FirstName"));
                person.setLastName(rs.getString("LastName"));
                person.setGender(rs.getString("Gender").charAt(0));
                person.setFather(rs.getString("FatherID"));
                person.setMother(rs.getString("MotherID"));
                person.setSpouse(rs.getString("SpouseID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return person;
    }

    public List<Person> getAllPeople(String username){
        String sql = "SELECT *  FROM Person where Descendant=?";
        List<Person> people = new ArrayList<>();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Person person = new Person();

                person.setPersonID(rs.getString("PersonID"));
                person.setDescendant(rs.getString("Descendant"));
                person.setFirstName(rs.getString("FirstName"));
                person.setLastName(rs.getString("LastName"));
                person.setGender(rs.getString("Gender").charAt(0));
                person.setFather(rs.getString("FatherID"));
                person.setMother(rs.getString("MotherID"));
                person.setSpouse(rs.getString("SpouseID"));

                people.add(person);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return people;
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
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, Character.toString(person.getGender()));
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
