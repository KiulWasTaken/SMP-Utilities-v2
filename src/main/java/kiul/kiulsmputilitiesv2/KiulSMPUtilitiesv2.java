package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.itemhistory.listeners.ItemCraft;
import kiul.kiulsmputilitiesv2.itemhistory.listeners.ItemPickupAfterDeath;
import kiul.kiulsmputilitiesv2.noobprotection.ProtectionConfig;
import kiul.kiulsmputilitiesv2.noobprotection.PvPListeners;
import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.*;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.CrateInteract;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.CrateInventoryClick;
import kiul.kiulsmputilitiesv2.supplydrop.listeners.ItemInteract;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {



    @Override
    public void onEnable() {
        // Plugin startup logic
        PlayerConfig.setup();
        RenownConfig.setup();
        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerKill(),this);
        getServer().getPluginManager().registerEvents(new Join(),this);
        getServer().getPluginManager().registerEvents(new GiveRenown(),this);
        getServer().getPluginManager().registerEvents(new DragonEgg(),this);
        getServer().getPluginManager().registerEvents(new BountyParticles(),this);
        getServer().getPluginManager().registerEvents(new CrateInteract(),this);
        getServer().getPluginManager().registerEvents(new CrateInventoryClick(),this);
        getServer().getPluginManager().registerEvents(new ItemCraft(),this);
        getServer().getPluginManager().registerEvents(new PvPListeners(),this);
        getServer().getPluginManager().registerEvents(new ProtectionConfig(),this);
        getServer().getPluginManager().registerEvents(new ItemPickupAfterDeath(),this);
        getServer().getPluginManager().registerEvents(new ItemInteract(),this);

        //Commands
        getCommand("noobprotection").setExecutor(new Commands());
        getCommand("renown").setExecutor(new Commands());
        getCommand("restart-in").setExecutor(new Commands());
        getCommand("claim").setExecutor(new Commands());

        //Dependencies
        C.luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        //Config
        RenownConfig.get().addDefault("overflowing",new ArrayList<Player>());
        RenownConfig.save();
        PlayerConfig.get().addDefault("long-protection",false);
        PlayerConfig.save();
        RenownMethods.initializeDailyRenownResetClock();
        BountyParticles.startRepeatingMovementCheck();

        PlayerConfig.get().options().copyDefaults(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Join.createPlayerConfigPaths(player);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        RenownConfig.get().set("overflowing",C.overflowingPlayers);
        RenownConfig.save();
    }
}
