package de.veroxar.forceItemBattle.tasks;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.team.TeamManager;
import org.bukkit.Material;
import java.util.*;

public class TaskManager {

    Data data = ForceItemBattle.getData();
    Configuration taskConfig = data.getConfigs().getTaskConfig();
    Configuration completedTaskConfig = data.getConfigs().getCompletedTaskConfig();
    TeamManager teamManager = data.getTeamManager();
    private final Map<UUID, Task> map;
    private final Map<String, TeamTask> teamMap;
    private final Map<UUID, List<CompletedTask>> map2;
    private final Map<String, List<CompletedTeamTask>> teamMap2;

    public TaskManager() {
        map = new HashMap<>();
        teamMap = new HashMap<>();
        map2 = new HashMap<>();
        teamMap2 = new HashMap<>();

        loadTeamTasks();
        loadTasks();
        loadCompletedTasks();
        loadCompletedTeamTasks();
    }

    public Task getTask(UUID uuid) {
        if(map.containsKey(uuid)) {
            return map.get(uuid);
        }

        Task task = new Task(uuid);
        map.put(uuid, task);
        return task;
    }

    public TeamTask getTeamTask(String teamName){
        if (teamMap.containsKey(teamName)) {
            return teamMap.get(teamName);
        }

        TeamTask teamTask = new TeamTask(teamName);
        teamMap.put(teamName, teamTask);
        return teamTask;
    }

    public void setTask(UUID uuid, Material material) {
        if (map.containsKey(uuid))
            removeTask(uuid);

        Task task = new Task(uuid, material);
        map.put(uuid, task);
    }

    public void setTeamTask(String teamName, Material material) {
        if (teamMap.containsKey(teamName))
            removeTask(teamName);

        TeamTask teamTask = new TeamTask(teamName, material);
        teamMap.put(teamName, teamTask);
    }

    public void removeTask(UUID uuid) {
        if (map.containsKey(uuid)) {
            Task task = map.get(uuid);
            map.remove(uuid, task);
        }
        if (taskConfig.toFileConfiguration().contains(uuid.toString() + ".task"))
            taskConfig.toFileConfiguration().set(uuid.toString(), null);
    }

    public void removeTask(String teamName) {
        if (teamMap.containsKey(teamName)) {
            TeamTask teamTask = teamMap.get(teamName);
            teamMap.remove(teamName, teamTask);
        }
        if (taskConfig.toFileConfiguration().contains(teamName + ".task"))
            taskConfig.toFileConfiguration().set(teamName, null);
    }

    public boolean hasTask(UUID uuid) {
        return map.containsKey(uuid);
    }

    public boolean hasTeamTask(String teamName) {
        return teamMap.containsKey(teamName);
    }

     public void createCompletedTask(UUID uuid, Material material, Integer time, boolean usedJoker) {
        List<CompletedTask> list = getCompletedTaskList(uuid);
        CompletedTask completedTask = new CompletedTask(uuid, material, time, usedJoker);
        list.add(completedTask);
        if (map2.containsKey(uuid)) {
            map2.replace(uuid, list);
            return;
        }
        map2.put(uuid, list);
     }

    public void createCompletedTeamTask(String teamName, Material material, Integer time, boolean usedJoker) {
        List<CompletedTeamTask> list = getCompletedTeamTaskList(teamName);
        CompletedTeamTask completedTask = new CompletedTeamTask(teamName, material, time, usedJoker);
        list.add(completedTask);
        if (teamMap2.containsKey(teamName)) {
            teamMap2.replace(teamName, list);
            return;
        }
        teamMap2.put(teamName, list);
    }

     public List<CompletedTask> getCompletedTaskList(UUID uuid) {
        if (map2.containsKey(uuid)) {
            return map2.get(uuid);
        }
         return new ArrayList<>();
     }

    public List<CompletedTeamTask> getCompletedTeamTaskList(String teamName) {
        if (teamMap2.containsKey(teamName)) {
            return teamMap2.get(teamName);
        }
        return new ArrayList<>();
    }

    public void setCompletedTaskList(UUID uuid ,List<CompletedTask> completedTaskList) {
        if (map2.containsKey(uuid)) {
            map2.replace(uuid, completedTaskList);
            return;
        }
        map2.put(uuid, completedTaskList);
    }

