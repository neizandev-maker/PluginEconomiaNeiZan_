package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class JobBlockBreakListener implements Listener {

    private final JobManager jobManager;

    public JobBlockBreakListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        List<Job> jobs = jobManager.getJobs(playerName);
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.EXCAVADOR) {
                double reward = job.getJobType().getBaseReward();
                double xpGain = job.getJobType().getBaseXp();
                int oldLevel = job.getLevel();

                job.addBalance(reward);
                job.addXp(xpGain);

                // Guardamos cambios del trabajo inmediatamente
                jobManager.updateJob(job);

                // Sumar dinero al balance general
                Main.getInstance().getEconomyManager().addBalance(playerName, reward);

                // Feedback al jugador
                player.sendMessage("§aHas ganado $" + reward + " y " + xpGain + " XP como " + job.getJobType().getNombre());
                if (job.getLevel() > oldLevel) {
                    player.sendMessage("§6¡Has subido al nivel " + job.getLevel() + "!");
                }
            }
        }
    }
}
