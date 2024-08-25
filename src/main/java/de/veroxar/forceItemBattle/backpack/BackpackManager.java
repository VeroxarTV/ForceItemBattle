package de.veroxar.forceItemBattle.backpack;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.*;

public class BackpackManager {

    Data data = ForceItemBattle.getData();
    Configs configs = data.getConfigs();
    private final Map<UUID, Backpack> map;
    private final Map<String, TeamBackpack> teamMap;

    public BackpackManager() {
        map = new HashMap<>();
        teamMap = new HashMap<>();
        loadBackpack();
        loadTeamBackpack();
    }

    public Backpack getBackpack(UUID uuid) {

        if(map.containsKey(uuid)) {
            return map.get(uuid);
        }

        Backpack backpack = new Backpack(uuid);
        map.put(uuid, backpack);
        return backpack;
    }

    public TeamBackpack getTeamBackpack(String teamName) {

        if(teamMap.containsKey(teamName)) {
            return teamMap.get(teamName);
        }

        TeamBackpack teamBackpack = new TeamBackpack(teamName);
        teamMap.put(teamName, teamBackpack);
        return teamBackpack;
    }

    private void loadBackpack() {
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

    public void saveBackpack(){

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
        backpackConfig.toFileConfiguration().set("teamNames", null);
        backpackConfig.saveConfiguration();
        map.clear();
        teamMap.clear();
        loadBackpack();
        loadTeamBackpack();
    }

    private void loadTeamBackpack() {
        FileConfiguration backpackConfig = configs.getBackpackConfig().toFileConfiguration();
        List<String> teamNames = backpackConfig.getStringList("teamNames");

        teamNames.forEach(teamName ->{
            String base64 = backpackConfig.getString("backpack." + teamName);

            try {
                teamMap.put(teamName, new TeamBackpack(teamName, base64));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveTeamBackpack(){

        List<String> teamNames = new ArrayList<>(teamMap.keySet());

        Configuration backpackConfig = configs.getBackpackConfig();

        backpackConfig.toFileConfiguration().set("teamNames", teamNames);
        teamMap.forEach((teamName, teamBackpack) -> backpackConfig.toFileConfiguration().set("backpack." + teamName, teamBackpack.toBase64()));
        backpackConfig.saveConfiguration();
    }

}
