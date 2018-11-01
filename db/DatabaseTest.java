package db;

import model.Person;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseTest {
    Database db = new Database();
    PersonDAO pDAO = new PersonDAO();

    @Before
    public void initialize(){
        db.clearDB();

        Person person = new Person();
        person.setDescendant("test");

        pDAO.addPerson(person);
    }

    @Test
    public void connect_valid() {
        assertNotNull(db.connect());
        assert db.connect() instanceof Connection;
    }

    @Test
    public void connect_open() {
        try {
            assertFalse(db.connect().isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTree_good() {
        db.deleteTree("test");
        assertEquals(0,pDAO.getAllPeople("test").size());
    }

    @Test
    public void deleteTree_bad() {

        initialize();

        db.deleteTree("bogus");
        assertEquals(1,pDAO.getAllPeople("test").size());
    }

    @Test
    public void clearDB_full() {
        initialize();

        db.clearDB();
        assertEquals(0,pDAO.getAllPeople("test").size());
    }

    @Test
    public void clearDB_empty() {
        db.clearDB();
        assertEquals(0,pDAO.getAllPeople("test").size());
    }
}