package com.neizan.plugin;

import com.neizan.plugin.commands.*;
import com.neizan.plugin.database.MySQLManager;
import com.neizan.plugin.economy.EconomyManager;
import com.neizan.plugin.events.*;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private EconomyManager economyManager;
    private JobManager jobManager;
    private MySQLManager mySQL;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        mySQL = new MySQLManager();
        mySQL.connect("localhost", "jobs", "root", "root");

        economyManager = new EconomyManager(mySQL);
        jobManager = new JobManager(mySQL);

        // Comandos
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("work").setExecutor(new WorkCommand(jobManager));
        getCommand("jobstats").setExecutor(new JobStatsCommand());
        getCommand("jobs").setExecutor(new JobsCommand(jobManager));
        getCommand("removejob").setExecutor(new RemoveJobCommand(jobManager));
        getCommand("jobinfo").setExecutor(new JobInfoCommand(jobManager));

        // Eventos
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(economyManager, jobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobBlockBreakListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobEntityKillListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobHarvestListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobFishingListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new WorkClickListener(jobManager), this);

        getLogger().info("PLUGIN NEIZAN MYSQL ACTIVADO");
    }

    public static Main getInstance() {
        return instance;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}
