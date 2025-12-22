package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import com.neizan.plugin.jobs.JobRewardService;
import com.neizan.plugin.jobs.RewardEntry;
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

        List<Job> jobs = jobManager.getJobs(player.getName());
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.PESCADOR) {
                RewardEntry rewardEntry = JobRewardService.findFishingReward(event).orElse(null);
                if (rewardEntry == null) continue;

                double reward = rewardEntry.getPay();
                double xpBase = JobRewardService.calculateScaledXp(job.getJobType(), reward);

                JobRewardService.grantReward(player, job, jobManager, reward, xpBase, "por pescar " + rewardEntry.getTitle());
            }
        }
    }
}
