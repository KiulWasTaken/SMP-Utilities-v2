package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class RenownJoinListener {

    // Sets up the playerConfig for Commands.java
    @EventHandler
    public void joinEvent (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!e.getPlayer().hasPlayedBefore()) {
            PlayerConfig.get().set(p.getUniqueId().toString()+".alerts",false);
            PlayerConfig.get().set(p.getUniqueId().toString()+".overflow",false);
            PlayerConfig.save();
            RenownMethods.renownCheckNull(p);
        }
    }
}
