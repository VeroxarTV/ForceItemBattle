package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class JokerListener implements Listener {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();
    TaskManager taskManager = data.getTaskManager();
    GameCountdown gameCountdown = data.getGameCountdown();
    Configuration playersConfig = data.getConfigs().getPlayersConfig();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (gameCountdown.isFinished() || !gameCountdown.isRunning()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.BARRIER) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                ItemStack item = player.getInventory().getItemInMainHand();

                if (item.getAmount() > 0) {
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItemInMainHand(item);
                }

                Material currentTaskItem = taskManager.getTask(player.getUniqueId()).getMaterial();
                HashMap<Integer, ItemStack> failed = player.getInventory().addItem(ItemStack.of(currentTaskItem));
                if (!failed.isEmpty()) {
                    player.getWorld().dropItem(player.getLocation(), ItemStack.of(currentTaskItem));
                    failed.clear();
                }
                logic.completedTask(player, true);
                int jokersLeft = playersConfig.toFileConfiguration().getInt(uuid + ".jokersLeft");
                int newJokersLeft = jokersLeft - 1;
                playersConfig.toFileConfiguration().set(uuid + ".jokersLeft", newJokersLeft);
                playersConfig.saveConfiguration();
                event.setCancelled(true);
            }
        }
    }
}
