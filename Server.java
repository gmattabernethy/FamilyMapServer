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
import java.lang.Object;

public class Server {
    /**
     * A Poster to use for posting to database
     */
    private static Facade facade;

    /**
     * Server constructor
     */
    public Server(){}

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        facade = Facade.buildFacade();

        HttpContext root = server.createContext("/");
        root.setHandler(Server::handleRequest);

        HttpContext register = server.createContext("/user/register");
        register.setHandler(Server::handleRegisterRequest);

        HttpContext login = server.createContext("/user/login");
        login.setHandler(Server::handleLoginRequest);

        HttpContext clear = server.createContext("/clear");
        clear.setHandler(Server::handleClearRequest);

        HttpContext fill = server.createContext("/fill/");
        fill.setHandler(Server::handleFillRequest);

        HttpContext load = server.createContext("/load");
        load.setHandler(Server::handleLoadRequest);

        HttpContext getPerson = server.createContext("/person/");
        getPerson.setHandler(Server::handlePersonRequest);

        HttpContext getPersons = server.createContext("/person");
        getPersons.setHandler(Server::handlePersonsRequest);

        HttpContext getEvent = server.createContext("/event/");
        getEvent.setHandler(Server::handleEventRequest);

        HttpContext getEvents = server.createContext("/event");
        getEvents.setHandler(Server::handleEventsRequest);

        server.start();
        System.out.println("running on port " + port);
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

    private static void handleRegisterRequest(HttpExchange exchange) throws IOException {
        AuthToken token = facade.register(null, null, null, null, null, ' ');
        printResponse(token.getToken(), exchange);
    }

    private static void handleLoginRequest(HttpExchange exchange) throws IOException {
        printResponse("login", exchange);
        facade.login(null, null);
    }

    private static void handleClearRequest(HttpExchange exchange) throws IOException {
        printResponse("clear", exchange);
        facade.clear();
    }

    private static void handleFillRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String str = requestURI.toString();
        String userId = str.split("/")[2];
        String generations = "";
        if(str.split("/")[3] != null){
            generations = str.split("/")[3];
            facade.fill(null,Integer.parseInt(generations));
        }
        printResponse("fill " + userId + " " + generations, exchange);
        facade.fill(null);
    }

    private static void handleLoadRequest(HttpExchange exchange) throws IOException {
        printResponse("load", exchange);
        facade.load(null, null, null);
    }

    private static void handlePersonRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String str = requestURI.toString();
        String userId = str.split("/")[2];
        printResponse("person " + userId, exchange);
        facade.getPerson(userId);
    }

    private static void handlePersonsRequest(HttpExchange exchange) throws IOException {
        printResponse("persons", exchange);
        facade.getFamily(null);
    }

    private static void handleEventRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String str = requestURI.toString();
        String eventId = str.split("/")[2];
        printResponse("event " + eventId , exchange);
        facade.getEvent(eventId);
    }

    private static void handleEventsRequest(HttpExchange exchange) throws IOException {
        printResponse("events", exchange);
        facade.getFamilyEvents(null);
    }

    private static void printResponse(String response, HttpExchange exchange) throws IOException{
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
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
}
