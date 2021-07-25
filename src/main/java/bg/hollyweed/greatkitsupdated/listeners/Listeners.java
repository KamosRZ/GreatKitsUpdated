package bg.hollyweed.greatkitsupdated.listeners;

import bg.hollyweed.greatkitsupdated.GreatKitsUpdated;
import bg.hollyweed.greatkitsupdated.utils.GUItems;
import bg.hollyweed.greatkitsupdated.utils.InventoryPage;
import bg.hollyweed.greatkitsupdated.utils.Kit;
import bg.hollyweed.greatkitsupdated.utils.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        final InventoryView invView = e.getView();
        final Inventory inv = e.getClickedInventory();
        final ItemStack item = e.getCurrentItem();
        if (p.getOpenInventory().getTitle().startsWith("§7Kit list: ") || p.getOpenInventory().getTitle().startsWith("§7Kit Preview : ")) {
            e.setCancelled(true);
        }
        if (item == null) {
            return;
        }
        if (invView.getTitle().startsWith("§7Kit list: ") || invView.getTitle().startsWith("§7Kit Preview : ")) {
            if (item.isSimilar(GUItems.getNextButton()) || item.isSimilar(GUItems.getPreviousButton()) || item.isSimilar(GUItems.getExitButton()) || item.isSimilar(GUItems.getSelectButton()) || item.isSimilar(GUItems.getHelpItem())) {
                if (item.isSimilar(GUItems.getNextButton())) {
                    final InventoryPage page = new InventoryPage(invView);
                    page.nextPage(p);
                }
                else if (item.isSimilar(GUItems.getPreviousButton())) {
                    final InventoryPage page = new InventoryPage(invView);
                    page.previousPage(p);
                }
                else if (item.isSimilar(GUItems.getExitButton())) {
                    final InventoryPage pageF = new InventoryPage();
                    pageF.returnToLastPage(p);
                }
                else if (item.isSimilar(GUItems.getSelectButton())) {
                    GreatKitsUpdated.getKit(invView.getTitle().substring(18)).give(p);
                }
                return;
            }
            if (invView.getTitle().startsWith("§7Kit Preview : ")) {
                return;
            }
            for (final Kit kits : KitManager.getKits()) {
                if (kits.getIcon() == null) {
                    return;
                }
                try {
                    if (!item.getItemMeta().getDisplayName().equals(kits.getIcon().getItemMeta().getDisplayName()) || item.getType() != kits.getIcon().getType()) {
                        continue;
                    }
                    if (GreatKitsUpdated.getInstance().getConfig().getBoolean("settings.enable-gui-kit-previewing") && e.isRightClick()) {
                        final InventoryPage page2 = new InventoryPage(p.getOpenInventory());
                        GreatKitsUpdated.getInstance().getPages().put(p.getName(), page2.getCurrentPage());
                        kits.preview(p);
                    }
                    else {
                        kits.give(p);
                    }
                }
                catch (NullPointerException ex) {
                    continue;
                }
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (!p.hasPlayedBefore()) {
            for (final Kit kits : KitManager.getKits()) {
                kits.giveFirstJoinKit(p);
            }
        }
    }
}
