package com.neizan.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobsTopCommand implements CommandExecutor {

    private final Connection connection;

    public JobsTopCommand(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUso: /jobs top <minero|asesino|dinero>");
            return true;
        }

        if (args[0].equalsIgnoreCase("top")) {
            if (args.length < 2) {
                sender.sendMessage("§cUso: /jobs top <minero|asesino|dinero>");
                return true;
            }

            String type = args[1].toLowerCase();
            switch (type) {
                case "minero" -> showTopJob(sender, "MINERO", "§b⛏ §lTOP 5 MINEROS");
                case "asesino" -> showTopJob(sender, "ASESINO", "§c⚔ §lTOP 5 ASESINOS");
                case "dinero" -> showTopMoney(sender);
                default -> sender.sendMessage("§cTipos válidos: minero, asesino, dinero");
            }
            return true;
        }

        return false;
    }

    private void showTopJob(CommandSender sender, String jobName, String title) {
        sender.sendMessage("§8§m------------------------");
        sender.sendMessage(title);
        sender.sendMessage("§8§m------------------------");

        String sql = "SELECT playerName, level FROM players WHERE jobName = ? ORDER BY level DESC LIMIT 5;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, jobName);
            ResultSet rs = ps.executeQuery();
            int pos = 1;
            while (rs.next()) {
                String p = rs.getString("playerName");
                int lvl = rs.getInt("level");
                sender.sendMessage("§7" + pos + ". §e" + p + " §8➜ §aNivel " + lvl);
                pos++;
            }
        } catch (SQLException e) {
            sender.sendMessage("§cError al cargar el ranking.");
            e.printStackTrace();
        }

        sender.sendMessage("§8§m------------------------");
    }

    private void showTopMoney(CommandSender sender) {
        sender.sendMessage("§8§m------------------------");
        sender.sendMessage("§6💰 §lTOP 5 MILLONARIOS");
        sender.sendMessage("§8§m------------------------");

        String sql = "SELECT player_name, balance FROM economy ORDER BY balance DESC LIMIT 5;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int pos = 1;
            while (rs.next()) {
                String p = rs.getString("player_name");
                double bal = rs.getDouble("balance");
                sender.sendMessage("§7" + pos + ". §e" + p + " §8➜ §a$" + ((long) bal));
                pos++;
            }
        } catch (SQLException e) {
            sender.sendMessage("§cError al cargar el ranking.");
            e.printStackTrace();
        }

        sender.sendMessage("§8§m------------------------");
    }
}
