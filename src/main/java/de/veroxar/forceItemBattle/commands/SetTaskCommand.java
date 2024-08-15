package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetTaskCommand implements CommandExecutor, TabCompleter {

    Data data = ForceItemBattle.getData();
    Logic logic = data.getLogic();
    GameCountdown gameCountdown = data.getGameCountdown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (gameCountdown.isRunning()) {
                if (args.length == 1) {
                    Material material;
                    try {
                        material = Material.valueOf(args[0].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(Messages.PREFIX.append(Component.text("Das ist kein gültiges Item").color(NamedTextColor.RED)));
                        return true;
                    }
                    logic.setTask(player, material);
                    player.sendMessage(Messages.PREFIX.append(Component.text("Die Aufgabe wurde gesetzt!!").color(NamedTextColor.GRAY)));
                } else {
                    sendUsage(sender);
                }
            } else {
                player.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel läuft nicht").color(NamedTextColor.RED)));
            }


        } else {
            sender.sendMessage(Messages.NOPLAYER);
        }
        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /task <Item> um das Item als Aufgabe zu setzen!").color(NamedTextColor.GRAY)));
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> subcommands = new ArrayList<>();

        if (args.length == 1) {
            for (Material material : Material.values()) {
                if (material.name().contains(args[0].toUpperCase()))
                    subcommands.add(material.name().toLowerCase());
            }
        }
        return subcommands;
    }
}
