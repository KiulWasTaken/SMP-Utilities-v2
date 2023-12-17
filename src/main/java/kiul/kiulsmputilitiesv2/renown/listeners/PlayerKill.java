package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerKill implements Listener {

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        Player deadPlayer = e.getEntity();
        if (deadPlayer.getKiller() != null) {
            PlayerConfig.get().set(deadPlayer.getUniqueId().toString() + ".kills",0);

            Player killerPlayer = e.getEntity().getKiller();
            PlayerConfig.get().set(killerPlayer.getUniqueId()+".kills",PlayerConfig.get().getInt(killerPlayer.getUniqueId()+".kills")+1);
            double givenRenown = calculateKillValue(deadPlayer);

            if (PlayerConfig.get().getBoolean(deadPlayer.getUniqueId() + ".overflow")) {
                PlayerConfig.get().set(deadPlayer.getUniqueId() + ".overflow", false);
                givenRenown = givenRenown + RenownConfig.get().getDouble(deadPlayer.getUniqueId() + ".daily");
                RenownConfig.get().set(deadPlayer.getUniqueId() + ".daily", 0);

            }
            PlayerConfig.get().set(deadPlayer.getUniqueId()+".value",0);


            if (PlayerConfig.get().getBoolean(killerPlayer.getUniqueId() + ".overflow")) {
                givenRenown = givenRenown * C.overflowMultiplier;
            }
            RenownMethods.giveRenown(killerPlayer, givenRenown);
            RenownConfig.save();
            PlayerConfig.save();
        }
    }

    public static List<Material> acceptedArmor = new ArrayList<>() {{
        add(Material.DIAMOND_HELMET);
        add(Material.DIAMOND_CHESTPLATE);
        add(Material.DIAMOND_LEGGINGS);
        add(Material.DIAMOND_BOOTS);
        add(Material.NETHERITE_HELMET);
        add(Material.NETHERITE_CHESTPLATE);
        add(Material.NETHERITE_LEGGINGS);
        add(Material.NETHERITE_BOOTS);
        }};
    public static int armorValue(Player p) {

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
        return wornAcceptedArmor.size();}


    @EventHandler
    public void brokenArmourTracker (PlayerItemBreakEvent e) {
        if (acceptedArmor.contains(e.getBrokenItem().getType()) && e.getBrokenItem().getEnchantments().size() > 2) {
            Player p = e.getPlayer();
            PlayerConfig.get().set(p.getUniqueId()+".value",PlayerConfig.get().getInt(p.getUniqueId()+".value")+1);
        }
    }
    public static double calculateKillValue (Player p) {
        int recentKills = (PlayerConfig.get().getInt(p.getUniqueId()+".kills")/5);
        double deadPlayerRenown = RenownConfig.get().getDouble(p.getUniqueId() + ".total");
        double euler = 2.718281828459;
        double time = (p.getStatistic(Statistic.TIME_SINCE_DEATH)/20/60/60) + armorValue(p) + PlayerConfig.get().getInt(p.getUniqueId()+".value");;
        double givenRenown = (0.8 + recentKills + (deadPlayerRenown / 10000)) / (0.001 + Math.pow(euler,(-time - 1.4)));
        return givenRenown;
    }
}
