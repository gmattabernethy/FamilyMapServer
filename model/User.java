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
     * @param userName the user's username
     * @param password the user's password
     * @param email the user's email address
     * @param fName the user's first name
     * @param lName the user's last name
     * @param gender the user's gender: 'f' or 'm'
     * @param personID the ID of the person associated with the user
     */

    public User(String userName, String password, String email, String fName, String lName, char gender, String personID){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.personID = personID;
    }

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
}
