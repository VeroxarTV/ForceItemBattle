package de.veroxar.forceItemBattle.tasks;

import org.bukkit.Material;

public record CompletedTeamTask(String teamName, Material material, int seconds, boolean usedJoker) {

    @Override
    public String toString() {
        return teamName + ";" + material.name() + ";" + seconds + ";" + usedJoker;
    }

    public static CompletedTeamTask fromString(String str) {
        String[] parts = str.split(";");
        return new CompletedTeamTask(parts[0], Material.getMaterial(parts[1]), Integer.parseInt(parts[2]), Boolean.parseBoolean(parts[3]));
    }
}
