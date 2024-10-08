package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkipCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 0 || args.length == 1 & args[0].equalsIgnoreCase(player.getName())) {
                if (logic.hasTask(player)) {
                    logic.skipTask(player);
                    player.sendMessage(Messages.PREFIX.append(Component.text("Your task has been skipped!").color(NamedTextColor.GRAY)));
                } else {
                    player.sendMessage(Messages.PREFIX.append(Component.text("You have no task!").color(NamedTextColor.RED)));
                }
            } else if (args.length == 1) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (target.getName().equalsIgnoreCase(args[0])) {
                        Component targetName = Component.text(target.getName()).color(NamedTextColor.GOLD);
                        Component playerName = Component.text(player.getName()).color(NamedTextColor.GOLD);
                        if (logic.hasTask(target)) {
                            logic.skipTask(target);
                            player.sendMessage(Messages.PREFIX.append(Component.text("The player's task: ").color(NamedTextColor.GRAY).append(targetName).append(Component.text("was skipped!").color(NamedTextColor.GRAY))));
                            target.sendMessage(Messages.PREFIX.append(Component.text("Your task was created by: ").color(NamedTextColor.GRAY).append(playerName).append(Component.text("skipped!").color(NamedTextColor.GRAY))));

                        } else
                            player.sendMessage(Messages.PREFIX.append(Component.text("The player: ").color(NamedTextColor.RED).append(targetName).append(Component.text("has no task!").color(NamedTextColor.RED))));
                    } else
                        player.sendMessage(Messages.PREFIX.append(Component.text("The player: ").color(NamedTextColor.RED).append(Component.text(args[0]).color(NamedTextColor.GOLD).append(Component.text(" is not online!").color(NamedTextColor.RED)))));
                }
            } else {
                sendUsage(sender);
            }
        } else {
            sender.sendMessage(Messages.NOPLAYER);
            return true;
        }
        return false;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /skip <Spieler> um die aktuelle Aufgabe zu überspringen!").color(NamedTextColor.GRAY)));

    }

}
