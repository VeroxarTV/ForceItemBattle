package de.veroxar.forceItemBattle.tasks;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import org.bukkit.Material;

import java.util.UUID;

public class Task {

    Data data = ForceItemBattle.getData();
    RandomItemGenerator randomItemGenerator = data.getRadomItemGenerator();

    private final UUID uuid;
    private final Material material;

    public Task(UUID uuid) {
        this.uuid = uuid;
        material = randomItemGenerator.getRandomItem();
    }

    public Task(UUID uuid, Material material) {
        this.uuid = uuid;
        this.material = material;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Material getMaterial() {
        return material;
    }
}
