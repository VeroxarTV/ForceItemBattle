package de.veroxar.forceItemBattle.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class WorldManager {

    public void deleteWorld(String worldName) {
        // Validate world name to prevent path traversal attacks
        if (worldName == null || worldName.isEmpty()) {
            Bukkit.getLogger().severe("World name cannot be null or empty");
            return;
        }

        if (worldName.contains("..") || worldName.contains("/") || worldName.contains("\\")) {
            Bukkit.getLogger().severe("Invalid world name: " + worldName + " (contains illegal characters)");
            return;
        }

        ArrayList<String> worlds = new ArrayList<>();
        worlds.add(worldName);
        worlds.add(worldName + "_nether");
        worlds.add(worldName + "_the_end");

        for (String world : worlds) {
            // Unload world if loaded
            World loadedWorld = Bukkit.getWorld(world);
            if (loadedWorld != null) {
                Bukkit.unloadWorld(loadedWorld, false);
            }

            // Use proper path construction with world container
            File worldFolder = new File(Bukkit.getWorldContainer(), world);

            if (!worldFolder.exists()) {
                continue;
            }

            try {
                FileUtils.cleanDirectory(worldFolder);
                new File(worldFolder, "playerdata").mkdirs();
                Bukkit.getLogger().info("Successfully cleaned world: " + world);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to delete world: " + world, e);
                Bukkit.getConsoleSender().sendMessage("§cThe world could not be deleted: " + world);
            }
        }
    }
}
