package model;

public class AuthToken {
    private String token;
    private String userName;

    /**
     * AuthToken constructor
     */
    public AuthToken(){}

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserName(String username) {
        this.userName = username;
    }
}
