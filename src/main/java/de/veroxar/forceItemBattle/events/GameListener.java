package de.veroxar.forceItemBattle.events;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;
import java.util.UUID;


public class GameListener implements Listener {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();
    TaskManager taskManager = data.getTaskManager();
    ResultInventoryManager resultInventoryManager = data.getResultInventoryManager();
    GameCountdown gameCountdown = data.getGameCountdown();
    TeamManager teamManager = data.getTeamManager();
    TeamInventoryManager teamInventoryManager = data.getTeamInventoryManager();

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!gameCountdown.isRunning() || gameCountdown.isFinished()) {
            return;
        }
        if (event.getEntity() instanceof Player player) {
            if (teamInventoryManager.isTeamMode()) {
                String teamName = teamManager.getTeamName(player);
                if (event.getItem().getItemStack().getType().equals(taskManager.getTeamTask(teamName).getMaterial())) {
                    logic.completedTeamTask(teamName, false);
                }
                return;
            }
            UUID uuid = player.getUniqueId();
            if (event.getItem().getItemStack().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                logic.completedTask(player, false);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            UUID uuid = player.getUniqueId();
            if (event.getView().title().equals(Component.text("Geschaffte Aufgaben"))) {
                if (!resultInventoryManager.isInAnimation()) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().hasItemMeta()) {
                            if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                                if (Objects.equals(event.getCurrentItem().getItemMeta().displayName(), Component.text("Nächste Seite").color(NamedTextColor.GREEN))) {
                                    if (teamInventoryManager.isTeamMode()) {
                                        resultInventoryManager.switchPagesTeamMode(event.getInventory(), true);
                                        event.setCancelled(true);
                                        return;
                                    }
                                    resultInventoryManager.switchPages(event.getInventory(), true);
                                    event.setCancelled(true);
                                } else if (Objects.equals(event.getCurrentItem().getItemMeta().displayName(), Component.text("Vorherige Seite").color(NamedTextColor.RED))) {
                                    if (teamInventoryManager.isTeamMode()) {
                                        resultInventoryManager.switchPagesTeamMode(event.getInventory(), false);
                                        event.setCancelled(true);
                                        return;
                                    }
                                    resultInventoryManager.switchPages(event.getInventory(), false);
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (event.getCurrentItem() != null) {
                if (!gameCountdown.isRunning() || gameCountdown.isFinished()) {
                    event.setCancelled(true);
                    return;
                }
                if (teamInventoryManager.isTeamMode()) {
                    String teamName = teamManager.getTeamName(player);
                    if (event.getCurrentItem().getType().equals(taskManager.getTeamTask(teamName).getMaterial())) {
                        logic.completedTeamTask(teamName, false);
                    }
                    return;
                }
                if (event.getCurrentItem().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                    logic.completedTask(player, false);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!gameCountdown.isRunning() || gameCountdown.isFinished()) {
            return;
        }
        if (event.getWhoClicked() instanceof Player player) {
            if (teamInventoryManager.isTeamMode()) {
                String teamName = teamManager.getTeamName(player);
                if (event.getCursor()!= null)
                    if (event.getCursor().getType().equals(taskManager.getTeamTask(teamName).getMaterial())) {
                        logic.completedTeamTask(teamName, false);
                    }
                return;
            }

            UUID uuid = player.getUniqueId();

            if (event.getCursor()!= null)
                if (event.getCursor().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                    logic.completedTask(player, false);
                }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!gameCountdown.isRunning())
            return;

        if (teamInventoryManager.isTeamMode()) {
            logic.showBlockAbovePlayer(event.getPlayer(), taskManager.getTeamTask(teamManager.getTeamName(event.getPlayer())).getMaterial());
            logic.giveJokerToTeam(event.getPlayer());
            return;
        }
        logic.giveJokerToPlayer(event.getPlayer());
        logic.showBlockAbovePlayer(event.getPlayer(), taskManager.getTask(event.getPlayer().getUniqueId()).getMaterial());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);

        if (event.getItemDrop().getItemStack().getType().equals(Material.BARRIER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(drop -> drop.getType().equals(Material.BARRIER));
    }

    @EventHandler
    public void onEntityAddToWorld(EntityAddToWorldEvent event) {
        if (gameCountdown.isRunning()) {
            if (event.getEntity() instanceof  Player player && logic.hasTask(player)) {
                int delay = 1;
                Bukkit.getScheduler().runTaskLater(data.getInstance(), () -> logic.showBlockAbovePlayer(player, taskManager.getTask(player.getUniqueId()).getMaterial()), delay);
            }
        }
    }


    @EventHandler
    public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        if (gameCountdown.isRunning()) {
            if (event.getEntity() instanceof  Player player && logic.hasTask(player))
                logic.removeBlockAbovePlayer(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (gameCountdown.isFinished())
            event.setCancelled(true);
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        if (teamInventoryManager.isTeamMode()) {
            Player player = event.getPlayer();
            if (teamManager.hasTeam(player)) {
                String teamName = "null";
                String playerName = player.getName();
                switch (teamManager.getTeamName(player).toLowerCase()) {
                    case "blue" -> teamName = "§9Blau";
                    case "red" -> teamName = "§cRot";
                    case "yellow" -> teamName = "§eGelb";
                    case "green" -> teamName = "§aGrün";
                }
                switch (teamManager.getTeamName(player).toLowerCase()) {
                    case "blue" -> playerName = "§9" + playerName;
                    case "red" -> playerName = "§c" + playerName;
                    case "yellow" -> playerName = "§e" + playerName;
                    case "green" -> playerName = "§a" + playerName;
                }
                Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize("§8[" + teamName + "§8] " + playerName + " §8>> ");
                Component message = event.message();
                Bukkit.getConsoleSender().sendMessage(prefix.append(message));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(prefix.append(message));
                }
                event.setCancelled(true);
            }
        }
    }

}
