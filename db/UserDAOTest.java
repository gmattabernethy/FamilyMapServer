package db;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDAOTest {
    Database db = new Database();
    UserDAO DAO = new UserDAO();

    @Before
    public void initialize() {
        db.clearDB();

        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('X');
        user.setPersonID("person");

        DAO.addUser(user);
    }

    @Test
    public void userAvailable_good() {
        assertTrue(DAO.userAvailable("unused"));
    }

    @Test
    public void userAvailable_bad() {
        assertFalse(DAO.userAvailable("test"));
    }

    @Test
    public void getUser_username_good() {
        User user = DAO.getUser("test");
        assertNotNull(user);
    }

    @Test
    public void getUser_username_bad() {
        User user = DAO.getUser("bogus");
        assertNull(user);
    }

    @Test
    public void getUser_username_password_good() {
        User user = DAO.getUser("test", "password");
        assertNotNull(user);
    }

    @Test
    public void getUser_username_password_bad() {
        User user = DAO.getUser("test", "wrong");
        assertNull(user);
    }


    @Test
    public void addUser_good() {
        db.clearDB();

        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('X');
        user.setPersonID("person");

        DAO.addUser(user);

        assertNotNull(DAO.getUser("test"));
    }

    @Test
    public void addUser_existing() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('X');
        user.setPersonID("person");

        DAO.addUser(user);

        assertNotNull(DAO.getUser("test"));
    }

    @After
    public void cleanUp(){
        db.clearDB();
    }
}