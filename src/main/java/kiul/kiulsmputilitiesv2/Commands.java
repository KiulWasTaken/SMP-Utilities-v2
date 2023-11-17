package kiul.kiulsmputilitiesv2;

import kiul.kiulsmputilitiesv2.renown.RenownMethods;
import kiul.kiulsmputilitiesv2.renown.config.RenownConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;
        switch (label) {
            case "renown":
                if (args[0] != null) {
                    switch (args[0]) {
                        case "showalerts":
                            // Decides whether to send an actionbar message to the player whenever they gain renown
                            PlayerConfig.get().set(p.getUniqueId().toString() + ".alerts", !PlayerConfig.get().getBoolean(p.getUniqueId().toString() + ".alerts"));
                            PlayerConfig.save();
                            break;
                        case "top":
                            // Send top 10 renown scores
                            break;
                        case "overflow":
                            // Disable player cap - enable "overflow", allowing player to earn extra renown.
                            if (RenownConfig.get().getDouble(p.getUniqueId().toString() + ".daily") >= RenownMethods.dailyRenownCap) {
                                PlayerConfig.get().set(p.getUniqueId().toString() + ".overflow", true);
                            } else {
                                p.sendMessage("You have not surpassed the daily limit of: " + RenownMethods.dailyRenownCap + ChatColor.GOLD + " \uD83D\uDC80");
                            }
                            break;
                    }
                } else {
                    // Send player their renown score
                    // Will make this nicer later!
                    p.sendMessage("" + (RenownConfig.get().getDouble(p.getUniqueId().toString()+".daily")+RenownConfig.get().getDouble(p.getUniqueId().toString()+".total"))+ ChatColor.GOLD + " \uD83D\uDC80");
                }
                break;
        }
        return false;
    }
}
