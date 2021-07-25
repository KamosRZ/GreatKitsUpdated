package bg.hollyweed.greatkitsupdated.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUItems
{
    private static ItemStack previousButton;
    private static ItemStack nextButton;
    private static ItemStack exitButton;
    private static ItemStack selectButton;
    private static ItemStack helpItem;
    
    public GUItems() {
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.HelpItem") != null) {
            GUItems.helpItem = bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.HelpItem");
        }
        else {
            final ItemStack helpItem = new ItemStack(Material.TRIPWIRE_HOOK);
            final ItemMeta helpMeta = helpItem.getItemMeta();
            helpMeta.setDisplayName("§cHelp");
            final ArrayList<String> helpLore = new ArrayList<String>();
            helpLore.add("§9Left click to get the kit");
            helpLore.add("§7Right click to preview the kit");
            helpMeta.setLore((List)helpLore);
            helpItem.setItemMeta(helpMeta);
            GUItems.helpItem = helpItem;
        }
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.SelectButton") != null) {
            GUItems.selectButton = bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.SelectButton");
        }
        else {
            final ItemStack selectButton = new ItemStack(Material.STONE_BUTTON);
            final ItemMeta selectMeta = selectButton.getItemMeta();
            selectMeta.setDisplayName("§cSelect");
            selectButton.setItemMeta(selectMeta);
            GUItems.selectButton = selectButton;
        }
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.ExitButton") != null) {
            GUItems.exitButton = bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.ExitButton");
        }
        else {
            final ItemStack exitButton = new ItemStack(Material.STONE_BUTTON);
            final ItemMeta exitMeta = exitButton.getItemMeta();
            exitMeta.setDisplayName("§cExit");
            exitButton.setItemMeta(exitMeta);
            GUItems.exitButton = exitButton;
        }
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.PreviousButton") != null) {
            GUItems.previousButton = bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.PreviousButton");
        }
        else {
            final ItemStack previousButton = new ItemStack(Material.STONE_BUTTON);
            final ItemMeta previousMeta = previousButton.getItemMeta();
            previousMeta.setDisplayName("§cPrevious");
            previousButton.setItemMeta(previousMeta);
            GUItems.previousButton = previousButton;
        }
        if (bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.NextButton") != null) {
            GUItems.nextButton = bg.hollyweed.greatkitsupdated.GreatKitsUpdated.getInstance().getConfig().getItemStack("GUItems.NextButton");
        }
        else {
            final ItemStack nextButton = new ItemStack(Material.STONE_BUTTON);
            final ItemMeta nextMeta = nextButton.getItemMeta();
            nextMeta.setDisplayName("§cNext");
            nextButton.setItemMeta(nextMeta);
            GUItems.nextButton = nextButton;
        }
    }
    
    public static ItemStack getPreviousButton() {
        return GUItems.previousButton;
    }
    
    public static void setPreviousButton(final ItemStack previousButton) {
        GUItems.previousButton = previousButton;
    }
    
    public static ItemStack getNextButton() {
        return GUItems.nextButton;
    }
    
    public static void setNextButton(final ItemStack nextButton) {
        GUItems.nextButton = nextButton;
    }
    
    public static ItemStack getExitButton() {
        return GUItems.exitButton;
    }
    
    public static void setExitButton(final ItemStack exitButton) {
        GUItems.exitButton = exitButton;
    }
    
    public static ItemStack getSelectButton() {
        return GUItems.selectButton;
    }
    
    public static void setSelectButton(final ItemStack selectButton) {
        GUItems.selectButton = selectButton;
    }
    
    public static ItemStack getHelpItem() {
        return GUItems.helpItem;
    }
    
    public static void setHelpItem(final ItemStack helpItem) {
        GUItems.helpItem = helpItem;
    }
}
