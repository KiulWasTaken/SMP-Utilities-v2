package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BountyParticles implements Listener {


    public static void startRepeatingMovementCheck () {

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (PlayerKill.calculateKillValue(onlinePlayers) > 500 && onlinePlayers.getVelocity().equals(new Vector(0,-0.0784000015258789,0))) {
                        for (Entity e : onlinePlayers.getNearbyEntities(15,15,15)) {
                            if (e instanceof Player) {
                                if (PlayerKill.calculateKillValue(onlinePlayers) < 1000) {
                                    ((Player) e).spawnParticle(Particle.WAX_OFF,onlinePlayers.getLocation().add(0,1,0),20,0.2,1,0.2);
                                } else {
                                    ((Player) e).spawnParticle(Particle.WAX_ON,onlinePlayers.getLocation().add(0,1,0),20,0.2,1,0.2);
                                }
                            }
                        }
                    }
                }

            }
        }.runTaskTimer(C.plugin,0,20);
    }
}
