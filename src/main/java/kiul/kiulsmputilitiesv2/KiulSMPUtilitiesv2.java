package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.DragonEgg;
import kiul.kiulsmputilitiesv2.renown.listeners.PlayerKill;
import kiul.kiulsmputilitiesv2.renown.listeners.GiveRenown;
import kiul.kiulsmputilitiesv2.renown.listeners.Join;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerKill(),this);
        getServer().getPluginManager().registerEvents(new Join(),this);
        getServer().getPluginManager().registerEvents(new GiveRenown(),this);
        getServer().getPluginManager().registerEvents(new DragonEgg(),this);


        //Commands
        getCommand("renown").setExecutor(new Commands());

        RenownConfig.setup();
        PlayerConfig.setup();
        RenownMethods.initializeDailyRenownResetClock();

        PlayerConfig.get().options().copyDefaults(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Join.createPlayerConfigPaths(player);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
