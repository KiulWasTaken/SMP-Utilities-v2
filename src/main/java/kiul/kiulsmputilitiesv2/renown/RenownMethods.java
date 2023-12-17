package kiul.kiulsmputilitiesv2.renown;

import kiul.kiulsmputilitiesv2.C;
import kiul.kiulsmputilitiesv2.KiulSMPUtilitiesv2;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
                    p.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                    p.sendMessage(ChatColor.YELLOW + "You have reached the daily " + ChatColor.WHITE+ "Renown " + C.symbol + ChatColor.YELLOW + " limit, to bypass the daily limit run: " + ChatColor.RED + " /renown overflow");
                    p.sendMessage(ChatColor.WHITE + "" + difference + ChatColor.WHITE + " Renown " + C.symbol + ChatColor.YELLOW + " was left over when you passed the limit. To claim this renown enable " + ChatColor.RED + "overflow");
                    p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WARNING:" + ChatColor.RESET + ChatColor.RED + " Enabling overflow will reveal your location to anyone with a renown compass. Use at your own risk.");
                    p.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
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
        double amount;
        if (RenownConfig.get().get(uuid.toString()+".daily") != null) {
            amount = RenownConfig.get().getDouble(uuid.toString() + ".daily");
        } else {
            amount = 0.0;
        }
        RenownConfig.get().set(uuid.toString()+".total",RenownConfig.get().getDouble(uuid.toString()+".total")+amount);
        RenownConfig.get().set(uuid.toString()+".daily",0.0);
        RenownConfig.save();
    }

    private static void giveDailyReward () {
        ConfigurationSection cf = RenownConfig.get().getConfigurationSection("");

        HashMap<String, Integer> unsortedBalance = new HashMap<>();
        for (String keys : cf.getKeys(false)) {
            unsortedBalance.put(keys, RenownConfig.get().getInt(keys + ".daily"));
        }

        LinkedHashMap<String, Integer> sortedRenown = new LinkedHashMap<>();
        unsortedBalance.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedRenown.put(x.getKey(), x.getValue()));

        List<String> keys = new ArrayList<>(sortedRenown.keySet());
        Player p = Bukkit.getServer().getPlayer(UUID.fromString(keys.get(0)));
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta keyMeta = key.getItemMeta();
        keyMeta.setLocalizedName("cratekey");
        keyMeta.setDisplayName(ChatColor.RED + "Supply Crate Key");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Right-Click to Consume");
        keyMeta.setLore(lore);
        key.setItemMeta(keyMeta);
        PlayerConfig.get().set(p.getUniqueId() + ".claim",key);
        PlayerConfig.save();
        if (p != null) {
            TextComponent message = new TextComponent(ChatColor.GREEN + "[CLICK HERE]");
            TextComponent message2 = new TextComponent(" "+ ChatColor.YELLOW + "or run " + ChatColor.GOLD + "/claim" + ChatColor.YELLOW + " to claim your reward.");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "claim"));
            p.sendMessage(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ " + ChatColor.RESET + ChatColor.GOLD + C.symbol + ChatColor.STRIKETHROUGH + ChatColor.BLUE+ " ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
            p.sendMessage(ChatColor.YELLOW + "You have received a supply crate key for collecting the most daily renown in the past 24 hours.");
            p.spigot().sendMessage(message,message2);
            p.sendMessage(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
        }
        User user = C.luckPerms.getUserManager().getUser(p.getUniqueId());
        Node permissionNode = PermissionNode.builder("kiultags.renown").build();
        user.data().add(permissionNode);
    }



    public static void initializeDailyRenownResetClock () {
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.HOUR_OF_DAY) >= C.dailyResetTime) {
            cal.add(Calendar.DATE, 1);
        }

        cal.set(Calendar.HOUR_OF_DAY, C.dailyResetTime);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);

        long waitingUntil = cal.getTimeInMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= waitingUntil) {
                    giveDailyReward();
                    for (OfflinePlayer allPlayers : Bukkit.getOfflinePlayers()) {
                        transferRenown(allPlayers.getUniqueId());
                        PlayerConfig.get().set(allPlayers.getUniqueId().toString()+".overflow",false);
                        if (warnedPlayers.contains(allPlayers)) {
                            warnedPlayers.remove(allPlayers);
                            differenceStorage.remove(allPlayers.getUniqueId());
                        }
                    }
                    if (C.overflowingPlayers != null && !C.overflowingPlayers.isEmpty()) {
                        C.overflowingPlayers.clear();
                    }
                    RenownConfig.get().set("overflowing",new ArrayList<Player>());
                    RenownConfig.save();
                    PlayerConfig.save();
                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " Daily renown transfer and overflow reset has been completed.");
                    initializeDailyRenownResetClock();
                    cancel();
                }

            }
        }.runTaskTimer(KiulSMPUtilitiesv2.getPlugin(KiulSMPUtilitiesv2.class),0,1200);
    }
}
