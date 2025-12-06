package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class JobBlockBreakListener implements Listener {

    private final JobManager jobManager;

    public JobBlockBreakListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!jobManager.hasJob(uuid)) return;

        List<Job> playerJobs = jobManager.getJobs(uuid);
        for (Job job : playerJobs) {
            double reward = 0;

            switch (job.getJobType()) {
                case EXCAVADOR:
                    if (event.getBlock().getType() == Material.DIRT ||
                            event.getBlock().getType() == Material.SAND ||
                            event.getBlock().getType() == Material.GRAVEL) {
                        reward = 1.0;
                    }
                    break;
                case MINERO:
                    if (event.getBlock().getType().name().contains("ORE")) {
                        reward = 5.0;
                    }
                    break;
                default:
                    continue;
            }

            if (reward > 0) {
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(uuid, reward);
                player.sendMessage("Has ganado $" + reward + " por tu trabajo de " + job.getJobType().getNombre());
            }
        }
    }
}
