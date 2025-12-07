package com.neizan.plugin.jobs;

import com.neizan.plugin.database.MySQLManager;

import java.sql.*;
import java.util.*;

public class JobManager {

    private final MySQLManager mySQL;

    public JobManager(MySQLManager mySQL) {
        this.mySQL = mySQL;
    }

    public List<Job> getJobs(String playerName) {
        List<Job> list = new ArrayList<>();
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "SELECT * FROM player_jobs WHERE player_name=?");
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JobsEnum type = JobsEnum.valueOf(rs.getString("job"));
                Job job = new Job(playerName, type);
                job.setLevel(rs.getInt("level"));
                job.setXp(rs.getDouble("xp"));
                job.setBalance(rs.getDouble("balance"));
                list.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasJob(String playerName) {
        return !getJobs(playerName).isEmpty();
    }

    public boolean addJob(String playerName, JobsEnum type) {
        if (getJobs(playerName).size() >= 3) return false;

        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "INSERT INTO player_jobs VALUES (?,?,?,?,?)");
            ps.setString(1, playerName);
            ps.setString(2, type.name());
            ps.setInt(3, 1);
            ps.setDouble(4, 0);
            ps.setDouble(5, 0);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeJob(String playerName, JobsEnum type) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "DELETE FROM player_jobs WHERE player_name=? AND job=?");
            ps.setString(1, playerName);
            ps.setString(2, type.name());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Job getJob(String playerName, JobsEnum type) {
        for (Job j : getJobs(playerName)) {
            if (j.getJobType() == type) return j;
        }
        return null;
    }

    public void saveJob(Job job) {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement(
                    "REPLACE INTO player_jobs VALUES (?,?,?,?,?)");
            ps.setString(1, job.getPlayerName());
            ps.setString(2, job.getJobType().name());
            ps.setInt(3, job.getLevel());
            ps.setDouble(4, job.getXp());
            ps.setDouble(5, job.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
