package kiul.kiulsmputilitiesv2.noobprotection;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class ProtectionConfig implements Listener {

    public static ArrayList<Player> protectedPlayers = new ArrayList<>();

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        long now = System.currentTimeMillis();
        long disableTime;

        if (!p.hasPlayedBefore()) {
            protectedPlayers.add(p);
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Team starTeam = scoreboard.getTeam("star");
            if (starTeam == null) {
                starTeam = scoreboard.registerNewTeam("star");
                starTeam.setSuffix(ChatColor.YELLOW + " ★");
            }
            starTeam.addEntry(p.getName());
            if (PlayerConfig.get().getBoolean("long-protection")) {
                disableTime = now + (2*1000*60*60);

            } else {
                disableTime = now + (10*1000*60);
            }
            PlayerConfig.get().set("protection-disable-time",disableTime);

        } else {
            if (System.currentTimeMillis() < PlayerConfig.get().getLong("protection-disable-time")) {
                protectedPlayers.add(p);
            }
        }
        if (protectedPlayers.contains(p)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (p != null) {
                        if (System.currentTimeMillis() > PlayerConfig.get().getLong("protection-disable-time")) {
                            protectedPlayers.remove(p);
                            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                            Team starTeam = scoreboard.getTeam("star");
                            starTeam.removeEntry(p.getName());
                            cancel();
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(C.plugin,0,1200);
        }
    }

    @EventHandler
    public void cleanupArrayList (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (protectedPlayers.contains(p)) {
            protectedPlayers.remove(p);
        }
    }

}
