package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.EntityKill;
import kiul.kiulsmputilitiesv2.renown.listeners.Interact;
import kiul.kiulsmputilitiesv2.renown.listeners.RenownJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Listeners
        getServer().getPluginManager().registerEvents(new EntityKill(),this);
        getServer().getPluginManager().registerEvents(new RenownJoinListener(),this);
        getServer().getPluginManager().registerEvents(new Interact(),this);

        //Commands
        getCommand("renown").setExecutor(new Commands());

        RenownConfig.setup();
        PlayerConfig.setup();
        RenownMethods.initializeDailyRenownResetClock();

        PlayerConfig.get().options().copyDefaults(true);
        RenownConfig.get().options().copyDefaults(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            RenownJoinListener.createPlayerConfigPaths(player);
        }
    }

    @Override
    public void onDisable() {
        for (Map.Entry<Player, Location> entry : Interact.targetedLocation.entrySet()) {
            entry.getValue().getBlock().setType(Material.BEDROCK);
        }
    }
}
