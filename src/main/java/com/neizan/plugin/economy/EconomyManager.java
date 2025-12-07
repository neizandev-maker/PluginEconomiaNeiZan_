package com.neizan.plugin.economy;

import com.neizan.plugin.database.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconomyManager {

    private final MySQLManager mySQL;

    public EconomyManager(MySQLManager mySQL) {
        this.mySQL = mySQL;
    }

    public double getBalance(UUID uuid) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "SELECT balance FROM players WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble("balance");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setBalance(UUID uuid, double amount) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "REPLACE INTO players VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setDouble(2, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBalance(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean removeBalance(UUID uuid, double amount) {
        double current = getBalance(uuid);
        if (current >= amount) {
            setBalance(uuid, current - amount);
            return true;
        }
        return false;
    }

    public void registerPlayer(UUID uuid) {
        setBalance(uuid, getBalance(uuid));
    }
}
