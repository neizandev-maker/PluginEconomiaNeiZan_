package com.neizan.plugin.jobs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum JobsEnum {
    EXCAVADOR(
            "Excavador",
            "Recolecta tierra, arena y grava. Cobros según el bloque que levantes.",
            Material.DIAMOND_SHOVEL,
            0.60,
            26,
            "romper",
            Arrays.asList(
                    Arrays.asList(
                            "§6Bloques blandos (Overworld)",
                            "§f• §eTierra/Grass: §a$0.40",
                            "§f• §ePodzol/Mycelium: §a$0.55",
                            "§f• §eArena/Red Sand: §a$0.50",
                            "§f• §eGrava: §a$0.60",
                            "§f• §eArcilla/Clay: §a$0.75",
                            "§f• §eSnow/Top Snow: §a$0.35",
                            "§7Bloques de piedra no pagan a excavadores."
                    ),
                    Arrays.asList(
                            "§6Otros materiales",
                            "§f• §eSoul Sand/Soul Soil: §a$0.70",
                            "§f• §eMud/Mud Brick: §a$0.80",
                            "§f• §eRooted Dirt: §a$0.65",
                            "§f• §eMoss/Moss Carpet: §a$0.50",
                            "§f• §eClay Block: §a$0.90",
                            "§7Bloques ajenos (troncos/stone) no dan pago."
                    ),
                    Arrays.asList(
                            "§6Nether/End y rocas duras",
                            "§f• §eNetherrack: §a$0.20",
                            "§f• §eBlackstone/Basalt: §a$0.55",
                            "§f• §eEnd Stone: §a$0.45",
                            "§f• §eSculk/Sculk Vein: §a$0.60",
                            "§7Obsidiana/piedra no paga al excavador."
                    )
            )
    ),
    MINERO(
            "Minero",
            "Extrae minerales. Solo ores procesados pagan.",
            Material.DIAMOND_PICKAXE,
            1.40,
            34,
            "romper",
            Arrays.asList(
                    Arrays.asList(
                            "§6Ores overworld",
                            "§f• §bCarbón/Deepslate: §a$1.20 / $1.40",
                            "§f• §bHierro/Deepslate: §a$1.60 / $1.85",
                            "§f• §bCobre/Deepslate: §a$1.30 / $1.55",
                            "§f• §bOro/Deepslate: §a$2.40 / $2.80",
                            "§f• §bRedstone/Deepslate: §a$1.40 / $1.75"
                    ),
                    Arrays.asList(
                            "§6Ores valiosos y nether",
                            "§f• §bLapis/Deepslate: §a$1.80 / $2.10",
                            "§f• §bDiamante/Deepslate: §a$4.80 / $5.50",
                            "§f• §bEsmeralda/Deepslate: §a$6.00 / $6.60",
                            "§f• §bNether Gold: §a$1.50",
                            "§f• §bNether Quartz: §a$1.20",
                            "§7Otros bloques rotos no otorgan pago como minero."
                    ),
                    Arrays.asList(
                            "§6Profundo y especiales",
                            "§f• §bAncient Debris: §a$6.80",
                            "§f• §bGilded Blackstone: §a$3.20",
                            "§f• §bAmatista (cluster): §a$1.30",
                            "§f• §bCobre oxidado (restaurado): §a$1.10",
                            "§7Stone/Deepslate sin mena no paga."
                    )
            )
    ),
    ASESINO(
            "Asesino",
            "Mata mobs hostiles. Cada criatura paga diferente.",
            Material.DIAMOND_SWORD,
            1.20,
            36,
            "matar",
            Arrays.asList(
                    Arrays.asList(
                            "§6Hostiles comunes",
                            "§f• §4Zombi/Esqueleto/Araña: §a$1.10",
                            "§f• §4Husk/Drowned/Stray: §a$1.35",
                            "§f• §4Creeper: §a$2.20",
                            "§f• §4Enderman: §a$2.20",
                            "§f• §4Bruja: §a$2.80"
                    ),
                    Arrays.asList(
                            "§6Nether y élite",
                            "§f• §4Blaze/Wither Skeleton: §a$3.40",
                            "§f• §4Piglin/Brute: §a$2.60 / $3.60",
                            "§f• §4Guardian/Elder: §a$3.80 / $5.20",
                            "§f• §4Shulker: §a$4.00",
                            "§7Animales pasivos no generan pago."
                    ),
                    Arrays.asList(
                            "§6Aéreos y jefes",
                            "§f• §4Ghast/Phantom: §a$2.40",
                            "§f• §4Vex: §a$2.80",
                            "§f• §4Ravager/Pillager: §a$3.20 / $2.20",
                            "§f• §4Warden: §a$6.50",
                            "§7Dragón/Wither no pagan por balance del servidor."
                    )
            )
    ),
    GRANJERO(
            "Granjero",
            "Cosecha cultivos maduros, cada cosecha paga.",
            Material.WHEAT,
            0.90,
            28,
            "cosechar",
            Arrays.asList(
                    Arrays.asList(
                            "§6Cultivos básicos",
                            "§f• §aTrigo: §a$0.90",
                            "§f• §aZanahoria/Patata: §a$0.95",
                            "§f• §aRemolacha: §a$1.00",
                            "§f• §aCaña de azúcar: §a$1.10",
                            "§f• §aCacao (maduros): §a$1.10"
                    ),
                    Arrays.asList(
                            "§6Cultivos avanzados",
                            "§f• §aCalabaza/Melón: §a$1.20",
                            "§f• §aBambú/Cactus: §a$0.80",
                            "§f• §aNether Wart (madura): §a$1.25",
                            "§f• §aChampiñón rojo/marrón: §a$1.05",
                            "§7Solo se paga por cultivos maduros."
                    ),
                    Arrays.asList(
                            "§6Frutos premium",
                            "§f• §aSweet Berries/Glow Berries: §a$0.95",
                            "§f• §aCereza (Cherry): §a$1.10",
                            "§f• §aPitcher Pod/Torchflower: §a$1.40",
                            "§7Romper cultivos verdes no paga."
                    )
            )
    ),
    PESCADOR(
            "Pescador",
            "Pesca peces. El valor depende de la captura.",
            Material.FISHING_ROD,
            0.95,
            30,
            "pescar",
            Arrays.asList(
                    Arrays.asList(
                            "§6Peces",
                            "§f• §dBacalao: §a$1.00",
                            "§f• §dSalmón: §a$1.30",
                            "§f• §dPez globo: §a$1.80",
                            "§f• §dPez tropical: §a$2.20"
                    ),
                    Arrays.asList(
                            "§6Otros botines",
                            "§f• §bTesoro (libros/cañas/arcos): §a$0.70",
                            "§f• §bNautilus Shell: §a$1.40",
                            "§f• §bJunk (bota, hilo, tinta): §a$0.30",
                            "§7La caña rota no paga. Encantamientos no modifican pago."
                    ),
                    Arrays.asList(
                            "§6Clima y bioma",
                            "§f• §bLluvia: §a+5% pago",
                            "§f• §bRío: botín estándar",
                            "§f• §bOcéano cálido: más peces tropicales",
                            "§7Los bonus no se acumulan con botín de tesoro."
                    )
            )
    );

    private final String nombre;
    private final String descripcion;
    private final Material icon;
    private final double baseReward;
    private final double baseXp;
    private final String action;

    // Mantengo lo viejo por compatibilidad (si lo usas en otro sitio)
    private final List<List<String>> rewardPages;

    JobsEnum(String nombre, String descripcion, Material icon, double baseReward, double baseXp, String action, List<List<String>> rewardPages) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icon = icon;
        this.baseReward = baseReward;
        this.baseXp = baseXp;
        this.action = action;
        this.rewardPages = rewardPages;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Material getIconMaterial() { return icon; }
    public ItemStack getIcon() { return new ItemStack(icon); }
    public double getBaseReward() { return baseReward; }
    public double getBaseXp() { return baseXp; }
    public String getAction() { return action; }

    // ======= Lo viejo (si aún lo usas) =======
    public List<List<String>> getRewardPages() { return Collections.unmodifiableList(rewardPages); }
    public List<String> getRewardPage(int index) {
        if (index < 0 || index >= rewardPages.size()) return Collections.emptyList();
        return Collections.unmodifiableList(rewardPages.get(index));
    }
    public int getRewardPagesCount() { return rewardPages.size(); }

    // ======= NUEVO: cada recompensa separada (un ítem por cosa) =======
    public List<RewardEntry> getRewards() {
        return switch (this) {
            case EXCAVADOR -> List.of(
                    new RewardEntry("Tierra / Césped", Material.DIRT, 0.40, List.of("Overworld", "Dirt y Grass Block")),
                    new RewardEntry("Podzol", Material.PODZOL, 0.55, List.of("Overworld")),
                    new RewardEntry("Mycelium", Material.MYCELIUM, 0.55, List.of("Overworld")),
                    new RewardEntry("Arena / Arena roja", Material.SAND, 0.50, List.of("Overworld", "SAND y RED_SAND")),
                    new RewardEntry("Grava", Material.GRAVEL, 0.60, List.of("Overworld")),
                    new RewardEntry("Arcilla", Material.CLAY, 0.75, List.of("Overworld")),
                    new RewardEntry("Nieve", Material.SNOW, 0.35, List.of("Overworld")),
                    new RewardEntry("Soul Sand", Material.SOUL_SAND, 0.70, List.of("Nether")),
                    new RewardEntry("Soul Soil", Material.SOUL_SOIL, 0.70, List.of("Nether")),
                    new RewardEntry("Mud", Material.MUD, 0.80, List.of("Overworld")),
                    new RewardEntry("Rooted Dirt", Material.ROOTED_DIRT, 0.65, List.of("Overworld")),
                    new RewardEntry("Moss Block", Material.MOSS_BLOCK, 0.50, List.of("Overworld")),
                    new RewardEntry("Netherrack", Material.NETHERRACK, 0.20, List.of("Nether")),
                    new RewardEntry("Blackstone", Material.BLACKSTONE, 0.55, List.of("Nether")),
                    new RewardEntry("Basalt", Material.BASALT, 0.55, List.of("Nether")),
                    new RewardEntry("End Stone", Material.END_STONE, 0.45, List.of("End")),
                    new RewardEntry("Sculk", Material.SCULK, 0.60, List.of("Deep Dark"))
            );

            case MINERO -> List.of(
                    new RewardEntry("Carbón Ore", Material.COAL_ORE, 1.20, List.of("Overworld")),
                    new RewardEntry("Deepslate Carbón Ore", Material.DEEPSLATE_COAL_ORE, 1.40, List.of("Overworld")),
                    new RewardEntry("Hierro Ore", Material.IRON_ORE, 1.60, List.of("Overworld")),
                    new RewardEntry("Deepslate Hierro Ore", Material.DEEPSLATE_IRON_ORE, 1.85, List.of("Overworld")),
                    new RewardEntry("Cobre Ore", Material.COPPER_ORE, 1.30, List.of("Overworld")),
                    new RewardEntry("Deepslate Cobre Ore", Material.DEEPSLATE_COPPER_ORE, 1.55, List.of("Overworld")),
                    new RewardEntry("Oro Ore", Material.GOLD_ORE, 2.40, List.of("Overworld")),
                    new RewardEntry("Deepslate Oro Ore", Material.DEEPSLATE_GOLD_ORE, 2.80, List.of("Overworld")),
                    new RewardEntry("Redstone Ore", Material.REDSTONE_ORE, 1.40, List.of("Overworld")),
                    new RewardEntry("Deepslate Redstone Ore", Material.DEEPSLATE_REDSTONE_ORE, 1.75, List.of("Overworld")),
                    new RewardEntry("Lapis Ore", Material.LAPIS_ORE, 1.80, List.of("Overworld")),
                    new RewardEntry("Deepslate Lapis Ore", Material.DEEPSLATE_LAPIS_ORE, 2.10, List.of("Overworld")),
                    new RewardEntry("Diamante Ore", Material.DIAMOND_ORE, 4.80, List.of("Overworld")),
                    new RewardEntry("Deepslate Diamante Ore", Material.DEEPSLATE_DIAMOND_ORE, 5.50, List.of("Overworld")),
                    new RewardEntry("Esmeralda Ore", Material.EMERALD_ORE, 6.00, List.of("Overworld")),
                    new RewardEntry("Deepslate Esmeralda Ore", Material.DEEPSLATE_EMERALD_ORE, 6.60, List.of("Overworld")),
                    new RewardEntry("Nether Gold Ore", Material.NETHER_GOLD_ORE, 1.50, List.of("Nether")),
                    new RewardEntry("Nether Quartz Ore", Material.NETHER_QUARTZ_ORE, 1.20, List.of("Nether")),
                    new RewardEntry("Ancient Debris", Material.ANCIENT_DEBRIS, 6.80, List.of("Nether")),
                    new RewardEntry("Gilded Blackstone", Material.GILDED_BLACKSTONE, 3.20, List.of("Nether")),
                    new RewardEntry("Amethyst Cluster", Material.AMETHYST_CLUSTER, 1.30, List.of("Geodas"))
            );

            case ASESINO -> List.of(
                    new RewardEntry("Zombi", Material.ROTTEN_FLESH, 1.10, List.of("Hostil común")),
                    new RewardEntry("Esqueleto", Material.BONE, 1.10, List.of("Hostil común")),
                    new RewardEntry("Araña", Material.SPIDER_EYE, 1.10, List.of("Hostil común")),
                    new RewardEntry("Husk", Material.ROTTEN_FLESH, 1.35, List.of("Variante desierto")),
                    new RewardEntry("Drowned", Material.TRIDENT, 1.35, List.of("Acuático (si no quieres TRIDENT cámbialo)")),
                    new RewardEntry("Stray", Material.ARROW, 1.35, List.of("Variante nieve")),
                    new RewardEntry("Creeper", Material.GUNPOWDER, 2.20, List.of("Explosivo")),
                    new RewardEntry("Enderman", Material.ENDER_PEARL, 2.20, List.of("End / Overworld noche")),
                    new RewardEntry("Bruja", Material.POTION, 2.80, List.of("Hostil")),
                    new RewardEntry("Blaze", Material.BLAZE_ROD, 3.40, List.of("Nether")),
                    new RewardEntry("Wither Skeleton", Material.WITHER_SKELETON_SKULL, 3.40, List.of("Nether Fortress")),
                    new RewardEntry("Piglin", Material.GOLD_INGOT, 2.60, List.of("Nether")),
                    new RewardEntry("Piglin Brute", Material.GOLDEN_AXE, 3.60, List.of("Bastion")),
                    new RewardEntry("Guardian", Material.PRISMARINE_SHARD, 3.80, List.of("Ocean Monument")),
                    new RewardEntry("Elder Guardian", Material.PRISMARINE_CRYSTALS, 5.20, List.of("Ocean Monument")),
                    new RewardEntry("Shulker", Material.SHULKER_SHELL, 4.00, List.of("End City")),
                    new RewardEntry("Ghast", Material.GHAST_TEAR, 2.40, List.of("Nether")),
                    new RewardEntry("Phantom", Material.PHANTOM_MEMBRANE, 2.40, List.of("No dormir")),
                    new RewardEntry("Vex", Material.IRON_SWORD, 2.80, List.of("Invocación")),
                    new RewardEntry("Pillager", Material.CROSSBOW, 2.20, List.of("Patrulla/Pillager Outpost")),
                    new RewardEntry("Ravager", Material.SADDLE, 3.20, List.of("Raid")),
                    new RewardEntry("Vindicator", Material.IRON_AXE, 2.60, List.of("Mansión/Raid")),
                    new RewardEntry("Warden", Material.SCULK_CATALYST, 6.50, List.of("Deep Dark"))
            );

            case GRANJERO -> List.of(
                    new RewardEntry("Trigo", Material.WHEAT, 0.90, List.of("Solo maduro")),
                    new RewardEntry("Zanahoria", Material.CARROT, 0.95, List.of("Solo madura")),
                    new RewardEntry("Patata", Material.POTATO, 0.95, List.of("Solo madura")),
                    new RewardEntry("Remolacha", Material.BEETROOT, 1.00, List.of("Solo madura")),
                    new RewardEntry("Caña de azúcar", Material.SUGAR_CANE, 1.10, List.of("Por bloque roto")),
                    new RewardEntry("Cacao", Material.COCOA_BEANS, 1.10, List.of("Solo maduro")),
                    new RewardEntry("Calabaza", Material.PUMPKIN, 1.20, List.of("Cosecha")),
                    new RewardEntry("Melón", Material.MELON, 1.20, List.of("Cosecha")),
                    new RewardEntry("Bambú", Material.BAMBOO, 0.80, List.of("Por bloque roto")),
                    new RewardEntry("Cactus", Material.CACTUS, 0.80, List.of("Por bloque roto")),
                    new RewardEntry("Nether Wart", Material.NETHER_WART, 1.25, List.of("Solo madura")),
                    new RewardEntry("Champiñón rojo", Material.RED_MUSHROOM, 1.05, List.of("Recolección")),
                    new RewardEntry("Champiñón marrón", Material.BROWN_MUSHROOM, 1.05, List.of("Recolección")),
                    new RewardEntry("Sweet Berries", Material.SWEET_BERRIES, 0.95, List.of("Recolección")),
                    new RewardEntry("Glow Berries", Material.GLOW_BERRIES, 0.95, List.of("Recolección"))
            );

            case PESCADOR -> List.of(
                    new RewardEntry("Bacalao", Material.COD, 1.00, List.of("Pesca")),
                    new RewardEntry("Salmón", Material.SALMON, 1.30, List.of("Pesca")),
                    new RewardEntry("Pez globo", Material.PUFFERFISH, 1.80, List.of("Pesca")),
                    new RewardEntry("Pez tropical", Material.TROPICAL_FISH, 2.20, List.of("Pesca")),
                    new RewardEntry("Nautilus Shell", Material.NAUTILUS_SHELL, 1.40, List.of("Tesoro")),
                    new RewardEntry("Basura (junk)", Material.STRING, 0.30, List.of("Basura: hilo, bota, tinta...")),
                    new RewardEntry("Tesoro genérico", Material.ENCHANTED_BOOK, 0.70, List.of("Libros, cañas, arcos...")),
                    new RewardEntry("Bono lluvia", Material.WATER_BUCKET, 0.05, List.of("+5% (si lo aplicas en tu lógica)"))
            );
        };
    }

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
        return "Pagos por cada cosa (abre Pagos y verás todo por separado).";
    }
}
