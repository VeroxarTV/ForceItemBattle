package de.veroxar.forceItemBattle.tasks;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.Material;
import java.util.*;

public class TaskManager {

    Data data = ForceItemBattle.getData();
    Configuration taskConfig = data.getConfigs().getTaskConfig();
    Configuration completedTaskConfig = data.getConfigs().getCompletedTaskConfig();
    private final Map<UUID, Task> map;
    private final Map<UUID, List<CompletedTask>> map2;

    public TaskManager() {
        map = new HashMap<>();
        map2 = new HashMap<>();

        loadTasks();
        loadCompletedTasks();
    }

    public Task getTask(UUID uuid) {
        if(map.containsKey(uuid)) {
            return map.get(uuid);
        }

        Task task = new Task(uuid);
        map.put(uuid, task);
        return task;
    }

    public void setTask(UUID uuid, Material material) {
        if (map.containsKey(uuid))
            removeTask(uuid);

        Task task = new Task(uuid, material);
        map.put(uuid, task);
    }

    public void removeTask(UUID uuid) {
        if (map.containsKey(uuid)) {
            Task task = map.get(uuid);
            map.remove(uuid, task);
        }
        if (taskConfig.toFileConfiguration().contains(uuid.toString() + ".task"))
            taskConfig.toFileConfiguration().set(uuid.toString(), null);
    }

    public boolean hasTask(UUID uuid) {
        return map.containsKey(uuid);
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

     public List<CompletedTask> getCompletedTaskList(UUID uuid) {
        if (map2.containsKey(uuid)) {
            return map2.get(uuid);
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
}
