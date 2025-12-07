package com.neizan.plugin.commands;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class JobStatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede usarse en el juego.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        List<Job> jobs = Main.getInstance().getJobManager().getJobs(uuid);

        if (jobs.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No tienes ningún trabajo asignado.");
            return true;
        }

        double totalBalance = Main.getInstance().getEconomyManager().getBalance(uuid);

        player.sendMessage(ChatColor.GOLD + "===== Tus Stats =====");

        for (Job job : jobs) {
            player.sendMessage(ChatColor.AQUA + "Trabajo: " + ChatColor.GREEN + job.getJobType().getNombre());
            player.sendMessage(ChatColor.AQUA + "Nivel: " + ChatColor.YELLOW + job.getLevel());
            player.sendMessage(ChatColor.AQUA + "XP: " + ChatColor.YELLOW
                    + (int) job.getXp() + "/" + (int) job.getXpToNextLevel());
            player.sendMessage(ChatColor.AQUA + "Dinero ganado: " + ChatColor.GREEN + "$" + job.getBalance());
            player.sendMessage(ChatColor.GRAY + "-------------------------");
        }

        player.sendMessage(ChatColor.AQUA + "Balance total: " + ChatColor.GREEN + "$" + totalBalance);

        return true;
    }
}
