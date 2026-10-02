package pl.localhost.rzucanetnt.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.localhost.rzucanetnt.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Main plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask actionBarTask;

    public CooldownManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean hasCooldown(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return false;
        return cooldowns.get(uuid) > System.currentTimeMillis();
    }

    public long getRemainingSeconds(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return 0;
        long timeDiff = cooldowns.get(uuid) - System.currentTimeMillis();
        return Math.max(0, Math.round(timeDiff / 1000.0));
    }

    public void setCooldown(UUID uuid) {
        long expireTime = System.currentTimeMillis() + (plugin.getConfigManager().getCooldown() * 1000L);
        cooldowns.put(uuid, expireTime);
    }

    public void clearCooldown(UUID uuid) {
        cooldowns.remove(uuid);
    }

    public void startActionBarTask() {
        if (actionBarTask != null) {
            actionBarTask.cancel();
        }

        actionBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().isActionBarEnabled()) {
                    return;
                }

                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ItemStack mainHand = player.getInventory().getItemInMainHand();
                    ItemStack offHand = player.getInventory().getItemInOffHand();

                    if (plugin.getTNTManager().isRzucaneTNT(mainHand) || plugin.getTNTManager().isRzucaneTNT(offHand)) {
                        if (hasCooldown(player.getUniqueId())) {
                            long remaining = getRemainingSeconds(player.getUniqueId());
                            String msg = plugin.getConfigManager().getRawMessage("actionbar-cooldown")
                                    .replace("%time%", String.valueOf(remaining));
                            player.sendActionBar(msg);
                        } else {
                            String msg = plugin.getConfigManager().getRawMessage("actionbar-ready");
                            player.sendActionBar(msg);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void stopActionBarTask() {
        if (actionBarTask != null) {
            actionBarTask.cancel();
            actionBarTask = null;
        }
        cooldowns.clear();
    }
}
