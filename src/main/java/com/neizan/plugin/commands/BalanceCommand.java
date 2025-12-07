package com.neizan.plugin.commands;

import com.neizan.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        double balance = Main.getInstance().getEconomyManager().getBalance(player.getName());
        player.sendMessage("Tu balance es: $" + balance);
        return true;
    }
}
