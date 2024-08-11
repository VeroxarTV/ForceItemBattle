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
