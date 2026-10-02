package pl.localhost.rzucanetnt.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.localhost.rzucanetnt.Main;

import java.util.Random;

public class TNTListener implements Listener {

    private final Main plugin;
    private final Random random = new Random();

    public TNTListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (!plugin.getTNTManager().isRzucaneTNT(item)) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (!player.hasPermission("rzucanetnt.use")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return;
        }

        if (plugin.getCooldownManager().hasCooldown(player.getUniqueId())) {
            long remaining = plugin.getCooldownManager().getRemainingSeconds(player.getUniqueId());
            String msg = plugin.getConfigManager().getMessage("cooldown")
                    .replace("%time%", String.valueOf(remaining));
            player.sendMessage(msg);
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            if (event.getHand() != null) {
                player.getInventory().setItem(event.getHand(), null);
            }
        }

        plugin.getTNTManager().throwTNT(player);
        player.sendMessage(plugin.getConfigManager().getMessage("thrown"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!plugin.getTNTManager().isRzucaneTNTEntity(event.getEntity())) {
            return;
        }

        Location loc = event.getLocation();
        double obsidianChance = plugin.getConfigManager().getObsidianBreakChance();
        boolean breakWater = plugin.getConfigManager().isBreakWaterBlocks();

        if (obsidianChance > 0) {
            int radius = 3;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block b = loc.clone().add(x, y, z).getBlock();
                        if (b.getType() == Material.OBSIDIAN || b.getType() == Material.CRYING_OBSIDIAN) {
                            if (random.nextDouble() * 100.0 <= obsidianChance) {
                                b.breakNaturally();
                            }
                        }
                    }
                }
            }
        }

        if (breakWater && loc.getBlock().isLiquid()) {
            int radius = 2;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block b = loc.clone().add(x, y, z).getBlock();
                        if (b.getType() != Material.BEDROCK && b.getType() != Material.AIR && !b.isLiquid()) {
                            if (b.getType() == Material.OBSIDIAN || b.getType() == Material.CRYING_OBSIDIAN) {
                                if (random.nextDouble() * 100.0 <= obsidianChance) {
                                    b.breakNaturally();
                                }
                            } else {
                                b.breakNaturally();
                            }
                        }
                    }
                }
            }
        }
    }
}
