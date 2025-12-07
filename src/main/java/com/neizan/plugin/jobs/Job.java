package com.neizan.plugin.jobs;

import java.util.UUID;

public class Job {

    private final UUID playerUuid;
    private final JobsEnum jobType;

    private double balance;
    private double xp;
    private int level;

    public Job(UUID playerUuid, JobsEnum jobType) {
        this.playerUuid = playerUuid;
        this.jobType = jobType;
        this.balance = 0;
        this.xp = 0;
        this.level = 1;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public JobsEnum getJobType() {
        return jobType;
    }

    // ───── DINERO ─────

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount * applyLevelMultiplier();
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // ───── XP Y NIVEL ─────

    public double getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addXp(double amount) {
        xp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (xp >= getXpToNextLevel() && level < 100) {
            xp -= getXpToNextLevel();
            level++;
        }
    }

    public double getXpToNextLevel() {
        return jobType.getBaseXp() * Math.pow(1.1, level - 1);
    }

    public double applyLevelMultiplier() {
        return 1 + (level - 1) * 0.05; // +5% por nivel
    }
}
