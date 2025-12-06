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

        playerDataFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playerDataFile.exists()) {
            try { playerDataFile.createNewFile(); }
            catch (IOException e) { e.printStackTrace(); }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        plugin.getEconomyManager().registerPlayer(uuid);

        if (playerDataConfig.contains(uuid.toString())) {
            double balance = playerDataConfig.getDouble(uuid + ".balance");
            plugin.getEconomyManager().setBalance(uuid, balance);

            // Cargar hasta 3 trabajos
            for (int i = 0; i < 3; i++) {
                if (playerDataConfig.contains(uuid + ".job" + i + ".type")) {
                    String jobName = playerDataConfig.getString(uuid + ".job" + i + ".type");
                    double jobBalance = playerDataConfig.getDouble(uuid + ".job" + i + ".balance");
                    double jobXp = playerDataConfig.getDouble(uuid + ".job" + i + ".xp");
                    int jobLevel = playerDataConfig.getInt(uuid + ".job" + i + ".level");

                    JobsEnum jobEnum = JobsEnum.valueOf(jobName);
                    jobManager.addJob(uuid, jobEnum);
                    Job job = jobManager.getJob(uuid, jobEnum);
                    job.setBalance(jobBalance);
                    job.setXp(jobXp);
                    job.setLevel(jobLevel);
                }
            }
        }
    }
}
