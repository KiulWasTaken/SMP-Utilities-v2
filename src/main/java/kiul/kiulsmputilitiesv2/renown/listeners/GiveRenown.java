package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class GiveRenown implements Listener {

    // Item materials and the renown value for crafting them
    public HashMap<ItemStack,Double> rewardedItems = new HashMap<ItemStack,Double>() {{
        put(new ItemStack(Material.NETHERITE_HELMET),7.5);
        put(new ItemStack(Material.NETHERITE_CHESTPLATE),12.0);
        put(new ItemStack(Material.NETHERITE_LEGGINGS),10.5);
        put(new ItemStack(Material.NETHERITE_BOOTS),6.0);
        put(new ItemStack(Material.NETHERITE_SWORD),3.0);
        put(new ItemStack(Material.NETHERITE_PICKAXE),4.5);
        put(new ItemStack(Material.NETHERITE_AXE),4.5);
        put(new ItemStack(Material.NETHERITE_SHOVEL),1.5);
        put(new ItemStack(Material.NETHERITE_HOE),3.0);

        put(new ItemStack(Material.DIAMOND_HELMET),5.0);
        put(new ItemStack(Material.DIAMOND_HELMET),8.0);
        put(new ItemStack(Material.DIAMOND_HELMET),7.0);
        put(new ItemStack(Material.DIAMOND_HELMET),4.0);
        put(new ItemStack(Material.DIAMOND_SWORD),2.0);
        put(new ItemStack(Material.DIAMOND_PICKAXE),3.0);
        put(new ItemStack(Material.DIAMOND_AXE),3.0);
        put(new ItemStack(Material.DIAMOND_SHOVEL),1.0);
        put(new ItemStack(Material.DIAMOND_HOE),2.0);
    }};

    // Block materials and the renown value for breaking them
    public HashMap<Material,Double> rewardedBlocks = new HashMap<>() {{
       put(Material.COAL_ORE,1.0);
       put(Material.IRON_ORE,1.5);
       put(Material.DIAMOND_ORE,2.0);
       put(Material.REDSTONE_ORE,0.8);
       put(Material.LAPIS_ORE,0.8);
       put(Material.EMERALD_ORE,2.4);
       put(Material.COPPER_ORE,1.2);
       put(Material.NETHER_QUARTZ_ORE,3.0);
       put(Material.NETHER_GOLD_ORE,1.5);

        put(Material.DEEPSLATE_COAL_ORE,1.5);
        put(Material.DEEPSLATE_IRON_ORE,2.3);
        put(Material.DEEPSLATE_DIAMOND_ORE,3.5);
        put(Material.DEEPSLATE_REDSTONE_ORE,1.2);
        put(Material.DEEPSLATE_LAPIS_ORE,1.2);
        put(Material.DEEPSLATE_EMERALD_ORE,3.6);
        put(Material.DEEPSLATE_COPPER_ORE,1.8);
        put(Material.ANCIENT_DEBRIS,12.0);
    }};

    // EntityTypes and the renown value for killing them
    public HashMap <EntityType,Double> rewardedEntities = new HashMap<>() {{
       put(EntityType.ENDER_DRAGON,5000.0);
    }};

    @EventHandler
    public void craftingGiveRenown (CraftItemEvent e) {
        if (rewardedItems.containsKey(e.getInventory().getResult())) {
            RenownMethods.giveRenown((Player) e.getView().getPlayer(),rewardedItems.get(e.getInventory().getResult()));
        }
    }

    @EventHandler
    public void villagerLevelUpRenown (VillagerAcquireTradeEvent e) {
        Villager villager = (Villager)e.getEntity();
        int level = villager.getVillagerLevel();
        if (level > 1) {
            for (Entity nearbyEntity : villager.getNearbyEntities(5,5,5)) {
                if (nearbyEntity instanceof Player p) {
                    RenownMethods.giveRenown(p,35.0);
                }
            }
        }
    }

    @EventHandler
    public void blockBreakRenown (BlockBreakEvent e) {
    if (rewardedBlocks.containsKey(e.getBlock().getType()) && !e.getBlock().hasMetadata("pp")) {
        RenownMethods.giveRenown(e.getPlayer(),rewardedBlocks.get(e.getBlock().getType()));
        }
    }
    @EventHandler
    public void noScum (BlockPlaceEvent e) {
        if (rewardedBlocks.containsKey(e.getBlock().getType())) {
            e.getBlock().setMetadata("fatass",new FixedMetadataValue(KiulSMPUtilitiesv2.getPlugin(KiulSMPUtilitiesv2.class),"pp"));
        }
    }

    @EventHandler
    public void enchantRenown (EnchantItemEvent e) {
        RenownMethods.giveRenown(e.getEnchanter(),e.getExpLevelCost()+(e.getEnchantsToAdd().size()*e.whichButton()));
    }

    @EventHandler
    public void killEntityRenown (EntityDeathEvent e) {
        if (rewardedEntities.containsKey(e.getEntity().getType()) && e.getEntity().getKiller() instanceof Player) {
            RenownMethods.giveRenown(e.getEntity().getKiller(),rewardedEntities.get(e.getEntity().getType()));
        }
    }
}
