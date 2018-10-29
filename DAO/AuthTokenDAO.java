package DAO;

import model.AuthToken;

public class AuthTokenDAO {

    public AuthTokenDAO(){}

    public AuthToken getAuthToken(String userName){
        AuthToken token;
        /*
        select * from AuthToken
        where Username = username
         */
        return null;
    }

    public void addAuthToken(AuthToken token){
        /*
        insert into AuthToken
        values (token.getToken(), token.getUserName());
         */
    }
}
