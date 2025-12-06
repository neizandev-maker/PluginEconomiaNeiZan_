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
import java.util.UUID;

public class JobFishingListener implements Listener {

    private final JobManager jobManager;

    public JobFishingListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!jobManager.hasJob(uuid)) return;

        List<Job> playerJobs = jobManager.getJobs(uuid);
        for (Job job : playerJobs) {
            if (job.getJobType() == JobsEnum.PESCADOR && event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                double reward = 3.0; // ejemplo
                job.addBalance(reward);
                Main.getInstance().getEconomyManager().addBalance(uuid, reward);
                player.sendMessage("Has ganado $" + reward + " por pescar como Pescador");
            }
        }
    }
}
