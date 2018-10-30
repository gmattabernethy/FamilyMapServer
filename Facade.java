import DAO.*;
import com.google.gson.Gson;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Facade {
    static private Facade facade;
    private AuthTokenDAO authTokenAccess;
    private EventDAO eventAccess;
    private PersonDAO personAccess;
    private UserDAO userAccess;
    private Database db;

    private class Location{
        private String country;
        private String city;
        private double latitude;
        private double longitude;
    }

    private Facade(){
        authTokenAccess = new AuthTokenDAO();
        eventAccess = new EventDAO();
        personAccess = new PersonDAO();
        userAccess = new UserDAO();
        db = new Database();
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
        Random rand = new Random();
        int birthYear = 2018 - (rand.nextInt(59) + 1);
        String personID  = UUID.randomUUID().toString();

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setfName(fName);
        user.setlName(lName);
        user.setGender(gender);
        user.setPersonID(personID);

        Person person = new Person();
        person.setPersonID(personID);
        person.setDescendant(userName);
        person.setfName(fName);
        person.setlName(lName);
        person.setGender(gender);

        Event birth = new Event();
        generateEvent(birth, person.getPersonID(), person.getDescendant(),"Birth", birthYear);

        List<Person> family = generateAncestors(person, 4, birthYear);

        userAccess.addUser(user);
        eventAccess.addEvent(birth);
        for(Person p: family) personAccess.addPerson(p);

        return login(userName, password);
    }

    /**
     * generates a randomized ancestor for given Person
     * @param person the Person to generate and ancestor for
     */
    private List<Person> generateAncestors(Person person, int generations, int birthYear){
        Random rand = new Random();
        int fatherBirthYear = birthYear + rand.nextInt(20) + 20;
        int motherBirthYear = birthYear + rand.nextInt(20) + 20;

        List<Person> family = new ArrayList<>();

        family.add(person);

        if(generations < 1) return family;

        Person father = new Person();
        Person mother = new Person();

        generatePerson(father, person.getlName(),'M', fatherBirthYear);
        generatePerson(mother, 'F', motherBirthYear);

        father.setDescendant(person.getDescendant());
        mother.setDescendant(person.getDescendant());

        person.setFatherID(father.getPersonID());
        person.setMotherID(mother.getPersonID());

        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());

        family.addAll(generateAncestors(father, generations - 1, fatherBirthYear));
        family.addAll(generateAncestors(mother, generations - 1, motherBirthYear));

        return family;
    }

    private void generatePerson(Person person, char gender, int birthYear){
        person.setPersonID(UUID.randomUUID().toString());
        person.setfName(randomName("fnames.json"));
        person.setlName(randomName("snames.json"));
        person.setGender(gender);

        generateLifeEvents(person.getPersonID(), person.getDescendant(), birthYear);
    }

    private void generatePerson(Person person, String lName, char gender, int birthYear){
        person.setPersonID(UUID.randomUUID().toString());
        person.setfName(randomName("fnames.json"));
        person.setlName(lName);
        person.setGender(gender);

        generateLifeEvents(person.getPersonID(), person.getDescendant(), birthYear);
    }

    private void generateLifeEvents(String personID, String userName, int birthYear){
        Random rand = new Random();
        int marriageYear = birthYear + rand.nextInt(22) + 18;
        int deathYear = 0;
        while(deathYear < marriageYear){
            deathYear = birthYear + rand.nextInt(100);
        }
        int baptismYear = 5000;
        while(baptismYear > deathYear){
            baptismYear = birthYear + rand.nextInt(100);
        }

        Event birth = new Event();
        generateEvent(birth, personID, userName,"Birth", birthYear);
        Event baptism = new Event();
        generateEvent(baptism, personID, userName,"Baptism", baptismYear);
        Event Marriage = new Event();
        generateEvent(Marriage, personID, userName,"Marriage", marriageYear);
        Event Death = new Event();
        generateEvent(Death, personID, userName,"Death", deathYear);
    }

    private void generateEvent(Event event, String personID, String userName, String eventType, int year){
        Location loc = randomLocation();
        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(personID);
        event.setDescendant(userName);
        event.setLatitude(loc.latitude);
        event.setLongitude(loc.longitude);
        event.setCountry(loc.country);
        event.setCity(loc.city);
        event.setEventType(eventType);
        event.setYear(year);

        facade.eventAccess.addEvent(event);
    }

    /**
     * log User in
     * @param userName the username of the user
     * @param password the password of the user
     * @return new AuthToken associated with User
     */
    public AuthToken login(String userName, String password){
        User user = userAccess.getUser(userName, password);

        if(user != null){
            AuthToken token = new AuthToken();
            token.setToken(UUID.randomUUID().toString());
            token.setUserName(userName);
            authTokenAccess.addAuthToken(token);
            return token;
        }

        return null;
    }

    /**
     * empty the database
     */
    public void clear(){
        db.clearDB();
    }


    /**
     * check if username in database
     * if so, remove all data connected to user from database
     * populate n generations for user
     * @param username the username to check
     * @param generations the number of generations to populate
     */
    public void fill(String username, int generations){
        User user = userAccess.getUser(username);
        if(user == null) return;

        Person person = personAccess.getPerson(user.getPersonID());
        Event birth = eventAccess.getEvent(person.getPersonID(), "Birth");
        generateAncestors(person, generations, birth.getYear());
    }

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
        String userName = authToken.getUserName();

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

        Person father;
        Person mother;

        if(fatherID != null) {
            father = personAccess.getPerson(fatherID);
            people.addAll(getFamilyRecurse(father));
        }
        if(motherID != null) {
            mother = personAccess.getPerson(motherID);
            people.addAll(getFamilyRecurse(mother));
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

        for(Person p: family) events.addAll(eventAccess.getAllEvents(p.getPersonID()));

        return events;
    }

    private String randomName(String fileName){
        Gson gson = new Gson();

        class Data {
            private List<String> strings;
        }

        Data data = gson.fromJson("/json/" + fileName, Data.class);

        Random rand = new Random();
        int i = rand.nextInt(data.strings.size());

        return data.strings.get(i);
    }

    private Location randomLocation(){
        Gson gson = new Gson();

        class Data {
            private List<Location> locations;
        }

        Data data = gson.fromJson("/json/locations.json", Data.class);

        Random rand = new Random();
        int i = rand.nextInt(data.locations.size());

        return data.locations.get(i);
    }

    public static void main(String[] args){
        Facade f = buildFacade();

        f.clear();

        Person p = new Person();
        f.generatePerson(p,'M', -1234);

        System.out.println(f.personAccess.getPerson(p.getPersonID()).getPersonID());
/*
        Event e = new Event();
        f.generateEvent(e, "1235", "matt", "test", -1234);

        System.out.println(f.eventAccess.getEvent("1235", "test").getCity());
        System.out.println(f.eventAccess.getEvent("1235", "test").getCountry());
/*
        AuthToken token = f.register("matt", "0000", ".com", "Matt", "Abernethy", 'M');

        System.out.println("valid token = " + f.authTokenAccess.validateAuthToken(token.getToken()));

        int i = 0;

        for (Person p: f.getFamily(token)) {
            i++;
        }

        System.out.println(i);
        */
    }
}
