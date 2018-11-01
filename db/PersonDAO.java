package db;

import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private String connectionUrl;

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
                person.setfName(rs.getString("FirstName"));
                person.setlName(rs.getString("LastName"));
                person.setGender(rs.getString("Gender").charAt(0));
                person.setFatherID(rs.getString("FatherID"));
                person.setMotherID(rs.getString("MotherID"));
                person.setSpouseID(rs.getString("SpouseID"));

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

    public static void main(String[] args){
        PersonDAO DAO = new PersonDAO();

        Person person = new Person();
        person.setPersonID("1235");
        person.setDescendant("matt");

        DAO.addPerson(person);
        System.out.println(DAO.getPerson("1235").getDescendant());
    }
}
