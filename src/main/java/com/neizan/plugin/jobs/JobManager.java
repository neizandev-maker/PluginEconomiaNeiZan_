package com.neizan.plugin.jobs;

import java.util.*;

public class JobManager {

    private final HashMap<UUID, List<Job>> jobs;

    public JobManager() {
        this.jobs = new HashMap<>();
    }

    public boolean addJob(UUID playerUUID, JobsEnum jobType) {
        List<Job> playerJobs = jobs.getOrDefault(playerUUID, new ArrayList<>());

        if (playerJobs.size() >= 3) return false; // máximo 3 trabajos

        for (Job j : playerJobs) {
            if (j.getJobType() == jobType) return false; // ya tiene este trabajo
        }

        Job newJob = new Job(playerUUID, jobType);
        playerJobs.add(newJob);
        jobs.put(playerUUID, playerJobs);
        return true;
    }

    public List<Job> getJobs(UUID playerUUID) {
        return jobs.getOrDefault(playerUUID, new ArrayList<>());
    }

    public boolean hasJob(UUID playerUUID) {
        return jobs.containsKey(playerUUID) && !jobs.get(playerUUID).isEmpty();
    }

    public Job getJob(UUID playerUUID, JobsEnum jobType) {
        for (Job j : getJobs(playerUUID)) {
            if (j.getJobType() == jobType) return j;
        }
        return null;
    }

    public boolean removeJob(UUID playerUUID, JobsEnum jobType) {
        List<Job> playerJobs = jobs.getOrDefault(playerUUID, new ArrayList<>());
        boolean removed = playerJobs.removeIf(j -> j.getJobType() == jobType);
        if (removed) jobs.put(playerUUID, playerJobs);
        return removed;
    }
}
