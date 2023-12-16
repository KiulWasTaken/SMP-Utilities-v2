package kiul.kiulsmputilitiesv2.supplydrop.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.supplydrop.SpawnCrate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;


public class ItemInteract implements Listener {

    @EventHandler
    public void crateKeyInteract (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getItemMeta() != null) {

            if (p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("cratekey")) {
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    e.setCancelled(true);
                    Random random = new Random();
                    String type;
                    switch (random.nextInt(0, 2)) {
                        case 0:
                            type = "END";
                            break;
                        case 1:
                            type = "NETHER";
                            break;
                        default:
                            type = "OVERWORLD";
                            break;
                    }

                    SpawnCrate.createNewCrate(type, 60, p.getWorld(), p);
                    if (p.getInventory().getItemInHand().getAmount() == 1) {
                        p.getInventory().setItemInHand(new ItemStack(Material.AIR));
                    } else {
                        p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount()-1);
                    }
                }
            }
        }
    }
}