package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
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
        PlayerConfig.get().options().copyDefaults(true);
        RenownConfig.get().options().copyDefaults(true);
        RenownConfig.get().addDefault(p.getUniqueId().toString() + ".daily", 0.00);
        RenownConfig.get().addDefault(p.getUniqueId().toString() + ".total", 0.00);
        RenownConfig.save();
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".alerts", false);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".overflow", false);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".value", 0);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".claim", null);
        PlayerConfig.get().addDefault(p.getUniqueId().toString() + ".kills",0);
        PlayerConfig.save();
    }
}
