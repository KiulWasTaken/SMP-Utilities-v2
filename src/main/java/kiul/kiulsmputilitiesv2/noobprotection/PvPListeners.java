/*
package kiul.kiulsmputilitiesv2.noobprotection;

import kiul.kiulsmputilitiesv2.C;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class PvPListeners implements Listener {

    private static ArrayList<Material> dangerousBlocks = new ArrayList<>() {{
       add(Material.LAVA);
       add(Material.FIRE);
    }};

    @EventHandler
    public void playerHitPlayer (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p  && e.getDamager() instanceof Player) {
            if (ProtectionConfig.protectedPlayers.contains(e.getEntity()) || ProtectionConfig.protectedPlayers.contains(e.getDamager())) {
                e.setCancelled(true);
                e.getDamager().sendMessage(C.prefix + ChatColor.RED + "You cannot hurt players with noob protection!");
            }
        }
    }

    @EventHandler
    public void playerPlaceDangerousBlock (BlockPlaceEvent e) {
        if (dangerousBlocks.contains(e.getBlock().getType())) {
            for (Entity nearbyEntity : e.getPlayer().getNearbyEntities(15,15,15)) {
                if (nearbyEntity instanceof Player p && ProtectionConfig.protectedPlayers.contains(p)) {
                    e.setCancelled(true);
                    p.sendMessage(C.prefix + ChatColor.RED + "Dangerous blocks cannot be placed near new players!");
                    return;
                }
            }
        }
    }
}
*/
