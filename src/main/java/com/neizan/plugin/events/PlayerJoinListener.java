package com.neizan.plugin.events;

import com.neizan.plugin.economy.EconomyManager;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final EconomyManager economy;
    private final JobManager jobManager;

    public PlayerJoinListener(EconomyManager economy, JobManager jobManager) {
        this.economy = economy;
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Registramos al jugador usando SU NOMBRE
        String playerName = event.getPlayer().getName();
        economy.registerPlayer(playerName);
    }
}
