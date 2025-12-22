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

public class JobHarvestListener implements Listener {

    private final JobManager jobManager;

    public JobHarvestListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();

        List<Job> jobs = jobManager.getJobs(player.getName());
        if (jobs.isEmpty()) return;

        for (Job job : jobs) {
            if (job.getJobType() == JobsEnum.GRANJERO) {
                Block block = event.getBlock();
                if (block.hasMetadata(JobRewardService.PLACED_METADATA)) continue; // Anti-exploit cultivos colocados

                RewardEntry rewardEntry = JobRewardService.findHarvestReward(block.getType(), block).orElse(null);
                if (rewardEntry == null) continue;

                double reward = rewardEntry.getPay();
                double xpBase = JobRewardService.calculateScaledXp(job.getJobType(), reward);

                JobRewardService.grantReward(player, job, jobManager, reward, xpBase, "por cosechar " + rewardEntry.getTitle());

                block.removeMetadata(JobRewardService.PLACED_METADATA, JobRewardService.getPlugin());
            }
        }
    }
}
