package com.neizan.plugin;

import com.neizan.plugin.commands.BalanceCommand;
import com.neizan.plugin.commands.JobStatsCommand;
import com.neizan.plugin.commands.PayCommand;
import com.neizan.plugin.commands.WorkCommand;
import com.neizan.plugin.economy.EconomyManager;
import com.neizan.plugin.events.*;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private EconomyManager economyManager;
    private JobManager jobManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Inicializar economía y JobManager
        economyManager = new EconomyManager();
        jobManager = new JobManager();

        // Registrar comandos
        this.getCommand("balance").setExecutor(new BalanceCommand());
        this.getCommand("pay").setExecutor(new PayCommand());
        this.getCommand("work").setExecutor(new WorkCommand(jobManager));
        this.getCommand("jobstats").setExecutor(new JobStatsCommand());

        // Registrar listeners
        getServer().getPluginManager().registerEvents(new WorkClickListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobBlockBreakListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobEntityKillListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobHarvestListener(jobManager), this);
        getServer().getPluginManager().registerEvents(new JobFishingListener(jobManager), this);

        getLogger().info("PluginEconomiaNeiZan habilitado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginEconomiaNeiZan deshabilitado!");
    }

    // Getter para la instancia
    public static Main getInstance() {
        return instance;
    }

    // Getter para economía
    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    // Getter para JobManager
    public JobManager getJobManager() {
        return jobManager;
    }
}
