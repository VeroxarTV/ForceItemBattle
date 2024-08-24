package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    TeamInventoryManager teamInventoryManager = data.getTeamInventoryManager();
    GameCountdown gameCountdown = data.getGameCountdown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (gameCountdown.isRunning()) {
                player.sendMessage(Component.text("Dieser Befehl ist nur außerhalb des Spiels nutzbar!").color(NamedTextColor.RED));
                return true;
            }

            if (player.hasPermission("forceItemBattle.menu.team")) {
                teamInventoryManager.openTeamInv(player);
                return true;
            }

            if (teamInventoryManager.teamMode) {
                teamInventoryManager.openTeamInv(player);
            } else {
                player.sendMessage(Messages.PREFIX.append(Component.text("Der Teammodus ist deaktiviert").color(NamedTextColor.GRAY)));
            }
        }
        return true;
    }
}
