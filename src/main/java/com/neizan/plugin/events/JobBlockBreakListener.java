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

public class JobBlockBreakListener implements Listener {

    private final JobManager jobManager;

    public JobBlockBreakListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!jobManager.hasJob(player.getUniqueId())) return;

        for (Job job : jobManager.getJobs(player.getUniqueId())) {
            JobsEnum type = job.getJobType();
            double reward = 0;
            double xpGain = 0;

            Block block = event.getBlock();
            switch (type) {
                case EXCAVADOR:
                    if (block.getType() == Material.DIRT || block.getType() == Material.SAND || block.getType() == Material.GRAVEL) {
                        reward = type.getBaseReward();
                        xpGain = type.getBaseXp() / 2;
                    }
                    break;
                case MINERO:
                    if (block.getType().name().contains("ORE")) {
                        reward = type.getBaseReward();
                        xpGain = type.getBaseXp() / 2;
                    }
                    break;
            }

            if (reward > 0) {
                job.addBalance(reward);
                job.addXp(xpGain);
                Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);
            }
        }
    }
}
