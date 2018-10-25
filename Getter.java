import java.util.List;

import model.AuthToken;
import model.Event;
import model.Person;
import model.User;

public class Getter {
    /**
     * The user to get information about
     */
    private User user;

    /**
     * Getter constructor
     * @param authToken the AuthToken used to identify the user
     */
    public Getter(AuthToken authToken){}

    /**
     * gets User from an AuthToken
     * @param authToken the AuthToken to get the user from
     * @return User associated with given AuthToken
     */
    private User userFromToken(AuthToken authToken){
        return null;
    }

    /**
     * gets person by ID
     * @param personID the ID to get the Person from
     * @return person with the specified ID
     */
    public Person getPerson(String personID){
        return null;
    }

    /**
     * gets ALL family members of user
     * @return list of family members of current user
     */
    public List<Person> getFamily(){
        return null;
    }

    /**
     * recursively gets ALL family members of a Person
     * @param person the Person to find the family of
     * @return list of family members of given Person
     */
    private List<Person> getFamilyRecurse(Person person){
        return null;
    }

    /**
     * gets event by ID
     * @param eventID the ID of the event to get
     * @return event with the specified ID
     */
    public Event getEvent(String eventID){
        return null;
    }

    /**
     * gets ALL the events associated with the current user's family
     * @param authToken the AuthToken to get the current user from
     * @return list of events associated with All family members of current user
     */
    public List<Event> getFamilyEvents(AuthToken authToken){
        return null;
    }
}
