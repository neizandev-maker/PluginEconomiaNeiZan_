package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final JobManager jobManager;

    public PlayerQuitListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        for (Job job : jobManager.getJobs(playerName)) {
            jobManager.saveJob(job);
        }
    }
}
