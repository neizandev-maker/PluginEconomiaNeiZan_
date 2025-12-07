package com.neizan.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLManager {

    private Connection connection;

    public void connect(String host, String database, String user, String password) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false",
                    user,
                    password
            );
            System.out.println("[MySQL] Conectado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
