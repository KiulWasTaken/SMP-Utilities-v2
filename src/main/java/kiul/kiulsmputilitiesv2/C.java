package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class C { //Inner-Config
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");
    public static Plugin plugin = KiulSMPUtilitiesv2.getPlugin(KiulSMPUtilitiesv2.class);

    public static String prefix = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SMP" + ChatColor.RESET + ChatColor.GRAY + " » ";
    public static String symbol = ChatColor.GOLD + "☠";
    public static boolean restarting = false;
    public static List<Player> overflowingPlayers = (List<Player>)RenownConfig.get().getList("overflowing");

    //
    /** Configurable-shit */
    public static double dailyRenownCap = 1200.00; //Cap of renown before overflow
    public static double overflowMultiplier = 1; //lower/raise the gains to our will.
    public static int dailyResetTime = 10; // Hour of the day at which the renown resets (24 hour)
    public static int dragonEggPickupDelayMinutes = 10;

    /** Repeatable Strings */
    public static String genericFail = C.t("&cCommand or function error, try again!");
    //


    /** Colored-Text */
    public static String t(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

}
