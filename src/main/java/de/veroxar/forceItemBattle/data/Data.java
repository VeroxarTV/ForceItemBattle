package de.veroxar.forceItemBattle.data;

import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import de.veroxar.forceItemBattle.util.Timer;
import org.bukkit.plugin.java.JavaPlugin;

public class Data {

    JavaPlugin instance;
    Timer timer;
    BackpackManager backpackManager;
    Configs configs;
    GameCountdown gameCountdown;
    RandomItemGenerator radomItemGenerator;
    TaskManager taskManager;
    Logic logic;
    ResultInventoryManager resultInventoryManager;

    //SETTER
    public void setInstance(JavaPlugin instance) {
        this.instance = instance;
    }
    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    public void setBackpackManager(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }
    public void setConfigs(Configs configs) {
        this.configs = configs;
    }
    public void setGameCountdown(GameCountdown gameCountdown) {
        this.gameCountdown = gameCountdown;
    }
    public void setRadomItemGenerator(RandomItemGenerator radomItemGenerator) {
        this.radomItemGenerator = radomItemGenerator;
    }
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    public void setLogic(Logic logic) {
        this.logic = logic;
    }
    public void setResultInventoryManager(ResultInventoryManager resultInventoryManager) {
        this.resultInventoryManager = resultInventoryManager;
    }

    //GETTER
    public JavaPlugin getInstance() {
        return instance;
    }
    public Timer getTimer() {
        return timer;
    }
    public BackpackManager getBackpackManager() {
        return backpackManager;
    }
    public Configs getConfigs() {
        return configs;
    }
    public GameCountdown getGameCountdown() {
        return gameCountdown;
    }
    public RandomItemGenerator getRadomItemGenerator() {
        return radomItemGenerator;
    }
    public TaskManager getTaskManager() {
        return taskManager;
    }
    public Logic getLogic() {
        return logic;
    }
    public ResultInventoryManager getResultInventoryManager() {
        return resultInventoryManager;
    }
}
