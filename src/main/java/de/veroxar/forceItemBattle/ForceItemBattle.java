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
import java.io.FileOutputStream;
import java.io.IOException;
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
        worldManager.deleteWorld(world);
        initializeData();
        initializeDefaultTeams();
        loadCommands();
        loadListeners();
        saveDefaultConfig();
        data.getTablistManager().setAllPlayerTeams();
    }

    private void editServerProperties(String key, String value) {
        // Pfad zur server.properties Datei
        File serverPropertiesFile = new File(getServer().getWorldContainer(), "server.properties");

        // Properties-Objekt erstellen
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(serverPropertiesFile)) {
            // Datei einlesen
            properties.load(in);

            // Property ändern
            properties.setProperty(key, value);

            // Änderungen speichern
            try (FileOutputStream out = new FileOutputStream(serverPropertiesFile)) {
                properties.store(out, null);
                getLogger().info(key + " wurde auf den Wert: " + value + " gesetzt!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getServerPropertiesValue(String key) {
        // Pfad zur server.properties Datei
        File serverPropertiesFile = new File(getServer().getWorldContainer(), "server.properties");

        // Properties-Objekt erstellen
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(serverPropertiesFile)) {
            // Datei einlesen
            properties.load(in);

            // Property ändern
            String value = properties.getProperty(key);

            // Änderungen speichern
            try (FileOutputStream out = new FileOutputStream(serverPropertiesFile)) {
                properties.store(out, null);
                getLogger().info("Der Wert von " + key + " lautet: " + value);
            }
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
