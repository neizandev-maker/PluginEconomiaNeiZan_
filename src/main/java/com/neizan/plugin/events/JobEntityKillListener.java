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
            if (job.getJobType() == JobsEnum.ASESINO) {
                double reward = job.getJobType().getBaseReward();
                double xpGain = job.getJobType().getBaseXp();
                job.addBalance(reward);
                job.addXp(xpGain);
                Main.getInstance().getEconomyManager().addBalance(killer.getUniqueId(), reward);
            }
        }
    }
}
