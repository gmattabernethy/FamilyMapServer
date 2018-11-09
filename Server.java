
import com.google.gson.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

import db.*;
import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {

    private static int PORT;
    private static Gson gson = new Gson();
    private static String INDEX = "index.html";
    private static Facade facade;

    public static void main(String[] args) throws IOException {
        facade = Facade.buildFacade();
        if(args.length==0){
            System.out.println("Usage : program port");
        }else{
            PORT = Integer.valueOf(args[0]);

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // This was the original - see below for possibly a better way
            //HttpContext context = server.createContext("/person");
            //context.setHandler(Server::handlePersonRequest);

            server.createContext("/", Server::handleIndexRequest);
            server.createContext("/person", Server::handlePersonRequest);
            server.createContext("/event", Server::handleEventRequest);
            server.createContext("/clear", Server::handleClearRequest);
            server.createContext("/load", Server::handleLoadRequest);
            server.createContext("/user/login", Server::handleLoginRequest);
            server.createContext("/user/register", Server::handleRegisterRequest);
            server.createContext("/fill", Server::handleFillRequest);

            server.start();
            System.out.println("Server started on port : " + Integer.toString(PORT));
        }
    }

    private static void handleFillRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String str = requestURI.toString();
        String[] params = str.split("/");
        String userName = params[2];
        String generations = "";
        UserDAO ud = new UserDAO();
        System.out.println(params.length);
        if(params.length > 3){
            System.out.println("not null");
            generations = params[3];
            if(Integer.parseInt(generations) < 0 || ud.userAvailable(userName)) {
                sendServerResponse(apiMessage("Invalid userName or generations parameter"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            }
            else {
                int[] counts = facade.fill(userName,Integer.parseInt(generations));
                sendServerResponse(apiMessage("Successfully added " + counts[0] + " persons and " + counts[1] + " events to the database"), HttpURLConnection.HTTP_OK, exchange);
            }
        }else{
            System.out.println("null");
            if(ud.userAvailable(userName)){
                sendServerResponse(apiMessage("Invalid userName or generations parameter"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            }
            else {
                int[] counts = facade.fill(userName,4);
                sendServerResponse(apiMessage("Successfully added " + counts[0] + " persons and " + counts[1] + " events to the database"), HttpURLConnection.HTTP_OK, exchange);
            }
        }
    }

    private static void handleRegisterRequest(HttpExchange exchange) throws IOException {
        JSONObject j = getRequestBody(exchange);

        if(j.has("userName") && j.has("password") && j.has("email") && j.has("firstName") && j.has("lastName") && j.has("gender")){
            UserDAO ud = new UserDAO();
            if(ud.userAvailable(j.getString("userName"))){
                AuthToken a = facade.register(j.getString("userName"), j.getString("password"), j.getString("email"), j.getString("firstName"), j.getString("lastName"), j.getString("gender").charAt(0));
                if(a!=null){
                    sendServerResponse(a, HttpURLConnection.HTTP_OK, exchange);
                }else{
                    sendServerResponse(apiMessage("Internal server error"), HttpURLConnection.HTTP_SERVER_ERROR, exchange);
                }
            }else{
                sendServerResponse(apiMessage("Username already taken by another user"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            }
        }else{
            sendServerResponse(apiMessage("Request property missing or has invalid value"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
        }
    }


    private static void handleLoginRequest(HttpExchange exchange) throws IOException {
        JSONObject j = getRequestBody(exchange);

        if(j.has("userName") && j.has("password")){
            AuthToken a = facade.login(j.getString("userName"), j.getString("password"));
            if(a!=null){
                sendServerResponse(a, HttpURLConnection.HTTP_OK, exchange);
            }else{
                sendServerResponse(apiMessage("Request property missing or has invalid value"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            }
        }else{
            sendServerResponse(apiMessage("Request property missing or has invalid value"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
        }
    }

    private static void handleClearRequest(HttpExchange exchange) throws IOException {

        if(facade.clear()){
            sendServerResponse(apiMessage("Clear succeeded."), HttpURLConnection.HTTP_OK, exchange);
        }else{
            sendServerResponse(apiMessage("Internal server error"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
        }
    }

    private static void handleLoadRequest(HttpExchange exchange) throws IOException {
        JSONObject j = getRequestBody(exchange);
        String json = j.toString();
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        if(json.equals("{}")){
            sendServerResponse(apiMessage("Invalid request data"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
        }else {

            facade.clear();

            JsonArray users = obj.getAsJsonArray("users");
            JsonArray persons = obj.getAsJsonArray("persons");
            JsonArray events = obj.getAsJsonArray("events");

            int[] counts = facade.load(users, persons, events);

            if (counts != null) {
                sendServerResponse(apiMessage("Successfully added " + counts[0] + " users, " + counts[1] + " persons, and " + counts[2] + " events to the database."), HttpURLConnection.HTTP_OK, exchange);
            } else {
                sendServerResponse(apiMessage("Internal server error"), HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            }
        }
    }

    private static void handlePersonRequest(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path  = exchange.getRequestURI().toString();
        String token = exchange.getRequestHeaders().getFirst("Authorization");
        System.out.println(requestMethod + " method called on route " + path + " with token : " + token);

        AuthTokenDAO auth = new AuthTokenDAO();
        if(auth.validateAuthToken(token)){
            String[] params=path.split("/");
            User user = facade.userFromToken(token);

            if(params.length<3){
                List<Person> people = facade.getFamily(user.getUserName());
                Person[] array = people.toArray(new Person[0]);

                Map<String, Person[]> map = new HashMap<>();
                map.put("data", array);

                if(people!=null) {
                    sendServerResponse(map, HttpURLConnection.HTTP_OK, exchange);
                }
            }else{
                String personID = params[2];
                Person p = facade.getPerson(personID);
                if(p!=null){
                    if(p.getDescendant().equals(user.getUserName())){
                        sendServerResponse(p, HttpURLConnection.HTTP_OK, exchange);
                    }
                    else{
                        sendServerResponse(apiMessage("Requested person does not belong to this user"), HttpURLConnection.HTTP_UNAUTHORIZED, exchange);
                    }
                }else{
                    sendServerResponse(apiMessage("Invalid personID parameter"), HttpURLConnection.HTTP_NOT_FOUND, exchange);
                }
            }
        }else{
            sendServerResponse(apiMessage("Invalid auth token"), HttpURLConnection.HTTP_UNAUTHORIZED, exchange);
        }
    }

    private static void handleEventRequest(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path  = exchange.getRequestURI().toString();
        String token = exchange.getRequestHeaders().getFirst("Authorization");
        System.out.println(requestMethod + " method called on route " + path + " with token : " + token);

        AuthTokenDAO auth = new AuthTokenDAO();
        if(auth.validateAuthToken(token)){
            String[] params=path.split("/");
            User user = facade.userFromToken(token);

            if(params.length<3){
                List<Event> events = facade.getFamilyEvents(user.getUserName());
                Event[] array = events.toArray(new Event[0]);

                Map<String, Event[]> map = new HashMap<>();
                map.put("data", array);
                if(events!=null) {
                    sendServerResponse(map, HttpURLConnection.HTTP_OK, exchange);
                }
            }else{
                String eventID = params[2];
                Event e = facade.getEvent(eventID);
                if(e!=null){
                    if(e.getDescendant().equals(user.getUserName())){
                        sendServerResponse(e, HttpURLConnection.HTTP_OK, exchange);
                    }
                    else{
                        sendServerResponse(apiMessage("Requested event does not belong to this user"), HttpURLConnection.HTTP_UNAUTHORIZED, exchange);
                    }
                }else{
                    sendServerResponse(apiMessage("Invalid eventID parameter"), HttpURLConnection.HTTP_NOT_FOUND, exchange);
                }
            }
        }else{
            sendServerResponse(apiMessage("Invalid auth token"), HttpURLConnection.HTTP_UNAUTHORIZED, exchange);
        }
    }

    private static void handleIndexRequest(HttpExchange exchange) throws IOException {
        String root = "web";
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("Requesting : " + path);
        if(path.length()<=1) path += INDEX; // put index.html on the end if empty
        File file = new File(root + path).getCanonicalFile();
        Headers h = exchange.getResponseHeaders();
        if (!file.isFile()) {// Object does not exist or is not a file: reject with 404 error.
            file = new File(root + "/HTML/404.html").getCanonicalFile();
            h.set("Content-Type", Files.probeContentType(file.toPath()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        } else { // Object exists and is a file: accept with response code 200.
            h.set("Content-Type", Files.probeContentType(file.toPath()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }
        OutputStream os = exchange.getResponseBody();
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        fs.close();
        os.close();
    }

    private static JsonObject apiMessage(String msg){
        JsonObject j = new JsonObject();
        j.addProperty("message",msg);
        return j;
    }

    public static void sendServerResponse(Object o, int statusCode, HttpExchange exchange){
        try{
            exchange.sendResponseHeaders(statusCode, 0);
            String res = "";
            if(o!=null) {
                res = gson.toJson(o);
            }else{
                res = "{\"message\":\"Object not found\"}"; // Do we ever get here?
            }
            OutputStream os = exchange.getResponseBody();
            os.write(res.getBytes());
            os.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static Map<String, String> getURLParams(URI url) throws UnsupportedEncodingException {
        Map<String, String> values = new LinkedHashMap<>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int i = pair.indexOf("=");
            values.put(URLDecoder.decode(pair.substring(0, i), "UTF-8"), URLDecoder.decode(pair.substring(i + 1), "UTF-8"));
        }
        return values;
    }

    private static JSONObject getRequestBody(HttpExchange exchange) throws IOException{
        StringBuilder body = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8")) {
            char[] buffer = new char[256];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                body.append(buffer, 0, read);
            }
        }
        String newJson = body.toString();
        try{
            JsonParser parser = new JsonParser();
            parser.parse(newJson);
        }
        catch(JsonSyntaxException jse){
            newJson = "{}";
            System.out.println("Not a valid Json String:"+jse.getMessage());
        }
        return new JSONObject(newJson);
    }
}