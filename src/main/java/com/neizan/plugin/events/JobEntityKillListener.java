package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class JobEntityKillListener implements Listener {

    private final JobManager jobManager;

    public JobEntityKillListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        if (!jobManager.hasJob(killer.getUniqueId())) return;

        for (Job job : jobManager.getJobs(killer.getUniqueId())) {
            JobsEnum jobType = job.getJobType();
            double reward = 0;
            double xpGain = 0;

            if (jobType == JobsEnum.ASESINO) {
                reward = 10.0;
                xpGain = 8.0;
            }

            if (reward > 0) {
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(killer.getUniqueId(), reward);

                job.addXp(xpGain);

                killer.sendMessage("Has ganado $" + reward + " y " + xpGain + " XP en tu trabajo de " + jobType.getNombre());
            }
        }
    }
}
