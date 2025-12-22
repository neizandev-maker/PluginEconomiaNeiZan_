package com.neizan.plugin.events;

import com.neizan.plugin.jobs.Job;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import com.neizan.plugin.jobs.RewardEntry;
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

        String title = event.getView().getTitle();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Cancelar solo si es inventario de plugin (incluye Pagos de)
        if (title.contains("Selecciona un Trabajo")
                || title.contains("Detalles de ")
                || title.contains("Salir del trabajo")
                || title.contains("Pagos de ")) {
            event.setCancelled(true);
        } else return;

        String playerName = player.getName();

        // -------------------- MENÚ PRINCIPAL --------------------
        if (ChatColor.stripColor(title).equalsIgnoreCase("Selecciona un Trabajo")) {
            JobsEnum job = JobsEnum.fromItem(clicked.getType());
            if (job == null) return;

            boolean alreadyInJob = jobManager.isPlayerInJob(playerName, job);

            if (alreadyInJob) {
                openQuitJobMenu(player, job);
            } else {
                openJobDetailsMenu(player, job, false);
            }
            return;
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

            } else if (clicked.getType() == Material.RED_WOOL || clicked.getType() == Material.ARROW) {
                player.performCommand("work");

            } else if (clicked.getType() == Material.WRITTEN_BOOK) {
                openRewardMenu(player, job, false, 0);
            }
            return;
        }

        // -------------------- MENÚ SALIR --------------------
        if (ChatColor.stripColor(title).startsWith("Salir del trabajo")) {
            JobsEnum job = JobsEnum.fromName(ChatColor.stripColor(title).replace("Salir del trabajo: ", "").trim());
            if (job == null) return;

            if (clicked.getType() == Material.GREEN_WOOL) {
                jobManager.removeJob(playerName, job);
                player.sendMessage("§aHas salido del trabajo: " + job.getNombre());
                player.performCommand("work");

            } else if (clicked.getType() == Material.RED_WOOL || clicked.getType() == Material.ARROW) {
                player.performCommand("work");

            } else if (clicked.getType() == Material.WRITTEN_BOOK) {
                openRewardMenu(player, job, true, 0);
            }
            return;
        }

        // -------------------- MENÚ PAGOS (nuevo, por items) --------------------
        if (ChatColor.stripColor(title).startsWith("Pagos de ")) {
            JobsEnum job = JobsEnum.fromName(ChatColor.stripColor(title).replace("Pagos de ", ""));
            if (job == null) return;

            if (!clicked.hasItemMeta()) return;
            ItemMeta cm = clicked.getItemMeta();
            if (!cm.hasLocalizedName()) return;

            String tag = cm.getLocalizedName();

            // Volver
            if (tag.startsWith("back:")) {
                boolean fromQuit = tag.endsWith("quit");
                if (fromQuit) {
                    openQuitJobMenu(player, job);
                } else {
                    boolean alreadyInJob = jobManager.isPlayerInJob(player.getName(), job);
                    openJobDetailsMenu(player, job, alreadyInJob);
                }
                return;
            }

            // Paginación
            if (tag.startsWith("page:")) {
                String[] parts = tag.split(":");
                if (parts.length >= 3) {
                    try {
                        int targetPage = Integer.parseInt(parts[1]);
                        boolean fromQuit = "quit".equals(parts[2]);
                        openRewardMenu(player, job, fromQuit, targetPage);
                    } catch (NumberFormatException ignored) {}
                }
            }

            // Si luego quieres hacer click en una recompensa concreta:
            // if (tag.startsWith("reward:")) { ... }
        }
    }

    // ===================== MENÚ DETALLES =====================
    private void openJobDetailsMenu(Player player, JobsEnum job, boolean alreadyInJob) {
        Job j = jobManager.getJob(player.getName(), job);
        int level = j != null ? j.getLevel() : 1;
        double xp = j != null ? j.getXp() : 0;
        double xpToNext = j != null ? j.getXpToNextLevel() : job.getBaseXp();

        Inventory inv = Bukkit.createInventory(null, 27, "§l§1Detalles de §l§1" + job.getNombre());
        addBluePanes(inv);

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
        inv.setItem(13, info);

        if (!alreadyInJob) {
            inv.setItem(11, createButton(Material.GREEN_WOOL, "§aAceptar trabajo"));
            inv.setItem(15, createButton(Material.RED_WOOL, "§cRechazar trabajo"));
        } else {
            inv.setItem(11, createButton(Material.YELLOW_WOOL, "§eSalir del trabajo"));
        }

        inv.setItem(26, createButton(Material.WRITTEN_BOOK, "§bPagos y recompensas"));

        // Flecha de volver
        inv.setItem(18, createButton(Material.ARROW, "§cVolver"));

        player.openInventory(inv);
    }

    // ===================== MENÚ SALIR =====================
    private void openQuitJobMenu(Player player, JobsEnum job) {
        Inventory inv = Bukkit.createInventory(null, 27, "§l§1Salir del trabajo: " + job.getNombre());
        addBluePanes(inv);

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

        inv.setItem(13, info);
        inv.setItem(11, createButton(Material.GREEN_WOOL, "§aSí, salir del trabajo"));
        inv.setItem(15, createButton(Material.RED_WOOL, "§cNo, volver al menú principal"));
        inv.setItem(26, createButton(Material.WRITTEN_BOOK, "§bPagos y recompensas"));
        inv.setItem(18, createButton(Material.ARROW, "§cVolver"));

        player.openInventory(inv);
    }

    // ===================== MENÚ PAGOS (items separados) =====================
    private void openRewardMenu(Player player, JobsEnum job, boolean fromQuitMenu, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§l§1Pagos de " + job.getNombre());
        addBluePanes54(inv);

        // Slots "limpios" (sin bordes) para meter recompensas
        List<Integer> contentSlots = List.of(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        );

        List<RewardEntry> rewards = job.getRewards();
        int perPage = contentSlots.size();
        int totalPages = Math.max(1, (int) Math.ceil(rewards.size() / (double) perPage));
        int safePage = Math.min(Math.max(page, 0), totalPages - 1);

        // Header/info en el centro (opcional)
        ItemStack header = new ItemStack(Material.PAPER);
        ItemMeta hm = header.getItemMeta();
        hm.setDisplayName("§bPagos de " + job.getNombre() + " §7(" + (safePage + 1) + "/" + totalPages + ")");
        List<String> hl = new ArrayList<>();
        hl.add("§7Base: §a$" + job.getBaseReward());
        hl.add("§7Cada item = una recompensa.");
        hm.setLore(hl);
        header.setItemMeta(hm);
        inv.setItem(4, header);

        // Back
        ItemStack back = createButton(Material.ARROW, "§cVolver");
        ItemMeta bm = back.getItemMeta();
        bm.setLocalizedName(fromQuitMenu ? "back:quit" : "back:details");
        back.setItemMeta(bm);
        inv.setItem(45, back);

        // Prev/Next
        if (safePage > 0) {
            ItemStack prev = createButton(Material.ARROW, "§ePágina anterior");
            ItemMeta pm = prev.getItemMeta();
            pm.setLocalizedName("page:" + (safePage - 1) + ":" + (fromQuitMenu ? "quit" : "details"));
            prev.setItemMeta(pm);
            inv.setItem(48, prev);
        }
        if (safePage + 1 < totalPages) {
            ItemStack next = createButton(Material.ARROW, "§ePágina siguiente");
            ItemMeta nm = next.getItemMeta();
            nm.setLocalizedName("page:" + (safePage + 1) + ":" + (fromQuitMenu ? "quit" : "details"));
            next.setItemMeta(nm);
            inv.setItem(50, next);
        }

        // Render entries
        int start = safePage * perPage;
        int end = Math.min(start + perPage, rewards.size());

        int slotIndex = 0;
        for (int i = start; i < end; i++) {
            RewardEntry e = rewards.get(i);
            int slot = contentSlots.get(slotIndex++);

            ItemStack it = new ItemStack(e.getIcon());
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("§6" + e.getTitle());

            List<String> lore = new ArrayList<>();
            lore.add("§7Pago: §a$" + e.getPay());
            for (String d : e.getDetails()) lore.add("§8• §7" + d);
            meta.setLore(lore);

            // Tag por si un día quieres hacer click y abrir detalle extra
            meta.setLocalizedName("reward:" + i);

            it.setItemMeta(meta);
            inv.setItem(slot, it);
        }

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

        for (int slot : borderSlots) inv.setItem(slot, bluePane);
    }

    // Bordes para 54 slots (6 filas)
    private void addBluePanes54(Inventory inv) {
        ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta paneMeta = bluePane.getItemMeta();
        paneMeta.setDisplayName(" ");
        bluePane.setItemMeta(paneMeta);

        for (int i = 0; i < 54; i++) {
            boolean top = i < 9;
            boolean bottom = i >= 45;
            boolean left = i % 9 == 0;
            boolean right = i % 9 == 8;

            // Marco, pero dejo el header (slot 4) y zona de botones abajo libre según tu gusto
            if (top || bottom || left || right) {
                // no tapar slot 45,48,50 (botones)
                if (i == 45 || i == 48 || i == 50) continue;
                inv.setItem(i, bluePane);
            }
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
        for (char c : text.toCharArray()) sb.append("§l").append(colorCode).append(c);
        return sb.toString();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String buildXpBar(double xp, double max) {
        int totalBlocks = 20;
        int filled = (max <= 0) ? 0 : (int) ((xp / max) * totalBlocks);
        if (filled < 0) filled = 0;
        if (filled > totalBlocks) filled = totalBlocks;

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filled) bar.append("§a▓");
            else bar.append("§7░");
        }
        return bar.toString();
    }
}
