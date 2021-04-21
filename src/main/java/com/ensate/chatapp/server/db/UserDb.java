package com.ensate.chatapp.server.db;

import java.sql.*;

public class UserDb {
    public static boolean authentificate (String username, String password) throws SQLException {
        return 
            DBApi.execQuery("SELECT Id FROM USERS WHERE Username='"
                    + username + "' and Password='" + password + "' LIMIT 1;")
                    .next();
    }

    public static boolean userExists (String username) throws SQLException {
        return 
            DBApi.execQuery("SELECT Id FROM USERS WHERE Username='"
                    + username + "' LIMIT 1;").next();
    }

    public static boolean createUser (String username, String password) throws SQLException {
        if (!userExists(username)) {
            Statement stat = DBApi.createStatement();
            stat.executeUpdate("INSERT INTO USERS (Username, Password) VALUES ('" + username + "', '" + password + "')");
            return true;
        }
        return false;
    }
}
