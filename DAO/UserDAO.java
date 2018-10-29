package DAO;

import model.User;

public class UserDAO {
    public UserDAO(){}

    public User getUser(String userName){
        User user;
        /*
        select * from User
        where UserName = userName
         */
        return null;
    }

    public void addUser(User user){
        /*
        insert into User
        values (user.getUserName(), user.getPassword(), user.getEmail(),
            user.getfName(), user.getlname(), user.getGender(), user.getPersonID());
         */
    }
}
