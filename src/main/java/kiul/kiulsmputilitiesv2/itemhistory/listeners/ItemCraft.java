package kiul.kiulsmputilitiesv2.itemhistory.listeners;

import kiul.kiulsmputilitiesv2.itemhistory.ItemMethods;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.listeners.GiveRenown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ItemCraft implements Listener {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");

    @EventHandler
    public void craftItemEvent (CraftItemEvent e) {
        if (GiveRenown.rewardedItems.containsKey(e.getRecipe().getResult())) {
            LocalDate currentDate = LocalDate.now();
            ItemMethods.addLore(e.getInventory().getResult(),ChatColor.GRAY + "\uD83D\uDEE0 - " + ((Player)e.getView().getPlayer()).getDisplayName() + ChatColor.DARK_GRAY + " (" + dtf.format(currentDate) + ")");

        }
    }

    @EventHandler
    public void smithDate (SmithItemEvent e) {
        if (GiveRenown.rewardedItems.containsKey(e.getInventory().getRecipe().getResult())) {
            LocalDate currentDate = LocalDate.now();
            ItemMethods.addLore(e.getInventory().getResult(),ChatColor.GRAY + "↑ - " + ((Player)e.getView().getPlayer()).getDisplayName() + ChatColor.DARK_GRAY + " (" + dtf.format(currentDate) + ")");

        }
    }
}
