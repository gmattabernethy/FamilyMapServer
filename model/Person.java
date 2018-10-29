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
     * @param personID the ID of the person
     * @param descendant the username of the user to which the person belongs
     * @param fName the person's first name
     * @param lName the person's last name
     * @param gender the person's gender: 'f' or 'm'
     */

    public Person(String personID, String descendant, String fName, String lName, char gender){
        this.personID = personID;
        this.descendant = descendant;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
    }

    public String getID(){
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

    public void setMotherID(String motherID){this.motherID = motherID;}

    public void setFatherID(String fatherID) {this.fatherID = fatherID;}

    public void setSpouseID(String spouseID) {this.spouseID = spouseID;}
}
