package de.veroxar.forceItemBattle.config;

public class Configs {
    final Configuration backpackConfig = new Configuration();
    final Configuration timerConfig = new Configuration();
    final Configuration countdownConfig = new Configuration();
    final Configuration taskConfig = new Configuration();
    final Configuration playersConfig = new Configuration();
    final Configuration completedTaskConfig = new Configuration();

    public Configs() {
        createConfigs();
    }

    public void createConfigs() {
        backpackConfig.createConfiguration("backpack");
        timerConfig.createConfiguration("timer");
        countdownConfig.createConfiguration("countdown");
        taskConfig.createConfiguration("task");
        playersConfig.createConfiguration("players");
        completedTaskConfig.createConfiguration("completed_task");
    }

    public Configuration getBackpackConfig() {
        return backpackConfig;
    }
    public Configuration getTimerConfig() {
        return timerConfig;
    }
    public Configuration getCountdownConfig() {
        return countdownConfig;
    }
    public Configuration getTaskConfig() {
        return taskConfig;
    }
    public Configuration getPlayersConfig() {
        return playersConfig;
    }
    public Configuration getCompletedTaskConfig() {
        return completedTaskConfig;
    }
}
