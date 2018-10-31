package db;

import model.AuthToken;

import java.sql.*;


public class AuthTokenDAO {
    private String connectionUrl;
    public AuthTokenDAO(){}

    public Boolean validateAuthToken(String token) {
        String sql = "SELECT count(*) cnt FROM AuthToken where Token=?";
        Database db = new Database();
        Integer count=0;
        Boolean validToken=false;
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, token);
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
        if(count>0) validToken=true;
        return validToken;
    }

    public AuthToken getAuthToken(String token){
        String sql = "SELECT Token, UserName  FROM AuthToken where Token=?";
        AuthToken authToken = new AuthToken();
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) {
                return null;
            }else{
                //token.setPersonID(rs.getString("PersonID"));
                authToken.setUserName(rs.getString("UserName"));
                authToken.setToken(token);
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return authToken;
    }

    public void addAuthToken(AuthToken token){
        if (validateAuthToken(token.getToken())) return;
        String sql = "INSERT INTO AuthToken values (?,?)";
        Database db = new Database();
        try (
                Connection conn = db.connect();
                PreparedStatement stmt  = conn.prepareStatement(sql);){
            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUserName());
            stmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        AuthTokenDAO DAO = new AuthTokenDAO();

        AuthToken token = new AuthToken();
        token.setToken("1234");
        token.setUserName("matt");

        DAO.addAuthToken(token);
        System.out.println(DAO.getAuthToken("matt").getToken());
    }
}
