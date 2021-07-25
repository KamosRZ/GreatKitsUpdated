package bg.hollyweed.greatkitsupdated.commands;

import bg.hollyweed.greatkitsupdated.utils.InventoryPage;
import bg.hollyweed.greatkitsupdated.utils.Kit;
import bg.hollyweed.greatkitsupdated.utils.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerCommands implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String msg, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (args.length == 0) {
                if (!bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-gui-kit-displaying")) {
                    String builder = "Kits: ";
                    for (final Kit kits : KitManager.getKits()) {
                        if (!p.hasPermission(kits.getPermission()) && !p.hasPermission("greatkits.kits.*") && !p.hasPermission("greatkits.admin")) {
                            continue;
                        }
                        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().get(p.getName() + "*" + kits.getName()) != null) {
                            final long cooldown = System.currentTimeMillis() - bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().get(p.getName() + "*" + kits.getName()) - kits.getCooldown() * 1000;
                            if (cooldown >= 0L) {
                                builder = builder.concat("§a" + kits.getName() + "§7, ");
                            }
                            else {
                                builder = builder.concat("§c" + kits.getName() + "§7, ");
                            }
                        }
                        else {
                            builder = builder.concat("§a" + kits.getName() + "§7, ");
                        }
                    }
                    if ((builder = builder.substring(0, builder.length() - 4)).equalsIgnoreCase("Ki")) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-access").replace("&", "§"));
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, builder);
                    }
                }
                else {
                    new InventoryPage(p);
                }
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("preview")) {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.preview-usage-error").replace("&", "§"));
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getBoolean("permission.kit-list") && !p.hasPermission("greatkits.list")) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-permission").replace("&", "§"));
                        return true;
                    }
                    String builder = "Kits: ";
                    for (final Kit kits : KitManager.getKits()) {
                        builder = builder.concat("§6" + kits.getName() + "§7, ");
                    }
                    if ((builder = builder.substring(0, builder.length() - 4)).equalsIgnoreCase("Ki")) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-kit-server").replace("&", "§"));
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, builder);
                    }
                }
                else {
                    if (args[0].equalsIgnoreCase("help")) {
                        try {
                            final List<String> list = (List<String>) bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getStringList("lang.list-help");
                            for (final String str : list) {
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, str.replace("&", "§"));
                            }
                            return true;
                        }
                        catch (NullPointerException e) {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§cERROR : The kit-help message doesn't exist");
                            return false;
                        }
                    }
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[0])) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[0]).give(p);
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.kit-doesnt-exist").replace("&", "§"));
                    }
                }
            }
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("preview")) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).preview(p);
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.kit-doesnt-exist").replace("&", "§"));
                    }
                }
                else if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[0])) {
                    if (Bukkit.getPlayer(args[1]).isOnline()) {
                        if (!(sender instanceof Player)) {
                            final Player target = Bukkit.getPlayer(args[1]);
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[0]).giveItems(target);
                            System.out.println("Kit successfully given to " + args[1]);
                        }
                        else if (p.hasPermission("greatkits.admin")) {
                            final Player target = Bukkit.getPlayer(args[1]);
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[0]).giveItems(target);
                            p.sendMessage("§aKit successfully given to §e" + args[1]);
                        }
                        else {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-permission").replace("&", "§"));
                        }
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§cThis player doesn't exist/is not online");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.kit-doesnt-exist").replace("&", "§"));
                }
            }
        }
        return false;
    }
}
