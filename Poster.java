import model.AuthToken;
import model.Event;
import model.Person;
import model.User;

public class Poster {

    /**
     * Poster constructor
     */
    public Poster(){}

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
        return null;
    }

    /**
     * generates a randomized ancestor for given Person
     * @param person the Person to generate and ancestor for
     */
    private void generateAncestor(Person person){}

    /**
     * log User in
     * @param userName the username of the user
     * @param password the password of the user
     * @return new AuthToken associated with User
     */
    public String login(String userName, String password){
        return null;
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
    public void load(User[] users, Person[] people, Event[] events){}

    /**
     * adds User to database
     * @param user the User to add
     */
    private void addUser(User user){}

    /**
     * adds Person to database
     * @param person the Person to add
     */
    private void addPerson(Person person){}

    /**
     * adds Event to database
     * @param event the Event to add
     */
    private void addEvent(Event event){}

}
