package com.neizan.plugin.commands;

import com.neizan.plugin.jobs.JobManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;

public class JobsRootCommand implements CommandExecutor {

    private final JobsCommand jobsCommand; // tu comando actual que lista trabajos
    private final JobsTopCommand jobsTopCommand;

    public JobsRootCommand(JobManager jobManager, Connection connection) {
        this.jobsCommand = new JobsCommand(jobManager);
        this.jobsTopCommand = new JobsTopCommand(connection);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Si es subcomando top -> delegate a JobsTopCommand
        if (args.length > 0 && args[0].equalsIgnoreCase("top")) {
            return jobsTopCommand.onCommand(sender, command, label, args);
        }

        // Si no, delegar al /jobs actual (sin args)
        return jobsCommand.onCommand(sender, command, label, args);
    }
}
