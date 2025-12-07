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

public class JobEntityKillListener implements Listener {

    private final JobManager jobManager;

    public JobEntityKillListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        String playerName = killer.getName();
        List<Job> jobs = jobManager.getJobs(playerName);
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.ASESINO) {
                double reward = job.getJobType().getBaseReward();
                double xpGain = job.getJobType().getBaseXp() * 0.4; // XP reducido
                int oldLevel = job.getLevel();

                job.addBalance(reward);
                job.addXp(xpGain);
                jobManager.updateJob(job);

                Main.getInstance().getEconomyManager().addBalance(playerName, reward);

                killer.sendMessage("§aHas ganado $" + reward + " y " + xpGain + " XP como " + job.getJobType().getNombre());
                if (job.getLevel() > oldLevel) {
                    killer.sendMessage("§6¡Has subido al nivel " + job.getLevel() + "!");
                }
            }
        }
    }
}
