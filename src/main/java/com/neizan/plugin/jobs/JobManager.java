package com.neizan.plugin.jobs;

import java.util.HashMap;
import java.util.UUID;

public class JobManager {

    private final HashMap<UUID, Job> jobs;

    public JobManager() {
        jobs = new HashMap<>();
    }

    // Asignar trabajo a jugador
    public void setJob(UUID playerUUID, JobsEnum jobType) {
        jobs.put(playerUUID, new Job(playerUUID, jobType));
    }

    public void setJob(UUID playerUUID, Job job) {
        jobs.put(playerUUID, job); // suponiendo que 'jobs' es tu HashMap<UUID, Job>
    }

    public Job getJob(UUID playerUUID) {
        return jobs.get(playerUUID);
    }

    public boolean hasJob(UUID playerUUID) {
        return jobs.containsKey(playerUUID);
    }
}
