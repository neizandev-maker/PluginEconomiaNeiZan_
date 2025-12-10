package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WorkClickListener implements Listener {

    private final JobManager jobManager;

    public WorkClickListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String playerName = player.getName();
        String title = event.getView().getTitle();
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // -------------------- MENÚ PRINCIPAL --------------------
        if (ChatColor.stripColor(title).equalsIgnoreCase("Selecciona un Trabajo")) {
            JobsEnum job = JobsEnum.fromItem(clicked.getType());
            if (job == null) return;

            boolean alreadyInJob = jobManager.isPlayerInJob(playerName, job);

            if (alreadyInJob) {
                openQuitJobMenu(player, job);
                return;
            }

            openJobDetailsMenu(player, job, false);
        }

        // -------------------- MENÚ DETALLES --------------------
        if (ChatColor.stripColor(title).startsWith("Detalles de ")) {
            JobsEnum job = JobsEnum.fromName(ChatColor.stripColor(title).replace("Detalles de ", ""));
            if (job == null) return;

            if (clicked.getType() == Material.YELLOW_WOOL) {
                openQuitJobMenu(player, job);

            } else if (clicked.getType() == Material.GREEN_WOOL) {
                jobManager.addJob(playerName, job);
                player.sendMessage("§aHas comenzado a trabajar como " + job.getNombre());
                player.closeInventory();

            } else if (clicked.getType() == Material.RED_WOOL) {
                // LA ROJA HACE DE "VOLVER"
                player.closeInventory();
            }
        }

        // -------------------- MENÚ SALIR --------------------
        if (ChatColor.stripColor(title).startsWith("Salir del trabajo")) {
            JobsEnum job = JobsEnum.fromName(
                    ChatColor.stripColor(title).replace("Salir del trabajo: ", "").trim()
            );
            if (job == null) return;

            if (clicked.getType() == Material.GREEN_WOOL) {
                jobManager.removeJob(playerName, job);
                player.sendMessage("§aHas salido del trabajo: " + job.getNombre());
                player.performCommand("work"); // vuelve al menú principal

            } else if (clicked.getType() == Material.RED_WOOL) {
                // VOLVER DIRECTO AL MENÚ DE TRABAJOS
                player.performCommand("work");
            }
        }
    }

    // ===================== MENÚ DE DETALLES =====================
    private void openJobDetailsMenu(Player player, JobsEnum job, boolean alreadyInJob) {
        Job j = jobManager.getJob(player.getName(), job);
        int level = j != null ? j.getLevel() : 1;
        double xp = j != null ? j.getXp() : 0;
        double xpToNext = j != null ? j.getXpToNextLevel() : job.getBaseXp();

        Inventory inv = Bukkit.createInventory(null, 27, "§l§1Detalles de §l§1" + job.getNombre());

        // Paneles azules
        addBluePanes(inv);

        // Icono central con info
        ItemStack info = new ItemStack(job.getIconMaterial());
        ItemMeta meta = info.getItemMeta();

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
        lore.add("§7Nivel: §e§l" + level);
        lore.add("§7XP: " + buildXpBar(xp, xpToNext) + " §f§l(" + (int) xp + "/" + (int) xpToNext + ")");
        lore.add("§7Recompensas: §a§l" + job.getRewardDescription());
        lore.add("§5Acción: " + capitalize(job.getAction()));

        if (alreadyInJob) {
            lore.add("§c¡Ya trabajas aquí!");
            lore.add("§eHaz clic en el botón para salir de este trabajo");
        } else {
            lore.add("§aHaz clic para trabajar aquí");
        }

        meta.setLore(lore);
        info.setItemMeta(meta);

        inv.setItem(13, info); // Icono central

        // Botones
        if (!alreadyInJob) {
            inv.setItem(11, createButton(Material.GREEN_WOOL, "§aAceptar trabajo"));
            inv.setItem(15, createButton(Material.RED_WOOL, "§cRechazar trabajo"));
        } else {
            inv.setItem(11, createButton(Material.YELLOW_WOOL, "§eSalir del trabajo"));
        }

        // Panel azul abajo a la izquierda
        inv.setItem(18, createButton(Material.BLUE_STAINED_GLASS_PANE, " "));

        player.openInventory(inv);
    }

    // ===================== MENÚ SALIR =====================
    private void openQuitJobMenu(Player player, JobsEnum job) {
        Inventory inv = Bukkit.createInventory(null, 27, "§l§1Salir del trabajo: " + job.getNombre());

        // Paneles azules
        addBluePanes(inv);

        // Icono central con info del trabajo
        Job j = jobManager.getJob(player.getName(), job);
        int level = j != null ? j.getLevel() : 1;
        double xp = j != null ? j.getXp() : 0;
        double xpToNext = j != null ? j.getXpToNextLevel() : job.getBaseXp();

        ItemStack info = new ItemStack(job.getIconMaterial());
        ItemMeta meta = info.getItemMeta();

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
        lore.add("§7Nivel: §e§l" + level);
        lore.add("§7XP: " + buildXpBar(xp, xpToNext) + " §f§l(" + (int) xp + "/" + (int) xpToNext + ")");
        lore.add("§7Recompensas: §a§l" + job.getRewardDescription());
        lore.add("§5Acción: " + capitalize(job.getAction()));

        meta.setLore(lore);
        info.setItemMeta(meta);

        inv.setItem(13, info); // icono central
        inv.setItem(11, createButton(Material.GREEN_WOOL, "§aSí, salir del trabajo"));
        inv.setItem(15, createButton(Material.RED_WOOL, "§cNo, volver al menú principal"));

        // Paneles azules especiales
        inv.setItem(18, createButton(Material.BLUE_STAINED_GLASS_PANE, " ")); // abajo izquierda
        inv.setItem(12, createButton(Material.BLUE_STAINED_GLASS_PANE, " ")); // extra para simetría

        player.openInventory(inv);
    }

    // ===================== UTILIDADES =====================
    private void addBluePanes(Inventory inv) {
        ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta paneMeta = bluePane.getItemMeta();
        paneMeta.setDisplayName(" ");
        bluePane.setItemMeta(paneMeta);

        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 12, 14, 16, 17,
                19, 20, 21, 22, 23, 24, 25, 26
        };

        for (int slot : borderSlots) {
            inv.setItem(slot, bluePane);
        }
    }

    private ItemStack createButton(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private String formatBoldColor(String colorCode, String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append("§l").append(colorCode).append(c);
        }
        return sb.toString();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String buildXpBar(double xp, double max) {
        int totalBlocks = 20;
        int filled = (int) ((xp / max) * totalBlocks);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filled) bar.append("§a▓");
            else bar.append("§7░");
        }
        return bar.toString();
    }
}
