package pl.localhost.rzucanetnt.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.localhost.rzucanetnt.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RzucaneTNTCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public RzucaneTNTCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("receptura") || args[0].equalsIgnoreCase("crafting")) {
            sender.sendMessage(plugin.getConfigManager().color("&8---------- &cRzucane TNT &8----------"));
            sender.sendMessage(plugin.getConfigManager().color(" &8» &fKliknij &ePPM &ftrzymając w dłoni, aby wyrzucić ładunek."));
            sender.sendMessage(plugin.getConfigManager().color(" &8» &fCzas do wybuchu: &c" + (plugin.getConfigManager().getFuseTicks() / 20.0) + "s"));
            sender.sendMessage(plugin.getConfigManager().color(" &8» &fSzansa na zniszczenie obsydianu: &c" + plugin.getConfigManager().getObsidianBreakChance() + "%"));
            sender.sendMessage(plugin.getConfigManager().getMessage("recipe-info"));
            sender.sendMessage(plugin.getConfigManager().color("&8------------------------------------"));
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("reload") || sub.equals("rl")) {
            if (!sender.hasPermission("rzucanetnt.admin")) {
                sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                return true;
            }

            plugin.getConfigManager().reloadConfig();
            plugin.getTNTManager().unregisterCraftingRecipe();
            plugin.getTNTManager().registerCraftingRecipe();
            sender.sendMessage(plugin.getConfigManager().getMessage("reload"));
            return true;
        }

        if (sub.equals("daj") || sub.equals("give")) {
            if (!sender.hasPermission("rzucanetnt.admin")) {
                sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(plugin.getConfigManager().getMessage("usage-give"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfigManager().getMessage("player-not-found"));
                return true;
            }

            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                    if (amount <= 0 || amount > 64) {
                        sender.sendMessage(plugin.getConfigManager().getMessage("invalid-number"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("invalid-number"));
                    return true;
                }
            }

            plugin.getTNTManager().giveRzucaneTNT(target, amount);

            String senderMsg = plugin.getConfigManager().getMessage("given-sender")
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%player%", target.getName());
            sender.sendMessage(senderMsg);

            String targetMsg = plugin.getConfigManager().getMessage("given-target")
                    .replace("%amount%", String.valueOf(amount));
            target.sendMessage(targetMsg);
            return true;
        }

        sender.sendMessage(plugin.getConfigManager().getMessage("usage"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("receptura");
            if (sender.hasPermission("rzucanetnt.admin")) {
                list.add("daj");
                list.add("reload");
            }
            return filter(list, args[0]);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("daj")) {
            if (!sender.hasPermission("rzucanetnt.admin")) return list;
            return filter(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[1]);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("daj")) {
            if (!sender.hasPermission("rzucanetnt.admin")) return list;
            list.add("1");
            list.add("16");
            list.add("64");
            return filter(list, args[2]);
        }

        return list;
    }

    private List<String> filter(List<String> list, String query) {
        String lower = query.toLowerCase();
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }
}
