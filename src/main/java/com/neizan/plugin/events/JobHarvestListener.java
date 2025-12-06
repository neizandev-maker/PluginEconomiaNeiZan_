package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.UUID;

public class JobHarvestListener implements Listener {

    private final JobManager jobManager;

    public JobHarvestListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!jobManager.hasJob(uuid)) return;

        Block block = event.getBlock();
        List<Job> playerJobs = jobManager.getJobs(uuid);

        for (Job job : playerJobs) {
            if (job.getJobType() != JobsEnum.GRANJERO) continue;

            if (block.getType() == Material.WHEAT || block.getType() == Material.CARROTS ||
                    block.getType() == Material.POTATOES || block.getType() == Material.BEETROOTS) {

                double reward = 2.0;
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(uuid, reward);
                player.sendMessage("Has ganado $" + reward + " por cosechar como Granjero");
            }
        }
    }
}
