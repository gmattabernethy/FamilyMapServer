package model;

public class Person {
    /**
     * the ID of the person
     */
    private String personID;
    /**
     * the username of the user to which the person belongs
     */
    private String descendant;
    /**
     * the person's first name
     */
    private String fName;
    /**
     * the person's last name
     */
    private String lName;
    /**
     * the person's gender: 'f' or 'm'
     */
    private char gender;
    /**
     * the ID of the person's mother
     */
    private String motherID;
    /**
     * the ID of the person's father
     */
    private String fatherID;
    /**
     * the ID of the person's spouse
     */
    private String spouseID;



    /**
     * Person constructor
     */

    public Person(){}

    public String getPersonID(){
        return personID;
    }

    public String getDescendant(){
        return descendant;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public char getGender(){
        return gender;
    }

    public String getMotherID(){
        return motherID;
    }

    public String getFatherID(){
        return fatherID;
    }

    public String getSpouseID(){
        return spouseID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
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

    public void setMotherID(String motherID){this.motherID = motherID;}

    public void setFatherID(String fatherID) {this.fatherID = fatherID;}

    public void setSpouseID(String spouseID) {this.spouseID = spouseID;}
}
