package de.veroxar.forceItemBattle.tasks;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class TaskManager {

    Data data = ForceItemBattle.getData();
    JavaPlugin instance = data.getInstance();
    Configuration taskConfig = data.getConfigs().getTaskConfig();
    private final Map<UUID, Task> map;

    public TaskManager() {
        map = new HashMap<>();

        load();
    }

    public Task getTask(UUID uuid) {
        if(map.containsKey(uuid)) {
            return map.get(uuid);
        }

        Task task = new Task(uuid);
        map.put(uuid, task);
        return task;
    }

    public void setTask(UUID uuid, Task task){
        map.put(uuid, task);
    }

    public void removeTask(UUID uuid) {
        if (map.containsKey(uuid)) {
            Task task = map.get(uuid);
            map.remove(uuid, task);
        }
    }

    public boolean hasTask(UUID uuid) {
        return map.containsKey(uuid);
    }

    private void load() {
        List<String> uuids = taskConfig.toFileConfiguration().getStringList("tasks");

        uuids.forEach(s ->{
            UUID uuid = UUID.fromString(s);

            String material = taskConfig.toFileConfiguration().getString("task." + s);

            assert material != null;
            map.put(uuid, new Task(uuid, Material.getMaterial(material)));
        });
    }

    public void save(){

        List<String> uuids = new ArrayList<>();

        for (UUID uuid : map.keySet()) {
            uuids.add(uuid.toString());
        }

        taskConfig.toFileConfiguration().set("tasks", uuids);
        map.forEach((uuid, task) -> taskConfig.toFileConfiguration().set("task." + uuid.toString(), task.getMaterial().name()));
        taskConfig.saveConfiguration();
    }

}
