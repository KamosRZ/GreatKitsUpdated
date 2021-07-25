package bg.hollyweed.greatkitsupdated.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kit
{
    private String name;
    private int cooldown;
    private String permission;
    private int order;
    private ItemStack[] mainContent;
    private ItemStack[] armorContent;
    private ItemStack icon;
    private boolean oneTimeUse;
    private boolean firstTimeJoinKit;
    private File file;
    private FileConfiguration config;
    
    public Kit(final String name) {
        this.name = name;
        this.cooldown = 0;
        this.permission = "greatkits.kits." + name;
        this.order = 0;
        this.oneTimeUse = false;
        this.firstTimeJoinKit = false;
        this.icon = null;
        this.file = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Kits", name + ".yml");
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        this.createFile();
    }
    
    public Kit(final String name, final int cooldown, final boolean oneTimeUse, final boolean firstTimeJoinKit, final String permission, final int order) {
        this.name = name;
        this.cooldown = cooldown;
        this.permission = permission;
        this.order = order;
        this.oneTimeUse = oneTimeUse;
        this.firstTimeJoinKit = firstTimeJoinKit;
        this.file = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Kits", name + ".yml");
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        this.icon = this.config.getItemStack("Icon");
    }
    
    public void give(final Player p) {
        if (p.hasPermission("greatkits.admin")) {
            this.giveItems(p);
            return;
        }
        if (this.oneTimeUse) {
            if (!p.hasPermission(this.permission) && !p.hasPermission("greatkits.kits.*")) {
                p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-permission").replace("&", "§"));
                return;
            }
            final File file = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Cooldowns", "OneTimeUseList.yml");
            final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                final List list = config.getStringList(this.name);
                if (list.contains(p.getName())) {
                    p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.already-got-one-time-use-kit").replace("&", "§"));
                    return;
                }
                this.giveItems(p);
                list.add(p.getName());
                config.set(this.name, (Object)list);
            }
            catch (NullPointerException e2) {
                this.giveItems(p);
                final ArrayList<String> list2 = new ArrayList<String>();
                list2.add(p.getName());
                config.set(this.name, (Object)list2);
            }
            try {
                config.save(file);
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (!p.hasPermission(this.permission) && !p.hasPermission("greatkits.kits.*")) {
            p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-permission").replace("&", "§"));
            return;
        }
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().containsKey(p.getName() + "*" + this.name)) {
            final long cooldown = System.currentTimeMillis() - bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().get(p.getName() + "*" + this.name) - this.cooldown * 1000;
            if (cooldown < 0L) {
                p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.cooldown-message").replace("&", "§").replace(":cooldown:", bg.hollyweed.greatkitsupdated.utils.TimeFormat.getFormattedCooldown(Math.abs(cooldown))));
                return;
            }
            this.giveItems(p);
        }
        else {
            this.giveItems(p);
        }
        if (p.hasPermission("greatkits.admin")) {
            return;
        }
        if (this.oneTimeUse) {
            return;
        }
        bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().put(p.getName() + "*" + this.name, System.currentTimeMillis());
    }
    
    public void giveItems(final Player p) {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        try {
            this.mainContent = (ItemStack[]) ((List)this.config.get("Inventory.Main")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            this.mainContent = new ItemStack[0];
        }
        try {
            this.armorContent = (ItemStack[]) ((List)this.config.get("Inventory.Armor")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            this.armorContent = new ItemStack[0];
        }
        if (this.mainContent.length + this.armorContent.length == 0) {
            p.sendMessage("§cThis kit doesn't have an inventory yet");
            return;
        }
        for (int i = 0; i < this.mainContent.length; ++i) {
            if (this.mainContent[i] != null) {
                if (i == 40 && p.getInventory().getItemInOffHand().getType().equals(Material.AIR)) {
                    p.getInventory().setItemInOffHand(this.mainContent[i]);
                }
                else if (p.getInventory().firstEmpty() != -1) {
                    p.getInventory().addItem(new ItemStack[] { this.mainContent[i] });
                }
                else {
                    p.getWorld().dropItemNaturally(p.getLocation(), this.mainContent[i]);
                }
            }
        }
        if (this.armorContent[3] != null && this.armorContent[3].getType() != Material.AIR) {
            if (p.getInventory().getHelmet() == null) {
                p.getInventory().setHelmet(this.armorContent[3]);
            }
            else if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(new ItemStack[] { this.armorContent[3] });
            }
            else {
                p.getWorld().dropItemNaturally(p.getLocation(), this.armorContent[3]);
            }
        }
        if (this.armorContent[2] != null && this.armorContent[2].getType() != Material.AIR) {
            if (p.getInventory().getChestplate() == null) {
                p.getInventory().setChestplate(this.armorContent[2]);
            }
            else if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(new ItemStack[] { this.armorContent[2] });
            }
            else {
                p.getWorld().dropItemNaturally(p.getLocation(), this.armorContent[2]);
            }
        }
        if (this.armorContent[1] != null && this.armorContent[1].getType() != Material.AIR) {
            if (p.getInventory().getLeggings() == null) {
                p.getInventory().setLeggings(this.armorContent[1]);
            }
            else if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(new ItemStack[] { this.armorContent[1] });
            }
            else {
                p.getWorld().dropItemNaturally(p.getLocation(), this.armorContent[1]);
            }
        }
        if (this.armorContent[0] != null && this.armorContent[0].getType() != Material.AIR) {
            if (p.getInventory().getBoots() == null) {
                p.getInventory().setBoots(this.armorContent[0]);
            }
            else if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(new ItemStack[] { this.armorContent[0] });
            }
            else {
                p.getWorld().dropItemNaturally(p.getLocation(), this.armorContent[0]);
            }
        }
        p.updateInventory();
        p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.received-kit").replace("&", "§").replace(":kitname:", this.name));
    }
    
    public void giveFirstJoinKit(final Player p) {
        if (this.firstTimeJoinKit) {
            if (this.oneTimeUse) {
                final File file = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Cooldowns", "OneTimeUseList.yml");
                final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    final List list = config.getStringList(this.name);
                    if (list.contains(p.getName())) {
                        p.sendMessage("§cYou already got this kit");
                        return;
                    }
                    this.giveItems(p);
                    list.add(p.getName());
                    config.set(this.name, (Object)list);
                }
                catch (NullPointerException ex2) {
                    this.giveItems(p);
                    final ArrayList<String> list2 = new ArrayList<String>();
                    list2.add(p.getName());
                    config.set(this.name, (Object)list2);
                }
                try {
                    config.save(file);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                this.giveItems(p);
            }
            if (!this.oneTimeUse) {
                bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().put(p.getName() + "*" + this.name, System.currentTimeMillis());
            }
        }
    }
    
    public void preview(final Player p) {
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getBoolean("permission.kit-preview") && !p.hasPermission("greatkits.preview")) {
            p.sendMessage(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getString("lang.no-permission").replace("&", "§"));
            return;
        }
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);
        ItemStack[] main;
        try {
            main = (ItemStack[]) ((List)config.get("Inventory.Main")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            main = new ItemStack[0];
        }
        ItemStack[] armor;
        try {
            armor = (ItemStack[]) ((List)config.get("Inventory.Armor")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            armor = new ItemStack[0];
        }
        if (main.length + armor.length == 0) {
            p.sendMessage("§cThis doesn't have an inventory yet");
            return;
        }
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (p.getOpenInventory() != null) ? 54 : 45, "§7Kit Preview : §9" + this.name);
        inventory.setContents(main);
        inventory.setItem(36, armor[3]);
        inventory.setItem(37, armor[2]);
        inventory.setItem(38, armor[1]);
        inventory.setItem(39, armor[0]);
        if (p.getOpenInventory() != null) {
            inventory.setItem(45, GUItems.getExitButton());
            inventory.setItem(53, GUItems.getSelectButton());
        }
        p.openInventory(inventory);
        p.updateInventory();
    }
    
    public void remove() {
        this.file.delete();
        for (final String str : bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().keySet()) {
            if (!str.contains("*" + this.name)) {
                continue;
            }
            bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getCooldowns().remove(str);
        }
        final File file1 = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Cooldowns", "Cooldowns.yml");
        final YamlConfiguration c1 = YamlConfiguration.loadConfiguration(file1);
        try {
            final HashMap<String, Long> hash = new HashMap<String, Long>();
            for (final String keys : c1.getConfigurationSection("Cooldowns").getKeys(false)) {
                if (keys.contains("*" + this.name)) {
                    continue;
                }
                hash.put(keys, c1.getConfigurationSection("Cooldowns").getLong(keys));
            }
            c1.set("Cooldowns", (Object)null);
            c1.set("Cooldowns", (Object)hash);
            try {
                c1.save(file1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (NullPointerException e3) {
            System.out.println("No cooldowns were found");
        }
        final File file2 = new File(bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getDataFolder() + File.separator + "Cooldowns", "OneTimeUseList.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file2);
        config.set(this.name, (Object)null);
        try {
            config.save(file2);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        bg.hollyweed.greatkitsupdated.utils.KitManager.getKits().remove(this);
    }
    
    private void createFile() {
        this.config.set("Name", (Object)this.name);
        this.config.set("Cooldown", (Object)this.cooldown);
        this.config.set("Permission", (Object)this.permission);
        this.config.set("Order", (Object)this.order);
        if (this.config.get("Inventory") == null) {
            this.config.set("Inventory", (Object)null);
        }
        this.config.set("FirstTimeJoinKit", (Object)false);
        this.config.set("OneTimeUse", (Object)false);
        this.config.set("Icon", (Object)this.icon);
        this.save();
    }
    
    private void save() {
        try {
            this.config.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }

    //cooldown should be Long, to allow for cooldowns longer than 25 days
    public void setCooldown(final int cooldown) {
        this.cooldown = cooldown;
        this.config.set("Cooldown", (Object)cooldown);
        this.save();
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public ItemStack[] getMainContent() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        try {
            this.mainContent = (ItemStack[]) ((List)this.config.get("Inventory.Main")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            this.mainContent = new ItemStack[0];
        }
        return this.mainContent;
    }
    
    public ItemStack[] getArmorContent() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        try {
            this.armorContent = (ItemStack[]) ((List)this.config.get("Inventory.Armor")).toArray(new ItemStack[0]);
        }
        catch (NullPointerException e) {
            this.armorContent = new ItemStack[0];
        }
        return this.armorContent;
    }
    
    public void setMainContent(final ItemStack[] mainContent) {
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("Inventory.Main", (Object)mainContent);
        this.save();
    }
    
    public void setArmorContent(final ItemStack[] armorContent) {
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("Inventory.Armor", (Object)armorContent);
        this.save();
    }
    
    public File getFile() {
        return this.file;
    }
    
    public boolean isOneTimeUse() {
        return this.oneTimeUse;
    }
    
    public void setOneTimeUse(final boolean oneTimeUse) {
        this.oneTimeUse = oneTimeUse;
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("OneTimeUse", (Object)oneTimeUse);
        this.save();
    }
    
    public boolean isFirstTimeJoinKit() {
        return this.firstTimeJoinKit;
    }
    
    public void setFirstTimeJoinKit(final boolean firstTimeJoinKit) {
        this.firstTimeJoinKit = firstTimeJoinKit;
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("FirstTimeJoinKit", (Object)firstTimeJoinKit);
        this.save();
    }
    
    public ItemStack getIcon() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        return this.icon = this.config.getItemStack("Icon");
    }
    
    public void setIcon(final ItemStack icon) {
        this.icon = icon;
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("Icon", (Object)icon);
        this.save();
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder(final int order) {
        this.order = order;
        (this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file)).set("Order", (Object)order);
        this.save();
    }
}
