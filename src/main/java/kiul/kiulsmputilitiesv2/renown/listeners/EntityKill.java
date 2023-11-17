package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntityKill implements Listener {

    //Armor required to count towards more renown
    public static int armorRequired = 2;

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        Player deadPlayer = e.getEntity();
        if (deadPlayer.getKiller() != null) {
            Player killerPlayer = e.getEntity().getKiller();
            if (isWearingArmor(deadPlayer)) {

                int v = 0;
                if (PlayerConfig.get().contains(killerPlayer.getUniqueId().toString() + ".playerskilled." + deadPlayer.getUniqueId()))
                    v = PlayerConfig.get().getInt(killerPlayer.getUniqueId().toString() + ".playerskilled." + deadPlayer.getUniqueId());

                int secondsAlive = (int) timeAlive(deadPlayer);
                int timesKilled = v+1;
                int deadValue = PlayerConfig.get().getInt(deadPlayer.getUniqueId().toString() + ".total");
                double renownGiven = (secondsAlive + deadValue)/(timesKilled);
                int killerValue = PlayerConfig.get().getInt(killerPlayer.getUniqueId().toString() + ".total");

                killerPlayer.sendMessage(deadPlayer.getName() + " is wearing armor");
                killerPlayer.sendMessage(timeAlive(deadPlayer) + "s alive (" + (int) renownGiven + ")" );
                PlayerConfig.get().set(killerPlayer.getUniqueId().toString() + ".total", killerValue + (int) renownGiven);
                PlayerConfig.get().set(killerPlayer.getUniqueId().toString() + ".daily", (int) renownGiven);
                PlayerConfig.get().set(killerPlayer.getUniqueId().toString() + ".playerskilled." + deadPlayer.getUniqueId(), v+1);
                PlayerConfig.save();

            }
        }
        PlayerConfig.get().set(deadPlayer.getUniqueId().toString() + ".timealive", Long.valueOf(System.currentTimeMillis()));
        PlayerConfig.save();
    }

    public static long timeAlive(Player p) {
        return (System.currentTimeMillis() - PlayerConfig.get().getLong(p.getUniqueId().toString() + ".timealive")) /1000; //return time alive in seconds
    }

    public static void saveTimeAlive(Player p) {

    }

    public static boolean isWearingArmor(Player p) {
        List<Material> acceptedArmor = new ArrayList<>();
        acceptedArmor.add(Material.DIAMOND_HELMET);
        acceptedArmor.add(Material.DIAMOND_CHESTPLATE);
        acceptedArmor.add(Material.DIAMOND_LEGGINGS);
        acceptedArmor.add(Material.DIAMOND_BOOTS);
        acceptedArmor.add(Material.NETHERITE_HELMET);
        acceptedArmor.add(Material.NETHERITE_CHESTPLATE);
        acceptedArmor.add(Material.NETHERITE_LEGGINGS);
        acceptedArmor.add(Material.NETHERITE_BOOTS);
        List<ItemStack> wornAcceptedArmor = new ArrayList<>();
        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null) {
                for (Material mat : acceptedArmor) {
                    if (armor.getType() == mat && armor.getEnchantments().size() > 0) {
                        wornAcceptedArmor.add(armor);
                        break;
                    }
                }
            }
        }
        if (wornAcceptedArmor.size() >= armorRequired) {
            return true;
        } else {
            return false;
        }
    }
}
