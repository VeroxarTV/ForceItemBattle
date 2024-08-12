package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ResultCommand implements CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(ResultCommand.class);
    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    Logic logic = data.getLogic();
    ResultInventoryManager resultInventoryManager = data.getResultInventoryManager();
    public static int currentIndex = -1;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NOPLAYER);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (gameCountdown.isFinished()) {

                // Hole die Spielerplatzierungen
                List<Map.Entry<UUID, Integer>> playerPlacements = logic.getPlayerPlacement();

                // Sortiere die Spieler nach Punkten in absteigender Reihenfolge
                List<Map.Entry<UUID, Integer>> sortedPlacements = playerPlacements.stream()
                        .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toList());

                // Erhöhe den Index und hole die nächste UUID
                currentIndex++;
                if (currentIndex >= sortedPlacements.size()) {
                    player.sendMessage(Messages.PREFIX + "Keine weiteren Spielerplätze verfügbar.");
                    return true;
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
                    player.sendMessage(Messages.PREFIX + "Der Spieler mit der UUID " + currentPlayerUUID + " ist derzeit nicht online.");
                }


            } else if (gameCountdown.isRunning()){
                sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Das Spiel ist noch nicht beendet!");
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Das Spiel hat noch nicht begonnen!");
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("no")) {
                player.openInventory(resultInventoryManager.createResultInvWithoutAnimation(target));
            }
        } else {
            sendUsage(sender);
            return true;
        }

        return false;
    }
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Bitte benutze: " + ChatColor.GOLD + "/result" +
                ChatColor.GRAY + " um das Ergebnis anzuzeigen!");
    }
}
