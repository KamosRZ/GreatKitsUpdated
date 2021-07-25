package bg.hollyweed.greatkitsupdated.commands;

import org.bukkit.inventory.ItemStack;
import bg.hollyweed.greatkitsupdated.utils.Kit;
import bg.hollyweed.greatkitsupdated.utils.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class AdminCommands implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String msg, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (cmd.getName().equalsIgnoreCase("greatkits") || cmd.getName().equalsIgnoreCase("gk")) {
            if (args.length == 0) {
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§cG§6reat§cK§6its");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7Admin's command list");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk create §6<kitName>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk remove §6<kitName>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk setinv §6<kitName>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk setcooldown §6<kitName> <cooldown>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk setonetimeuse §6<kitName> <true:false>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk setfirstjoinkit §6<kitName> <true:false>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk seticon §6<kitName>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk iconorder");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk setorder <kitName> <orderNumber>");
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§8\u25ba §c/gk version");
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 2) {
                    if (!bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1]) && !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("preview") && !args[1].equalsIgnoreCase("help")) {
                        KitManager.getKits().add(new Kit(args[1]));
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully created the kit §e" + args[1]);
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit already exists");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk create <kitName>");
                }
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).remove();
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully §cremoved §athe kit §e" + args[1]);
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk remove <kitName>");
                }
            }
            else if (args[0].equalsIgnoreCase("setinv")) {
                if (args.length == 2) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        final ItemStack[] main = p.getInventory().getContents();
                        for (int i = main.length - 2; i > main.length - 6; --i) {
                            if (main.length != 36) {
                                main[i] = null;
                            }
                        }
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setMainContent(main);
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setArmorContent(p.getInventory().getArmorContents());
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the inventory of the kit §e" + args[1]);
                        p.updateInventory();
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setinv <kitName>");
                }
            }
            else if (args[0].equalsIgnoreCase("setcooldown")) {
                if (args.length == 3) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        if (args[2].matches(".*\\d+.*")) {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setCooldown(Integer.valueOf(args[2]));
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the cooldown of the kit §e" + args[1] + " §ato : §c" + args[2] + "s");
                        }
                        else {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cOrder must be a number");
                        }
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setcooldown <kitName> <cooldown>");
                }
            }
            else if (args[0].equalsIgnoreCase("setonetimeuse")) {
                if (args.length == 3) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                            if (args[2].equalsIgnoreCase("true")) {
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setOneTimeUse(true);
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the value of the parameter §e'OneTimeUse' §ato §ctrue");
                            }
                            else {
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setOneTimeUse(false);
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the value of the parameter §e'OneTimeUse' §ato §cfalse");
                            }
                        }
                        else {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setonetimeuse <kitName> <true:false>");
                        }
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setonetimeuse <kitName> <true:false>");
                }
            }
            else if (args[0].equalsIgnoreCase("setfirstjoinkit")) {
                if (args.length == 3) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                            if (args[2].equalsIgnoreCase("true")) {
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setFirstTimeJoinKit(true);
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the value of the parameter §e'FirstJoinKit' §ato §ctrue");
                            }
                            else {
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setFirstTimeJoinKit(false);
                                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the value of the parameter §e'FirstJoinKit' §ato §cfalse");
                            }
                        }
                        else {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setfirstjoinkit <kitName> <true:false>");
                        }
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setfirstjoinkit <kitName> <true:false>");
                }
            }
            else if (args[0].equalsIgnoreCase("seticon")) {
                if (args.length == 2) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        if (p.getItemInHand().getItemMeta().getDisplayName() == null) {
                            p.sendMessage("§7[GK] §cError, this icon doesn't have a name");
                            return true;
                        }
                        try {
                            for (final Kit kits : KitManager.getKits()) {
                                if (!p.getItemInHand().isSimilar(kits.getIcon())) {
                                    continue;
                                }
                                p.sendMessage("§7[GK] §cError, this is already the icon of the kit §e" + kits.getName());
                                return true;
                            }
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setIcon(p.getItemInHand());
                            p.sendMessage("§7[GK] §aYou successfully set the icon of the kit §e" + args[1]);
                        }
                        catch (NullPointerException e) {
                            p.sendMessage("§7[GK] §cThis item is null (the one in your hand)");
                        }
                    }
                    else {
                        p.sendMessage("§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    p.sendMessage("§7[GK] §cUsage : /gk setfirstjoinkit <kitName> <true:false>");
                }
            }
            else if (args[0].equalsIgnoreCase("iconorder")) {
                for (final Kit kits : KitManager.getKits()) {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§eOrder : §c" + kits.getOrder() + " §e|| §9" + kits.getName());
                }
            }
            else if (args[0].equalsIgnoreCase("setorder")) {
                if (args.length == 3) {
                    if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.exists(args[1])) {
                        if (args[2].matches(".*\\d+.*")) {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getKit(args[1]).setOrder(Integer.valueOf(args[2]));
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aYou successfully set the order of the kit §e" + args[1] + " §ato : §c" + args[2]);
                        }
                        else {
                            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cOrder must be a number");
                        }
                    }
                    else {
                        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cThis kit doesn't exist");
                    }
                }
                else {
                    bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §cUsage : /gk setorder <kitName> <orderNumber>");
                }
            }
            else if (args[0].equalsIgnoreCase("version") && args.length == 1) {
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.sendText(sender, "§7[GK] §aVersion : §63.0.0");
            }
        }
        return false;
    }
}
