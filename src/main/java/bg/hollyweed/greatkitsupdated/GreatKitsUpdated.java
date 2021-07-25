package bg.hollyweed.greatkitsupdated;

import bg.hollyweed.greatkitsupdated.commands.AdminCommands;
import bg.hollyweed.greatkitsupdated.commands.PlayerCommands;
import bg.hollyweed.greatkitsupdated.listeners.Listeners;
import bg.hollyweed.greatkitsupdated.utils.GUItems;
import bg.hollyweed.greatkitsupdated.utils.Kit;
import bg.hollyweed.greatkitsupdated.utils.KitManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GreatKitsUpdated extends JavaPlugin
{
    public static final String VERSION = "3.0";
    private HashMap<String, Long> cooldowns;
    private HashMap<String, Integer> pages;
    private static bg.hollyweed.greatkitsupdated.GreatKitsUpdated INSTANCE;

    public void onEnable() {
        (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.INSTANCE = this).saveDefaultConfig();
        this.registerCommands();
        this.registerListeners();
        this.createKitsFolder();
        this.createCooldownsFolder();
        this.cooldowns = new HashMap<String, Long>();
        this.pages = new HashMap<String, Integer>();
        this.deserializeUItems();
        this.deserializeKits();
        this.deserializeCooldowns();
        System.out.println("GreatKits 3.0.0 --> ON");
    }

    private void deserializeUItems() {
        new GUItems();
    }

    public void onDisable() {
        this.serializeCooldowns();
        System.out.println("GreatKits 3.0.0 --> OFF");
    }

    private void deserializeKits() {
        final File file = new File(this.getDataFolder(), "Kits");
        try {
            for (final File files : file.listFiles()) {
                final YamlConfiguration c = YamlConfiguration.loadConfiguration(files);
                KitManager.getKits().add(new Kit(files.getName().substring(0, files.getName().length() - 4), c.getInt("Cooldown"), c.getBoolean("OneTimeUse"), c.getBoolean("FirstTimeJoinKit"), c.getString("Permission"), c.getInt("Order")));
            }
        }
        catch (NullPointerException e) {
            System.out.println("No kit were found");
        }
    }

    private void deserializeCooldowns() {
        final File file = new File(getInstance().getDataFolder() + File.separator + "Cooldowns", "Cooldowns.yml");
        final YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
        try {
            for (final String keys : c.getConfigurationSection("Cooldowns").getKeys(false)) {
                final String kitName = keys.substring(keys.indexOf("*") + 1);
                final long cooldown = System.currentTimeMillis() - c.getConfigurationSection("Cooldowns").getLong(keys) - getKit(kitName).getCooldown() * 1000L;
                if (cooldown >= 0L) {
                    continue;
                }
                this.cooldowns.put(keys, c.getConfigurationSection("Cooldowns").getLong(keys));
            }
        }
        catch (NullPointerException e) {
            System.out.println("No cooldowns were found");
        }
    }

    private void serializeCooldowns() {
        final File file = new File(getInstance().getDataFolder() + File.separator + "Cooldowns", "Cooldowns.yml");
        final YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
        c.set("Cooldowns", (Object)null);
        c.set("Cooldowns", (Object)this.cooldowns);
        try {
            c.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents((Listener)new Listeners(), (Plugin)this);
    }

    private void registerCommands() {
        this.getCommand("greatkits").setExecutor((CommandExecutor)new AdminCommands());
        this.getCommand("kit").setExecutor((CommandExecutor)new PlayerCommands());
    }

    private void createKitsFolder() {
        final File dir = new File(this.getDataFolder(), "Kits");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private void createCooldownsFolder() {
        final File dir = new File(this.getDataFolder(), "Cooldowns");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static Kit getKit(final String name) {
        for (final Kit kit : KitManager.getKits()) {
            if (!kit.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return kit;
        }
        return null;
    }

    public static boolean exists(final String name) {
        return getKit(name) != null;
    }

    public static void sendText(final CommandSender sender, final String text) {
        if (sender instanceof Player) {
            final Player p = (Player)sender;
            if (getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                p.sendMessage(PlaceholderAPI.setPlaceholders(p, text));
            }
            else {
                p.sendMessage(text);
            }
        }
        else {
            System.out.println(text);
        }
    }

    public static bg.hollyweed.greatkitsupdated.GreatKitsUpdated getInstance() {
        return bg.hollyweed.greatkitsupdated.GreatKitsUpdated.INSTANCE;
    }

    public HashMap<String, Long> getCooldowns() {
        return this.cooldowns;
    }

    public HashMap<String, Integer> getPages() {
        return this.pages;
    }
}
