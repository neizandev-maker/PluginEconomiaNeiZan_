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

        // Iterar por todos los trabajos del jugador
        for (Job job : jobManager.getJobs(player.getUniqueId())) {
            JobsEnum jobType = job.getJobType();
            double reward = 0;
            double xpGain = 0;

            Block block = event.getBlock();
            switch (jobType) {
                case EXCAVADOR:
                    if (block.getType() == Material.DIRT ||
                            block.getType() == Material.SAND ||
                            block.getType() == Material.GRAVEL) {
                        reward = 1.0;
                        xpGain = 2.0;
                    }
                    break;
                case MINERO:
                    if (block.getType().name().contains("ORE")) {
                        reward = 5.0;
                        xpGain = 5.0;
                    }
                    break;
            }

            if (reward > 0) {
                // Añadir dinero con bonus de nivel
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);

                // Añadir XP y subir de nivel si corresponde
                job.addXp(xpGain);

                player.sendMessage("Has ganado $" + reward + " y " + xpGain + " XP en tu trabajo de " + jobType.getNombre());
            }
        }
    }
}
