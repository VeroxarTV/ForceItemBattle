package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TeamInventoryManager implements InventoryHolder {

    private final Inventory inventory;
    private final InventoryHolder holder;
    public boolean teamMode;

    Data data = ForceItemBattle.getData();
    Configuration settingsConfig = data.getConfigs().getSettingsConfig();
    TeamManager teamManager = data.getTeamManager();

    public TeamInventoryManager() {
        this.holder = this;
        this.inventory = Bukkit.createInventory(holder, 9, Component.text("Teams: "));
        if (settingsConfig.toFileConfiguration().contains("settings.teamMode")) {
            this.teamMode = settingsConfig.toFileConfiguration().getBoolean("settings.teamMode");
        } else {
            this.teamMode = false;
            settingsConfig.toFileConfiguration().set("settings.teamMode", false);
            settingsConfig.saveConfiguration();
        }
    }

    public void openTeamInv(Player player) {
        if (player.hasPermission("forceItemBattle.menu.team")) {
            setupInvOp(inventory);
            player.openInventory(inventory);
        } else {
            setupInvNonOp(inventory);
            player.openInventory(inventory);
        }
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public void setTeamMode(boolean teamMode) {
        this.teamMode = teamMode;
        settingsConfig.toFileConfiguration().set("settings.teamMode", teamMode);
        settingsConfig.saveConfiguration();
    }

    public boolean isTeamMode() {
        return teamMode;
    }

    private void setupInvOp(Inventory inventory) {
        ItemStack toggleOn = new ItemBuilder(Material.RED_STAINED_GLASS).setName("§cTeams [AUS]").addLoreLine("§6Schalte den Teammodus: §aAN").toItemStack();
        ItemStack toggleOff = new ItemBuilder(Material.GREEN_STAINED_GLASS).setName("§aTeams [AN]").addLoreLine("§6Schalte den Teammodus: §cAUS").toItemStack();

        setupGlassPanes(inventory);
        setupTeamItems(inventory);

        if (teamMode) {
            inventory.setItem(0, toggleOff);
        } else {
            inventory.setItem(0, toggleOn);
        }
    }

    private void setupInvNonOp(Inventory inventory){
        setupGlassPanes(inventory);
        setupTeamItems(inventory);
    }

    public void updateInv(Inventory inventory) {
        ItemStack toggleOn = new ItemBuilder(Material.RED_STAINED_GLASS).setName("§cTeams [AUS]").addLoreLine("§6Schalte den Teammodus: §aAN").toItemStack();
        ItemStack toggleOff = new ItemBuilder(Material.GREEN_STAINED_GLASS).setName("§aTeams [AN]").addLoreLine("§6Schalte den Teammodus: §cAUS").toItemStack();
        if (teamMode) {
            inventory.setItem(0, toggleOff);
        } else {
            inventory.setItem(0, toggleOn);
        }
    }

    private void setupTeamItems(Inventory inventory) {
        ItemStack teamBlue = new ItemBuilder(Material.BLUE_WOOL).setName("§1Blau").addLoreLine("§6Tritt dem §1blauen §6Team bei!").toItemStack();
        ItemStack teamRed = new ItemBuilder(Material.RED_WOOL).setName("§cRot").addLoreLine("§6Tritt dem §croten §6Team bei!").toItemStack();
        ItemStack teamYellow = new ItemBuilder(Material.YELLOW_WOOL).setName("§eGelb").addLoreLine("§6Tritt dem §egelben §6Team bei!").toItemStack();
        ItemStack teamGreen = new ItemBuilder(Material.GREEN_WOOL).setName("§aGrün").addLoreLine("§6Tritt dem §agrünen §6Team bei!").toItemStack();

        inventory.setItem(2, teamBlue);
        inventory.setItem(3, teamRed);
        inventory.setItem(5, teamYellow);
        inventory.setItem(6, teamGreen);
        addAllPlayersToLore();
    }

    private void addAllPlayersToLore() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (teamManager.hasTeam(player)) {
                String teamName = teamManager.getTeamName(player);
                int index;
                switch (teamName.toLowerCase()) {
                    case "blue" -> index = 2;
                    case "red" -> index = 3;
                    case "yellow" -> index = 5;
                    case "green" -> index = 6;
                    default -> index = 0;
                }
                ItemStack itemStack = inventory.getItem(index);
                ItemMeta im = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                if(im.hasLore())lore = new ArrayList<>(im.getLore());
                lore.add(player.getName());
                im.setLore(lore);
                itemStack.setItemMeta(im);
                inventory.setItem(index, itemStack);
            }
        }
    }

    private void setupGlassPanes(Inventory inventory) {
        ItemStack grayGlassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("").toItemStack();
        inventory.setItem(0, grayGlassPane);
        inventory.setItem(1, grayGlassPane);
        inventory.setItem(4, grayGlassPane);
        inventory.setItem(7, grayGlassPane);
        inventory.setItem(8, grayGlassPane);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
