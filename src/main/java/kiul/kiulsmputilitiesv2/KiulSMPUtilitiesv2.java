package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerKill(),this);
        getServer().getPluginManager().registerEvents(new Join(),this);
        getServer().getPluginManager().registerEvents(new GiveRenown(),this);
        getServer().getPluginManager().registerEvents(new DragonEgg(),this);
        getServer().getPluginManager().registerEvents(new BountyParticles(),this);


        //Commands
        getCommand("renown").setExecutor(new Commands());
        getCommand("restart-in").setExecutor(new Commands());

        RenownConfig.setup();
        PlayerConfig.setup();
        RenownConfig.get().addDefault("overflowing",new ArrayList<Player>());
        RenownConfig.save();
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
