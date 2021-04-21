package com.ensate.chatapp.server.db;

import java.sql.*;

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
}

