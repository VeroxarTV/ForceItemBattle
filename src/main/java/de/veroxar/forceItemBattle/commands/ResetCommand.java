package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.tasks.CompletedTask;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ResetCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    JavaPlugin instance = data.getInstance();
    Logic logic = data.getLogic();
    BackpackManager backpackManager = data.getBackpackManager();
    TaskManager taskManager = data.getTaskManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            instance.reloadConfig();
            logic.resetPlayersConfig();
            backpackManager.clear();
            ResultCommand.currentIndex = -1;
            if (instance.getConfig().getInt(".time") != 0) {
                gameCountdown.setTime(instance.getConfig().getInt(".time"));
            } else {
                gameCountdown.setTime(10800);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                List<CompletedTask> list = taskManager.getCompletedTaskList(uuid);
                list.clear();
                taskManager.setCompletedTaskList(uuid, list);
                taskManager.saveCompletedTasks();
                logic.removeTask(player);
                player.getInventory().clear();
            }

            if (gameCountdown.isRunning()) {
                gameCountdown.setRunning(false);
                gameCountdown.setFinished(false);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Messages.PREFIX.append(Component.text("The game has been reset!").color(NamedTextColor.GRAY)));
                }
            } else {
                sender.sendMessage(Messages.PREFIX.append(Component.text("The game has been reset!").color(NamedTextColor.GRAY)));
            }
        } else {
            sendUsage(sender);
        }
        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Please use /reset to reset the game").color(NamedTextColor.GRAY)));
    }
}
