package de.veroxar.forceItemBattle.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class WorldManager {

    public boolean deleteWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Entlade die Welt
            Bukkit.unloadWorld(world, false);
        }

        // Hole das Verzeichnis der Welt
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

        // Lösche die Welt (Verzeichnis)
        return deleteDirectory(worldFolder);
    }

    private boolean deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    deleteDirectory(child);
                }
            }
        }
        return file.delete();
    }

}
