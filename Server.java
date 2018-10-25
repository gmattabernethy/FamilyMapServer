import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.net.URI;
import com.sun.net.httpserver.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;

public class Server {
    /**
     * A Poster to use for posting to database
     */
    private Poster poster;
    /**
     * A Getter to use for getting from database
     */
    private Getter getter;

    /**
     * Server constructor
     */
    public Server(){}

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(Server::handleRequest);
        HttpContext person = server.createContext("/Person");
        person.setHandler(Server::handlePersonRequest);
        server.start();
        System.out.println("running...");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        printRequestInfo(exchange);
        String response = "This is the response at " + requestURI;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void printRequestInfo(HttpExchange exchange) {
        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);
    }

    private static void handlePersonRequest(HttpExchange exchange) throws IOException {
        String response = "person";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * use Poster to register new User
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
     * user Poster to log a User in
     * @param userName the username of the user
     * @param password the password of the user
     * @return new AuthToken associated with User
     */
    public String login(String userName, String password){
        return null;
    }

    /**
     * use Poster to empty the database
     */
    public void clear(){}

    /**
     * use Poster to fill a user with geneological data to n generations
     * default n = 4
     * @param username the username to check
     * @param generations the number of generations to populate
     */
    public void fill(String username, int generations){}

    /**
     * use Poster to populate database solely with given objects
     * @param users a list of user objects to add
     * @param people a list of person objects to add
     * @param events a list of event objects to add
     */
    public void load(User[] users, Person[] people, Event[] events){}

    /**
     * Use getter to get a person by ID
     * @param personID the ID of the Person to get
     * @return person with the specified ID
     */
    public Person getPerson(String personID){
        return null;
    }

    /**
     * Use getter to get ALL family members of a user
     * @return list of family members of current user
     */
    public List<Person> getFamily(){
        return null;
    }

    /**
     * Use getter to get an event by ID
     * @param eventID the ID of the event to get
     * @return event with the specified ID
     */
    public Event getEvent(String eventID){
        return null;
    }

    /**
     * Use getter to get ALL the events associated with the current user's family
     * @param authToken the AuthToken associated with the current user
     * @return list of events associated with All family members of current user
     */
    public List<Event> getFamilyEvents(String authToken){
        return null;
    }


}
