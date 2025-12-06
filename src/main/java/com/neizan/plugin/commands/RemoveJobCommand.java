package com.neizan.plugin.commands;

import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveJobCommand implements CommandExecutor {

    private final JobManager jobManager;

    public RemoveJobCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede usarlo un jugador.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUsa: /removejob <trabajo>");
            return true;
        }

        try {
            JobsEnum jobEnum = JobsEnum.valueOf(args[0].toUpperCase());
            boolean removed = jobManager.removeJob(player.getUniqueId(), jobEnum);
            if (removed) {
                player.sendMessage("§aHas eliminado el trabajo: " + jobEnum.getNombre());
            } else {
                player.sendMessage("§cNo tienes ese trabajo.");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cTrabajo no válido.");
        }

        return true;
    }
}
