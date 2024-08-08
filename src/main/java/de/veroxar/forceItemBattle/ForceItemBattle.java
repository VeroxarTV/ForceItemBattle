package de.veroxar.forceItemBattle;

import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.commands.*;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.events.GameListener;
import de.veroxar.forceItemBattle.events.JokerListener;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.events.ConnectionListener;
import de.veroxar.forceItemBattle.util.Logic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ForceItemBattle extends JavaPlugin {

    private static final Data data = new Data();
    private final Plugin instance = this;
    private final PluginManager manager = instance.getServer().getPluginManager();

    @Override
    public void onEnable() {
        initializeData();
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
    }

    public void initializeData(){
        data.setInstance(this);
        data.setConfigs(new Configs());
        //data.setTimer(new Timer());
        data.setBackpackManager(new BackpackManager());
        data.setTaskManager(new TaskManager());
        data.setLogic(new Logic());
        data.setGameCountdown(new GameCountdown());
        data.setRadomItemGenerator(new RandomItemGenerator());
    }

    public void saveConfigs(){
        //data.getTimer().saveTime();
        data.getBackpackManager().save();
        data.getGameCountdown().saveTime();
        data.getTaskManager().save();
        data.getLogic().savePoints();
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
