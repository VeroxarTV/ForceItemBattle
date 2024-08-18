package de.veroxar.forceItemBattle;

import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.commands.*;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.events.ConnectionListener;
import de.veroxar.forceItemBattle.events.GameListener;
import de.veroxar.forceItemBattle.events.JokerListener;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.team.DefaultTeams;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import de.veroxar.forceItemBattle.util.TablistManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/*
@Author: Veroxar
 */


public final class ForceItemBattle extends JavaPlugin {

    private static final Data data = new Data();
    private final Plugin instance = this;
    private final PluginManager manager = instance.getServer().getPluginManager();

    @Override
    public void onEnable() {
        initializeData();
        initializeDefaultTeams();
        loadCommands();
        loadListeners();
        saveDefaultConfig();
    }

    public void loadListeners(){
        manager.registerEvents(new ConnectionListener(), this);
        manager.registerEvents(new GameListener(), this);
        manager.registerEvents(new JokerListener(), this);
    }

    public void loadCommands(){
        //Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("backpack")).setExecutor(new BackpackCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());
        Objects.requireNonNull(getCommand("reset")).setExecutor(new ResetCommand());
        Objects.requireNonNull(getCommand("skip")).setExecutor(new SkipCommand());
        Objects.requireNonNull(getCommand("result")).setExecutor(new ResultCommand());
        Objects.requireNonNull(getCommand("task")).setExecutor(new SetTaskCommand());
        Objects.requireNonNull(getCommand("end")).setExecutor(new EndCommand());
    }

    public void initializeData(){
        data.setInstance(this);
        data.setConfigs(new Configs());
        data.setTeamManager(new TeamManager());
        data.setTablistManager(new TablistManager());
        data.setBackpackManager(new BackpackManager());
        data.setTaskManager(new TaskManager());
        data.setLogic(new Logic());
        data.setRadomItemGenerator(new RandomItemGenerator());
        data.setResultInventoryManager(new ResultInventoryManager());
    }

    public void initializeDefaultTeams(){
        new DefaultTeams();
    }

    public void saveConfigs(){
        data.getBackpackManager().save();
        data.getGameCountdown().saveTime();
        data.getTaskManager().saveTasks();
        data.getLogic().savePoints();
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
