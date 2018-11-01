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
    private String firstName;
    /**
     * the person's last name
     */
    private String lastName;
    /**
     * the person's gender: 'f' or 'm'
     */
    private char gender;
    /**
     * the ID of the person's mother
     */
    private String mother;
    /**
     * the ID of the person's father
     */
    private String father;
    /**
     * the ID of the person's spouse
     */
    private String spouse;



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

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public char getGender(){
        return gender;
    }

    public String getMother(){
        return mother;
    }

    public String getFather(){
        return father;
    }

    public String getSpouse(){
        return spouse;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setMother(String mother){this.mother = mother;}

    public void setFather(String father) {this.father = father;}

    public void setSpouse(String spouse) {this.spouse = spouse;}
}
