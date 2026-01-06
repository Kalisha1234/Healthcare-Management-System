package org.example.config;

import java.sql.*;

public class DBConfig {
    private final String DBHOST = System.getenv("DB_HOST");
    private final String DBPORT = System.getenv("DB_PORT");
    private final String DBNAME = System.getenv("DB_NAME");
    private final String DBUSER = System.getenv("DB_USER");
    private final String DBPASSWORD = System.getenv("DB_PASSWORD");

    public Connection connect() throws SQLException {
        if(DBPASSWORD == null) {
            throw new SQLException("DB_PASSWORD environment variable is not set.");
        }
        try{
            String url = String.format("jdbc:postgresql://%s:%s/%s", DBHOST, DBPORT, DBNAME);
            return DriverManager.getConnection(url, DBUSER, DBPASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to the database: " + e.getMessage(), e);
        }
    }

}
