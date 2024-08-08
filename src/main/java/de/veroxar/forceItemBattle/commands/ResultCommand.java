package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ResultCommand implements CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(ResultCommand.class);
    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    Logic logic = data.getLogic();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            if (gameCountdown.isFinished()) {
                // Map zum Speichern der Spieler und ihrer Punkte
                Map<UUID, Integer> playerPoints = new HashMap<>();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    int points = logic.getPoints(player);
                    if (points > 0) {
                        playerPoints.put(player.getUniqueId(), points);
                    }
                }

                if (playerPoints.isEmpty()) {
                    Bukkit.broadcastMessage(Messages.PREFIX + ChatColor.GRAY + "Kein Spieler hat mehr als 0 Punkte erreicht!");
                    return true;
                }

                // Sortiere die Spieler nach Punkten (aufsteigend)
                List<Map.Entry<UUID, Integer>> sortedEntries = new ArrayList<>(playerPoints.entrySet());
                sortedEntries.sort(Map.Entry.<UUID, Integer>comparingByValue().reversed());

                // Ausgabe im Chat, von der letzten bis zur ersten Position
                int position = 1;
                for (Map.Entry<UUID, Integer> entry : sortedEntries) {
                    UUID playerUUID = entry.getKey();
                    int points = entry.getValue();

                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        String message = ChatColor.GOLD.toString() + position + ". " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " - " + ChatColor.GREEN + points + " Punkte";
                        Bukkit.broadcastMessage(Messages.PREFIX + message);
                        position++;
                    }
                }
            } else if (gameCountdown.isRunning()){
                sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Das Spiel ist noch nicht beendet!");
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Das Spiel hat noch nicht begonnen!");
            }
        } else {
            sendUsage(sender);
        }

        return false;
    }
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Bitte benutze: " + ChatColor.GOLD + "/result" +
                ChatColor.GRAY + " um das Ergebnis anzuzeigen!");
    }
}
