package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EndCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length >= 1) {
            sendUsage(sender);
            return true;
        }


        if (gameCountdown.isRunning() && gameCountdown.getTime() > 10) {
            gameCountdown.setTime(3);
            sender.sendMessage(Messages.PREFIX.append(Component.text("The game will end in 3 seconds").color(NamedTextColor.GRAY)));
        } else {
            sender.sendMessage(Messages.PREFIX.append(Component.text("The game is not running or is about to end!").color(NamedTextColor.RED)));
        }

        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Please use /end to end the game early!").color(NamedTextColor.GRAY)));
    }

}
