package DAO;

import model.Person;

public class PersonDAO {
    public PersonDAO(){}

    public Person getPerson(String personID){
        Person person;
        /*
        select * from person
        where PersonID = personID

         */
        return null;
    }

    public void addPerson(Person person){
        /*
        insert into Person
        values (person.getPersonID(), person.getDescendant(), person.getfName(), person.getlname(), person.getGender(),
        person.getFatherID(), person.getMotherID(), person.getSpouseID());
         */
    }
}
