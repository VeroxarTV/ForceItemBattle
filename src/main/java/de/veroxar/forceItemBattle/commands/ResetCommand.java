package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    JavaPlugin instance = data.getInstance();
    Logic logic = data.getLogic();
    BackpackManager backpackManager = data.getBackpackManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            instance.reloadConfig();
            logic.resetPlayersConfig();
            backpackManager.clear();
            if (instance.getConfig().getInt(".time") != 0) {
                gameCountdown.setTime(instance.getConfig().getInt(".time"));
            } else {
                gameCountdown.setTime(10800);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                logic.removeTask(player);
                player.getInventory().clear();
            }

            if (gameCountdown.isRunning()) {
                gameCountdown.setRunning(false);
                gameCountdown.setFinished(false);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Das Spiel wurde zurückgesetzt!");
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Das Spiel wurde zurückgesetzt!");
            }
        } else {
            sendUsage(sender);
        }
        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Bitte benutze: " + ChatColor.GOLD + "/reset" +
                ChatColor.GRAY + " um das Spiel zurückzusetzen!");
    }
}
