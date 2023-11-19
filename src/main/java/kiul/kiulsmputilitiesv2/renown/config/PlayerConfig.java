package kiul.kiulsmputilitiesv2.renown.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {

        private static File file;
        private static FileConfiguration playerConfigFile;

        public static void setup() {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("KiulSMPUtilitiesv2").getDataFolder(), "player-preferences.yml");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
            }
            playerConfigFile = YamlConfiguration.loadConfiguration(file);
        }

        public static FileConfiguration get() {
            return playerConfigFile;
        }



        public static void save(){
            try {
                playerConfigFile.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save, playerConfigFile.");
            }
        }

        public static void reload(){
            playerConfigFile = YamlConfiguration.loadConfiguration(file);
        }


        
}
