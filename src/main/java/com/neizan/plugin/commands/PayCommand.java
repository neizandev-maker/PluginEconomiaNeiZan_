package com.neizan.plugin.commands;

import com.neizan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player payer = (Player) sender;

        if (args.length != 2) {
            payer.sendMessage("Uso correcto: /pay <jugador> <cantidad>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            payer.sendMessage("Jugador no encontrado.");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                payer.sendMessage("La cantidad debe ser mayor a 0.");
                return true;
            }
        } catch (NumberFormatException e) {
            payer.sendMessage("Cantidad inválida.");
            return true;
        }

        var economy = Main.getInstance().getEconomyManager();

        boolean hasMoney = economy.removeBalance(payer.getName(), amount); // <- cambio aquí
        if (!hasMoney) {
            if (!payer.isOp()) {
                payer.sendMessage("No tienes suficiente dinero.");
                return true;
            }
        }

        economy.addBalance(target.getName(), amount); // <- cambio aquí

        payer.sendMessage("Has enviado $" + amount + " a " + target.getName());
        target.sendMessage("Has recibido $" + amount + " de " + payer.getName());

        return true;
    }
}
