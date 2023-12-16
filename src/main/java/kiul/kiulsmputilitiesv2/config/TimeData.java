package kiul.kiulsmputilitiesv2.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TimeData {

    private static File file;
    private static FileConfiguration tagdataFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SMPEvents").getDataFolder(), "timestamps.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        tagdataFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return tagdataFile;
    }



    public static void save(){
        try {
            tagdataFile.save(file);
        } catch (IOException e) {
            System.out.println("Failed to save, timestamps File.");
        }
    }

    public static void reload(){
        tagdataFile = YamlConfiguration.loadConfiguration(file);
    }



}
