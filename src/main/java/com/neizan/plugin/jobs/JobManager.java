package com.neizan.plugin.jobs;

import com.neizan.plugin.database.MySQLManager;

import java.sql.*;
import java.util.*;

public class JobManager {

    private final MySQLManager mySQL;
    // Map de jugador -> (JobType -> Job)
    private final Map<String, Map<JobsEnum, Job>> jobsMap = new HashMap<>();

    public JobManager(MySQLManager mySQL) {
        this.mySQL = mySQL;
        createTable();
    }

    private void createTable() {
        try (Statement stmt = mySQL.getConnection().createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                    "playerName VARCHAR(16) NOT NULL," +
                    "jobName VARCHAR(50) NOT NULL," +
                    "level INT NOT NULL DEFAULT 1," +
                    "xp DOUBLE NOT NULL DEFAULT 0," +
                    "balance DOUBLE NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (playerName, jobName)" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtener todos los trabajos del jugador
    public List<Job> getJobs(String playerName) {
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "SELECT * FROM players WHERE playerName=?")) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JobsEnum type = JobsEnum.valueOf(rs.getString("jobName"));
                Job job = new Job(playerName, type);
                job.setLevel(rs.getInt("level"));
                job.setXp(rs.getDouble("xp"));
                job.setBalance(rs.getDouble("balance"));

                jobs.add(job);
                jobsMap.computeIfAbsent(playerName, k -> new HashMap<>()).put(type, job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public boolean isPlayerInJob(String playerName, JobsEnum job) {
        Map<JobsEnum, Job> playerJobs = jobsMap.get(playerName);
        return playerJobs != null && playerJobs.containsKey(job);
    }

    public Job getJob(String playerName, JobsEnum jobType) {
        Map<JobsEnum, Job> playerJobs = jobsMap.get(playerName);
        if (playerJobs != null && playerJobs.containsKey(jobType)) {
            return playerJobs.get(jobType);
        }

        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "SELECT * FROM players WHERE playerName=? AND jobName=?")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Job job = new Job(playerName, jobType);
                job.setLevel(rs.getInt("level"));
                job.setXp(rs.getDouble("xp"));
                job.setBalance(rs.getDouble("balance"));

                jobsMap.computeIfAbsent(playerName, k -> new HashMap<>()).put(jobType, job);
                return job;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addJob(String playerName, JobsEnum jobType) {
        if (getJob(playerName, jobType) != null) return;

        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "INSERT INTO players (playerName, jobName, level, xp, balance) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            ps.setInt(3, 1);
            ps.setDouble(4, 0);
            ps.setDouble(5, 0);
            ps.executeUpdate();

            Job job = new Job(playerName, jobType);
            jobsMap.computeIfAbsent(playerName, k -> new HashMap<>()).put(jobType, job);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeJob(String playerName, JobsEnum jobType) {
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "DELETE FROM players WHERE playerName=? AND jobName=?")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            boolean removed = ps.executeUpdate() > 0;

            Map<JobsEnum, Job> playerJobs = jobsMap.get(playerName);
            if (playerJobs != null) {
                playerJobs.remove(jobType);
                if (playerJobs.isEmpty()) jobsMap.remove(playerName);
            }

            return removed;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveJob(Job job) {
        updateJob(job);
    }

    public void updateJob(Job job) {
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "UPDATE players SET level=?, xp=?, balance=? WHERE playerName=? AND jobName=?")) {
            ps.setInt(1, job.getLevel());
            ps.setDouble(2, job.getXp());
            ps.setDouble(3, job.getBalance());
            ps.setString(4, job.getPlayerName());
            ps.setString(5, job.getJobType().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
