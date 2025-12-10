package com.neizan.plugin.commands;

import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WorkCommand implements CommandExecutor {

    private final JobManager jobManager;

    public WorkCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede usarlo un jugador.");
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§l§1Selecciona un Trabajo");

        int[] jobSlots = {11, 12, 13, 14, 15};
        JobsEnum[] jobs = JobsEnum.values();

        // panel azul
        ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta paneMeta = bluePane.getItemMeta();
        paneMeta.setDisplayName(" ");
        bluePane.setItemMeta(paneMeta);

        int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        for (int slot : borderSlots) inv.setItem(slot, bluePane);
        inv.setItem(10, bluePane);
        inv.setItem(16, bluePane);

        // colocar trabajos
        for (int i = 0; i < jobs.length; i++) {
            JobsEnum job = jobs[i];
            ItemStack item = job.getIcon();
            ItemMeta meta = item.getItemMeta();

            String color;
            switch (job) {
                case EXCAVADOR -> color = "§e";
                case MINERO -> color = "§b";
                case ASESINO -> color = "§4";
                case GRANJERO -> color = "§a";
                case PESCADOR -> color = "§d";
                default -> color = "§f";
            }

            meta.setDisplayName(formatBoldColor(color, job.getNombre()));

            List<String> lore = new ArrayList<>();
            lore.add("§7" + job.getDescripcion());
            lore.add("§5Acción: " + capitalize(job.getAction()));

            // ---------- Comprobar si el jugador ya tiene el trabajo ----------
            if (jobManager.isPlayerInJob(player.getName(), job)) {
                lore.add("§c¡Ya trabajas aquí!");
                lore.add("§eHaz clic para salir de este trabajo");
            } else {
                lore.add("§aHaz clic derecho para trabajar aquí");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(jobSlots[i], item);
        }

        player.openInventory(inv);
        return true;
    }

    private String formatBoldColor(String colorCode, String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) sb.append("§l").append(colorCode).append(c);
        return sb.toString();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
