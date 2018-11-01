package db;

import model.AuthToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    AuthTokenDAO DAO = new AuthTokenDAO();
    Database db = new Database();

    @Before public void initialize() {
        db.clearDB();

        AuthToken token = new AuthToken();
        token.setToken("testToken");
        token.setUserName("test");

        DAO.addAuthToken(token);
    }

    @Test
    public void validateAuthToken_good() {
        assertTrue(DAO.validateAuthToken("testToken"));
    }

    @Test
    public void validateAuthToken_bad() {
        assertFalse(DAO.validateAuthToken("bogus"));
    }

    @Test
    public void getAuthToken_good() {
        assert DAO.getAuthToken("testToken") instanceof AuthToken;
    }

    @Test
    public void getAuthToken_bad() {
        assertNull(DAO.getAuthToken("bogus"));
    }

    @Test
    public void addAuthToken_good(){
        AuthToken token = new AuthToken();
        token.setToken("newToken");
        token.setUserName("user");
        token.setPersonID("person");

        DAO.addAuthToken(token);

        assertEquals(token.getToken(),DAO.getAuthToken("newToken").getToken());
        assertEquals(token.getUserName(),DAO.getAuthToken("newToken").getUserName());
    }

    @Test
    public void addAuthToken_existing(){
        AuthToken token = new AuthToken();
        token.setToken("testToken");
        token.setUserName("test");

        DAO.addAuthToken(token);

        assertNotNull(DAO.getAuthToken("testToken"));
    }

    @After public void cleanUp(){
        db.clearDB();
    }

}