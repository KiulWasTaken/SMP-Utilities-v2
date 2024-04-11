package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.itemhistory.listeners.ItemCraft;
import kiul.kiulsmputilitiesv2.itemhistory.listeners.ItemPickupAfterDeath;
/*import kiul.kiulsmputilitiesv2.noobprotection.ProtectionConfig;
import kiul.kiulsmputilitiesv2.noobprotection.PvPListeners;*/
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.*;
import kiul.kiulsmputilitiesv2.supplydrop.SpawnCrate;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.CrateInteract;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.CrateInventoryClick;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.ItemInteract;
import net.luckperms.api.LuckPerms;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static kiul.kiulsmputilitiesv2.renown.RenownMethods.differenceStorage;
import static kiul.kiulsmputilitiesv2.renown.RenownMethods.warnedPlayers;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();

        SpawnCrate.runAutoCrates();

        //Config
        RenownConfig.setup();
        RenownConfig.save();
        PlayerConfig.setup();
        PlayerConfig.get().addDefault("long-protection",false);
        PlayerConfig.save();
        RenownMethods.initializeDailyRenownResetClock();
//        BountyParticles.startRepeatingMovementCheck();

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerKill(),this);
        getServer().getPluginManager().registerEvents(new Join(),this);
        getServer().getPluginManager().registerEvents(new GiveRenown(),this);
        getServer().getPluginManager().registerEvents(new DragonEgg(),this);
        getServer().getPluginManager().registerEvents(new BountyParticles(),this);
        getServer().getPluginManager().registerEvents(new CrateInteract(),this);
        getServer().getPluginManager().registerEvents(new CrateInventoryClick(),this);
        getServer().getPluginManager().registerEvents(new ItemCraft(),this);
        /*getServer().getPluginManager().registerEvents(new PvPListeners(), this);
        getServer().getPluginManager().registerEvents(new ProtectionConfig(),this);*/
        getServer().getPluginManager().registerEvents(new ItemPickupAfterDeath(),this);
        getServer().getPluginManager().registerEvents(new ItemInteract(),this);
        getServer().getPluginManager().registerEvents(new Interact(),this);
        getServer().getPluginManager().registerEvents(new OverflowBonuses(),this);

        //Recipes
        ItemStack toughnessUpgrade = RenownMethods.createUpgrade();

        ShapedRecipe toughnessUpgradeRecipe = new ShapedRecipe(new NamespacedKey(this,"toughness-upgrade"),toughnessUpgrade);

        toughnessUpgradeRecipe.shape("ABA","ACA","AAA");

        toughnessUpgradeRecipe.setIngredient('A', Material.NETHERITE_SCRAP);
        toughnessUpgradeRecipe.setIngredient('B', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        toughnessUpgradeRecipe.setIngredient('C', Material.NETHERRACK);

        Bukkit.addRecipe(toughnessUpgradeRecipe);

        SmithingRecipe helmetRecipe = new SmithingTransformRecipe(new NamespacedKey(this, "helmet"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_HELMET),
                new RecipeChoice.MaterialChoice(Material.AIR)
        );
        Bukkit.addRecipe(helmetRecipe);
        SmithingRecipe chestplateRecipe = new SmithingTransformRecipe(new NamespacedKey(this, "chestplate"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_CHESTPLATE),
                new RecipeChoice.MaterialChoice(Material.AIR)
        );
        Bukkit.addRecipe(chestplateRecipe);
        SmithingRecipe leggingsRecipe = new SmithingTransformRecipe(new NamespacedKey(this, "leggings"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_LEGGINGS),
                new RecipeChoice.MaterialChoice(Material.AIR)
        );
        Bukkit.addRecipe(leggingsRecipe);
        SmithingRecipe bootsRecipe = new SmithingTransformRecipe(new NamespacedKey(this, "boots"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_BOOTS),
                new RecipeChoice.MaterialChoice(Material.AIR)
        );
        Bukkit.addRecipe(bootsRecipe);


        //Commands
        getCommand("noobprotection").setExecutor(new Commands());
        getCommand("renown").setExecutor(new Commands());
        getCommand("restart-in").setExecutor(new Commands());
        getCommand("claim").setExecutor(new Commands());

        //Dependencies
        C.luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        PlayerConfig.get().options().copyDefaults(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Join.createPlayerConfigPaths(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (C.overflowingPlayers.contains(onlinePlayers.getUniqueId())) {
                        if (C.overflowTimer.get(onlinePlayers.getUniqueId()) > 0) {
                            int timer = C.overflowTimer.get(onlinePlayers.getUniqueId());
                            C.overflowTimer.put(onlinePlayers.getUniqueId(),timer-1);
                        } else {
                            UUID pUUID = onlinePlayers.getUniqueId();
                            PlayerConfig.get().set(pUUID + ".overflow", false);
                            PlayerConfig.save();
                            onlinePlayers.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " You have run out of overflow and can no longer gain extra renown today.");
                            C.overflowingPlayers.remove(onlinePlayers.getUniqueId());
                            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " " + C.overflowingPlayers.size() + " Player(s) are currently using overflow.");
                            return;
                        }
                    }
                }
            }
        }.runTaskTimer(this,0,20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if (C.overflowingPlayers.contains(allPlayers.getUniqueId())) {
                PlayerConfig.get().set(allPlayers.getUniqueId()+".overflow-timestamp",C.overflowTimer.get(allPlayers.getUniqueId()));
            }
        }
        PlayerConfig.save();
    }
}
