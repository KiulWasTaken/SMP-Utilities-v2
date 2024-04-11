package kiul.kiulsmputilitiesv2.renown.listeners;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.google.common.collect.Multimap;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OverflowBonuses implements Listener {

    @EventHandler
    public void bonusExperience(PlayerExpChangeEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer())) {
            e.setAmount((int) (e.getAmount() * 1.25));
        }
    }

    @EventHandler
    public void updatePotionEffect(PlayerJoinEvent e) {
        if (C.overflowingPlayers.contains(e.getPlayer()) || PlayerConfig.get().getBoolean(e.getPlayer().getUniqueId() + ".overflow")) {
            if (e.getPlayer().hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE)) {
                Player p = e.getPlayer();
                p.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, C.overflowTimer.get(p.getUniqueId()) * 20, 1));
            }
        }
    }

    ArrayList<Material> allowedTypes = new ArrayList<>() {{
        add(Material.NETHERITE_HELMET);
        add(Material.NETHERITE_CHESTPLATE);
        add(Material.NETHERITE_LEGGINGS);
        add(Material.NETHERITE_BOOTS);
    }};
    HashMap<Material,Double> defaultArmorValue = new  HashMap<>() {{
        put(Material.NETHERITE_HELMET,3.0);
        put(Material.NETHERITE_CHESTPLATE,8.0);
        put(Material.NETHERITE_LEGGINGS,6.0);
        put(Material.NETHERITE_BOOTS,3.0);
    }};

    List<Recipe> taggedRecipes = new  ArrayList<>() {{

            add(Bukkit.getRecipe(NamespacedKey.fromString("helmet")));
            add(Bukkit.getRecipe(NamespacedKey.fromString("chestplate")));
            add(Bukkit.getRecipe(NamespacedKey.fromString("leggings")));
            add(Bukkit.getRecipe(NamespacedKey.fromString("boots")));
        }};

            @EventHandler
            public void useNetherite2Upgrade (PrepareSmithingEvent e){
            Inventory smithingInventory = e.getInventory();
            if (smithingInventory.getItem(1) != null && smithingInventory.getItem(0) != null) {
                if (allowedTypes.contains(smithingInventory.getItem(1).getType())) {
                    if (smithingInventory.getItem(0).getItemMeta().getLocalizedName().equalsIgnoreCase("toughness-upgrade")) {
                        ItemStack result = smithingInventory.getItem(1).clone();
                        ItemMeta meta = result.getItemMeta();
                        double magicNumber;
                        double amount;
                        if (meta.hasLocalizedName()) {
                            magicNumber = Double.parseDouble(meta.getLocalizedName());
                            amount = Math.pow(0.4, magicNumber);
                            double total = 0.0;
                            for (AttributeModifier modifiers : meta.getAttributeModifiers(Attribute.GENERIC_ARMOR_TOUGHNESS)) {
                                total = total+modifiers.getAmount();
                            }
                            meta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
                            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR_TOUGHNESS", total+amount, AttributeModifier.Operation.ADD_NUMBER,result.getType().getEquipmentSlot()));
                            meta.setLocalizedName((int)magicNumber+1 + "");
                        } else {
                            amount = 3.5;
                            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR_TOUGHNESS", amount, AttributeModifier.Operation.ADD_NUMBER,result.getType().getEquipmentSlot()));
                            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"GENERIC_ARMOR", defaultArmorValue.get(result.getType()), AttributeModifier.Operation.ADD_NUMBER,result.getType().getEquipmentSlot()));
                            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"GENERIC_KNOCKBACK_RESISTANCE", 0.1, AttributeModifier.Operation.ADD_NUMBER,result.getType().getEquipmentSlot()));
                            meta.setLocalizedName("2");
                        }



                        result.setItemMeta(meta);
                        e.setResult(result);
                        List<HumanEntity> viewers = e.getViewers();
                        viewers.forEach(humanEntity -> ((Player) humanEntity).updateInventory());
                    }
                }
            }
        }

        @EventHandler
        public void tagSmithEvent (SmithItemEvent e) {
                Player p = (Player) e.getView().getPlayer();
                if (taggedRecipes.contains(e.getInventory().getRecipe())) {
                    if (RenownConfig.get().getDouble(p.getUniqueId().toString() + ".total") > 0) {
                        if (RenownConfig.get().getInt(p.getUniqueId().toString() + ".crafts") + 1 < (int)RenownConfig.get().getDouble(p.getUniqueId().toString() + ".total")/600) {
                            RenownConfig.get().set(p.getUniqueId().toString() + ".crafts", RenownConfig.get().getInt(p.getUniqueId().toString() + ".crafts") + 1);
                            int remainingCrafts = ((int)RenownConfig.get().getDouble(p.getUniqueId().toString() + ".total")/600) - (RenownConfig.get().getInt(p.getUniqueId().toString() + ".crafts"));
                            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You have " + ChatColor.YELLOW + remainingCrafts + ChatColor.RED + " crafting uses remaining.");
                        } else {
                            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You have " + ChatColor.YELLOW + "0" + ChatColor.RED + " crafting uses remaining." + ChatColor.YELLOW + " Collect more renown" + ChatColor.RED + " to gain more crafts.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You must have more than " + ChatColor.YELLOW + "600 " + C.symbol + ChatColor.RED + "" + ChatColor.ITALIC + " before you can use toughness upgrades!");
                    }
                }
        }


            public double getArmorToughness (ItemStack item){
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                double totalToughness = 0.0;
                if (meta.hasAttributeModifiers()) {
                    for (AttributeModifier modifier : meta.getAttributeModifiers(Attribute.GENERIC_ARMOR_TOUGHNESS)) {
                        totalToughness = totalToughness + modifier.getAmount();

                    }
                    return totalToughness;
                }
            }
            return 0.0; // Default value if no armor toughness modifier found
        }
}
