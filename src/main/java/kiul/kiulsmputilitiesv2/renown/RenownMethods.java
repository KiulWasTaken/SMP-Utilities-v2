package kiul.kiulsmputilitiesv2.renown;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class RenownMethods {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static List<UUID> warnedPlayers = new ArrayList<>();
    // Gives the player any amount of renown (added to their daily)

    public static HashMap<UUID,Double> differenceStorage = new HashMap<>();
    public static void giveRenown (Player p,double amount) {
        if (PlayerConfig.get().getBoolean(p.getUniqueId()+".overflow")) {
            amount = amount * C.overflowMultiplier;
        }
        if (p.getInventory().contains(Material.DRAGON_EGG)) {
            amount = amount*1.5;
        }
        if (RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily")+amount >= C.dailyRenownCap && !PlayerConfig.get().getBoolean(p.getUniqueId()+".overflow")) {
                if (!warnedPlayers.contains(p.getUniqueId())) {
                    double difference = RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily")+amount-C.dailyRenownCap;
                    p.sendMessage(ChatColor.RED + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                    p.sendMessage(ChatColor.YELLOW + "You have reached the daily " + ChatColor.WHITE+ "Renown " + C.symbol + ChatColor.YELLOW + " limit, to bypass the daily limit run: " + ChatColor.RED + " /renown overflow");
                    p.sendMessage(ChatColor.WHITE + "" + difference + ChatColor.WHITE + " Renown " + C.symbol + ChatColor.YELLOW + " was left over when you passed the limit. To claim this renown enable " + ChatColor.RED + "overflow");
                    p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WARNING:" + ChatColor.RESET + ChatColor.RED + " Enabling overflow will reveal your location to anyone with a renown compass. Use at your own risk.");
                    p.sendMessage(ChatColor.RED + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                    warnedPlayers.add(p.getUniqueId());
                    differenceStorage.put(p.getUniqueId(),difference);
                    RenownConfig.get().set(p.getUniqueId()+".daily",C.dailyRenownCap);
                    RenownConfig.save();
                }
                return;
        }
        RenownConfig.get().set(p.getUniqueId().toString()+".daily",RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily")+amount);
        RenownConfig.save();
        if (PlayerConfig.get().getBoolean(p.getUniqueId().toString()+".alerts")) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("+ " + df.format(amount) + " " + C.symbol));
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

        cal.set(Calendar.HOUR_OF_DAY, C.dailyResetTime);
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
                        if (warnedPlayers.contains(allPlayers)) {
                            warnedPlayers.remove(allPlayers);
                            differenceStorage.remove(allPlayers.getUniqueId());
                            C.overflowingPlayers.clear();
                            RenownConfig.get().set("overflowing",new ArrayList<Player>());
                            RenownConfig.save();
                        }
                    }
                    initializeDailyRenownResetClock();
                    cancel();
                }

            }
        }.runTaskTimer(KiulSMPUtilitiesv2.getPlugin(KiulSMPUtilitiesv2.class),0,1200);
    }
}
