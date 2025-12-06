package com.neizan.plugin.jobs;

import java.util.*;

public class JobManager {

    // Cada jugador puede tener hasta 3 trabajos
    private final HashMap<UUID, List<Job>> jobs;

    public JobManager() {
        this.jobs = new HashMap<>();
    }

    // Añadir un trabajo
    public boolean addJob(UUID playerUUID, JobsEnum jobType) {
        List<Job> playerJobs = jobs.getOrDefault(playerUUID, new ArrayList<>());

        // Si ya tiene el trabajo
        for (Job j : playerJobs) {
            if (j.getJobType() == jobType) return false;
        }

        // Limite de 3 trabajos
        if (playerJobs.size() >= 3) return false;

        Job newJob = new Job(playerUUID, jobType);
        playerJobs.add(newJob);
        jobs.put(playerUUID, playerJobs);
        return true;
    }

    // Obtener lista de trabajos
    public List<Job> getJobs(UUID playerUUID) {
        return jobs.getOrDefault(playerUUID, new ArrayList<>());
    }

    // Ver si tiene algún trabajo
    public boolean hasJob(UUID playerUUID) {
        return jobs.containsKey(playerUUID) && !jobs.get(playerUUID).isEmpty();
    }

    // Obtener trabajo específico por tipo
    public Job getJob(UUID playerUUID, JobsEnum jobType) {
        List<Job> playerJobs = jobs.getOrDefault(playerUUID, new ArrayList<>());
        for (Job j : playerJobs) {
            if (j.getJobType() == jobType) return j;
        }
        return null;
    }

    // Eliminar un trabajo
    public boolean removeJob(UUID playerUUID, JobsEnum jobType) {
        List<Job> playerJobs = jobs.getOrDefault(playerUUID, new ArrayList<>());
        boolean removed = playerJobs.removeIf(j -> j.getJobType() == jobType);
        if (removed) jobs.put(playerUUID, playerJobs);
        return removed;
    }

    // Para guardar todos los trabajos
    public HashMap<UUID, List<Job>> getAllJobs() {
        return jobs;
    }
}
