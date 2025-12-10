package com.neizan.plugin.jobs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum JobsEnum {
    EXCAVADOR("Excavador", "Recolecta tierra, arena y grava", Material.DIAMOND_SHOVEL, 1, 10, "romper"),
    MINERO("Minero", "Extrae minerales y ores", Material.DIAMOND_PICKAXE, 5, 15, "romper"),
    ASESINO("Asesino", "Mata mobs hostiles", Material.DIAMOND_SWORD, 10, 20, "matar"),
    GRANJERO("Granjero", "Cosecha cultivos", Material.WHEAT, 2, 10, "cosechar"),
    PESCADOR("Pescador", "Pesca peces", Material.FISHING_ROD, 3, 12, "pescar");

    private final String nombre;
    private final String descripcion;
    private final Material icon;
    private final double baseReward;
    private final double baseXp;
    private final String action;

    JobsEnum(String nombre, String descripcion, Material icon, double baseReward, double baseXp, String action) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icon = icon;
        this.baseReward = baseReward;
        this.baseXp = baseXp;
        this.action = action;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Material getIconMaterial() { return icon; }
    public ItemStack getIcon() { return new ItemStack(icon); }
    public double getBaseReward() { return baseReward; }
    public double getBaseXp() { return baseXp; }
    public String getAction() { return action; }

    public static JobsEnum fromItem(Material m) {
        for (JobsEnum j : values()) {
            if (j.icon == m) return j;
        }
        return null;
    }

    public static JobsEnum fromName(String name) {
        for (JobsEnum j : values()) {
            if (j.nombre.equalsIgnoreCase(name)) return j;
        }
        return null;
    }

    public String getRewardDescription() {
        switch (this) {
            case EXCAVADOR: return "$" + baseReward + " por DIRT, SAND o GRAVEL";
            case MINERO: return "$" + baseReward + " por ORES";
            case ASESINO: return "$" + baseReward + " por mobs hostiles";
            case GRANJERO: return "$" + baseReward + " por cosechar cultivos";
            case PESCADOR: return "$" + baseReward + " por pescar peces";
            default: return "$" + baseReward + " por acción";
        }
    }
}
