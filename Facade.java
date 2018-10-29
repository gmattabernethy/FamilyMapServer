import DAO.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    static private Facade facade;
    private AuthTokenDAO authTokenAccess;
    private EventDAO eventAccess;
    private PersonDAO personAccess;
    private UserDAO userAccess;

    private Facade(){
        authTokenAccess = new AuthTokenDAO();
        eventAccess = new EventDAO();
        personAccess = new PersonDAO();
        userAccess = new UserDAO();
    }

    public static Facade buildFacade(){
        if(facade == null) facade = new Facade();
        return facade;
    }

    /**
     * create new User
     * generate 4 generations of ancestor data for user
     * log user in
     * @param userName the username of the user to register
     * @param password the password of the user to register
     * @param email the email address of the user to register
     * @param fName the first name of the user to register
     * @param lName the last name of the user to register
     * @param gender the gender of the user to register: 'f' or 'm'
     * @return AuthToken associated with new User
     */
    public AuthToken register(String userName, String password, String email, String fName, String lName, char gender){
        String personID = "";
        User user = new User(userName, password, email, fName, lName, gender, personID);

        Person person = new Person(personID, userName, fName, lName, gender);
        personAccess.addPerson(person);

        List<Person> family = generateAncestors(person, 4);

        userAccess.addUser(user);

        return login(userName, password);
    }

    /**
     * generates a randomized ancestor for given Person
     * @param person the Person to generate and ancestor for
     */
    private List<Person> generateAncestors(Person person, int generations){
        List<Person> family = new ArrayList<>();

        Person father = new Person(null, null, null, null, ' ');
        Person mother = new Person(null, null, null, null, ' ');

        person.setFatherID(father.getID());
        person.setMotherID(mother.getID());

        father.setSpouseID(mother.getID());
        mother.setSpouseID(father.getID());

        family.add(father);
        family.add(mother);

        family.addAll(generateAncestors(father, generations - 1));
        family.addAll(generateAncestors(mother, generations - 1));

        return family;
    }

    /**
     * log User in
     * @param userName the username of the user
     * @param password the password of the user
     * @return new AuthToken associated with User
     */
    public AuthToken login(String userName, String password){
        AuthToken token = new AuthToken(null, userName);
        authTokenAccess.addAuthToken(token);

        return token;
    }

    /**
     * empty the database
     */
    public void clear(){}


    /**
     * check if username in database
     * if so, remove all data connected to user from database
     * populate n generations for user
     * @param username the username to check
     * @param generations the number of generations to populate
     */
    public void fill(String username, int generations){}

    /**
     * check if username in database
     * if so, remove all data connected to user from database
     * populate a default of 4 generations for user
     * @param username the username to check
     */
    public void fill(String username){}


    /**
     * empty database
     * load provided objects into database
     * @param users a list of user objects to add
     * @param people a list of person objects to add
     * @param events a list of event objects to add
     */
    public void load(User[] users, Person[] people, Event[] events){
        clear();
        for(User u: users) userAccess.addUser(u);
        for(Person p: people) personAccess.addPerson(p);
        for(Event e: events) eventAccess.addEvent(e);
    }

    /**
     * gets User from an AuthToken
     * @param authToken the AuthToken to get the user from
     * @return User associated with given AuthToken
     */
    private User userFromToken(AuthToken authToken){
        String userName = authToken.getUsername();

        return userAccess.getUser(userName);
    }

    /**
     * gets person by ID
     * @param personID the ID to get the Person from
     * @return person with the specified ID
     */
    public Person getPerson(String personID){
        return personAccess.getPerson(personID);
    }

    /**
     * gets ALL family members of user
     * @return list of family members of current user
     */
    public List<Person> getFamily(AuthToken token){
        List<Person> people = new ArrayList<>();
        User user = userFromToken(token);
        Person person = personAccess.getPerson(user.getPersonID());

        people.addAll(getFamilyRecurse(person));

        return people;
    }

    /**
     * recursively gets ALL family members of a Person
     * @param person the Person to find the family of
     * @return list of family members of given Person
     */
    private List<Person> getFamilyRecurse(Person person){
        List<Person> people = new ArrayList<>();
        people.add(person);

        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();
        String spouseID = person.getSpouseID();

        Person father;
        Person mother;
        Person spouse;

        if(fatherID != null) {
            father = personAccess.getPerson(fatherID);
            people.addAll(getFamilyRecurse(father));
        }
        if(motherID != null) {
            mother = personAccess.getPerson(motherID);
            people.addAll(getFamilyRecurse(mother));
        }
        if(spouseID != null) {
            spouse = personAccess.getPerson(spouseID);
            people.addAll(getFamilyRecurse(spouse));
        }

        return people;
    }

    /**
     * gets event by ID
     * @param eventID the ID of the event to get
     * @return event with the specified ID
     */
    public Event getEvent(String eventID){
        return eventAccess.getEvent(eventID);
    }

    /**
     * gets ALL the events associated with the current user's family
     * @param token the AuthToken to get the current user from
     * @return list of events associated with All family members of current user
     */
    public List<Event> getFamilyEvents(AuthToken token){
        List<Person> family = getFamily(token);
        List<Event> events = new ArrayList<>();

        for(Person p: family){
            Event event = eventAccess.getByPersonID(p.getID());
            events.add(event);
        }

        return events;
    }

    //TODO

    private String generatePersonID(){return null;}
    private String generateToken(){return null;}
}
