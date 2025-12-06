package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorkClickListener implements Listener {

    private final JobManager jobManager;

    public WorkClickListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getView().getTitle().equals("§6Selecciona un trabajo")) {
            event.setCancelled(true); // evitar sacar los items del inventario

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            // Determinar trabajo por el tipo de item
            JobsEnum selectedJob = switch (clicked.getType()) {
                case DIAMOND_SHOVEL -> JobsEnum.EXCAVADOR;
                case DIAMOND_PICKAXE -> JobsEnum.MINERO;
                case DIAMOND_SWORD -> JobsEnum.ASESINO;
                case WHEAT -> JobsEnum.GRANJERO;
                case FISHING_ROD -> JobsEnum.PESCADOR;
                default -> null;
            };

            if (selectedJob == null) return;

            List<?> playerJobs = jobManager.getJobs(player.getUniqueId());

            if (playerJobs.size() >= 3) {
                player.sendMessage("§cYa tienes 3 trabajos. No puedes añadir más.");
                player.closeInventory();
                return;
            }

            boolean added = jobManager.addJob(player.getUniqueId(), selectedJob);
            if (added) {
                player.sendMessage("§aHas añadido el trabajo: " + selectedJob.getNombre());
            } else {
                player.sendMessage("§cYa tienes ese trabajo.");
            }
            player.closeInventory();
        }
    }
}
