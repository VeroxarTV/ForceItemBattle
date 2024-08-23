package de.veroxar.forceItemBattle.data;

import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Data {

    JavaPlugin instance;
    BackpackManager backpackManager;
    Configs configs;
    GameCountdown gameCountdown;
    RandomItemGenerator radomItemGenerator;
    TaskManager taskManager;
    Logic logic;
    ResultInventoryManager resultInventoryManager;
    TeamManager teamManager;
    TablistManager tablistManager;
    TeamInventoryManager teamInventoryManager;
    WorldManager worldManager;

    //SETTER
    public void setInstance(JavaPlugin instance) {
        this.instance = instance;
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
    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }
    public void setTablistManager(TablistManager tablistManager) {
        this.tablistManager = tablistManager;
    }
    public void setTeamInventoryManager(TeamInventoryManager teamInventoryManager) {
        this.teamInventoryManager = teamInventoryManager;
    }
    public void setWorldManager(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    //GETTER
    public JavaPlugin getInstance() {
        return instance;
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
    public TeamManager getTeamManager() {
        return teamManager;
    }
    public TablistManager getTablistManager() {
        return tablistManager;
    }
    public TeamInventoryManager getTeamInventoryManager() {
        return teamInventoryManager;
    }
    public WorldManager getWorldManager() {
        return worldManager;
    }
}
