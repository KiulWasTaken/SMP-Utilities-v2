package kiul.kiulsmputilitiesv2.renown;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.UUID;

public class RenownMethods {

    // Check and subsequently fix any possible null errors with a new player's renown

    // Gives the player any amount of renown (added to their daily)
    public void giveRenown (Player p,double amount) {
        if (RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily") > C.dailyRenownCap) {
            amount = amount* C.overflowMultiplier;
        }
        RenownConfig.get().set(p.getUniqueId().toString()+".daily",RenownConfig.get().get(p.getUniqueId().toString()+".daily"+amount));
        RenownConfig.save();
        if (PlayerConfig.get().getBoolean(p.getUniqueId().toString()+".alerts")) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("+ " + amount + ChatColor.GOLD + " \uD83D\uDC80"));
        }
    }


    // Sends all daily renown to the player's total
    // Intended to be used as part of the daily reset
    public static void transferRenown (UUID uuid) {
        double amount = RenownConfig.get().getDouble(uuid.toString()+".daily");
        RenownConfig.get().set(uuid.toString()+".total",(double)RenownConfig.get().get(uuid.toString()+".total")+amount);
        RenownConfig.get().set(uuid.toString()+".daily",0);
        RenownConfig.save();
    }

    public static void initializeDailyRenownResetClock () {
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.HOUR_OF_DAY) >= C.dailyResetTime)
            cal.add(Calendar.DATE, 1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);

        long waitingUntil = cal.getTimeInMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= waitingUntil) {
                    for (OfflinePlayer allPlayers : Bukkit.getOfflinePlayers()) {
                        transferRenown(allPlayers.getUniqueId());
                        PlayerConfig.get().set(allPlayers.getUniqueId().toString()+".overflow",false);
                    }
                    initializeDailyRenownResetClock();
                    cancel();
                }

            }
        }.runTaskTimer(KiulSMPUtilitiesv2.getPlugin(KiulSMPUtilitiesv2.class),0,1200);
    }
}
