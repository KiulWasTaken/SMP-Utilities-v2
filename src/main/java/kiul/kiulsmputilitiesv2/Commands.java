package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.PlayerConfig;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;
        switch (label) {
            case "renown":
                if (args.length == 0) {
                    p.sendMessage(C.prefix + ChatColor.GRAY + "Personal Renown: " + ChatColor.WHITE + RenownConfig.get().getDouble(p.getUniqueId().toString()+".total") + ChatColor.GRAY + " (" + (RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily") + ") " + C.symbol));
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
                                    p.sendMessage(C.t("&#39f782Renown leaderboard &6- &#578063page &c" + page + "&#578063 of &c" + (sortedRenown.size() / playersPerPage + 1)));
                                    for (i = (page - 1) * playersPerPage; i >= page - 1 * 5 && i <= page * 5; i++) {
                                        String key = keys.get(i);
                                        p.sendMessage(C.t("&#baad49#" + (i + 1) + "&6. &#8d959e" + Bukkit.getOfflinePlayer(UUID.fromString(key)).getName() + " &8» &#7bd488" + sortedRenown.get(key)));
                                        if (i == page * playersPerPage - 1 || i == sortedRenown.size() - 1) {
                                            break;
                                        }
                                    }
                                } else {
                                    p.sendMessage(C.genericFail);
                                }
                            } catch (IndexOutOfBoundsException exa) {
                                p.sendMessage(C.genericFail);
                            }
                            break;
                        case "overflow":
                            // Disable player cap - enable "overflow", allowing player to earn extra renown.
                            if (RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily") >= RenownMethods.dailyRenownCap) {
                                PlayerConfig.get().set(p.getUniqueId().toString() + ".overflow", true);
                                if (RenownMethods.differenceStorage.containsKey(p.getUniqueId())) {
                                    RenownConfig.get().set(p.getUniqueId().toString() + ".daily", RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily" + RenownMethods.differenceStorage.get(p.getUniqueId())));
                                    RenownConfig.save();
                                }
                                PlayerConfig.save();
                            } else {
                                p.sendMessage(C.prefix + "You have not surpassed the daily limit of: " + RenownMethods.dailyRenownCap + " " + C.symbol);
                            }
                            break;
                    }
                }
                break;
            case "restart-in":
                if (!C.restarting) {
                    C.restarting = true;
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
                                    p.sendMessage(ChatColor.YELLOW + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RESTART" + ChatColor.RESET + ChatColor.GRAY + " » " + ChatColor.WHITE + "Server Restarting In " + ChatColor.RED + (tick - minutes) + "m");
                                    p.sendMessage(ChatColor.YELLOW + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                    warnFrequency = 0;
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RESTART" + ChatColor.RESET + ChatColor.GRAY + "»" + ChatColor.WHITE + "Server Restarting");
                                p.sendMessage(ChatColor.RED + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
                                Bukkit.getServer().shutdown();
                                cancel();
                            }

                        }
                    }.runTaskTimer(C.plugin,0,1200);
                } else {
                    p.sendMessage(C.prefix + ChatColor.RED+"Restart has already been scheduled");
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
