package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Bukkit;
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
        if (!title.equals("§6Selecciona un trabajo") && !title.startsWith("§6Detalles de ")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (title.equals("§6Selecciona un trabajo")) {
            JobsEnum job = JobsEnum.fromItem(clicked.getType());
            if (job == null) return;

            Inventory inv = Bukkit.createInventory(null, 27, "§6Detalles de " + job.getNombre());

            ItemStack info = new ItemStack(job.getIcon());
            ItemMeta meta = info.getItemMeta();
            meta.setDisplayName("§a" + job.getNombre());

            Job j = jobManager.getJob(playerName, job);
            int level = j != null ? j.getLevel() : 1;
            double xp = j != null ? j.getXp() : 0;
            double xpToNext = j != null ? j.getXpToNextLevel() : job.getBaseXp();

            List<String> lore = new ArrayList<>();
            lore.add("§7" + job.getDescripcion());
            lore.add("§7Nivel: §e" + level);
            lore.add("§7XP: " + buildXpBar(xp, xpToNext) + " §f(" + (int) xp + "/" + (int) xpToNext + ")");
            lore.add("§7Recompensas: " + job.getRewardDescription());

            meta.setLore(lore);
            info.setItemMeta(meta);
            inv.setItem(13, info);

            ItemStack accept = new ItemStack(Material.GREEN_WOOL);
            ItemMeta am = accept.getItemMeta();
            am.setDisplayName("§aAceptar trabajo");
            accept.setItemMeta(am);

            ItemStack decline = new ItemStack(Material.RED_WOOL);
            ItemMeta dm = decline.getItemMeta();
            dm.setDisplayName("§cRechazar trabajo");
            decline.setItemMeta(dm);

            inv.setItem(11, accept);
            inv.setItem(15, decline);

            player.openInventory(inv);
        }

        if (title.startsWith("§6Detalles de ")) {
            if (clicked.getType() == Material.GREEN_WOOL) {
                JobsEnum job = JobsEnum.fromName(title.replace("§6Detalles de ", ""));
                jobManager.addJob(playerName, job); // <-- Cambiado a playerName
                player.sendMessage("§aHas aceptado el trabajo: " + job.getNombre());
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
}
