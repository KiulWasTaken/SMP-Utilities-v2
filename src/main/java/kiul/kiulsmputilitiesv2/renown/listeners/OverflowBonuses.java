package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class OverflowBonuses implements Listener {

    @EventHandler
    public void bonusExperience (PlayerExpChangeEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer())) {
            e.setAmount((int)(e.getAmount()*1.25));
        }
    }

}
