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

public class JobHarvestListener implements Listener {

    private final JobManager jobManager;

    public JobHarvestListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!jobManager.hasJob(player.getUniqueId())) return;

        for (Job job : jobManager.getJobs(player.getUniqueId())) {
            JobsEnum jobType = job.getJobType();
            if (jobType != JobsEnum.GRANJERO) continue;

            Block block = event.getBlock();
            double reward = 0;
            double xpGain = 0;

            if (block.getType() == Material.WHEAT || block.getType() == Material.CARROTS ||
                    block.getType() == Material.POTATOES || block.getType() == Material.BEETROOTS) {
                reward = 2.0;
                xpGain = 3.0;
            }

            if (reward > 0) {
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);

                job.addXp(xpGain);

                player.sendMessage("Has ganado $" + reward + " y " + xpGain + " XP por cosechar como " + jobType.getNombre());
            }
        }
    }
}
