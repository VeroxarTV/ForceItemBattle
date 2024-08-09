package de.veroxar.forceItemBattle.randomizer;

import org.bukkit.Material;
import org.bukkit.inventory.CreativeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomItemGenerator {

    private final List<Material> survivalItems;
    private final Random random;

    public RandomItemGenerator() {
        this.survivalItems = new ArrayList<>();
        this.random = new Random();

        for (Material material : Material.values()) {
            if (isSurvivalObtainable(material)) {
                survivalItems.add(material);
            }
        }
    }

    private boolean isSurvivalObtainable(Material material) {

        if (material.getCreativeCategory() == null)
            return false;

        if (material.name().contains("STRUCTURE"))
            return false;

        if (material.equals(Material.GRASS_BLOCK))
            return false;

        if (material.equals(Material.DIRT_PATH))
            return false;

        if (material.name().contains("SPAWN"))
            return false;

        if (material.equals(Material.BEDROCK))
            return false;

        if (material.equals(Material.VAULT))
            return false;

        if (material.equals(Material.DEBUG_STICK))
            return false;

        if (material.equals(Material.BARRIER))
            return false;

        if (material.name().contains("POTION"))
            return false;

        if (material.name().contains("INFESTED"))
            return false;

        if (material.name().contains("DISC"))
            return false;

        if (material.name().contains("BANNER_PATTERN"))
            return false;

        if (material.name().contains("HEAD"))
            return false;

        if (material.equals(Material.AIR))
            return false;

        return true;
    }

    public Material getRandomItem() {
        int index = random.nextInt(survivalItems.size());
        return survivalItems.get(index);
    }

}
