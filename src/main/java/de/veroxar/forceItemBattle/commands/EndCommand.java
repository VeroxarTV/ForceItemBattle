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
            sender.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel wird in 3 Sekunden beendet").color(NamedTextColor.GRAY)));
        } else {
            sender.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel läuft nicht oder ist gleich beendet!").color(NamedTextColor.RED)));
        }

        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /end um das Spiel vorzeitig zu beenden!").color(NamedTextColor.GRAY)));
    }

}
