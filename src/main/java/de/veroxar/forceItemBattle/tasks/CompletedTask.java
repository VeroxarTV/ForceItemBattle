package de.veroxar.forceItemBattle.tasks;

import org.bukkit.Material;

import java.util.UUID;

public class CompletedTask {

    private UUID uuid;
    private Material material;
    private int seconds;

    public CompletedTask(UUID uuid, Material material, int seconds) {
        this.uuid = uuid;
        this.material = material;
        this.seconds = seconds;
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
}
