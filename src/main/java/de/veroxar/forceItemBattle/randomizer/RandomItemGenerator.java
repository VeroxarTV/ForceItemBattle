package de.veroxar.forceItemBattle.randomizer;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("UnstableApiUsage")
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

        if (material.equals(Material.BEDROCK))
            return false;

        if (material.isEmpty())
            return false;

        if (material.equals(Material.DEBUG_STICK))
            return false;

        if (material.equals(Material.COMMAND_BLOCK))
            return false;

        if (material.equals(Material.CHAIN_COMMAND_BLOCK))
            return false;

        if (material.equals(Material.REPEATING_COMMAND_BLOCK))
            return false;

        if (material.name().contains("PLAYER"))
            return false;

        if (material.name().contains("SPAWN"))
            return false;

        if (material.equals(Material.VAULT))
            return false;

        if (material.name().contains("CANDLE_CAKE"))
            return false;

        if (!(material.isItem()))
            return false;

        if (material.name().contains("SHERD"))
            return false;

        if (material.name().contains("TEMPLATE"))
            return false;

        if (material.equals(Material.ENCHANTED_BOOK))
            return false;

        if(material.name().contains("OXIDIZED"))
            return false;

        if (material.name().contains("EXPOSED"))
            return false;

        if (material.isRecord())
            return false;

        if (material.name().contains("CORAL"))
            return false;

        if (material.name().contains("ICE"))
            return false;

        if (material.name().contains("INFESTED"))
            return false;

        if (material.name().contains("STRUCTURE"))
            return false;

        if (material.equals(Material.LIGHT))
            return false;

        if (material.name().contains("WEATHERED"))
            return false;

        if (material.equals(Material.KNOWLEDGE_BOOK))
            return false;

        if (material.equals(Material.BUNDLE))
            return false;

        if (material.equals(Material.FARMLAND))
            return false;

        if (material.equals(Material.DIRT_PATH))
            return false;

        if (material.equals(Material.PIGLIN_HEAD))
            return false;

        if (material.equals(Material.BARRIER))
            return false;

        if (material.equals(Material.COMMAND_BLOCK_MINECART))
            return false;

        if (material.equals(Material.SUSPICIOUS_GRAVEL))
            return false;

        if (material.equals(Material.SUSPICIOUS_SAND))
            return false;

        if (material.equals(Material.END_PORTAL_FRAME))
            return false;

        return !material.equals(Material.JIGSAW);
    }

    public Material getRandomItem() {
        int index = random.nextInt(survivalItems.size());
        return survivalItems.get(index);
    }

}
