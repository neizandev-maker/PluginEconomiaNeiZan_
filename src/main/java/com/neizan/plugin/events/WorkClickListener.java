package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorkClickListener implements Listener {

    private final JobManager jobManager;

    public WorkClickListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        if (!title.equals("§6Selecciona un trabajo") && !title.startsWith("§6Detalles de ")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Menú principal → abrir submenú
        if (title.equals("§6Selecciona un trabajo")) {
            JobsEnum job = JobsEnum.fromItem(clicked.getType());
            if (job == null) return;

            // Submenú
            Inventory inv = Main.getInstance().getServer().createInventory(null, 27, "§6Detalles de " + job.getNombre());
            // Descripción y stats
            ItemStack info = new ItemStack(job.getIcon());
            var meta = info.getItemMeta();
            meta.setDisplayName("§a" + job.getNombre());
            Job j = jobManager.getJob(player.getUniqueId(), job);
            int level = j != null ? j.getLevel() : 0;
            meta.setLore(List.of(
                    "§7" + job.getDescripcion(),
                    "§7Nivel: " + level,
                    "§7XP: " + (j != null ? j.getXp() : 0) + "/" + (j != null ? j.xpToNextLevel() : job.getBaseXp())
            ));
            info.setItemMeta(meta);
            inv.setItem(13, info);

            // Botones aceptar/rechazar
            ItemStack accept = new ItemStack(Material.GREEN_WOOL);
            var ameta = accept.getItemMeta();
            ameta.setDisplayName("§aAceptar trabajo");
            accept.setItemMeta(ameta);

            ItemStack decline = new ItemStack(Material.RED_WOOL);
            var dmeta = decline.getItemMeta();
            dmeta.setDisplayName("§cRechazar trabajo");
            decline.setItemMeta(dmeta);

            inv.setItem(11, accept);
            inv.setItem(15, decline);

            player.openInventory(inv);
        }

        // Submenú → aceptar/rechazar
        if (title.startsWith("§6Detalles de ")) {
            if (clicked.getType() == Material.GREEN_WOOL) {
                JobsEnum job = JobsEnum.fromName(title.replace("§6Detalles de ", ""));
                jobManager.addJob(player.getUniqueId(), job);
                player.sendMessage("Has aceptado el trabajo: " + job.getNombre());
                player.closeInventory();
            } else if (clicked.getType() == Material.RED_WOOL) {
                player.closeInventory();
            }
        }
    }
}
