package de.veroxar.forceItemBattle.backpack;

import de.veroxar.forceItemBattle.util.Base64;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public class TeamBackpack {

    private final String teamName;
    private final Inventory inventory;

    public TeamBackpack(String teamName) {
        this.teamName = teamName;
        this.inventory = Bukkit.createInventory(null, 9*3, Component.text("Backpack"));
    }

    public TeamBackpack(String teamName, String base64) throws IOException {
        this.teamName = teamName;
        this.inventory = Bukkit.createInventory(null, 9*3, Component.text("Backpack"));
        this.inventory.setContents(Base64.itemStackArrayFromBase64(base64));
    }

    public String getTeamName() {
        return teamName;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String toBase64() {
        return Base64.itemStackArrayToBase64(inventory.getContents());
    }
}
