package bg.hollyweed.greatkitsupdated.utils;

import bg.hollyweed.greatkitsupdated.GreatKitsUpdated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InventoryPage
{
    private int currentPage;
    private int totalPage;
    private boolean canGoToPreviousPage;
    private boolean canGoToNextPage;

    public InventoryPage() {
        this.canGoToPreviousPage = false;
        this.canGoToNextPage = false;
    }

    public InventoryPage(final InventoryView inv) {
        this.currentPage = Integer.valueOf(inv.getTitle().substring(17, 18));
        this.totalPage = Integer.valueOf(inv.getTitle().substring(19, 20));
        this.canGoToPreviousPage = false;
        this.canGoToNextPage = false;
    }

    public InventoryPage(final Player p) {
        this.initializeInventory(p);
    }

    private void initializeInventory(final Player p) {
        int kitPlayerNumber = 0;
        for (final Kit kits : KitManager.getKits()) {
            if (kits.getIcon() == null) {
                p.sendMessage("§cThe kit §e" + kits.getName() + " §cdoesn't have an icon");
                return;
            }
            if (kits.getIcon().getItemMeta().getDisplayName() == null) {
                p.sendMessage("§cThe icon of the kit §e" + kits.getName() + " §cdoesn't have a name");
                return;
            }
            if (!p.hasPermission(kits.getPermission()) && !p.hasPermission("greatkits.kits.*") && !GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.kit-display-without-perm")) {
                continue;
            }
            ++kitPlayerNumber;
        }
        if (kitPlayerNumber == 0) {
            p.sendMessage(GreatKitsUpdated.getInstance().getConfig().getString("lang.no-kit-server").replace("&", "§"));
            return;
        }
        if (GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item")) {
            ++kitPlayerNumber;
        }
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, (kitPlayerNumber <= 9) ? 9 : ((kitPlayerNumber <= 18) ? 18 : ((kitPlayerNumber <= 27) ? 27 : ((kitPlayerNumber <= 36) ? 36 : ((kitPlayerNumber <= 45) ? 45 : 54)))), "§7Kit list: Page 1/" + (int)Math.ceil(kitPlayerNumber / 45.0));
        if (GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-gui-kit-previewing") && GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item")) {
            inv.addItem(new ItemStack[] { GUItems.getHelpItem() });
        }
        if (kitPlayerNumber > 45) {
            inv.setItem(45, GUItems.getPreviousButton());
            inv.setItem(53, GUItems.getNextButton());
            this.currentPage = 1;
            this.totalPage = (int)Math.ceil(kitPlayerNumber / 45.0);
            this.canGoToPreviousPage = false;
            this.canGoToNextPage = false;
        }
        final ArrayList<Integer> orders = new ArrayList<Integer>();
        for (int i = 0; i < KitManager.getKits().size(); ++i) {
            if (p.hasPermission("greatkits.admin") || p.hasPermission(KitManager.getKits().get(i).getPermission()) || p.hasPermission("greatkits.kits.*") || GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.kit-display-without-perm")) {
                orders.add(KitManager.getKits().get(i).getOrder());
            }
            else {
                orders.add(10000000);
            }
        }
        for (int substract = GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item") ? 1 : 0, j = 0; j < kitPlayerNumber - substract; ++j) {
            final int min = orders.indexOf(Collections.min(orders));
            inv.addItem(new ItemStack[] { this.getModifiedItem(p, min) });
            orders.set(min, 10000000);
            if (inv.firstEmpty() == 46) {
                break;
            }
        }
        p.openInventory(inv);
        p.updateInventory();
    }

    public void nextPage(final Player p) {
        if (this.CanGoToNextPage()) {
            this.setCurrentPage(this.currentPage + 1);
            this.changePage(p);
        }
    }

    public void previousPage(final Player p) {
        if (this.CanGoToPreviousPage()) {
            this.setCurrentPage(this.currentPage - 1);
            this.changePage(p);
        }
    }

    public void returnToLastPage(final Player p) {
        try {
            this.setCurrentPage(GreatKitsUpdated.getInstance().getPages().get(p.getName()));
        }
        catch (NullPointerException e) {
            this.setCurrentPage(1);
        }
        this.changePage(p);
    }

    private void changePage(final Player p) {
        int kitplayerNumber = 0;
        for (final Kit kits : KitManager.getKits()) {
            if (!p.hasPermission("greatkits.admin") && !p.hasPermission(kits.getPermission()) && !p.hasPermission("greatkits.kits.*") && !GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.kit-display-without-perm")) {
                continue;
            }
            ++kitplayerNumber;
        }
        if (GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item")) {
            ++kitplayerNumber;
        }
        if (kitplayerNumber <= 45) {
            this.initializeInventory(p);
            return;
        }
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§7Kit list: Page " + this.currentPage + "/" + (int)Math.ceil(kitplayerNumber / 45.0));
        if (GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-gui-kit-previewing") && GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item") && this.currentPage == 1) {
            inv.addItem(new ItemStack[] { GUItems.getHelpItem() });
        }
        inv.setItem(45, GUItems.getPreviousButton());
        inv.setItem(53, GUItems.getNextButton());
        final ArrayList<Integer> orders = new ArrayList<Integer>();
        for (int i2 = 0; i2 < KitManager.getKits().size(); ++i2) {
            if (p.hasPermission("greatkits.admin") || p.hasPermission(KitManager.getKits().get(i2).getPermission()) || p.hasPermission("greatkits.kits.*") || GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.kit-display-without-perm")) {
                orders.add(KitManager.getKits().get(i2).getOrder());
            }
            else {
                orders.add(10000000);
            }
        }
        final int substract = GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-help-item") ? 1 : 0;
        for (int j = 0; j < 45 * (this.currentPage - 1) - substract; ++j) {
            final int min = orders.indexOf(Collections.min(orders));
            orders.set(min, 10000000);
        }
        for (int j = 0; j < kitplayerNumber - substract; ++j) {
            final int min = orders.indexOf(Collections.min(orders));
            inv.addItem(new ItemStack[] { KitManager.getKits().get(min).getIcon() });
            orders.set(min, 10000000);
            if (inv.firstEmpty() == 46) {
                break;
            }
            if (Collections.min((Collection<? extends Integer>)orders) == 10000000) {
                break;
            }
        }
        p.openInventory(inv);
        p.updateInventory();
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(final int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean CanGoToPreviousPage() {
        return this.canGoToPreviousPage = (this.currentPage > 1);
    }

    public boolean CanGoToNextPage() {
        return this.canGoToNextPage = (this.currentPage < this.totalPage);
    }

    private ItemStack getModifiedItem(final Player p, final int min) {
        final ItemStack current = KitManager.getKits().get(min).getIcon();
        final ItemMeta currentMeta = current.getItemMeta();
        final List<String> currentLore = (currentMeta.getLore() == null) ? new ArrayList<String>() : currentMeta.getLore();
        if (GreatKitsUpdated.getInstance().getCooldowns().containsKey(p.getName() + "*" + KitManager.getKits().get(min).getName())) {
            final long cooldown = System.currentTimeMillis() - GreatKitsUpdated.getInstance().getCooldowns().get(p.getName() + "*" + KitManager.getKits().get(min).getName()) - KitManager.getKits().get(min).getCooldown() * 1000;
            if (cooldown >= 0L) {
                currentLore.add("§aAvailable");
            }
            else {
                currentLore.add("§cOn cooldown");
            }
        }
        else {
            currentLore.add("§aAvailable");
        }
        currentMeta.setLore((List)currentLore);
        current.setItemMeta(currentMeta);
        return current;
    }
}
