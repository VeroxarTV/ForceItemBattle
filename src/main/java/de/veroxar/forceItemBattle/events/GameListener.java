package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configs;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;


public class GameListener implements Listener {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();
    TaskManager taskManager = data.getTaskManager();
    ResultInventoryManager resultInventoryManager = data.getResultInventoryManager();

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();
            if (event.getItem().getItemStack().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                logic.completedTask(player, false);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if (event.getView().getTitle().toString().equalsIgnoreCase("Geschaffte Aufgaben")) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Nächste Seite")){
                            resultInventoryManager.switchPages(event.getInventory(), true);
                            event.setCancelled(true);
                                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Vorherige Seite")) {
                                    resultInventoryManager.switchPages(event.getInventory(), false);
                                    event.setCancelled(true);
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (event.getCurrentItem() != null)
                if (event.getCurrentItem().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                    logic.completedTask(player, false);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if (event.getCursor()!= null)
                if (event.getCursor().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                    logic.completedTask(player, false);
                }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        logic.showBlockAbovePlayer(event.getPlayer(), taskManager.getTask(event.getPlayer().getUniqueId()).getMaterial());
        logic.giveJokerToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType().equals(Material.BARRIER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(drop -> drop.getType().equals(Material.BARRIER));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Geschaffte Aufgaben")) {
        }
    }
}
