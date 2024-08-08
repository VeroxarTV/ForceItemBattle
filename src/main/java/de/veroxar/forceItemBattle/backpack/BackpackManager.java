package de.veroxar.forceItemBattle.backpack;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

public class BackpackManager {

    Data data = ForceItemBattle.getData();
    JavaPlugin instance = data.getInstance();
    Configs configs = data.getConfigs();
    private final Map<UUID, Backpack> map;

    public BackpackManager() {
        map = new HashMap<>();

        load();
    }

    public Backpack getBackpack(UUID uuid) {

        if(map.containsKey(uuid)) {
            return map.get(uuid);
        }

        Backpack backpack = new Backpack(uuid);
        map.put(uuid, backpack);
        return backpack;
    }

    public void setBackpack(UUID uuid, Backpack backpack){
        map.put(uuid, backpack);
    }

    private void load() {
        FileConfiguration backpackConfig = configs.getBackpackConfig().toFileConfiguration();
        List<String> uuids = backpackConfig.getStringList("backpacks");

        uuids.forEach(s ->{
            UUID uuid = UUID.fromString(s);

            String base64 = backpackConfig.getString("backpack." + s);

            try {
                map.put(uuid, new Backpack(uuid, base64));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void save(){

        List<String> uuids = new ArrayList<>();

        for (UUID uuid : map.keySet()) {
            uuids.add(uuid.toString());
        }

        Configuration backpackConfig = configs.getBackpackConfig();

        backpackConfig.toFileConfiguration().set("backpacks", uuids);
        map.forEach((uuid, backpack) -> backpackConfig.toFileConfiguration().set("backpack." + uuid.toString(), backpack.toBase64()));
        backpackConfig.saveConfiguration();
    }

    public void clear(){
        Configuration backpackConfig = configs.getBackpackConfig();
        backpackConfig.toFileConfiguration().set("backpack", null);
        backpackConfig.toFileConfiguration().set("backpacks", null);
        backpackConfig.saveConfiguration();
        map.clear();
        load();
    }
}
