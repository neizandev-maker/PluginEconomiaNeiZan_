package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import com.neizan.plugin.jobs.JobRewardService;
import com.neizan.plugin.jobs.RewardEntry;
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

        List<Job> jobs = jobManager.getJobs(killer.getName());
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.ASESINO) {
                RewardEntry rewardEntry = JobRewardService.findKillReward(event.getEntity().getType()).orElse(null);
                if (rewardEntry == null) continue;

                double reward = rewardEntry.getPay();
                double xpBase = JobRewardService.calculateScaledXp(job.getJobType(), reward);

                JobRewardService.grantReward(killer, job, jobManager, reward, xpBase, "por matar " + rewardEntry.getTitle());
            }
        }
    }
}
