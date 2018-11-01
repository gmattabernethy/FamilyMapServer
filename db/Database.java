package db;

import java.sql.*;

public class Database {

    Connection conn;
    /**
     * Connect to a sample database
     */
    public Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:FamilyMapServer.db";
        try{
            Class.forName("org.sqlite.JDBC");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("On Connect : " + e.getMessage());
        }
        return conn;
    }

    public void deleteTree(String descendant){
        String deleteEvent = "DELETE FROM Event Where Descendant =?";
        String deletePerson = "DELETE FROM Person Where Descendant =?";

        try (
                Connection conn = this.connect();
                PreparedStatement dEventStmt  = conn.prepareStatement(deleteEvent);
                PreparedStatement dPersonStmt  = conn.prepareStatement(deletePerson)
            ){
            dEventStmt.setString(1, descendant);
            dPersonStmt.setString(1, descendant);

            dEventStmt.executeUpdate();
            dPersonStmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println("Clear Error : " + e.getMessage());
        }
    }

    public boolean clearDB(){

        String createEvent =  "CREATE TABLE IF NOT EXISTS Event (\n" +
                "`EventID` varchar ( 255 ) UNIQUE,\n" +
                "`Descendant` varchar ( 255 ),\n" +
                "`PersonID` varchar ( 255 ),\n" +
                "`Latitude` REAL,\n" +
                "`Longitude` REAL,\n" +
                "`Country` varchar ( 255 ),\n" +
                "`City` varchar ( 255 ),\n" +
                "`EventType` varchar ( 255 ),\n" +
                "`Year` INTEGER,\n" +
                " PRIMARY KEY(`EventID`)\n" +
                ")";

        String createPerson = "CREATE TABLE IF NOT EXISTS Person (\n" +
                "`PersonID` varchar ( 255 ) UNIQUE,\n" +
                "`Descendant` varchar ( 255 ),\n" +
                "`FirstName` varchar ( 255 ),\n" +
                "`LastName` varchar ( 255 ),\n" +
                "`Gender` varchar ( 1 ),\n" +
                "`FatherID` varchar ( 255 ),\n" +
                "`MotherID` varchar ( 255 ),\n" +
                "`SpouseID` varchar ( 255 ),\n" +
                " PRIMARY KEY(`PersonID`)\n" +
                ")";

        String createUser = "CREATE TABLE IF NOT EXISTS User (\n" +
                "`Username` varchar ( 255 ) UNIQUE,\n" +
                "`Password` varchar ( 255 ),\n" +
                "`Email` varchar ( 255 ),\n" +
                "`FirstName` varchar ( 255 ),\n" +
                "`LastName` varchar ( 255 ),\n" +
                "`Gender` char ( 1 ),\n" +
                "`PersonID` varchar ( 255 ) UNIQUE,\n" +
                " PRIMARY KEY(`Username`)\n" +
                ")";

        String createAuthToken = "CREATE TABLE IF NOT EXISTS AuthToken (\n" +
                "`Token` varchar ( 255 ) UNIQUE,\n" +
                "`UserName` varchar ( 255 ),\n" +
                " PRIMARY KEY(`Token`)\n" +
                ")";

        String dropEvent = "Drop table IF  EXISTS Event;";
        String dropPerson = "Drop table IF  EXISTS Person;";
        String dropUser = "Drop table IF  EXISTS User;";
        String dropAuthToken = "Drop table IF  EXISTS AuthToken;";
        
        try (
                Connection conn = this.connect();){
                Statement dE  = conn.createStatement();
                dE.executeUpdate(dropEvent);
                Statement dP  = conn.createStatement();
                dP.executeUpdate(dropPerson);
                Statement dU  = conn.createStatement();
                dU.executeUpdate(dropUser);
                Statement dAT  = conn.createStatement();
                dAT.executeUpdate(dropAuthToken);
                Statement cE  = conn.createStatement();
                cE.executeUpdate(createEvent);
                Statement cP  = conn.createStatement();
                cP.executeUpdate(createPerson);
                Statement cU  = conn.createStatement();
                cU.executeUpdate(createUser);
                Statement cAT  = conn.createStatement();
                cAT.executeUpdate(createAuthToken);
                return true;
        } catch (SQLException e) {
            System.out.println("Clear Error : " + e.getMessage());
            return false;
        }

    }
}
