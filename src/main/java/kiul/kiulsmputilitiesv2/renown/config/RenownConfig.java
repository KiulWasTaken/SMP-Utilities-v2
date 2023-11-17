package kiul.kiulsmputilitiesv2.renown.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RenownConfig {

        private static File file;
        private static FileConfiguration renownConfigFile;

        public static void setup() {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("KiulSMPUtilitiesv2").getDataFolder(), "player-renown.yml");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
            }
            renownConfigFile = YamlConfiguration.loadConfiguration(file);
        }

        public static FileConfiguration get() {
            return renownConfigFile;
        }



        public static void save(){
            try {
                renownConfigFile.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save, renownConfigFile.");
            }
        }

        public static void reload(){
            renownConfigFile = YamlConfiguration.loadConfiguration(file);
        }


        
}
