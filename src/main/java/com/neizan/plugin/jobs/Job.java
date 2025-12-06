package com.neizan.plugin.jobs;

import java.util.UUID;

public class Job {
    private final UUID playerUUID;
    private final JobsEnum jobType;
    private double balance; // dinero ganado en este trabajo
    private int level;      // nivel del trabajo
    private double xp;      // experiencia acumulada

    public Job(UUID playerUUID, JobsEnum jobType) {
        this.playerUUID = playerUUID;
        this.jobType = jobType;
        this.balance = 0.0;
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

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public void addBalance(double amount) {
        balance += amount;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public void addXp(double amount) {
        xp += amount;
        // Subida de nivel
        while (xp >= xpToNextLevel()) {
            xp -= xpToNextLevel();
            level++;
            if (level > 100) { // nivel máximo
                level = 100;
                xp = 0;
                break;
            }
        }
    }

    // Experiencia necesaria para el siguiente nivel (curva progresiva)
    public double xpToNextLevel() {
        return 50 + (level - 1) * 20; // ejemplo: aumenta 20 XP por nivel
    }

    // Multiplicador de dinero según nivel
    public double getMultiplier() {
        return 1.0 + (level - 1) * 0.05; // 5% más por nivel
    }
}
