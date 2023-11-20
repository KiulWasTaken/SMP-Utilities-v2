package kiul.kiulsmputilitiesv2.renown.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DragonEgg implements Listener {

    @EventHandler
    public void makeItemInvulnerable (PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.DRAGON_EGG)) {
            e.getItemDrop().setInvulnerable(true);
            e.getItemDrop().setUnlimitedLifetime(true);
        }
    }
}
