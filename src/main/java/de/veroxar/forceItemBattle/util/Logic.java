package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.tasks.Task;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Logic {

    Data data = ForceItemBattle.getData();
    TaskManager taskManager = data.getTaskManager();
    Configuration playersConfig = data.getConfigs().getPlayersConfig();
    Map<UUID, Integer> playerPointsMap = new HashMap<>();
    JavaPlugin instance = data.getInstance();

    public void newTask(Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        Material material = task.getMaterial();
        String materialName = ItemStack.of(material).getI18NDisplayName();
        createTeam(player);
        BossBar bossBar = BossBar.bossBar(Component.text(materialName), 1, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Nächste Aufgabe: " + ChatColor.GOLD + materialName);
        player.showBossBar(bossBar);
        player.getScoreboard().getTeam(player.getName()).setSuffix(ChatColor.GRAY + " [" + ChatColor.GOLD + materialName + ChatColor.GRAY + "]");
        showBlockAbovePlayer(player, material);
        checkForItem(player);
    }

    public void removeTask(Player player) {
        UUID uuid = player.getUniqueId();
        taskManager.removeTask(uuid);
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

    public void completedTask(Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        Material material = task.getMaterial();
        String materialName = ItemStack.of(material).getI18NDisplayName();
        removeTask(player);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
        player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Aufgabe " + ChatColor.GOLD + materialName + ChatColor.GREEN + " geschafft!");
        addPoint(player);
        newTask(player);
    }

    public void skipTask(Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        Material material = task.getMaterial();
        String materialName = ItemStack.of(material).getI18NDisplayName();
        removeTask(player);
        player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Aufgabe " + ChatColor.GOLD + materialName + ChatColor.GREEN + " übersprungen!");
        newTask(player);
    }

    public boolean hasTask(Player player) {
        UUID uuid = player.getUniqueId();
        return taskManager.hasTask(uuid);
    }

    public void checkForItem(Player player) {
        UUID uuid = player.getUniqueId();
        Task task = taskManager.getTask(uuid);
        if (player.getInventory().contains(task.getMaterial()))
                completedTask(player);
    }

    public void createTeam(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.getTeam(player.getName()) == null) {
            Team players = scoreboard.registerNewTeam(player.getName());
            players.addEntry(player.getName());
        }
    }

    public void showBlockAbovePlayer(Player player, Material material) {
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

    public void addPoint(Player player) {
        UUID uuid = player.getUniqueId();

        if (playerPointsMap.containsKey(uuid)) {
            int currentPoints = playerPointsMap.get(uuid);
            playerPointsMap.remove(uuid, currentPoints);
            int newPoints = currentPoints + 1;
            playerPointsMap.put(uuid, newPoints);
        } else {
            int configPoints = playersConfig.toFileConfiguration().getInt("points." + uuid.toString());
            int newPoints;
            if (configPoints != 0) {
                newPoints = configPoints + 1;
            } else {
                newPoints = 1;
            }
            playerPointsMap.put(uuid, newPoints);
        }
    }

    public void savePoints() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int points = 0;

            if (playerPointsMap.containsKey(uuid)) {
                points = points + playerPointsMap.get(uuid);
                playersConfig.toFileConfiguration().set("points." + uuid.toString(), points);
                continue;
            } else if (playersConfig.toFileConfiguration().contains("points." + uuid.toString())) {
                points = playersConfig.toFileConfiguration().getInt("points." + uuid.toString());
            }

            playersConfig.toFileConfiguration().set("points." + uuid.toString(), points);
        }
        playersConfig.saveConfiguration();
    }

    public int getPoints(Player player) {
        UUID uuid = player.getUniqueId();

        if (playerPointsMap.containsKey(uuid)) {
            return playerPointsMap.get(uuid);
        }

        if (playersConfig.toFileConfiguration().contains("points." + uuid.toString())) {
            return playersConfig.toFileConfiguration().getInt("points." + uuid.toString());
        }
        return 0;
    }

    public void resetPoints() {
        playerPointsMap.clear();
        playersConfig.toFileConfiguration().set("points", null);
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
            player.getInventory().setItem(0, joker);
        }
    }
}
