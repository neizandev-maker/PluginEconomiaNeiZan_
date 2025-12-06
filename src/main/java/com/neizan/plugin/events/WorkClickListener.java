package com.neizan.plugin.events;

import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

public class WorkClickListener implements Listener {

    private final JobManager jobManager;

    public WorkClickListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!event.getView().getTitle().equals("§6Selecciona un trabajo")) return;

        event.setCancelled(true); // Evita mover los items

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        String nombreTrabajo = meta.getDisplayName().replace("§a", "");

        // Buscar el JobsEnum correspondiente
        JobsEnum selectedJob = null;
        for (JobsEnum job : JobsEnum.values()) {
            if (job.getNombre().equals(nombreTrabajo)) {
                selectedJob = job;
                break;
            }
        }

        if (selectedJob != null) {
            jobManager.setJob(player.getUniqueId(), selectedJob);
            player.closeInventory();
            player.sendMessage("§a¡Ahora eres un " + selectedJob.getNombre() + "!");
        }
    }
}