    public void setCompletedTeamTaskList(String teamName ,List<CompletedTeamTask> completedTeamTaskList) {
        if (teamMap2.containsKey(teamName)) {
            teamMap2.replace(teamName, completedTeamTaskList);
            return;
        }
        teamMap2.put(teamName, completedTeamTaskList);
    }

    private void loadTasks() {
        List<String> uuids = taskConfig.toFileConfiguration().getStringList("tasks");

        uuids.forEach(s ->{
            UUID uuid = UUID.fromString(s);

            String material = taskConfig.toFileConfiguration().getString(s + ".task");

            assert material != null;
            map.put(uuid, new Task(uuid, Material.getMaterial(material)));
        });
    }

    public void saveTasks(){

        List<String> uuids = new ArrayList<>();

        for (UUID uuid : map.keySet()) {
            uuids.add(uuid.toString());
        }

        taskConfig.toFileConfiguration().set("tasks", uuids);
        map.forEach((uuid, task) -> taskConfig.toFileConfiguration().set(uuid.toString() + ".task", task.getMaterial().name()));
        taskConfig.saveConfiguration();
    }

    private void loadTeamTasks() {
        List<String> teamNames = taskConfig.toFileConfiguration().getStringList("teamTasks");

        teamNames.forEach(s ->{
            String material = taskConfig.toFileConfiguration().getString(s + ".task");

            assert material != null;
            teamMap.put(s, new TeamTask(s, Material.getMaterial(material)));
        });
    }

    public void saveTeamTasks(){

        List<String> teamNames = new ArrayList<>(teamMap.keySet());

        taskConfig.toFileConfiguration().set("teamTasks", teamNames);
        teamMap.forEach((teamName, teamTask) -> taskConfig.toFileConfiguration().set(teamName + ".task", teamTask.getMaterial().name()));
        taskConfig.saveConfiguration();
    }

    private void loadCompletedTasks() {
        List<String> uuids = completedTaskConfig.toFileConfiguration().getStringList("completed_tasks");
        uuids.forEach(s -> {
            UUID uuid = UUID.fromString(s);
            List<String> completedTasks = completedTaskConfig.toFileConfiguration().getStringList(uuid + ".completed_tasks");
            List<CompletedTask> completedTaskList = new ArrayList<>(); // Erstelle eine neue Liste für jede UUID

            completedTasks.forEach(s1 -> completedTaskList.add(CompletedTask.fromString(s1)));

            setCompletedTaskList(uuid, completedTaskList);
        });
    }

    public void saveCompletedTasks(){

        List<String> uuids = new ArrayList<>();

        for (UUID uuid : map2.keySet()) {
            uuids.add(uuid.toString());
        }

        for (String s : uuids) {
            List<String> completedTasksStringList = new ArrayList<>();
            UUID uuid = UUID.fromString(s);
            for (CompletedTask completedTask : map2.get(uuid)) {
                completedTasksStringList.add(completedTask.toString());
            }
            completedTaskConfig.toFileConfiguration().set(uuid + ".completed_tasks", completedTasksStringList);
        }

        completedTaskConfig.toFileConfiguration().set("completed_tasks", uuids);
        completedTaskConfig.saveConfiguration();

    }

    private void loadCompletedTeamTasks() {
        List<String> teamNames = completedTaskConfig.toFileConfiguration().getStringList("completed_team_tasks");
        teamNames.forEach(s -> {
            List<String> completedTeamTasks = completedTaskConfig.toFileConfiguration().getStringList(s + ".completed_team_tasks");
            List<CompletedTeamTask> completedTeamTaskList = new ArrayList<>(); // Erstelle eine neue Liste für jede UUID

            completedTeamTasks.forEach(s1 -> completedTeamTaskList.add(CompletedTeamTask.fromString(s1)));

            setCompletedTeamTaskList(s, completedTeamTaskList);
        });
    }

    public void saveCompletedTeamTasks(){

        List<String> teamNames = new ArrayList<>(teamMap2.keySet());

        for (String s : teamNames) {
            List<String> completedTeamTasksStringList = new ArrayList<>();
            for (CompletedTeamTask completedTeamTask : teamMap2.get(s)) {
                completedTeamTasksStringList.add(completedTeamTask.toString());
            }
            completedTaskConfig.toFileConfiguration().set(s + ".completed_team_tasks", completedTeamTasksStringList);
        }

        completedTaskConfig.toFileConfiguration().set("completed_team_tasks", teamNames);
        completedTaskConfig.saveConfiguration();

    }

}
