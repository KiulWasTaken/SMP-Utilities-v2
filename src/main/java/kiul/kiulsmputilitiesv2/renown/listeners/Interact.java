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

import java.util.ArrayList;
import java.util.HashMap;

public class Interact implements Listener {

    public static HashMap<Player, Integer> playerTarget = new HashMap<>();


    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
            if (!C.overflowingPlayers.isEmpty()) {
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (playerTarget.get(p) != null) {
                        Player targetPlayer = Bukkit.getPlayer(new ArrayList<>(C.overflowingPlayers).get(playerTarget.get(p)));
                        Location targetLocation = targetPlayer.getLocation();
                        ItemStack playersItem = p.getInventory().getItemInMainHand();
                        CompassMeta compassMeta = (CompassMeta) playersItem.getItemMeta();
                        compassMeta.setDisplayName(ChatColor.GOLD + "Renown Compass");
                        if (targetLocation.getWorld() != p.getWorld()) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Target Dimension:  " + targetPlayer.getWorld().toString()));
                            compassMeta.setLodestone(targetLocation);
                            compassMeta.setLodestoneTracked(false);
                            playersItem.setItemMeta(compassMeta);
                            return;
                        }
                        if (C.overflowTimer.get(targetPlayer.getUniqueId()) < 7200) {
                            compassMeta.setLodestone(targetLocation);
                            compassMeta.setLodestoneTracked(false);
                            playersItem.setItemMeta(compassMeta);
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Target Position Updated"));
                        } else {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Cannot Track Target " + ChatColor.YELLOW + "(Protection Timer)"));
                        }


                        p.playSound(p, Sound.BLOCK_LEVER_CLICK, 0.9f, 1f);
                        if (p.getLocation().distance(targetPlayer.getLocation()) < C.proximityWarning) {
                            targetPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Proximity Alert"));
                        }
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Select a Target (left-click)"));
                    }
                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                    if (playerTarget.get(p) == null) {
                        playerTarget.put(p, 0);
                    } else {

                        int trackNum = playerTarget.get(p);
                        trackNum++;
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.2f, 2);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Changed Target"));
                        if (trackNum > C.overflowingPlayers.size()) {
                            trackNum = 0;

                        }
                        playerTarget.put(p,trackNum);
                    }
                }
            }
        }
    }
}
