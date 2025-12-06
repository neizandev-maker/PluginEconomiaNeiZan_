package com.neizan.plugin.economy;

import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {

    private final HashMap<UUID, Double> balances;

    public EconomyManager() {
        this.balances = new HashMap<>();
    }

    // Obtener dinero de un jugador
    public double getBalance(UUID playerUUID) {
        return balances.getOrDefault(playerUUID, 0.0);
    }

    // Añadir dinero a un jugador
    public void addBalance(UUID playerUUID, double amount) {
        balances.put(playerUUID, getBalance(playerUUID) + amount);
    }

    // Quitar dinero de un jugador
    public boolean removeBalance(UUID playerUUID, double amount) {
        double current = getBalance(playerUUID);
        if (current >= amount) {
            balances.put(playerUUID, current - amount);
            return true;
        } else {
            return false;
        }
    }

    // Registrar jugador
    public void registerPlayer(UUID playerUUID) {
        balances.putIfAbsent(playerUUID, 0.0);
    }

    // Comprobar si el jugador existe
    public boolean hasPlayer(UUID playerUUID) {
        return balances.containsKey(playerUUID);
    }

    // Establecer balance directamente
    public void setBalance(UUID playerUUID, double amount) {
        balances.put(playerUUID, amount);
    }
}
