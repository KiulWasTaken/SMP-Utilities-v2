package kiul.kiulsmputilitiesv2.itemhistory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemMethods {

    public static void addLore (ItemStack item, String itemLore) {
        ItemStack resultItem = item;
        ItemMeta resultItemMeta = resultItem.getItemMeta();
        List<String> lore;
        if (resultItemMeta.getLore() != null) {
            lore = resultItemMeta.getLore();
        } else {
            lore = new ArrayList<>() {{
                add("");
            }};
        }
        lore.add(itemLore);
        resultItemMeta.setLore(lore);
        resultItem.setItemMeta(resultItemMeta);
    }
}
