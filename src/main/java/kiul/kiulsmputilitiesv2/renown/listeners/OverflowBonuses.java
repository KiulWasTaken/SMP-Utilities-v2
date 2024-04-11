package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import org.bukkit.Material;
import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OverflowBonuses implements Listener {

    @EventHandler
    public void bonusExperience (PlayerExpChangeEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer())) {
            e.setAmount((int)(e.getAmount()*1.25));
        }
    }

    @EventHandler
    public void updatePotionEffect (PlayerJoinEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer()) || PlayerConfig.get().getBoolean(e.getPlayer().getUniqueId()+".overflow")) {
            if (e.getPlayer().hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE)) {
                Player p = e.getPlayer();
                p.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE,C.overflowTimer.get(p.getUniqueId())*20,1));
            }
        }
    }

    @EventHandler
    public void useNetherite2Upgrade (PrepareSmithingEvent e) {
        Inventory smithingInventory = e.getInventory();
        if (GiveRenown.rewardedItems.containsKey(smithingInventory.getItem(0))) {
            if (smithingInventory.getItem(1).equals(RenownMethods.createUpgrade())) {
                ItemStack result = smithingInventory.getItem(0);
                ItemMeta meta = result.getItemMeta();
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS,new AttributeModifier("GENERIC_ARMOR_TOUGHNESS",1, AttributeModifier.Operation.ADD_NUMBER));
                result.setItemMeta(meta);
                e.setResult(result);
                List<HumanEntity> viewers = e.getViewers();
                viewers.forEach(humanEntity -> ((Player)humanEntity).updateInventory());
            }
        }
    }

}
