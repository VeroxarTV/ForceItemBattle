package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class GameListener implements Listener {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();
    TaskManager taskManager = data.getTaskManager();

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();
            if (event.getItem().getItemStack().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                logic.completedTask(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if (event.getCurrentItem() != null)
                if (event.getCurrentItem().getType().equals(taskManager.getTask(uuid).getMaterial())) {
                    logic.completedTask(player);
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
                    logic.completedTask(player);
                }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        logic.showBlockAbovePlayer(event.getPlayer(), taskManager.getTask(event.getPlayer().getUniqueId()).getMaterial());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().equals(ItemStack.of(Material.BARRIER))) {
            event.setCancelled(true);
        }

    }
}
