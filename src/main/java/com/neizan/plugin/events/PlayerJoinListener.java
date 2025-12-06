package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Main plugin;
    private final JobManager jobManager;
    private final File playerDataFile;
    private final FileConfiguration playerDataConfig;

    public PlayerJoinListener() {
        this.plugin = Main.getInstance();
        this.jobManager = plugin.getJobManager();

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
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Registrar jugador en economía si no existe
        if (!plugin.getEconomyManager().hasPlayer(uuid)) {
            plugin.getEconomyManager().registerPlayer(uuid);
        }

        // Cargar datos si existen
        if (playerDataConfig.contains(uuid.toString())) {
            double balance = playerDataConfig.getDouble(uuid + ".balance");
            plugin.getEconomyManager().setBalance(uuid, balance);

            if (playerDataConfig.contains(uuid + ".job")) {
                String jobName = playerDataConfig.getString(uuid + ".job");
                double jobBalance = playerDataConfig.getDouble(uuid + ".jobBalance");

                JobsEnum jobEnum = JobsEnum.valueOf(jobName);
                Job job = new Job(uuid, jobEnum);
                job.setBalance(jobBalance);

                jobManager.setJob(uuid, job);
            }
        }
    }
}
