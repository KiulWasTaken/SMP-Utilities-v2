package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Join implements Listener {

    // Sets up the playerConfig for Commands.java
    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        createPlayerConfigPaths(p);
        if (PlayerConfig.get().getBoolean(p.getUniqueId()+".overflow")) {
            if (!C.overflowingPlayers.contains(p)) {
                C.overflowingPlayers.add(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p == null) {
                            cancel();
                        }
                        if (System.currentTimeMillis() >= PlayerConfig.get().getLong(p.getUniqueId()+".overflow-timestamp") || !PlayerConfig.get().getBoolean(".overflow")) {
                            PlayerConfig.get().set(p.getUniqueId()+".overflow",false);
                            PlayerConfig.save();
                            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " You have run out of overflow and can no longer gain extra renown today.");
                            C.overflowingPlayers.remove(p);
                            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " " + C.overflowingPlayers.size() + " Player(s) are currently using overflow.");
                            cancel();
                        }
                    }
                }.runTaskTimer(C.plugin,0,1200);
            }
        }
    }

    @EventHandler
    public void leaveEvent (PlayerQuitEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer())) {
            C.overflowingPlayers.remove(e.getPlayer());
        }
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
