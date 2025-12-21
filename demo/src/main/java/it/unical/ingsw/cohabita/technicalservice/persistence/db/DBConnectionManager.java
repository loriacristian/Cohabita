package it.unical.ingsw.cohabita.technicalservice.persistence.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static DBConnectionManager istance;
    private Connection connection;
    private static final String URL = "jdbc:postgresql://ep-wandering-king-agg5zuix-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require&socketTimeout=120&connectTimeout=60&loginTimeout=60";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_74QFLmNtSGyh";

    private DBConnectionManager() {
    }

    public static DBConnectionManager getInstance(){
        if(istance == null){
            istance = new DBConnectionManager();
        }
        return istance;
    }

    public Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
