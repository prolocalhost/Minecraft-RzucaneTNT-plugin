package pl.localhost.rzucanetnt.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pl.localhost.rzucanetnt.Main;

import java.util.HashMap;
import java.util.List;

public class TNTManager {

    private final Main plugin;
    private final NamespacedKey itemKey;
    private final NamespacedKey entityKey;
    private final NamespacedKey recipeKey;

    public TNTManager(Main plugin) {
        this.plugin = plugin;
        this.itemKey = new NamespacedKey(plugin, "rzucane_tnt");
        this.entityKey = new NamespacedKey(plugin, "rzucane_tnt_entity");
        this.recipeKey = new NamespacedKey(plugin, "rzucane_tnt_recipe");
    }

    public ItemStack createRzucaneTNT(int amount) {
        Material mat = plugin.getConfigManager().getItemMaterial();
        ItemStack item = new ItemStack(mat, Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(plugin.getConfigManager().getItemDisplayName());
            List<String> lore = plugin.getConfigManager().getItemLore();
            meta.setLore(lore);

            int cmd = plugin.getConfigManager().getItemCustomModelData();
            if (cmd > 0) {
                meta.setCustomModelData(cmd);
            }

            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }

        return item;
    }

    public boolean isRzucaneTNT(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(itemKey, PersistentDataType.BYTE)) {
            return true;
        }

        if (meta.hasCustomModelData() && meta.getCustomModelData() == plugin.getConfigManager().getItemCustomModelData()) {
            return true;
        }

        if (meta.hasDisplayName() && meta.getDisplayName().equals(plugin.getConfigManager().getItemDisplayName())) {
            return true;
        }

        return false;
    }

    public void throwTNT(Player player) {
        TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
        tnt.setFuseTicks(plugin.getConfigManager().getFuseTicks());
        tnt.setSource(player);

        tnt.getPersistentDataContainer().set(entityKey, PersistentDataType.BYTE, (byte) 1);

        double velMultiplier = plugin.getConfigManager().getVelocityMultiplier();
        double upLift = plugin.getConfigManager().getUpwardLift();

        Vector dir = player.getLocation().getDirection().normalize();
        Vector velocity = dir.multiply(velMultiplier);
        velocity.setY(velocity.getY() + upLift);
        tnt.setVelocity(velocity);

        plugin.getCooldownManager().setCooldown(player.getUniqueId());

        Sound sound = plugin.getConfigManager().getLaunchSound();
        player.getWorld().playSound(player.getLocation(), sound, 0.8f, 1.2f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f);

        if (plugin.getConfigManager().isParticleTrailEnabled()) {
            startParticleTrail(tnt);
        }
    }

    private void startParticleTrail(TNTPrimed tnt) {
        Particle particle = plugin.getConfigManager().getTrailParticle();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (tnt.isDead() || !tnt.isValid()) {
                    cancel();
                    return;
                }
                tnt.getWorld().spawnParticle(particle, tnt.getLocation().add(0, 0.25, 0), 3, 0.05, 0.05, 0.05, 0.01);
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    public boolean isRzucaneTNTEntity(Entity entity) {
        if (!(entity instanceof TNTPrimed)) {
            return false;
        }
        return entity.getPersistentDataContainer().has(entityKey, PersistentDataType.BYTE);
    }

    public void registerCraftingRecipe() {
        if (!plugin.getConfigManager().isCraftingEnabled()) {
            return;
        }

        int amount = plugin.getConfigManager().getCraftingResultAmount();
        ItemStack result = createRzucaneTNT(amount);

        ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);
        recipe.shape("GTG", "TFT", "GTG");
        recipe.setIngredient('G', Material.GUNPOWDER);
        recipe.setIngredient('T', Material.TNT);
        recipe.setIngredient('F', Material.FIRE_CHARGE);

        plugin.getServer().addRecipe(recipe);
    }

    public void unregisterCraftingRecipe() {
        plugin.getServer().removeRecipe(recipeKey);
    }

    public boolean giveRzucaneTNT(Player player, int amount) {
        ItemStack item = createRzucaneTNT(amount);
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);

        if (!leftover.isEmpty()) {
            for (ItemStack left : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), left);
            }
            player.sendMessage(plugin.getConfigManager().getMessage("inventory-full"));
            return false;
        }
        return true;
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    public NamespacedKey getEntityKey() {
        return entityKey;
    }
}
