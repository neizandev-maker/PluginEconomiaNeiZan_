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
        if (!ChatColor.stripColor(title).equalsIgnoreCase("Selecciona un trabajo") &&
                !ChatColor.stripColor(title).startsWith("Detalles de ")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Menú "Selecciona un trabajo"
        if (ChatColor.stripColor(title).equalsIgnoreCase("Selecciona un trabajo")) {
            JobsEnum job = JobsEnum.fromItem(clicked.getType());
            if (job == null) return;

            // Inventario de detalles del trabajo
            Inventory inv = Bukkit.createInventory(null, 27,
                    "§l§1Detalles de §l§1" + job.getNombre());

            // Item principal con información del trabajo
            ItemStack info = new ItemStack(job.getIcon());
            ItemMeta meta = info.getItemMeta();
            meta.setAttributeModifiers(null); // quitar atributos automáticos
            meta.setDisplayName(formatBoldColor("§a", job.getNombre()));

            Job j = jobManager.getJob(playerName, job);
            int level = j != null ? j.getLevel() : 1;
            double xp = j != null ? j.getXp() : 0;
            double xpToNext = j != null ? j.getXpToNextLevel() : job.getBaseXp();

            List<String> lore = new ArrayList<>();
            lore.add(formatBoldColor("§7", job.getDescripcion()));
            lore.add("§7Nivel: " + formatBoldColor("§e", String.valueOf(level)));
            lore.add("§7XP: " + buildXpBar(xp, xpToNext) + " " + formatBoldColor("§f", (int) xp + "/" + (int) xpToNext));
            lore.add("§7Recompensas: " + formatBoldColor("§a", job.getRewardDescription()));

            meta.setLore(lore);
            info.setItemMeta(meta);
            inv.setItem(13, info);

            // Botones de aceptar/rechazar
            ItemStack accept = new ItemStack(Material.GREEN_WOOL);
            ItemMeta am = accept.getItemMeta();
            am.setAttributeModifiers(null); // quitar atributos
            am.setDisplayName(formatBoldColor("§a", "Aceptar trabajo"));
            accept.setItemMeta(am);

            ItemStack decline = new ItemStack(Material.RED_WOOL);
            ItemMeta dm = decline.getItemMeta();
            dm.setAttributeModifiers(null);
            dm.setDisplayName(formatBoldColor("§c", "Rechazar trabajo"));
            decline.setItemMeta(dm);

            // Paneles azules alrededor
            ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            ItemMeta paneMeta = bluePane.getItemMeta();
            paneMeta.setDisplayName(" ");
            bluePane.setItemMeta(paneMeta);

            int[] borderSlots = {
                    0, 1, 2, 3, 4, 5, 6, 7, 8,
                    9, 11, 15, 17,
                    18, 19, 20, 21, 22, 23, 24, 25, 26
            };
            for (int slot : borderSlots) inv.setItem(slot, bluePane);

            // Colocar botones
            inv.setItem(11, accept);
            inv.setItem(15, decline);

            player.openInventory(inv);
        }

        // Menú "Detalles de <trabajo>"
        if (title.startsWith("§l§1Detalles de §l§1")) {
            if (clicked.getType() == Material.GREEN_WOOL) {
                JobsEnum job = JobsEnum.fromName(ChatColor.stripColor(title).replace("Detalles de ", ""));
                jobManager.addJob(playerName, job);
                player.sendMessage(formatBoldColor("§a", "Has aceptado el trabajo: " + job.getNombre()));
                player.closeInventory();
            } else if (clicked.getType() == Material.RED_WOOL) {
                player.closeInventory();
            }
        }
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

    private String formatBoldColor(String colorCode, String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append("§l").append(colorCode).append(c);
        }
        return sb.toString();
    }
}
