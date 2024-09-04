package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamChatCommand implements CommandExecutor, TabCompleter {

    Data data = ForceItemBattle.getData();
    TeamInventoryManager teamInventoryManager = data.getTeamInventoryManager();
    TeamManager teamManager = data.getTeamManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (teamInventoryManager.isTeamMode()) {
            if (args.length >= 1) {
                if (sender instanceof Player player) {
                    if (teamManager.hasTeam(player)) {
                        StringBuilder message = new StringBuilder();
                        for (@NotNull String arg : args) {
                            message.append(arg + " ");
                        }
                        for (Player teamPlayers : teamManager.getPlayersInTeam(teamManager.getTeamName(player))) {
                            teamPlayers.sendMessage("§8[§bTeamChat§8] §r" + player.getName() + " §8>> §r" + message);
                        }
                    } else {
                        player.sendMessage(Messages.PREFIX.append(Component.text("Du bist in keinem Team!").color(NamedTextColor.RED)));
                    }
                }
            }
        } else {
            sender.sendMessage(Messages.PREFIX.append(Component.text("Der Team Modus ist deaktiviert!").color(NamedTextColor.RED)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
