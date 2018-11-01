package db;

import model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PersonDAOTest {
    Database db = new Database();
    PersonDAO DAO = new PersonDAO();

    @Before
    public void initialize() {
        db.clearDB();

        Person person = new Person();
        person.setPersonID("test");
        person.setDescendant("user");
        person.setFirstName("first");
        person.setLastName("last");
        person.setGender('X');

        DAO.addPerson(person);
    }

    @Test
    public void getPerson_good() {
        Person person = DAO.getPerson("test");
        assertNotNull(person);
        assert  person instanceof Person;
    }

    @Test
    public void getPerson_bad() {
        Person person = DAO.getPerson("bogus");
        assertNull(person);
    }

    @Test
    public void getAllPeople_good() {
        Person person = new Person();
        person.setPersonID("newPerson");
        person.setDescendant("user");
        person.setFirstName("first");
        person.setLastName("last");
        person.setGender('X');

        DAO.addPerson(person);

        List<Person> people = DAO.getAllPeople("user");

        assertNotNull(people);
        assertEquals(2, people.size());
    }

    @Test
    public void getAllPeople_bad() {
        db.clearDB();

        List<Person> people = DAO.getAllPeople("user");

        assertEquals(0, people.size());
    }

    @Test
    public void addPerson_good() {
        db.clearDB();

        Person person = new Person();
        person.setPersonID("newPerson");
        person.setDescendant("user");
        person.setFirstName("first");
        person.setLastName("last");
        person.setGender('X');

        DAO.addPerson(person);

        assertNotNull(DAO.getPerson("newPerson"));
    }

    @Test
    public void addPerson_existing() {
        Person person = new Person();
        person.setPersonID("newPerson");
        person.setDescendant("user");
        person.setFirstName("first");
        person.setLastName("last");
        person.setGender('X');

        DAO.addPerson(person);

        assertNotNull(DAO.getPerson("newPerson"));
    }

    @After
    public void cleanUp(){
        db.clearDB();
    }
}