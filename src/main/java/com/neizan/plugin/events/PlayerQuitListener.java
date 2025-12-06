package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final JobManager jobManager;
    private final Main plugin;
    private final File playerDataFile;
    private final FileConfiguration playerDataConfig;

    // ✅ Recibir JobManager en el constructor
    public PlayerQuitListener(JobManager jobManager) {
        this.jobManager = jobManager;
        this.plugin = Main.getInstance();

        // Crear archivo players.yml si no existe
        playerDataFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Guardar balance
        double balance = plugin.getEconomyManager().getBalance(uuid);
        playerDataConfig.set(uuid + ".balance", balance);

        // Guardar trabajo
        if (jobManager.hasJob(uuid)) {
            Job job = jobManager.getJob(uuid);
            playerDataConfig.set(uuid + ".job", job.getJobType().name());
            playerDataConfig.set(uuid + ".jobBalance", job.getBalance());
        }

        // Guardar archivo
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
