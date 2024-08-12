package de.veroxar.forceItemBattle.tasks;

import org.bukkit.Material;

import java.util.UUID;

public class CompletedTask {

    private UUID uuid;
    private Material material;
    private int seconds;
    private boolean usedJoker;

    public CompletedTask(UUID uuid, Material material, int seconds, boolean usedJoker) {
        this.uuid = uuid;
        this.material = material;
        this.seconds = seconds;
        this.usedJoker = usedJoker;
    }

    @Override
    public String toString(){
        return uuid.toString() + ";" + material.name() + ";" + seconds + ";" + usedJoker;
    }

    public static CompletedTask fromString(String str) {
        String[] parts = str.split(";");
        return new CompletedTask(UUID.fromString(parts[0]), Material.getMaterial(parts[1]), Integer.parseInt(parts[2]), Boolean.parseBoolean(parts[3]));
    }

    public UUID getUuid() {
        return uuid;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isUsedJoker() {
        return usedJoker;
    }
}
