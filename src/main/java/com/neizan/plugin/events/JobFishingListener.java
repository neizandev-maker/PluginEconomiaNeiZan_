package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class JobFishingListener implements Listener {

    private final JobManager jobManager;

    public JobFishingListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        List<Job> jobs = jobManager.getJobs(playerName);
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.PESCADOR) {
                double reward = job.getJobType().getBaseReward();
                double xpGain = job.getJobType().getBaseXp();
                int oldLevel = job.getLevel();

                job.addBalance(reward);
                job.addXp(xpGain);
                jobManager.updateJob(job);

                Main.getInstance().getEconomyManager().addBalance(playerName, reward);

                player.sendMessage("§aHas ganado $" + reward + " y " + xpGain + " XP como " + job.getJobType().getNombre());
                if (job.getLevel() > oldLevel) {
                    player.sendMessage("§6¡Has subido al nivel " + job.getLevel() + "!");
                }
            }
        }
    }
}
