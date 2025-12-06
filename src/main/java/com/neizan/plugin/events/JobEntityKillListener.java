package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.UUID;

public class JobEntityKillListener implements Listener {

    private final JobManager jobManager;

    public JobEntityKillListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        UUID uuid = killer.getUniqueId();

        if (!jobManager.hasJob(uuid)) return;

        List<Job> playerJobs = jobManager.getJobs(uuid);
        for (Job job : playerJobs) {
            if (job.getJobType() == JobsEnum.ASESINO) {
                double reward = 10.0;
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(uuid, reward);
                killer.sendMessage("Has ganado $" + reward + " por tu trabajo de " + job.getJobType().getNombre());
            }
        }
    }
}
