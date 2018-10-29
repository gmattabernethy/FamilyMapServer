package DAO;

import model.AuthToken;
import model.User;

import java.sql.*;

public class UserDAO {
    private String connectionUrl;
    public UserDAO(){
        connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=FamilyMap Database Schema;>";
    }

    public User getUser(String userName){
        /*
        select * from User
        where UserName = userName
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "select * from User where Username = " + userName;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                return new User(rs.getString(0),rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5).charAt(0), rs.getString(6));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user){
        /*
        insert into User
        values (user.getUserName(), user.getPassword(), user.getEmail(),
            user.getfName(), user.getlname(), user.getGender(), user.getPersonID());
         */
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "insert into User values (" + user.getUserName() + "," + user.getPassword() + user.getEmail() + "," +
                    user.getfName() + user.getlName() + "," + user.getGender() + user.getPersonID() +   ")";
            stmt.executeQuery(SQL);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
