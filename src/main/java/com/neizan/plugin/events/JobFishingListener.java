package com.neizan.plugin.events;

import com.neizan.plugin.Main;
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

        JobsEnum job = jobManager.getJob(player.getUniqueId()).getJobType();
        if (job != JobsEnum.PESCADOR) return;

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            double reward = 3.0; // ejemplo
            jobManager.getJob(player.getUniqueId()).addBalance(reward);
            Main.getInstance().getEconomyManager().addBalance(player.getUniqueId(), reward);
            player.sendMessage("Has ganado $" + reward + " por pescar como Pescador");
        }
    }
}
