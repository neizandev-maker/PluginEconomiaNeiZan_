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

import java.util.UUID;

public class JobHarvestListener implements Listener {

    private final JobManager jobManager;

    public JobHarvestListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!jobManager.hasJob(playerName)) return;

        Block block = event.getBlock();

        for (Job job : jobManager.getJobs(playerName)) {
            if (job.getJobType() != JobsEnum.GRANJERO) continue;

            double reward = 0;
            double xpGain = 0;

            if (block.getType() == Material.WHEAT || block.getType() == Material.CARROTS ||
                    block.getType() == Material.POTATOES || block.getType() == Material.BEETROOTS) {
                reward = job.getJobType().getBaseReward();
                xpGain = job.getJobType().getBaseXp() / 2;
            }

            if (reward > 0) {
                job.addBalance(reward);
                job.addXp(xpGain);
                Main.getInstance().getEconomyManager().addBalance(UUID.fromString(playerName), reward);
            }
        }
    }
}
