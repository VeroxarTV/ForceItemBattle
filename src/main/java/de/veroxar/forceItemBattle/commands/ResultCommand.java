package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.ResultInventoryManager;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    TeamInventoryManager inventoryManager = data.getTeamInventoryManager();
    TeamManager teamManager = data.getTeamManager();
    public static int currentIndex = -1;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.NOPLAYER);
            return true;
        }

        if (args.length == 0 && player.hasPermission("forceItemBattle.commands.result")) {
            if (gameCountdown.isFinished()) {

                if (inventoryManager.isTeamMode()) {

                    // Hole die Teamplatzierungen
                    List<Map.Entry<String, Integer>> teamPlacements = logic.getTeamPlacement();

                    // Sortiere die Teams nach Punkten in absteigender Reihenfolge
                    List<Map.Entry<String, Integer>> sortedPlacements = teamPlacements.stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .toList();

                    // Erhöhe den Index und hole das nächste Team
                    currentIndex++;
                    if (currentIndex >= sortedPlacements.size()) {
                        player.sendMessage(Messages.PREFIX.append(Component.text("Keine weiteren Teamplätze verfügbar.").color(NamedTextColor.GRAY)));
                        return true;
                    }

                    // Hole das aktuelle team der aktuellen Position
                    String currentTeam = sortedPlacements.get(sortedPlacements.size() - 1 - currentIndex).getKey();

                    int pos = sortedPlacements.size() - currentIndex;

                    if (teamManager.getActiveTeams().contains(currentTeam)) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.openInventory(resultInventoryManager.createResultInv(currentTeam,pos));
                        }
                    } else {
                        player.sendMessage(Messages.PREFIX + "Das Team " + currentTeam + " existiert nicht.");
                    }

                    return true;
                }

                // Hole die Spielerplatzierungen
                List<Map.Entry<UUID, Integer>> playerPlacements = logic.getPlayerPlacement();

                // Sortiere die Spieler nach Punkten in absteigender Reihenfolge
                List<Map.Entry<UUID, Integer>> sortedPlacements = playerPlacements.stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

                // Erhöhe den Index und hole die nächste UUID
                currentIndex++;
                if (currentIndex >= sortedPlacements.size()) {
                    player.sendMessage(Messages.PREFIX.append(Component.text("Keine weiteren Spielerplätze verfügbar.").color(NamedTextColor.GRAY)));
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
                sender.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel ist noch nicht beendet!").color(NamedTextColor.RED)));
            } else {
                sender.sendMessage(Messages.PREFIX.append(Component.text("Das Spiel hat noch nicht begonnen!").color(NamedTextColor.RED)));
            }
        } else if (args.length == 2) {
            if (inventoryManager.isTeamMode()) {
                String teamName = args[0];
                player.openInventory(resultInventoryManager.createResultInvWithoutAnimation(teamName));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("no")) {
                assert target != null;
                player.openInventory(resultInventoryManager.createResultInvWithoutAnimation(target));
            }
        } else {
            if (player.hasPermission("forceItemBattle.commands.result")) {
                sendUsage(sender);
            } else {
                player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                        "§cNur ein Admin kann diesen Befehl ausführen!")));
            }
            return true;
        }

        return true;
    }
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(Component.text("Bitte nutze /result um das Endergebnis anzuzeigen!").color(NamedTextColor.GRAY)));

    }
}
