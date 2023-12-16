package kiul.kiulsmputilitiesv2.supplydrop.listeners;

import kiul.kiulsmputilitiesv2.supplydrop.SpawnCrate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CrateInteract implements Listener {



    static List<ItemStack> overworldCrateGuaranteed = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.ENCHANTED_BOOK));
        add(new ItemStack(Material.EXPERIENCE_BOTTLE));

    }};

    static List<ItemStack> overworldCrateLuckyDip = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.SPONGE));
        add(new ItemStack(Material.EMERALD_BLOCK));
        add(new ItemStack(Material.DIAMOND_BLOCK));
        add(new ItemStack(Material.SLIME_BALL));
        add(new ItemStack(Material.IRON_BLOCK));
        add(new ItemStack(Material.IRON_INGOT));
        add(new ItemStack(Material.EMERALD));
        add(new ItemStack(Material.DIAMOND));
        add(new ItemStack(Material.COBWEB));
        add(new ItemStack(Material.OBSIDIAN));
        add(new ItemStack(Material.TOTEM_OF_UNDYING));
        add(new ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE));
    }};

    static List<ItemStack> netherCrateGuaranteed = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
        add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        add(new ItemStack(Material.NETHERITE_INGOT));
        add(new ItemStack(Material.GOLD_BLOCK));
    }};

    static List<ItemStack> netherCrateLuckyDip = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.TOTEM_OF_UNDYING));
        add(new ItemStack(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.NETHERITE_SCRAP));
        add(new ItemStack(Material.WITHER_SKELETON_SKULL));
        add(new ItemStack(Material.SOUL_SAND));
        add(new ItemStack(Material.GOLD_INGOT));
    }};

    static List<ItemStack> endCrateGuaranteed = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.SHULKER_BOX));
    }};

    static List<ItemStack> endCrateLuckyDip = new ArrayList<ItemStack>() {{
        add(new ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE));
        add(new ItemStack(Material.CHORUS_FRUIT));
        add(new ItemStack(Material.CHORUS_FLOWER));
        add(new ItemStack(Material.END_STONE));
        add(new ItemStack(Material.OBSIDIAN));
        add(new ItemStack(Material.DIAMOND_HELMET));
        add(new ItemStack(Material.DIAMOND_BOOTS));
        add(new ItemStack(Material.DIAMOND_LEGGINGS));
        add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        add(new ItemStack(Material.ENDER_PEARL));
        add(new ItemStack(Material.ENDER_EYE));

    }};

    public static HashMap<ArmorStand,Inventory> crateInventoryMap = new HashMap<>();
    public static HashMap<ArmorStand,Long> crateUnlockTime = new HashMap<>();

    public static ArrayList<String> playersWhoGotLoot = new ArrayList<>();

    public static void populateCrate (String type, ArmorStand crate, World world) {
        Random random = new Random();
        ArrayList<ItemStack> crateInventory = new ArrayList<>();
        switch (type) {
            case "OVERWORLD":

                // Adding the items
                for (ItemStack guaranteedItems : overworldCrateGuaranteed) {
                    crateInventory.add(guaranteedItems);

                    if (guaranteedItems.getType().name().equalsIgnoreCase("EXPERIENCE_BOTTLE")) {
                        guaranteedItems.setAmount(random.nextInt(32, 65));
                    }
                    if (guaranteedItems.getType().name().equalsIgnoreCase("ENCHANTED_BOOK")) {
                        switch (random.nextInt(1,4)) {
                            case 1:
                                guaranteedItems.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,5);
                                break;
                            case 2:
                                guaranteedItems.addUnsafeEnchantment(Enchantment.DIG_SPEED,5);
                                break;
                            case 3:
                                guaranteedItems.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
                                guaranteedItems.addUnsafeEnchantment(Enchantment.DURABILITY,3);
                                break;
                        }
                    }
                    crateInventory.add(guaranteedItems);
                }
                for (int i = 0; i < 4; i++) {
                    int number = 0;
                    number = random.nextInt(0,overworldCrateLuckyDip.size());
                    if (overworldCrateLuckyDip.get(number).getType().name().contains("IRON")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(4,17));
                    }
                    if (overworldCrateLuckyDip.get(number).getType().name().contains("BLOCK")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(4, 13));
                    }
                    if (overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("SLIME_BALL")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(4, 33));
                    }
                    if (overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("DIAMOND") || overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("EMERALD")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(8,24));
                    }
                    if (overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("SPONGE")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(1, 5));
                    }
                    if (overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("OBSIDIAN") || overworldCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("COBWEB")) {
                        overworldCrateLuckyDip.get(number).setAmount(random.nextInt(24,48));
                    }
                    crateInventory.add(overworldCrateLuckyDip.get(number));
                }



                break;
            case "NETHER":
                // Adding the items
                for (ItemStack guaranteedItems : netherCrateGuaranteed) {
                    if (guaranteedItems.getType().name().contains("BLOCK")) {
                        guaranteedItems.setAmount(random.nextInt(16, 33));
                    }
                    if (guaranteedItems.getType().name().equalsIgnoreCase("ENCHANTED_GOLDEN_APPLE")) {
                        guaranteedItems.setAmount(random.nextInt(1, 4));
                    }
                    crateInventory.add(guaranteedItems);
                }
                for (int i = 0; i < 3; i++) {
                    int number = 0;
                    number = random.nextInt(0,netherCrateLuckyDip.size());
                    if (netherCrateLuckyDip.get(number).getType().name().contains("SCRAP")) {
                        netherCrateLuckyDip.get(number).setAmount(random.nextInt(1,9));
                    }
                    if (netherCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("WITHER_SKELETON_SKULL") || netherCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("SOUL_SAND")) {
                        netherCrateLuckyDip.get(number).setAmount(random.nextInt(1,4));
                    }
                    if (netherCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("GOLD_INGOT")) {
                        netherCrateLuckyDip.get(number).setAmount(random.nextInt(24,48));
                    }
                    crateInventory.add(netherCrateLuckyDip.get(number));
                }
                break;
            case "END":
                // Adding the items
                for (ItemStack guaranteedItems : endCrateGuaranteed) {
                    if (guaranteedItems.getType().name().contains("BLOCK")) {
                        guaranteedItems.setAmount(random.nextInt(16, 33));
                    }
                    if (guaranteedItems.getType().name().equalsIgnoreCase("ENCHANTED_GOLDEN_APPLE")) {
                        guaranteedItems.setAmount(random.nextInt(1, 4));
                    }
                    crateInventory.add(guaranteedItems);
                }
                for (int i = 0; i < 4; i++) {
                    int number = 0;
                    number = random.nextInt(0,endCrateLuckyDip.size());
                    if (endCrateLuckyDip.get(number).getType().name().contains("DIAMOND")) {
                        endCrateLuckyDip.get(number).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
                        if (random.nextBoolean()) {
                            endCrateLuckyDip.get(number).addEnchantment(Enchantment.DURABILITY,3);
                        }
                        if (random.nextBoolean()) {
                            endCrateLuckyDip.get(number).addEnchantment(Enchantment.MENDING,1);
                        }
                        if (random.nextBoolean()) {
                            endCrateLuckyDip.get(number).addEnchantment(Enchantment.BINDING_CURSE,1);
                        }
                        if (random.nextBoolean()) {
                            endCrateLuckyDip.get(number).addEnchantment(Enchantment.VANISHING_CURSE,1);
                        }
                    }
                    if (endCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("CHORUS_FRUIT") || endCrateLuckyDip.get(number).getType().name().equalsIgnoreCase("OBSIDIAN")) {
                        endCrateLuckyDip.get(number).setAmount(random.nextInt(1,4));
                    }
                    if (endCrateLuckyDip.get(number).getType().name().contains("ENDER")) {
                        endCrateLuckyDip.get(number).setAmount(random.nextInt(4,17));
                    }
                    crateInventory.add(endCrateLuckyDip.get(number));
                }
                break;
        }
        // Shuffling the contents
        Collections.shuffle(crateInventory);
        Inventory trueCrateInventory = Bukkit.createInventory(null,27,"Crate");
        for (ItemStack items : crateInventory) {
            int slot = random.nextInt(0,28);
            if (trueCrateInventory.getItem(slot) == null) {
                trueCrateInventory.setItem(slot, items);
            }
        }
        crateInventoryMap.put(crate,trueCrateInventory);

    }

    @EventHandler
    public void OpenCrate (PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand crate) {
            if (!SpawnCrate.unlocking.contains(crate) && !SpawnCrate.locked.contains(crate) || crateInventoryMap.get(crate) != null) {
                e.getPlayer().openInventory(crateInventoryMap.get(crate));
            } else if (SpawnCrate.locked.contains(crate)) {
                SpawnCrate.locked.remove(crate);
                int unlockMinutes = 1;
                long unlockTime = System.currentTimeMillis()+(unlockMinutes*60*1000);
                crateUnlockTime.put(crate,unlockTime);
                SpawnCrate.unlocking.add(crate);
            }
        }
    }


}
