import db.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

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

        generatePerson(father, person.getlName(),'m', fatherBirthYear);
        generatePerson(mother, 'f', motherBirthYear);

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
        person.setfName(randomName(Character.toLowerCase(gender) + "names.json"));
        person.setlName(randomName("snames.json"));
        person.setGender(gender);

        generateLifeEvents(person.getPersonID(), person.getDescendant(), birthYear);
    }

    private void generatePerson(Person person, String lName, char gender, int birthYear){
        person.setPersonID(UUID.randomUUID().toString());
        person.setfName(randomName(Character.toLowerCase(gender) + "names.json"));
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
    public boolean clear(){
        return db.clearDB();
    }


    /**
     * check if username in database
     * if so, remove all data connected to user from database
     * populate n generations for user
     * @param username the username to check
     * @param generations the number of generations to populate
     */
    public int[] fill(String username, int generations){
        User user = userAccess.getUser(username);
        if(user == null) return null;

        db.deleteTree(username);

        Person person = personAccess.getPerson(user.getPersonID());
        Event birth = eventAccess.getEvent(person.getPersonID(), "Birth");
        generateAncestors(person, generations, birth.getYear());
        return null;//TODO count up people and events
    }

    /**
     * empty database
     * load provided objects into database
     * @param users a list of user objects to add
     * @param people a list of person objects to add
     * @param events a list of event objects to add
     */
    public int[] load(JsonArray users, JsonArray people, JsonArray events){
        clear();
        Gson gson = new Gson();

        if(users == null || people == null || events == null) return null;

        int[] counts = {
          users.size(),
          people.size(),
          events.size()
        };


        for(int i = 0; i < users.size(); i++){
            JsonObject obj = users.get(i).getAsJsonObject();
            User user = gson.fromJson(obj.toString(),User.class);
            userAccess.addUser(user);
        }
        for(int i = 0; i < people.size(); i++){
            JsonObject obj = people.get(i).getAsJsonObject();
            Person person = gson.fromJson(obj.toString(),Person.class);
            personAccess.addPerson(person);
        }
        for(int i = 0; i < events.size(); i++){
            JsonObject obj = events.get(i).getAsJsonObject();
            Event event = gson.fromJson(obj.toString(),Event.class);
            eventAccess.addEvent(event);
        }

        return counts;
    }

    /**
     * gets User from an AuthToken
     * @param token the AuthToken to get the user from
     * @return User associated with given AuthToken
     */
    public User userFromToken(String token){
        AuthToken authToken = authTokenAccess.getAuthToken(token);
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
    public List<Person> getFamily(String personID){
        List<Person> people = new ArrayList<>();
        Person person = personAccess.getPerson(personID);

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
     * @param personID the ID of the Person to get the family of
     * @return list of events associated with All family members of current user
     */
    public List<Event> getFamilyEvents(String personID){
        List<Person> family = getFamily(personID);
        List<Event> events = new ArrayList<>();

        for(Person p: family) events.addAll(eventAccess.getAllEvents(p.getPersonID()));

        return events;
    }

    private String randomName(String fileName){
        String name = "";
        try {
            String path = "json/" + fileName;
            BufferedReader reader = new BufferedReader(new FileReader(path));

            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            JsonArray array = obj.get("data").getAsJsonArray();

            Random rand = new Random();
            int i = rand.nextInt(array.size());

            name = array.get(i).getAsString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    private Location randomLocation(){
        Location loc = new Location();
        try {
            String path = "json/locations.json";
            BufferedReader reader = new BufferedReader(new FileReader(path));

            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            JsonArray array = obj.get("data").getAsJsonArray();

            Random rand = new Random();
            int i = rand.nextInt(array.size());
            JsonObject packet = array.get(i).getAsJsonObject();

            loc = gson.fromJson(packet.toString(),Location.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return loc;
    }

    public static void main(String[] args){
        Facade f = buildFacade();

        ;



    }
}
