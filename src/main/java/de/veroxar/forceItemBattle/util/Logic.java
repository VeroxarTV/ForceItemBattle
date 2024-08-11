package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.tasks.Task;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Logic {

    Data data = ForceItemBattle.getData();
    TaskManager taskManager = data.getTaskManager();
    Configuration playersConfig = data.getConfigs().getPlayersConfig();
    Map<UUID, Integer> playerPointsMap = new HashMap<>();
    JavaPlugin instance = data.getInstance();
    BackpackManager backpackManager = data.getBackpackManager();
    GameCountdown gameCountdown = data.getGameCountdown();

    public Component getCurrentItemName(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (hasTask(player)) {
            Material material = taskManager.getTask(uuid).getMaterial();
            return Component.translatable(material.translationKey()).color(NamedTextColor.GOLD);
        }
        return Component.text("");
    }

    public void newTask(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        Material material = task.getMaterial();

        createTeam(player);
        player.sendMessage(Messages.COMPONENT_PREFIX.append(Component.text("Nächste Aufgabe: ").color(NamedTextColor.GRAY)
                .append(getCurrentItemName(player))));
        player.getScoreboard().getTeam(player.getName()).suffix(Component.text(" [").color(NamedTextColor.GRAY)
                .append(getCurrentItemName(player).append(Component.text("]").color(NamedTextColor.GRAY))));
        showBlockAbovePlayer(player, material);
        checkForItem(player);
    }

    public void removeTask(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        taskManager.removeTask(uuid);
        if (player.getScoreboard().getTeam(player.getName()) != null)
            player.getScoreboard().getTeam(player.getName()).setSuffix("");
        for (ArmorStand armorStand : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (armorStand.getScoreboardTags().contains(player.getUniqueId().toString())) {
                armorStand.remove();
            }
        }
        for (BossBar bossBar : player.activeBossBars()) {
            player.hideBossBar(bossBar);
        }
    }

    public void removeAllTasks() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeTask(player);
        }
    }

    public void completedTask(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        Material material = taskManager.getTask(uuid).getMaterial();
        int time = gameCountdown.getTime();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
        player.sendMessage(Messages.COMPONENT_PREFIX.append(Component.text("Aufgabe ").color(NamedTextColor.GRAY)
                .append(getCurrentItemName(player).append(Component.text(" geschafft!").color(NamedTextColor.GREEN)))));
        addPoint(player);
        taskManager.createCompletedTask(uuid, material, time);
        removeTask(player);
        newTask(player);
    }

    public void skipTask(@NotNull Player player) {
        player.sendMessage(Messages.COMPONENT_PREFIX.append(Component.text("Aufgabe ").color(NamedTextColor.GRAY)
                .append(getCurrentItemName(player).append(Component.text(" übersprungen").color(NamedTextColor.GREEN)))));
        removeTask(player);
        newTask(player);
    }

    public boolean hasTask(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        return taskManager.hasTask(uuid);
    }

    public void checkForItem(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        if (player.getInventory().contains(task.getMaterial()))
                completedTask(player);
    }

    public void createTeam(@NotNull Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.getTeam(player.getName()) == null) {
            Team players = scoreboard.registerNewTeam(player.getName());
            players.addEntry(player.getName());
        }
    }

    public void showBlockAbovePlayer(@NotNull Player player, Material material) {

        for (ArmorStand armorStand : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (armorStand.getScoreboardTags().contains(player.getUniqueId().toString())) {
                armorStand.remove();
            }
        }

        Location location = player.getLocation();
        ArmorStand armorStand = player.getWorld().spawn(location, ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomNameVisible(false);
        armorStand.addScoreboardTag(player.getUniqueId().toString());

        armorStand.getEquipment().setHelmet(ItemStack.of(material));

        player.addPassenger(armorStand);
    }

    public void addPoint(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        if (playerPointsMap.containsKey(uuid)) {
            int currentPoints = playerPointsMap.get(uuid);
            playerPointsMap.remove(uuid, currentPoints);
            int newPoints = currentPoints + 1;
            playerPointsMap.put(uuid, newPoints);
        } else {
            int configPoints = playersConfig.toFileConfiguration().getInt(uuid.toString() + ".points");
            int newPoints;
            if (configPoints != 0) {
                newPoints = configPoints + 1;
            } else {
                newPoints = 1;
            }
            playerPointsMap.put(uuid, newPoints);
        }
        savePoints();
    }

    public void savePoints() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int points = 0;

            if (playerPointsMap.containsKey(uuid)) {
                points = points + playerPointsMap.get(uuid);
                playersConfig.toFileConfiguration().set(uuid.toString() + ".points", points);
                continue;
            } else if (playersConfig.toFileConfiguration().contains(uuid.toString() + ".points")) {
                points = playersConfig.toFileConfiguration().getInt(uuid.toString() + ".points");
            }

            playersConfig.toFileConfiguration().set(uuid.toString() + ".points", points);
        }
        playersConfig.saveConfiguration();
    }

    public int getPoints(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        if (playerPointsMap.containsKey(uuid)) {
            return playerPointsMap.get(uuid);
        }

        if (playersConfig.toFileConfiguration().contains(uuid.toString() + ".points")) {
            return playersConfig.toFileConfiguration().getInt(uuid.toString() + ".points");
        }
        return 0;
    }

    public void resetPlayersConfig() {
        playerPointsMap.clear();
        playersConfig.toFileConfiguration().set("", null);
        playersConfig.saveConfiguration();
    }

    public void giveJokers() {

        int amount = instance.getConfig().getInt("joker");

        if (amount <= 0)
            amount = 5;
        ItemStack joker = new ItemStack(Material.BARRIER, amount);
        ItemMeta jokerMeta = joker.getItemMeta();
        jokerMeta.setDisplayName(ChatColor.GOLD + "Joker");
        joker.setItemMeta(jokerMeta);

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            if (playersConfig.toFileConfiguration().contains(uuid.toString() + ".jokersLeft")) {
                 amount = playersConfig.toFileConfiguration().getInt(uuid.toString() + ".jokersLeft");
            }


            backpackManager.getBackpack(uuid).getInventory().remove(Material.BARRIER);
            player.getInventory().setItem(0, joker);
            playersConfig.toFileConfiguration().set(uuid.toString() + ".jokersLeft", amount);
            playersConfig.saveConfiguration();
        }
    }

    public void giveJokerToPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        int amount = instance.getConfig().getInt("joker");

        if (amount <= 0)
            amount = 5;
        ItemStack joker = new ItemStack(Material.BARRIER, amount);
        ItemMeta jokerMeta = joker.getItemMeta();
        jokerMeta.setDisplayName(ChatColor.GOLD + "Joker");
        joker.setItemMeta(jokerMeta);
        if (playersConfig.toFileConfiguration().contains(uuid.toString() + ".jokersLeft")) {
            amount = playersConfig.toFileConfiguration().getInt(uuid.toString() + ".jokersLeft");
        }


        backpackManager.getBackpack(uuid).getInventory().remove(Material.BARRIER);
        player.getInventory().setItem(0, joker);
        playersConfig.toFileConfiguration().set(uuid.toString() + ".jokersLeft", amount);
        playersConfig.saveConfiguration();
    }
}
