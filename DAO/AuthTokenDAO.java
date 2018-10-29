package DAO;

import model.AuthToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AuthTokenDAO {
    private String connectionUrl;
    public AuthTokenDAO(){
        connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=FamilyMap Database Schema;>";
    }

    public AuthToken getAuthToken(String userName){
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "select * from AutToken where Username = " + userName;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                return new AuthToken(rs.getString(0),rs.getString(1));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAuthToken(AuthToken token){
        /*
        insert into AuthToken
        values (token.getToken(), token.getUserName());
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "insert into AuthToken values (" + token.getToken() + "," + token.getUsername() + ")";
            stmt.executeQuery(SQL);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        AuthTokenDAO DAO = new AuthTokenDAO();
        DAO.getAuthToken("matt");
    }
}
