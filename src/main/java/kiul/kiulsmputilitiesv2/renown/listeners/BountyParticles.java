package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class BountyParticles implements Listener {


    public static void startRepeatingMovementCheck () {

        new BukkitRunnable() {
            String targetVelocity = "0.0,-0.0784000015258789,0.0";

            @Override
            public void run() {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    String currentVelocity = onlinePlayers.getVelocity().toString();
                    if (PlayerKill.calculateKillValue(onlinePlayers) > 600 && currentVelocity.equals(targetVelocity)) {
                        for (Entity e : onlinePlayers.getNearbyEntities(15,15,15)) {
                            if (e instanceof Player) {
                                if (PlayerKill.calculateKillValue(onlinePlayers) < 1000) {
                                    ((Player) e).spawnParticle(Particle.WAX_OFF,onlinePlayers.getLocation().add(0,1,0),8,0.2,0.7,0.2);
                                } else {
                                    ((Player) e).spawnParticle(Particle.WAX_ON,onlinePlayers.getLocation().add(0,1,0),8,0.2,0.7,0.2);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(C.plugin,0,20);
    }
}
