package kiul.kiulsmputilitiesv2.renown;

import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class RenownMethods {

    public static double dailyRenownCap = 1000.00;

    // Having a multiplier for overflow will allow us to lower/raise the gains to our will.
    // For instance, we could make it so grinding overflow in the wee hours of the morning will give you terrible gains
    // This would be an overall positive change to prevent cheesing the system!
    public static double overflowMultiplier = 1;

    // Hour of the day at which the renown resets (24 hour)
    public static int dailyResetTime = 10;

    // Check and subsequently fix any possible null errors with a new player's renown
    public static List<UUID> warnedPlayers = new ArrayList<>();
    // Gives the player any amount of renown (added to their daily)
    public static void giveRenown (Player p,double amount) {
        if (RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily") > dailyRenownCap) {
            if (PlayerConfig.get().getBoolean(p.getUniqueId()+".overflow")) {
                amount = amount * overflowMultiplier;
            } else {
                if (!warnedPlayers.contains(p.getUniqueId())) {
                    double difference = RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily") - dailyRenownCap;
                    p.sendMessage(ChatColor.YELLOW + "You are " + difference + ChatColor.GOLD + " \uD83D\uDC80 " + ChatColor.YELLOW + "over the daily limit, to bypass the daily limit run: " + ChatColor.RED + " /renown overflow");
                    p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WARNING:" + ChatColor.RESET + ChatColor.RED + " Enabling overflow will reveal your location to anyone with a renown compass. Use at your own risk.");
                    warnedPlayers.add(p.getUniqueId());
                }
                return;
            }
        }
        if (p.getInventory().contains(Material.DRAGON_EGG)) {
            amount = amount*1.5;
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
        if(cal.get(Calendar.HOUR_OF_DAY) >= dailyResetTime)
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
