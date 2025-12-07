package com.neizan.plugin.economy;

import com.neizan.plugin.database.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconomyManager {

    private final MySQLManager mySQL;

    public EconomyManager(MySQLManager mySQL) {
        this.mySQL = mySQL;
    }

    public double getBalance(String playerName) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "SELECT balance FROM economy WHERE player_name=?");
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble("balance");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setBalance(String playerName, double amount) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "REPLACE INTO economy (player_name, balance) VALUES (?, ?)");
            ps.setString(1, playerName);
            ps.setDouble(2, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBalance(String playerName, double amount) {
        setBalance(playerName, getBalance(playerName) + amount);
    }

    public boolean removeBalance(String playerName, double amount) {
        double current = getBalance(playerName);
        if (current >= amount) {
            setBalance(playerName, current - amount);
            return true;
        }
        return false;
    }

    public void registerPlayer(String playerName) {
        // Si el jugador no tiene registro, se crea con balance inicial 0
        setBalance(playerName, getBalance(playerName));
    }
}
