package de.veroxar.forceItemBattle.config;

public class Configs {
    final Configuration backpackConfig = new Configuration();
    final Configuration countdownConfig = new Configuration();
    final Configuration taskConfig = new Configuration();
    final Configuration playersConfig = new Configuration();
    final Configuration completedTaskConfig = new Configuration();
    final Configuration teamsConfig = new Configuration();

    public Configs() {
        createConfigs();
    }

    public void createConfigs() {
        backpackConfig.createConfiguration("backpack");
        countdownConfig.createConfiguration("countdown");
        taskConfig.createConfiguration("task");
        playersConfig.createConfiguration("players");
        completedTaskConfig.createConfiguration("completed_task");
        teamsConfig.createConfiguration("teams");
    }

    public Configuration getBackpackConfig() {
        return backpackConfig;
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

    public Configuration getTeamsConfig() {
        return teamsConfig;
    }
}
