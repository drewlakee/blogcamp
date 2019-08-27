package ru.aleynikov.blogcamp;

public class Config {

    // PSQL

    public static final String USERNAME_PSQL = System.getProperty("psql.username");
    public static final String PASSWORD_PSQL = System.getProperty("psql.password");
    public static final String JDBC_URL = System.getProperty("jdbc.url");
    public static final String JDBC_DRIVER = System.getProperty("jdbc.driver");
    
}
