package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerKill implements Listener {

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        Player deadPlayer = e.getEntity();
        if (deadPlayer.getKiller() != null) {

            Player killerPlayer = e.getEntity().getKiller();
            double deadPlayerRenown = RenownConfig.get().getDouble(deadPlayer.getUniqueId()+".total");
            double euler = 2.718281828459;
            double time = deadPlayer.getStatistic(Statistic.TIME_SINCE_DEATH) + armorValue(deadPlayer);
            double givenRenown = (0.4+(deadPlayerRenown/10000))/(0.001+Math.pow(euler,time-1.4));

            if (PlayerConfig.get().getBoolean(deadPlayer.getUniqueId()+".overflow")) {
                PlayerConfig.get().set(deadPlayer.getUniqueId()+".overflow",false);
                givenRenown = givenRenown + RenownConfig.get().getDouble(deadPlayer.getUniqueId()+".daily");
                RenownConfig.get().set(deadPlayer.getUniqueId()+".daily",0);
                RenownConfig.save();
            }
            if (PlayerConfig.get().getBoolean(killerPlayer.getUniqueId()+".overflow")) {
                givenRenown = givenRenown*RenownMethods.overflowMultiplier;
            }
            RenownMethods.giveRenown(killerPlayer,givenRenown);
        }
    }

    public static int armorValue(Player p) {
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
        return wornAcceptedArmor.size();}
}
