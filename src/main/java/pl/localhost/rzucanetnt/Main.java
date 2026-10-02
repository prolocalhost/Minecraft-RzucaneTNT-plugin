package pl.localhost.rzucanetnt;

import org.bukkit.plugin.java.JavaPlugin;
import pl.localhost.rzucanetnt.command.RzucaneTNTCommand;
import pl.localhost.rzucanetnt.listener.TNTListener;
import pl.localhost.rzucanetnt.manager.ConfigManager;
import pl.localhost.rzucanetnt.manager.CooldownManager;
import pl.localhost.rzucanetnt.manager.TNTManager;

public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private CooldownManager cooldownManager;
    private TNTManager tntManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.cooldownManager = new CooldownManager(this);
        this.tntManager = new TNTManager(this);

        this.tntManager.registerCraftingRecipe();

        RzucaneTNTCommand cmd = new RzucaneTNTCommand(this);
        if (getCommand("rzucanetnt") != null) {
            getCommand("rzucanetnt").setExecutor(cmd);
            getCommand("rzucanetnt").setTabCompleter(cmd);
        }

        getServer().getPluginManager().registerEvents(new TNTListener(this), this);

        this.cooldownManager.startActionBarTask();

        getLogger().info("Plugin localhost-rzucanetnt zostal pomyslnie wlaczony!");
    }

    @Override
    public void onDisable() {
        if (this.cooldownManager != null) {
            this.cooldownManager.stopActionBarTask();
        }
        if (this.tntManager != null) {
            this.tntManager.unregisterCraftingRecipe();
        }

        getLogger().info("Plugin localhost-rzucanetnt zostal pomyslnie wylaczony!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public TNTManager getTNTManager() {
        return tntManager;
    }

    public String color(String text) {
        return configManager.color(text);
    }
}
