package com.ensate.chatapp.server.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBApi {
    private static Connection dbh;
    
    public static void startDB () throws SQLException, ClassNotFoundException {
        dbh = connect();
    }

    private static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        String password = "toor";
        String user = "silverest";
        String dbLocation = "jdbc:mysql://0.0.0.0:3306/CHATAPP";
        return DriverManager.getConnection(dbLocation, user, password);
    }

    public static ResultSet execQuery (String query) throws SQLException {
        Statement stat = dbh.createStatement();
        return
            stat
            .executeQuery(query);
    }

    public static Statement createStatement() throws SQLException {
        return dbh.createStatement();
    }

    public static Set<String> loadUsers() {
        HashSet<String> users = new HashSet<>();
        try {
            ResultSet rs = execQuery("SELECT * FROM USERS;"); 
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

