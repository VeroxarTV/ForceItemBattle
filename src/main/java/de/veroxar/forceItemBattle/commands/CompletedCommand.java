package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.tasks.CompletedTask;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CompletedCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    TaskManager taskManager = data.getTaskManager();
    GameCountdown gameCountdown = data .getGameCountdown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if ( sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            for (CompletedTask completedTask : taskManager.getCompletedTaskList(uuid)) {
                Component playerName = Component.text(player.getName());
                Component materialName = Component.translatable(completedTask.getMaterial().translationKey());
                Component time = Component.text(gameCountdown.formatTime(completedTask.getSeconds()));
                Component usedJoker = Component.text(completedTask.isUsedJoker());
                player.sendMessage(Component.text("Spieler: ").append(playerName).append(Component.text("\nBlock/Item: ")
                        .append(materialName).append(Component.text("\nZeit: ")
                                .append(time).append(Component.text("\nJoker: ").append(usedJoker)))));

            }
            return true;
        } else {
            sender.sendMessage(Messages.NOPLAYER);
            return true;
        }
    }
}
