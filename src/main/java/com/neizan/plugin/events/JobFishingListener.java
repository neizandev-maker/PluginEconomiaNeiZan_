package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class JobFishingListener implements Listener {

    private final JobManager jobManager;

    public JobFishingListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!jobManager.hasJob(player.getUniqueId())) return;

        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        for (Job job : jobManager.getJobs(player.getUniqueId())) {
            JobsEnum jobType = job.getJobType();
            if (jobType != JobsEnum.PESCADOR) continue;

            double reward = 3.0;
            double xpGain = 2.0;

            job.addBalance(reward);
            Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);

            job.addXp(xpGain);

            player.sendMessage("Has ganado $" + reward + " y " + xpGain + " XP por pescar como " + jobType.getNombre());
        }
    }
}
