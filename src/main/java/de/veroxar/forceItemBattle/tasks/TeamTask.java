package de.veroxar.forceItemBattle.tasks;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.randomizer.RandomItemGenerator;
import org.bukkit.Material;

public class TeamTask {

    Data data = ForceItemBattle.getData();
    RandomItemGenerator randomItemGenerator = data.getRadomItemGenerator();

    private final String teamName;
    private final Material material;

    public TeamTask(String teamName) {
        this.teamName = teamName;
        material = randomItemGenerator.getRandomItem();
    }

    public TeamTask(String teamName, Material material) {
        this.teamName = teamName;
        this.material = material;
    }

    public String getTeamName() {
        return teamName;
    }
    public Material getMaterial() {
        return material;
    }
}
