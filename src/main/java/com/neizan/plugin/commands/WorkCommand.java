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

import java.util.Arrays;

public class WorkCommand implements CommandExecutor {

    private final JobManager jobManager;

    public WorkCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        // Inventario de 27 slots (3 filas) para centrar los trabajos
        Inventory inv = Bukkit.createInventory(null, 27, "§6Selecciona un trabajo");

        // Slots centrales donde pondremos los trabajos (segunda fila)
        int[] slots = {11, 12, 13, 14, 15};

        JobsEnum[] jobs = JobsEnum.values();
        for (int i = 0; i < jobs.length; i++) {
            JobsEnum job = jobs[i];

            ItemStack item = switch (job) {
                case EXCAVADOR -> new ItemStack(Material.DIAMOND_SHOVEL);
                case MINERO -> new ItemStack(Material.DIAMOND_PICKAXE);
                case ASESINO -> new ItemStack(Material.DIAMOND_SWORD);
                case GRANJERO -> new ItemStack(Material.WHEAT);
                case PESCADOR -> new ItemStack(Material.FISHING_ROD);
            };

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a" + job.getNombre()); // Nombre en verde
            meta.setLore(Arrays.asList("§7" + job.getDescripcion())); // Lore en gris
            item.setItemMeta(meta);

            inv.setItem(slots[i], item); // Colocar en slot central
        }

        player.openInventory(inv);
        return true;
    }
}
