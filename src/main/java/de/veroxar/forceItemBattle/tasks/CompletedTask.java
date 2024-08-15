package de.veroxar.forceItemBattle.tasks;

import org.bukkit.Material;

import java.util.UUID;

public record CompletedTask(UUID uuid, Material material, int seconds, boolean usedJoker) {

    @Override
    public String toString() {
        return uuid.toString() + ";" + material.name() + ";" + seconds + ";" + usedJoker;
    }

    public static CompletedTask fromString(String str) {
        String[] parts = str.split(";");
        return new CompletedTask(UUID.fromString(parts[0]), Material.getMaterial(parts[1]), Integer.parseInt(parts[2]), Boolean.parseBoolean(parts[3]));
    }
}
