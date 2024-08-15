package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class ResultCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    Logic logic = data.getLogic();
    ResultInventoryManager resultInventoryManager = data.getResultInventoryManager();
    public static int currentIndex = -1;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.NOPLAYER);
            return true;
        }

        if (args.length == 0) {
            if (gameCountdown.isFinished()) {

                // Hole die Spielerplatzierungen
                List<Map.Entry<UUID, Integer>> playerPlacements = logic.getPlayerPlacement();

                // Sortiere die Spieler nach Punkten in absteigender Reihenfolge
                List<Map.Entry<UUID, Integer>> sortedPlacements = playerPlacements.stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

                // Erhöhe den Index und hole die nächste UUID
                currentIndex++;
                if (currentIndex >= sortedPlacements.size()) {
                    player.sendMessage(Messages.PREFIX.append(Component.text("No further player places available.").color(NamedTextColor.GRAY)));
                }

                // Hole die UUID der aktuellen Position
                UUID currentPlayerUUID = sortedPlacements.get(sortedPlacements.size() - 1 - currentIndex).getKey();
                Player currentPlayer = Bukkit.getPlayer(currentPlayerUUID);

                int pos = sortedPlacements.size() - currentIndex;

                if (currentPlayer != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.openInventory(resultInventoryManager.createResultInv(currentPlayer,pos));
                    }
                } else {
                    player.sendMessage(Messages.PREFIX.append(Component.text("The player with the UUID " + currentPlayerUUID + " is currently not online.")));
                }


            } else if (gameCountdown.isRunning()){
                sender.sendMessage(Messages.PREFIX.append(Component.text("The game is not over yet!").color(NamedTextColor.RED)));
            } else {
                sender.sendMessage(Messages.PREFIX.append(Component.text("The game hasn't started yet!").color(NamedTextColor.RED)));
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("no")) {
                assert target != null;
                player.openInventory(resultInventoryManager.createResultInvWithoutAnimation(target));
            }
        } else {
            sendUsage(sender);
        }

        return true;
    }
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /result um das Endergebnis anzuzeigen!").color(NamedTextColor.GRAY)));

    }
}
