package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0 || args.length == 1 & args[0].equalsIgnoreCase(player.getName())) {
                if (logic.hasTask(player)) {
                    logic.skipTask(player);
                    player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Deine Aufgabe wurde übersprungen!");
                } else {
                    player.sendMessage(Messages.PREFIX + ChatColor.RED + "Du hast keine Aufgabe!");
                }
            } else if (args.length == 1) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (target.getName().equalsIgnoreCase(args[0])) {
                        if (logic.hasTask(target)) {
                            logic.skipTask(target);
                            player.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Die Aufgabe von dem Spieler wurde übersprungen!");
                            target.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Deine Aufgabe wurde übersprungen!");
                        } else {
                            player.sendMessage(Messages.PREFIX + ChatColor.RED + "Der Spieler hat keine Aufgabe!");
                        }
                    }
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
        sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Bitte benutze: " + ChatColor.GOLD + "/skip <Spieler>" +
                ChatColor.GRAY + " um eine Aufgabe zu überspringen!");
    }

}
