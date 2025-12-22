package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import com.neizan.plugin.jobs.JobRewardService;
import com.neizan.plugin.jobs.RewardEntry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class JobBlockBreakListener implements Listener {

    private final JobManager jobManager;

    public JobBlockBreakListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        List<Job> jobs = jobManager.getJobs(player.getName());
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.EXCAVADOR || job.getJobType() == JobsEnum.MINERO) {
                Block block = event.getBlock();
                if (block.hasMetadata(JobRewardService.PLACED_METADATA)) continue; // Anti-exploit: bloques colocados por jugadores

                RewardEntry rewardEntry = JobRewardService.findBlockReward(job.getJobType(), block.getType()).orElse(null);
                if (rewardEntry == null) continue;

                double reward = rewardEntry.getPay();
                double xpBase = JobRewardService.calculateScaledXp(job.getJobType(), reward);

                JobRewardService.grantReward(player, job, jobManager, reward, xpBase, "por romper " + rewardEntry.getTitle());

                block.removeMetadata(JobRewardService.PLACED_METADATA, JobRewardService.getPlugin());
            }
        }
    }
}
