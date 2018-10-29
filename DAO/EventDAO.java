package DAO;

import model.Event;

public class EventDAO {

    public EventDAO(){}

    public Event getEvent(String eventID){
        Event event;
        /*
        select * from Event
        where EventID = eventID
         */
        return null;
    }

    public void addEvent(Event event){
        /*
        insert into Event
        values (event.getEventID(), event.getDescendant(), event.getPersonID(), event.getLatitude(), event.getlongitude(),
        event.getCountry(), event.getCity(), event.getEventType(), event.getYear());
         */
    }

    public Event getByPersonID(String personID){
        Event event;
        /*
        select * from Event
        where PersonID = personID
         */
        return null;
    }
}
