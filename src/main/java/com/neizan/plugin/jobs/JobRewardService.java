package com.neizan.plugin.jobs;

import com.neizan.plugin.Main;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class JobRewardService {

    public static final String PLACED_METADATA = "player-placed";

    private static final Map<JobsEnum, Map<Material, RewardEntry>> MATERIAL_REWARDS =
            Map.of(
                    JobsEnum.EXCAVADOR, toMapByMaterial(JobsEnum.EXCAVADOR),
                    JobsEnum.MINERO, toMapByMaterial(JobsEnum.MINERO),
                    JobsEnum.GRANJERO, toMapByMaterial(JobsEnum.GRANJERO),
                    JobsEnum.PESCADOR, toMapByMaterial(JobsEnum.PESCADOR)
            );

    private static final Set<Material> REQUIRE_FULL_AGE = EnumSet.of(
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.NETHER_WART,
            Material.COCOA,
            Material.SWEET_BERRY_BUSH,
            Material.CAVE_VINES,
            Material.CAVE_VINES_PLANT
    );

    private JobRewardService() {
    }

    private static Map<Material, RewardEntry> toMapByMaterial(JobsEnum job) {
        return job.getRewards().stream().collect(Collectors.toMap(RewardEntry::getIcon, Function.identity(), (a, b) -> a));
    }

    public static Optional<RewardEntry> findBlockReward(JobsEnum jobType, Material material) {
        Map<Material, RewardEntry> rewards = MATERIAL_REWARDS.get(jobType);
        if (rewards == null) return Optional.empty();
        return Optional.ofNullable(rewards.get(material));
    }

    public static Optional<RewardEntry> findHarvestReward(Material material, org.bukkit.block.Block block) {
        // Ageables deben estar maduros, salvo los de crecimiento vertical (caña/bambú/cactus)
        if (REQUIRE_FULL_AGE.contains(material) && block.getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() < ageable.getMaximumAge()) return Optional.empty();
        }
        return findBlockReward(JobsEnum.GRANJERO, material);
    }

    public static Optional<RewardEntry> findKillReward(EntityType type) {
        return switch (type) {
            case ZOMBIE -> findByTitle(JobsEnum.ASESINO, "Zombi");
            case SKELETON -> findByTitle(JobsEnum.ASESINO, "Esqueleto");
            case SPIDER -> findByTitle(JobsEnum.ASESINO, "Araña");
            case HUSK -> findByTitle(JobsEnum.ASESINO, "Husk");
            case DROWNED -> findByTitle(JobsEnum.ASESINO, "Drowned");
            case STRAY -> findByTitle(JobsEnum.ASESINO, "Stray");
            case CREEPER -> findByTitle(JobsEnum.ASESINO, "Creeper");
            case ENDERMAN -> findByTitle(JobsEnum.ASESINO, "Enderman");
            case WITCH -> findByTitle(JobsEnum.ASESINO, "Bruja");
            case BLAZE -> findByTitle(JobsEnum.ASESINO, "Blaze");
            case WITHER_SKELETON -> findByTitle(JobsEnum.ASESINO, "Wither Skeleton");
            case PIGLIN -> findByTitle(JobsEnum.ASESINO, "Piglin");
            case PIGLIN_BRUTE -> findByTitle(JobsEnum.ASESINO, "Piglin Brute");
            case GUARDIAN -> findByTitle(JobsEnum.ASESINO, "Guardian");
            case ELDER_GUARDIAN -> findByTitle(JobsEnum.ASESINO, "Elder Guardian");
            case SHULKER -> findByTitle(JobsEnum.ASESINO, "Shulker");
            case GHAST -> findByTitle(JobsEnum.ASESINO, "Ghast");
            case PHANTOM -> findByTitle(JobsEnum.ASESINO, "Phantom");
            case VEX -> findByTitle(JobsEnum.ASESINO, "Vex");
            case VINDICATOR -> findByTitle(JobsEnum.ASESINO, "Vindicator");
            case WARDEN -> findByTitle(JobsEnum.ASESINO, "Warden");
            case RAVAGER -> findByTitle(JobsEnum.ASESINO, "Ravager");
            case PILLAGER -> findByTitle(JobsEnum.ASESINO, "Pillager");
            default -> Optional.empty();
        };
    }

    public static Optional<RewardEntry> findFishingReward(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return Optional.empty();
        Entity caught = event.getCaught();
        if (!(caught instanceof Item item)) return Optional.empty();

        ItemStack stack = item.getItemStack();
        Material type = stack.getType();

        // Peces directos
        Optional<RewardEntry> direct = findBlockReward(JobsEnum.PESCADOR, type);
        if (direct.isPresent()) return direct;

        // Tesoro y basura
        if (TREASURE.contains(type)) {
            return findByTitle(JobsEnum.PESCADOR, "Tesoro genérico");
        }
        if (JUNK.contains(type)) {
            return findByTitle(JobsEnum.PESCADOR, "Basura (junk)");
        }
        if (type == Material.NAUTILUS_SHELL) {
            return findByTitle(JobsEnum.PESCADOR, "Nautilus Shell");
        }
        return Optional.empty();
    }

    private static Optional<RewardEntry> findByTitle(JobsEnum job, String title) {
        return job.getRewards().stream().filter(r -> r.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    public static double calculateScaledXp(JobsEnum jobType, double rewardAmount) {
        double baseXp = jobType.getBaseXp() * 0.18;
        double baseReward = jobType.getBaseReward();
        if (baseReward <= 0) return baseXp;
        double ratio = rewardAmount / baseReward;
        return baseXp * Math.max(0.1, ratio); // Evita XP demasiado baja
    }

    public static void grantReward(Player player, Job job, JobManager jobManager, double rewardAmount, double xpBase, String detail) {
        if (rewardAmount <= 0) return;

        int oldLevel = job.getLevel();
        double appliedReward = rewardAmount * job.applyLevelMultiplier();
        double appliedXp = xpBase * 0.4;

        job.addBalance(rewardAmount);
        job.addXp(xpBase);
        jobManager.updateJob(job);

        Main.getInstance().getEconomyManager().addBalance(job.getPlayerName(), appliedReward);

        String suffix = (detail == null || detail.isEmpty()) ? "" : " " + detail;
        String message = "§a+$" + format(appliedReward) + " §7/ §b" + format(appliedXp) + " XP §8(" + job.getJobType().getNombre() + ")" + suffix;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        if (job.getLevel() > oldLevel) {
            player.sendMessage("§6¡Has subido al nivel " + job.getLevel() + "!");
        }
    }

    private static String format(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private static final Set<Material> TREASURE = EnumSet.of(
            Material.ENCHANTED_BOOK,
            Material.FISHING_ROD,
            Material.BOW,
            Material.NAME_TAG,
            Material.NAUTILUS_SHELL,
            Material.SADDLE
    );

    private static final Set<Material> JUNK = EnumSet.of(
            Material.STRING,
            Material.LEATHER_BOOTS,
            Material.BONE,
            Material.INK_SAC,
            Material.STICK,
            Material.BOWL
    );

    public static Main getPlugin() {
        return Main.getInstance();
    }
}
