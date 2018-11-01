import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FacadeTest {
    static private Facade facade = Facade.buildFacade();;
    private AuthTokenDAO authTokenAccess = new AuthTokenDAO();
    private EventDAO eventAccess = new EventDAO();
    private PersonDAO personAccess = new PersonDAO();
    private UserDAO userAccess = new UserDAO();
    private Database db = new Database();

    @Before
    public void initialize(){
        db.clearDB();

        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        userAccess.addUser(user);
    }

    @Test
    public void register_good() {
        db.clearDB();

        AuthToken token = facade.register("test", "password", "email", "first", "last", 'X');
        assertNotNull(token);
        assertNotNull(authTokenAccess.getAuthToken(token.getToken()));
    }

    @Test
    public void register_bad() {
        AuthToken token = facade.register("test", "password", "email", "first", "last", 'X');
        assertNull(token);
    }

    @Test
    public void login_good() {
        AuthToken token = facade.login("test", "password");
        assertNotNull(token);
        assertTrue(authTokenAccess.validateAuthToken(token.getToken()));
    }

    @Test
    public void login_bad() {
        assertNull(facade.login("bogus", "password"));
        assertNull(facade.login("test", "bogus"));
    }

    @Test
    public void clear_full() {
        facade.clear();
        assertNull(userAccess.getUser("test"));
    }

    @Test
    public void clear_empty() {
        db.clearDB();
        facade.clear();
        assertNull(userAccess.getUser("test"));
    }

    @Test
    public void fill_objects() {
        facade.fill("test", 3);
        assertEquals(15, facade.getFamily("test").size());
        assertEquals(57, facade.getFamilyEvents("test").size());
    }

    @Test
    public void fill_count() {
        int[] counts = facade.fill("test", 4);
        assertEquals(31, counts[0]);
        assertEquals(121, counts[1]);
    }

    @Test
    public void load_good() {
        String path = "json/example.json";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(reader, JsonObject.class);

            JsonArray users = obj.getAsJsonArray("users");
            JsonArray persons = obj.getAsJsonArray("persons");
            JsonArray events = obj.getAsJsonArray("events");

            int[] counts = facade.load(users, persons, events);

            assertEquals(1, counts[0]);
            assertEquals(3, counts[1]);
            assertEquals(2, counts[2]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load_bad() {
          assertNull(facade.load(null, null, null));
    }

    @Test
    public void userFromToken_good() {
        AuthToken token = new AuthToken();
        token.setUserName("test");
        token.setToken("token");
        authTokenAccess.addAuthToken(token);

        User user = facade.userFromToken("token");
        assertNotNull(user);
        assertEquals("test", user.getUserName());
    }

    @Test
    public void userFromToken_bad() {
        assertNull(facade.userFromToken("token"));
    }

    @Test
    public void getPerson_good() {
        Person person = new Person();
        person.setPersonID("test");
        person.setDescendant("test");
        personAccess.addPerson(person);

        Person p = facade.getPerson("test");
        assertNotNull(p);
        assertEquals("test", p.getDescendant());
    }

    @Test
    public void getPerson_bad() {
        Person p = facade.getPerson("test");
        assertNull(p);
    }

    @Test
    public void getFamily_good() {
        facade.register("newUser", null, null, null, null, 'X');
        List<Person> family = facade.getFamily("newUser");
        assertNotNull(family);
        assertEquals(31, family.size());
    }

    @Test
    public void getFamily_bad() {
        List<Person> family = facade.getFamily("bogus");
        assertEquals(0,family.size());

        facade.register("test", null, null, null, null, 'X');
        family = facade.getFamily("test");
        assertNotNull(family);
        assertEquals(0, family.size());
    }

    @Test
    public void getEvent_good() {
        Event event = new Event();
        event.setEventID("test");
        event.setYear(2018);
        eventAccess.addEvent(event);

        Event e = facade.getEvent("test");
        assertNotNull(e);
        assertEquals(2018, e.getYear());
    }

    @Test
    public void getEvent_bad() {
        Event e = facade.getEvent("test");
        assertNull(e);
    }

    @Test
    public void getFamilyEvents_good() {
        facade.register("newUser", null, null, null, null, 'X');
        List<Event> events = facade.getFamilyEvents("newUser");

        assertEquals(121, events.size());
    }

    @Test
    public void getFamilyEvents_bad() {
        List<Event> events = facade.getFamilyEvents("bogus");
        assertEquals(0, events.size());
    }

    @After
    public void cleanup(){
        db.clearDB();
    }
}