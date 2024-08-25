package de.veroxar.forceItemBattle.util;

import org.bukkit.Bukkit;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldManager {

    public void deleteWorld(String worldName) {
        ArrayList<String> worlds = new ArrayList<>();
        worlds.add(worldName);
        worlds.add(worldName + "_nether");
        worlds.add(worldName + "_the_end");
        for (String world : worlds) {
            try {
                FileUtils.cleanDirectory(new File(world));
                new File(Bukkit.getWorldContainer() + "/" + world + "/playerdata").mkdirs();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§cDie Welt konnte nicht gelöscht werden");
            }
        }
    }
}
