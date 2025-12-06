package com.neizan.plugin.jobs;

import java.util.UUID;

public class Job {
    private final UUID playerUUID;
    private final JobsEnum jobType;
    private double balance; // dinero ganado en este trabajo

    public Job(UUID playerUUID, JobsEnum jobType) {
        this.playerUUID = playerUUID;
        this.jobType = jobType;
        this.balance = 0.0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public JobsEnum getJobType() {
        return jobType;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    // Nuevo método para setear balance directamente
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
