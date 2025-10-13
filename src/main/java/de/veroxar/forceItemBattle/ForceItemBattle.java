package de.veroxar.forceItemBattle;

import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.commands.*;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.events.ConnectionListener;
import de.veroxar.forceItemBattle.events.GameListener;
import de.veroxar.forceItemBattle.events.JokerListener;
import de.veroxar.forceItemBattle.events.TeamInvListener;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.team.DefaultTeams;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/*
@Author: Veroxar
 */


public final class ForceItemBattle extends JavaPlugin {

    private static final Data data = new Data();
    private final Plugin instance = this;
    private final PluginManager manager = instance.getServer().getPluginManager();
    private final WorldManager worldManager = new WorldManager();
    private final String world = getServerPropertiesValue("level-name");

    @Override
    public void onLoad() {
        editServerProperties("spawn-protection", "0");
        editServerProperties("difficulty", "easy");
    }

    @Override
    public void onEnable() {
        initializeData();
        if (data.getConfigs().getSettingsConfig().toFileConfiguration().getBoolean("settings.WorldReset")) {
            worldManager.deleteWorld(world);
            data.getConfigs().getSettingsConfig().toFileConfiguration().set("settings.WorldReset", false);
            data.getConfigs().getSettingsConfig().saveConfiguration();
        }
        initializeDefaultTeams();
        loadCommands();
        loadListeners();
        saveDefaultConfig();
        data.getTablistManager().setAllPlayerTeams();
    }

    private void editServerProperties(String key, String value) {
        File serverPropertiesFile = new File(getServer().getWorldContainer(), "server.properties");

        try {
            List<String> lines = Files.readAllLines(serverPropertiesFile.toPath(), StandardCharsets.ISO_8859_1);
            boolean replaced = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(key + "=")) {
                    // Unveränderte Speicherung — bestehende Escapes bleiben
                    lines.set(i, key + "=" + value);
                    replaced = true;
                    break;
                }
            }

            if (!replaced) {
                lines.add(key + "=" + value);
            }

            // Zeilenweise speichern, ISO-8859-1, keine automatische Escape-Konvertierung
            Files.write(serverPropertiesFile.toPath(), lines, StandardCharsets.ISO_8859_1);
            getLogger().info("Property '" + key + "' wurde auf '" + value + "' gesetzt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getServerPropertiesValue(String key) {
        File serverPropertiesFile = new File(getServer().getWorldContainer(), "server.properties");
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(serverPropertiesFile)) {
            properties.load(in); // Nur lesen, keine Speicherung!
            String value = properties.getProperty(key);
            getLogger().info("Der Wert von " + key + " lautet: " + value);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void loadListeners(){
        manager.registerEvents(new ConnectionListener(), this);
        manager.registerEvents(new GameListener(), this);
        manager.registerEvents(new JokerListener(), this);
        manager.registerEvents(new TeamInvListener(), this);
    }

    private void loadCommands(){
        Objects.requireNonNull(getCommand("backpack")).setExecutor(new BackpackCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());
        Objects.requireNonNull(getCommand("reset")).setExecutor(new ResetCommand());
        Objects.requireNonNull(getCommand("skip")).setExecutor(new SkipCommand());
        Objects.requireNonNull(getCommand("result")).setExecutor(new ResultCommand());
        Objects.requireNonNull(getCommand("task")).setExecutor(new SetTaskCommand());
        Objects.requireNonNull(getCommand("end")).setExecutor(new EndCommand());
        Objects.requireNonNull(getCommand("team")).setExecutor(new TeamCommand());
        Objects.requireNonNull(getCommand("countdown")).setExecutor(new CountdownCommand());
        Objects.requireNonNull(getCommand("teamchat")).setExecutor(new TeamChatCommand());
    }

    private void initializeData(){
        data.setInstance(this);
        data.setConfigs(new Configs());
        data.setTeamManager(new TeamManager());
        data.setTablistManager(new TablistManager());
        data.setTeamInventoryManager(new TeamInventoryManager());
        data.setBackpackManager(new BackpackManager());
        data.setTaskManager(new TaskManager());
        data.setLogic(new Logic());
        data.setRadomItemGenerator(new RandomItemGenerator());
        data.setResultInventoryManager(new ResultInventoryManager());
        data.setWorldManager(worldManager);
    }

    private void initializeDefaultTeams(){
        new DefaultTeams();
    }

    private void saveConfigs(){
        data.getBackpackManager().saveBackpack();
        data.getBackpackManager().saveTeamBackpack();
        data.getGameCountdown().saveTime();
        data.getTaskManager().saveTeamTasks();
        data.getTaskManager().saveTasks();
        data.getLogic().savePoints();
        data.getTaskManager().saveCompletedTeamTasks();
        data.getTaskManager().saveCompletedTasks();
    }

    @Override
    public void onDisable() {
        saveConfigs();
        data.getLogic().removeAllTasks();
    }

    public static Data getData() {
        return data;
    }
}
