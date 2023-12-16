package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;

public class Interact implements Listener {

    public static HashMap<Player, Location> targetedLocation = new HashMap<>();
    public static HashMap<Player, Player> targetedPlayer = new HashMap<>();

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
            ItemStack compass = e.getItem();
            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) { //Change target
                trackPlayer(p);
            } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) { //Update target location
                if (targetedPlayer.containsKey(p)) {
                    if (targetedPlayer.get(p).isOnline()) {
                        targetedLocation.get(p).getBlock().setType(Material.BEDROCK);
                        Location loc = targetedLocation.get(p);
                        loc.setY(-64);
                        loc.getBlock().setType(Material.LODESTONE);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(C.plugin, new Runnable() {
                            @Override
                            public void run() {
                                compassMeta.setLodestone(loc);
                                compassMeta.setLodestoneTracked(true);
                                compass.setItemMeta(compassMeta);
                            }
                        }, 1);
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(C.t("&cPlayer cannot be tracked anymore!")));
                        targetedLocation.get(p).getBlock().setType(Material.BEDROCK);
                        targetedPlayer.remove(p);
                        trackPlayer(p);
                    }
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(C.t("&cYet to start tracking anyone! &8Left-Click")));
                }
            }
        }

    }

    private static void trackPlayer(Player p) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != p) {
                if (targetedPlayer.containsKey(p)) {
                    if (targetedPlayer.get(p) != player) {
                        targetedPlayer.put(p, player);
                        targetedLocation.put(p, player.getLocation());
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(C.t("&6Started tracking a new player! &8Right click to update")));
                    }
                } else {
                    targetedPlayer.put(p, player);
                    targetedLocation.put(p, player.getLocation());
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(C.t("&6Started tracking a player! &8Right click to update")));
                }
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.2f, 2);
            }
        }
    }
}
