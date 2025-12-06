package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

public class JobBlockBreakListener implements Listener {

    private final JobManager jobManager;

    public JobBlockBreakListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!jobManager.hasJob(player.getUniqueId())) return;

        JobsEnum job = jobManager.getJob(player.getUniqueId()).getJobType();

        double reward = 0;

        switch (job) {
            case EXCAVADOR:
                if (event.getBlock().getType() == Material.DIRT ||
                        event.getBlock().getType() == Material.SAND ||
                        event.getBlock().getType() == Material.GRAVEL) {
                    reward = 1.0; // ejemplo
                }
                break;
            case MINERO:
                if (event.getBlock().getType().name().contains("ORE")) {
                    reward = 5.0; // ejemplo
                }
                break;
        }

        if (reward > 0) {
            jobManager.getJob(player.getUniqueId()).addBalance(reward);
            Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);
            player.sendMessage("Has ganado $" + reward + " por tu trabajo de " + job.getNombre());
        }
    }
}
