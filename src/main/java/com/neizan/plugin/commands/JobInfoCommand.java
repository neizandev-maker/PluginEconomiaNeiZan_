package com.neizan.plugin.commands;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JobInfoCommand implements CommandExecutor {

    private final JobManager jobManager;

    public JobInfoCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede usarlo un jugador.");
            return true;
        }

        List<Job> jobs = jobManager.getJobs(player.getName());
        if (jobs.isEmpty()) {
            player.sendMessage("§cNo tienes trabajos.");
            return true;
        }

        player.sendMessage("§6Información de tus trabajos:");
        for (Job job : jobs) {
            player.sendMessage("§a- " + job.getJobType().getNombre() + " §7(Balance acumulado: $" + job.getBalance() + ")");
        }
        return true;
    }
}
