package ru.aleynikov.blogcamp;

public class Config {

    // PSQL

    public static final String USERNAME_PSQL = "postgres";
    public static final String PASSWORD_PSQL = "123";
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/blogcamp";
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    // Hikari Connection Pool

    public static final int MAX_HIKARI_POOL_SIZE = 5;

}
