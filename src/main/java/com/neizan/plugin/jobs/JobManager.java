package com.neizan.plugin.jobs;

import com.neizan.plugin.database.MySQLManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobManager {

    private final MySQLManager mySQL;

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

    public List<Job> getJobs(String playerName) {
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "SELECT * FROM players WHERE playerName=?")) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JobsEnum type = JobsEnum.valueOf(rs.getString("jobName"));
                int level = rs.getInt("level");
                double xp = rs.getDouble("xp");
                double balance = rs.getDouble("balance");

                Job job = new Job(playerName, type);
                job.setLevel(level);
                job.setXp(xp);
                job.setBalance(balance);

                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public Job getJob(String playerName, JobsEnum jobType) {
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "SELECT * FROM players WHERE playerName=? AND jobName=?")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int level = rs.getInt("level");
                double xp = rs.getDouble("xp");
                double balance = rs.getDouble("balance");

                Job job = new Job(playerName, jobType);
                job.setLevel(level);
                job.setXp(xp);
                job.setBalance(balance);

                return job;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addJob(String playerName, JobsEnum jobType) {
        if (getJob(playerName, jobType) != null) return; // ya tiene el trabajo
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "INSERT INTO players (playerName, jobName, level, xp, balance) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            ps.setInt(3, 1);     // nivel inicial
            ps.setDouble(4, 0);  // xp inicial
            ps.setDouble(5, 0);  // balance inicial
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeJob(String playerName, JobsEnum jobType) {
        try (PreparedStatement ps = mySQL.getConnection().prepareStatement(
                "DELETE FROM players WHERE playerName=? AND jobName=?")) {
            ps.setString(1, playerName);
            ps.setString(2, jobType.name());
            return ps.executeUpdate() > 0;
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
