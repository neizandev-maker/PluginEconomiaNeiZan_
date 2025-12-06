package com.neizan.plugin.events;

import com.neizan.plugin.Main;
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

        JobsEnum job = jobManager.getJob(player.getUniqueId()).getJobType();
        if (job != JobsEnum.GRANJERO) return;

        Block block = event.getBlock();
        if (block.getType() == Material.WHEAT || block.getType() == Material.CARROTS ||
                block.getType() == Material.POTATOES || block.getType() == Material.BEETROOTS) {
            double reward = 2.0; // ejemplo
            jobManager.getJob(player.getUniqueId()).addBalance(reward);
            Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);
            player.sendMessage("Has ganado $" + reward + " por cosechar como Granjero");
        }
    }
}
