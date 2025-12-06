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
            sender.sendMessage("Este comando solo puede usarlo un jugador.");
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§6Selecciona un trabajo");

        int[] slots = {11, 12, 13, 14, 15};
        JobsEnum[] jobs = JobsEnum.values();

        for (int i = 0; i < jobs.length; i++) {
            JobsEnum job = jobs[i];
            ItemStack item = job.getIcon(); // cada job define su icono

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a" + job.getNombre());
            meta.setLore(Arrays.asList(
                    "§7" + job.getDescripcion(),
                    "§7Recompensa base: $" + job.getBaseReward()
            ));
            item.setItemMeta(meta);

            inv.setItem(slots[i], item);
        }

        player.openInventory(inv);
        return true;
    }
}
