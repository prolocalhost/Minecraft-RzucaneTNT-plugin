package pl.localhost.rzucanetnt.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import pl.localhost.rzucanetnt.Main;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final Main plugin;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getPrefix() {
        return color(getConfig().getString("messages.prefix", "&8[&cRzucaneTNT&8] &7"));
    }

    public String getMessage(String path) {
        String msg = getConfig().getString("messages." + path, "&cWiadomość nieznaleziona: " + path);
        return getPrefix() + color(msg);
    }

    public String getRawMessage(String path) {
        String msg = getConfig().getString("messages." + path, "");
        return color(msg);
    }

    public int getCooldown() {
        return getConfig().getInt("settings.cooldown", 5);
    }

    public int getFuseTicks() {
        return getConfig().getInt("settings.fuse-ticks", 35);
    }

    public double getVelocityMultiplier() {
        return getConfig().getDouble("settings.velocity-multiplier", 1.4);
    }

    public double getUpwardLift() {
        return getConfig().getDouble("settings.upward-lift", 0.2);
    }

    public float getExplosionPower() {
        return (float) getConfig().getDouble("settings.explosion-power", 4.0);
    }

    public boolean isBreakWaterBlocks() {
        return getConfig().getBoolean("settings.break-water-blocks", true);
    }

    public double getObsidianBreakChance() {
        return getConfig().getDouble("settings.obsidian-break-chance", 25.0);
    }

    public boolean isActionBarEnabled() {
        return getConfig().getBoolean("settings.actionbar-enabled", true);
    }

    public boolean isParticleTrailEnabled() {
        return getConfig().getBoolean("settings.particle-trail", true);
    }

    public Particle getTrailParticle() {
        String name = getConfig().getString("settings.trail-particle", "FLAME");
        try {
            return Particle.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Particle.FLAME;
        }
    }

    public Sound getLaunchSound() {
        String name = getConfig().getString("settings.launch-sound", "ENTITY_FIREWORK_ROCKET_LAUNCH");
        try {
            return Sound.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
        }
    }

    public Material getItemMaterial() {
        String matName = getConfig().getString("item.material", "TNT");
        Material mat = Material.matchMaterial(matName);
        return mat != null ? mat : Material.TNT;
    }

    public int getItemCustomModelData() {
        return getConfig().getInt("item.custom-model-data", 201);
    }

    public String getItemDisplayName() {
        return color(getConfig().getString("item.name", "&c&lRzucane TNT"));
    }

    public List<String> getItemLore() {
        List<String> raw = getConfig().getStringList("item.lore");
        List<String> formatted = new ArrayList<>();
        int cd = getCooldown();
        for (String line : raw) {
            formatted.add(color(line.replace("%cooldown%", String.valueOf(cd))));
        }
        return formatted;
    }

    public boolean isCraftingEnabled() {
        return getConfig().getBoolean("crafting.enabled", true);
    }

    public int getCraftingResultAmount() {
        return getConfig().getInt("crafting.result-amount", 1);
    }
}
