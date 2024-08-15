package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    Logic logic = data.getLogic();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            if (!(gameCountdown.isRunning())) {

                gameCountdown.setRunning(true);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel wurde gestartet!").color(NamedTextColor.GREEN)));
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.getWorld().setTime(0);
                    player.getWorld().setDifficulty(Difficulty.EASY);
                    player.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                    player.getWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, true);
                    logic.giveJokers();
                    logic.newTask(player);
                }

            } else {
                sender.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel wurde bereits gestartet!").color(NamedTextColor.RED)));
                return true;
            }

        } else {
            sendUsage(sender);
            return true;
        }

        return false;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /start um das Spiel zu beginnen!").color(NamedTextColor.GRAY)));
    }
}
