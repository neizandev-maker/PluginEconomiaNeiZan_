package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.UUID;

public class JobFishingListener implements Listener {

    private final JobManager jobManager;

    public JobFishingListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!jobManager.hasJob(playerName)) return;

        for (Job job : jobManager.getJobs(playerName)) {
            if (job.getJobType() != JobsEnum.PESCADOR) continue;

            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                double reward = job.getJobType().getBaseReward();
                double xpGain = job.getJobType().getBaseXp();
                job.addBalance(reward);
                job.addXp(xpGain);
                Main.getInstance().getEconomyManager().addBalance(UUID.fromString(playerName), reward);
            }
        }
    }
}
