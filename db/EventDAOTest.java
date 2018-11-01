package db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.Event;
import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class EventDAOTest {
    Database db = new Database();
    EventDAO DAO = new EventDAO();

    @Before
    public void initialize() {
        db.clearDB();

        Event event = new Event();
        event.setEventID("test");
        event.setDescendant("user");
        event.setPersonID("person");
        event.setLatitude(0);
        event.setLongitude(0);
        event.setCountry("USA");
        event.setCity("Provo");
        event.setEventType("type");
        event.setYear(2018);

        DAO.addEvent(event);
    }

    @Test
    public void getEvent_good() {
        assertEquals("user", DAO.getEvent("test").getDescendant());
    }

    @Test
    public void getEvent_bad() {
        assertNull(DAO.getEvent("bogus"));
    }

    @Test
    public void getAllEvents_good() {
        Event event = new Event();
        event.setEventID("newEvent");
        event.setDescendant("user");
        event.setPersonID("person");
        event.setLatitude(0);
        event.setLongitude(0);
        event.setCountry("USA");
        event.setCity("Provo");
        event.setEventType("type");
        event.setYear(2018);

        DAO.addEvent(event);

        List<Event> events = DAO.getAllEvents("user");

        assertEquals(2, events.size());
    }

    @Test
    public void getAllEvents_bad() {
        List<Event> events = DAO.getAllEvents("bogus");

        assertEquals( 0, events.size());
    }

    @Test
    public void addEvent() {
        db.clearDB();

        Event event = new Event();
        event.setEventID("newEvent");
        event.setDescendant("user");
        event.setPersonID("person");
        event.setLatitude(0);
        event.setLongitude(0);
        event.setCountry("USA");
        event.setCity("Provo");
        event.setEventType("type");
        event.setYear(2018);

        DAO.addEvent(event);

        assertNotNull(DAO.getEvent("newEvent"));
    }

    @Test
    public void addEvent_existing() {
        Event event = new Event();
        event.setEventID("newEvent");
        event.setDescendant("user");
        event.setPersonID("person");
        event.setLatitude(0);
        event.setLongitude(0);
        event.setCountry("USA");
        event.setCity("Provo");
        event.setEventType("type");
        event.setYear(2018);

        DAO.addEvent(event);

        assertNotNull(DAO.getEvent("newEvent"));
    }

    @After
    public void cleanUp(){
        db.clearDB();
    }
}