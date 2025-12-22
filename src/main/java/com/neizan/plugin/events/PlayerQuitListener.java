package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class PlayerQuitListener implements Listener {

    private final JobManager jobManager;

    public PlayerQuitListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Job job : jobManager.getJobs(player.getName())) {
            jobManager.updateJob(job);
        }
    }
}
