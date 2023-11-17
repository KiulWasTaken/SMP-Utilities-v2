package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class KiulSMPUtilitiesv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        RenownConfig.setup();
        PlayerConfig.setup();
        RenownMethods.initializeDailyRenownResetClock();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
