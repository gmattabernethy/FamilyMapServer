package db;

import model.User;

import java.sql.*;

public class UserDAO {
    private String connectionUrl;
    public UserDAO(){}

    public boolean userAvailable(String userName){
        String sql = "SELECT count(*) cnt FROM User where UserName=?";
        Database db = new Database();
        Integer count=0;
        Boolean validUser = false;
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return false;
            }else{
                count = rs.getInt("cnt");
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(count == 0) validUser =true;
        return validUser;
    }

    public User getUser(String userName){
        String sql = "SELECT * FROM User where UserName=?";
        User user = new User();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                user.setUserName(rs.getString("UserName"));
                user.setPassword(rs.getString("Password"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setGender(rs.getString("Gender").charAt(0));
                user.setPersonID(rs.getString("PersonID"));
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public User getUser(String userName, String password){
        String sql = "SELECT * FROM User where UserName=? AND Password = ?";
        User user = new User();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                user.setUserName(rs.getString("UserName"));
                user.setPassword(rs.getString("Password"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setGender(rs.getString("Gender").charAt(0));
                user.setPersonID(rs.getString("PersonID"));

                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void addUser(User user){
        if (getUser(user.getUserName(), user.getPassword()) != null) return;
        String sql = "INSERT INTO User values (?,?,?,?,?,?,?)";
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, Character.toString(user.getGender()));
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
