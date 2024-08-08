package de.veroxar.forceItemBattle.config;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Configuration {

    final Data data = ForceItemBattle.getData();

    final JavaPlugin instance = data.getInstance();
    private File file;
    private FileConfiguration fileConfiguration;

    public void createConfiguration(String name) {
        this.file = new File(instance.getDataFolder(), name + ".yml");
        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration toFileConfiguration() {
        return fileConfiguration;
    }

    public void saveConfiguration() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
