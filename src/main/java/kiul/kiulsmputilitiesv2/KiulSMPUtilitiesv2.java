package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import kiul.kiulsmputilitiesv2.renown.listeners.EntityKill;
import kiul.kiulsmputilitiesv2.renown.listeners.RenownJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Listeners
        getServer().getPluginManager().registerEvents(new EntityKill(),this);
        getServer().getPluginManager().registerEvents(new RenownJoinListener(),this);

        //Commands
        getCommand("renown").setExecutor(new Commands());

        RenownConfig.setup();
        PlayerConfig.setup();
        RenownMethods.initializeDailyRenownResetClock();

        PlayerConfig.get().options().copyDefaults(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            RenownJoinListener.createPlayerConfigPaths(player);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
