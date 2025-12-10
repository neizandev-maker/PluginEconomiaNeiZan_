package com.neizan.plugin.commands;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JobStatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede usarse en el juego.");
            return true;
        }

        String playerName = player.getName();
        List<Job> jobs = Main.getInstance().getJobManager().getJobs(playerName);

        if (jobs.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No tienes ningún trabajo asignado.");
            return true;
        }

        double totalBalance = Main.getInstance().getEconomyManager().getBalance(playerName);

        player.sendMessage(ChatColor.DARK_GRAY + "════════════════════════════");
        player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "✦ TUS ESTADÍSTICAS ✦");
        player.sendMessage(ChatColor.DARK_GRAY + "════════════════════════════");

        for (Job job : jobs) {
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "➤ Trabajo: "
                    + ChatColor.LIGHT_PURPLE + job.getJobType().getNombre());

            player.sendMessage(ChatColor.YELLOW + "  ▸ Nivel: "
                    + ChatColor.GREEN + job.getLevel());

            player.sendMessage(ChatColor.YELLOW + "  ▸ XP: "
                    + ChatColor.AQUA + (int) job.getXp()
                    + ChatColor.GRAY + "/"
                    + ChatColor.AQUA + (int) job.getXpToNextLevel());

            player.sendMessage(ChatColor.YELLOW + "  ▸ Dinero ganado: "
                    + ChatColor.GREEN + "$" + job.getBalance());

            player.sendMessage(ChatColor.DARK_GRAY + "────────────────────────────");
        }

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "💰 Balance total: "
                + ChatColor.GREEN + "$" + totalBalance);
        player.sendMessage(ChatColor.DARK_GRAY + "════════════════════════════");
        return true;
    }
}
