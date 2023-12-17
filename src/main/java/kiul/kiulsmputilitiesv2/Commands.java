package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.config.RenownConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commands implements TabExecutor {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;
        switch (label) {
            case "renown":
                if (args.length == 0) {
                    p.sendMessage(C.prefix + ChatColor.GRAY + "Personal Renown: " + ChatColor.WHITE + df.format(RenownConfig.get().getDouble(p.getUniqueId().toString()+".total")) + ChatColor.GRAY + " (" + df.format((RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily")) + ") " + C.symbol));
                } else {
                    switch (args[0]) {
                        case "showalerts":
                            // Decides whether to send an actionbar message to the player whenever they gain renown
                            PlayerConfig.get().set(p.getUniqueId().toString() + ".alerts", !PlayerConfig.get().getBoolean(p.getUniqueId().toString() + ".alerts"));
                            PlayerConfig.save();
                            if (PlayerConfig.get().getBoolean(p.getUniqueId().toString() + ".alerts")) {
                                p.sendMessage(C.prefix + ChatColor.WHITE + "Renown actionbar alerts " + ChatColor.GREEN + "On");
                            } else {
                                p.sendMessage(C.prefix + ChatColor.WHITE + "Renown actionbar alerts " + ChatColor.RED + "Off");
                            }

                            break;
                        case "top":
                            ConfigurationSection cf = RenownConfig.get().getConfigurationSection("");

                            HashMap<String, Integer> unsortedBalance = new HashMap<>();
                            for (String keys : cf.getKeys(false)) {
                                unsortedBalance.put(keys, RenownConfig.get().getInt(keys + ".total"));
                            }

                            LinkedHashMap<String, Integer> sortedRenown = new LinkedHashMap<>();
                            unsortedBalance.entrySet()
                                    .stream()
                                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                    .forEachOrdered(x -> sortedRenown.put(x.getKey(), x.getValue()));

                            List<String> keys = new ArrayList<>(sortedRenown.keySet());
                            keys.remove("overflowing");
                            int playersPerPage = 10;
                            int page = 1;
                            if (args.length == 2) {
                                try {
                                    page = Integer.parseInt(args[1]);
                                } catch (NumberFormatException ea) {
                                }
                            }

                            int i = (page - 1) * playersPerPage;

                            try {
                                keys.get(i);
                                if (page > 0 && sortedRenown.size() > 0 && sortedRenown.size() <= (sortedRenown.size() - 1 / playersPerPage) * playersPerPage) {
                                    p.sendMessage(C.t("&6&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ &6" + C.symbol + "&6&m ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                                    for (i = (page - 1) * playersPerPage; i >= page - 1 * 5 && i <= page * 5; i++) {
                                        String key = keys.get(i);
                                        p.sendMessage(C.t("&e#" + (i + 1) + "&6. &e" + Bukkit.getOfflinePlayer(UUID.fromString(key)).getName() + " &8» &#7bd488" + df.format(sortedRenown.get(key))+" &7(" + df.format(RenownConfig.get().getDouble(UUID.fromString(key)+".daily")) + ")"));
                                        if (i == page * playersPerPage - 1 || i == sortedRenown.size() - 1) {
                                            break;
                                        }
                                    }
                                    p.sendMessage(C.t("&ePage &c" + page + "&e of &c" + (sortedRenown.size() / playersPerPage + 1)));
                                    p.sendMessage(C.t("&6&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                                } else {
                                    p.sendMessage(C.genericFail);
                                }
                            } catch (IndexOutOfBoundsException exa) {
                                p.sendMessage(C.genericFail);
                            }
                            break;
                        case "overflow":
                            HashMap<UUID,Long> disableMap = new HashMap<>();
                            // Disable player cap - enable "overflow", allowing player to earn extra renown.
                            if (RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily") >= C.dailyRenownCap) {
                                if (PlayerConfig.get().getBoolean(p.getUniqueId()+".overflow") == false) {
                                    PlayerConfig.get().set(p.getUniqueId().toString() + ".overflow", true);
                                    C.overflowingPlayers.add(p);
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " " + C.overflowingPlayers.size() + " Player(s) are currently using overflow.");
                                    long now = System.currentTimeMillis();
                                    long disableTime = (now+1000*60*60*3);
                                    UUID pUUID = p.getUniqueId();
                                    disableMap.put(pUUID,disableTime);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (System.currentTimeMillis() >= disableTime) {
                                                PlayerConfig.get().set(pUUID+".overflow",false);
                                                if (p != null) {
                                                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + " You have run out of overflow and can no longer gain extra renown today.");
                                                }
                                                disableMap.remove(pUUID);
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(C.plugin,0,1200);
                                    if (RenownMethods.differenceStorage.containsKey(p.getUniqueId())) {
                                        RenownConfig.get().set(p.getUniqueId().toString() + ".daily", RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily" + RenownMethods.differenceStorage.get(p.getUniqueId())));
                                        RenownConfig.save();
                                    }
                                    PlayerConfig.save();

                                } else {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(disableMap.get(p.getUniqueId()));
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    String dateString = sdf.format(calendar.getTime());
                                    p.sendMessage(C.prefix + "Overflow will expire in: " + ChatColor.RED + dateString);
                                }
                            } else {
                                p.sendMessage(C.prefix + "You have not surpassed the daily limit of: " + C.dailyRenownCap + " " + C.symbol);
                            }
                            break;
                    }
                }
                break;
            case "restart-in":
                if (!C.restarting && p.isOp()) {
                    C.restarting = true;
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RESTART" + ChatColor.RESET + ChatColor.GRAY + " » " + ChatColor.WHITE + "Server Restarting In " + ChatColor.RED + args[0] + "m");
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                    new BukkitRunnable() {
                        int minutes = Integer.parseInt(args[0]);
                        int tick = 0;
                        int warnFrequency = 0;
                        @Override
                        public void run() {
                            if (tick < minutes) {
                                tick++;
                                warnFrequency++;
                                if (warnFrequency >= 2) {
                                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RESTART" + ChatColor.RESET + ChatColor.GRAY + " » " + ChatColor.WHITE + "Server Restarting In " + ChatColor.RED + (minutes - tick) + "m");
                                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                    warnFrequency = 0;
                                }
                            } else {
                                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RESTART" + ChatColor.RESET + ChatColor.GRAY + "»" + ChatColor.WHITE + "Server Restarting");
                                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                Bukkit.getServer().shutdown();
                                cancel();
                            }

                        }
                    }.runTaskTimer(C.plugin,0,1200);
                } else {
                    p.sendMessage(C.prefix + ChatColor.RED+"Restart has already been scheduled");
                }
                break;
            case "noobprotection":
                if (p.isOp()) {
                    boolean toggle = PlayerConfig.get().getBoolean("long-protection");
                    PlayerConfig.get().set("long-protection", !toggle);
                    p.sendMessage(C.prefix + "long noob protection is now: " + toggle);
                } else {
                    p.sendMessage(C.prefix + ChatColor.RED + "You must be operator to run this command.");
                }
                break;
            case "claim":
                if (PlayerConfig.get().getItemStack(p.getUniqueId()+".claim") != null) {
                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(C.prefix + "clear a slot from your inventory first!");
                        return true;
                    }
                    p.getInventory().setItem(p.getInventory().firstEmpty(),PlayerConfig.get().getItemStack(p.getUniqueId()+".claim"));
                    PlayerConfig.get().set(p.getUniqueId()+".claim", null);
                    p.sendMessage(C.prefix + "reward claimed!");
                    PlayerConfig.save();


                } else {
                    p.sendMessage(C.prefix + "you have nothing to claim!");
                }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> arguments = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("showalerts");
            arguments.add("top");
            arguments.add("overflow");
        } else {
            switch (args[0].toLowerCase()) {
                case "top":
                    break;
            }
        }

        return arguments;
    }
}
