package com.neizan.plugin.jobs;

import java.util.UUID;

public class Job {

    private final UUID playerUUID;
    private final JobsEnum jobType;
    private double balance;
    private int level;
    private double xp;

    // Base de XP para subir de nivel 1 → puedes ajustarla según el trabajo
    private final double baseXp = 10.0;

    public Job(UUID playerUUID, JobsEnum jobType) {
        this.playerUUID = playerUUID;
        this.jobType = jobType;
        this.balance = 0;
        this.level = 1;
        this.xp = 0;
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
        this.balance += amount * (1 + 0.05 * (level - 1)); // bonus por nivel
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public void addXp(double amount) {
        this.xp += amount;
        // Si alcanza XP necesaria, subir nivel
        while (xp >= xpToNextLevel() && level < 100) {
            xp -= xpToNextLevel();
            level++;
        }
    }

    // XP necesario para subir de nivel
    public double xpToNextLevel() {
        return baseXp * Math.pow(1.1, level - 1); // sube cada vez más
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }
}
