package DAO;

import model.AuthToken;
import model.User;

import java.sql.*;

public class UserDAO {
    private String connectionUrl;
    public UserDAO(){}

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
                user.setfName(rs.getString("FirstName"));
                user.setlName(rs.getString("LastName"));
                user.setGender(rs.getString("Gender").charAt(0));
                user.setPersonID(rs.getString("PersonID"));
            }
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
                user.setfName(rs.getString("FirstName"));
                user.setlName(rs.getString("LastName"));
                user.setGender(rs.getString("Gender").charAt(0));
                user.setPersonID(rs.getString("PersonID"));
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
            stmt.setString(4, user.getfName());
            stmt.setString(5, user.getlName());
            stmt.setString(6, Character.toString(user.getGender()));
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        UserDAO DAO = new UserDAO();

        User user = new User();
        user.setPassword("0000");
        user.setUserName("matt");

        DAO.addUser(user);
        System.out.println(DAO.getUser("matt").getUserName());
    }
}
