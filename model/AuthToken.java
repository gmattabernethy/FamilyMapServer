package model;

public class AuthToken {
    private String token;
    private String username;

    /**
     * AuthToken constructor
     * @param token the token
     * @param username the username of the user the token is associated with
     */
    public AuthToken(String token, String username){
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
