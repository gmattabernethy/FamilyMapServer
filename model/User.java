package model;

public class User {
    /**
     * the user's username
     */
    private String userName;
    /**
     * the user's password
     */
    private String password;
    /**
     * the user's email address
     */
    private String email;
    /**
     * the user's first name
     */
    private String fName;
    /**
     * the user's last name
     */
    private String lName;
    /**
     * the user's gender: 'f' or 'm'
     */
    private char gender;
    /**
     * the ID of the person associated with the user
     */
    private String personID;

    /**
     * User constructer
     */

    public User(){}

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public char getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
