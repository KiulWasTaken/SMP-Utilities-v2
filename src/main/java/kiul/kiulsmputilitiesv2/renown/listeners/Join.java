package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    // Sets up the playerConfig for Commands.java
    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        createPlayerConfigPaths(p);
    }

    public static void createPlayerConfigPaths(Player p) {
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".alerts", false);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".overflow", false);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".value", 0);
        RenownConfig.get().addDefault(p.getUniqueId().toString() + ".daily", 0);
        RenownConfig.get().addDefault(p.getUniqueId().toString() + ".total", 0);
        if (!PlayerConfig.get().contains(p.getUniqueId().toString() + ".timealive"))
            PlayerConfig.get().set(p.getUniqueId().toString() + ".timealive", Long.valueOf(System.currentTimeMillis()));
        RenownConfig.save();
    }
}
