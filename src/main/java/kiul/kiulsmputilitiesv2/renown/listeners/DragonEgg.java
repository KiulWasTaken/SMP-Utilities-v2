package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class DragonEgg implements Listener {

    @EventHandler
    public void makeItemInvulnerable (PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.DRAGON_EGG)) {
            e.getItemDrop().setInvulnerable(true);
            e.getItemDrop().setUnlimitedLifetime(true);
            e.getItemDrop().setVisualFire(false);
            e.getItemDrop().setPickupDelay(12000);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "EVENT" + ChatColor.RESET + ChatColor.GRAY + " » " + ChatColor.WHITE + "The " + ChatColor.LIGHT_PURPLE + "Dragon Egg" + ChatColor.WHITE + " Has been dropped at the coordinates: " + e.getItemDrop().getLocation().getBlockX() + ", " + e.getItemDrop().getLocation().getBlockY() + ", " + e.getItemDrop().getLocation().getBlockZ());
            Bukkit.broadcastMessage("");
            new BukkitRunnable() {
                int tick = 0;
                long dropTime = System.currentTimeMillis();
                long unlockTime = dropTime+(C.dragonEggPickupDelayMinutes*1000*60);
                ArmorStand stand = (ArmorStand) e.getPlayer().getWorld().spawnEntity(e.getItemDrop().getLocation(),EntityType.ARMOR_STAND);

                @Override
                public void run() {
                    if (!e.getItemDrop().isDead()) {
                        tick ++;
                        if (System.currentTimeMillis() < unlockTime) {
                            stand.teleport(e.getItemDrop().getLocation().add(0,1,0));
                            stand.setMarker(true);
                            stand.setVisible(false);
                            stand.setCustomNameVisible(true);
                            stand.setCustomName(ChatColor.GRAY + String.format("%02d : %02d",
                                    TimeUnit.MILLISECONDS.toMinutes(unlockTime - System.currentTimeMillis()),
                                    TimeUnit.MILLISECONDS.toSeconds(unlockTime - System.currentTimeMillis()) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(unlockTime - System.currentTimeMillis()))
                            ));
                        } else {
                            stand.remove();
                            e.getItemDrop().setPickupDelay(20);
                            cancel();
                        }


                        if (tick >= 3600) {
                            Bukkit.broadcastMessage(C.prefix + ChatColor.LIGHT_PURPLE + " Dragon egg" + ChatColor.GRAY + " rests at the coordinates " + e.getItemDrop().getLocation().toString());
                            tick = 0;
                        }
                    } else {
                        stand.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(C.plugin,0,20);
        }
    }
    @EventHandler
    public void preventDeath (EntityDamageEvent e) {
        if (e.getEntity() instanceof Item && ((Item) e.getEntity()).getItemStack().getType().equals(Material.DRAGON_EGG)) {
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventDie (ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType().equals(Material.DRAGON_EGG)) {
            e.getEntity().setInvulnerable(true);
            e.getEntity().setUnlimitedLifetime(true);
            e.getEntity().setVisualFire(false);
        }
    }

}
