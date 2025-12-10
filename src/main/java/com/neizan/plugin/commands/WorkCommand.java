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

        // Panel azul para marco
        ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta paneMeta = bluePane.getItemMeta();
        paneMeta.setDisplayName(" ");
        bluePane.setItemMeta(paneMeta);

        // Colocar marco azul
        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26
        };
        for (int slot : borderSlots) inv.setItem(slot, bluePane);

        // Paneles adicionales al lado de trabajos (izq/dcha de pala y caña)
        inv.setItem(10, bluePane);
        inv.setItem(16, bluePane);

        // Colocar trabajos
        for (int i = 0; i < jobs.length; i++) {
            JobsEnum job = jobs[i];
            ItemStack item = job.getIcon();
            ItemMeta meta = item.getItemMeta();

            // Quitar atributos automáticos de ítems para que no salga daño extra
            meta.setAttributeModifiers(null);

            // Color por trabajo
            String colorCode;
            switch (job) {
                case EXCAVADOR -> colorCode = "§e"; // amarillo
                case MINERO -> colorCode = "§b"; // aqua
                case ASESINO -> colorCode = "§4"; // rojo
                case GRANJERO -> colorCode = "§a"; // verde
                case PESCADOR -> colorCode = "§d"; // rosa
                default -> colorCode = "§f"; // blanco
            }

            meta.setDisplayName(formatBoldColor(colorCode, job.getNombre()));

            // Lore simple sin atributos extra
            List<String> lore = new ArrayList<>();
            lore.add(formatBoldColor("§7", job.getDescripcion()));
            lore.add("§7Recompensa base: " + formatBoldColor("§a", "$" + job.getBaseReward()));
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.setItem(jobSlots[i], item);
        }

        player.openInventory(inv);
        return true;
    }

    private String formatBoldColor(String colorCode, String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append("§l").append(colorCode).append(c);
        }
        return sb.toString();
    }
}
