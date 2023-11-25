package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonEgg implements Listener {

    @EventHandler
    public void makeItemInvulnerable (PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.DRAGON_EGG)) {
            e.getItemDrop().setInvulnerable(true);
            e.getItemDrop().setUnlimitedLifetime(true);
            e.getItemDrop().setVisualFire(false);
        }
    }
}
