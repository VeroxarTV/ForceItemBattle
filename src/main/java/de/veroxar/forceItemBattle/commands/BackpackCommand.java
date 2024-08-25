package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.backpack.Backpack;
import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.backpack.TeamBackpack;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    BackpackManager backpackManager = data.getBackpackManager();
    TeamManager teamManager = data.getTeamManager();
    TeamInventoryManager inventoryManager = data.getTeamInventoryManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            if (inventoryManager.isTeamMode()) {
                if (teamManager.hasTeam(player)) {
                    TeamBackpack teamBackpack = backpackManager.getTeamBackpack(teamManager.getTeamName(player));

                    player.openInventory(teamBackpack.getInventory());
                } else {
                    player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            " §7Der Teammodus ist §aaktiv. §cDu bist in keinem Team!")));
                }
            } else {
                Backpack backpack = backpackManager.getBackpack(player.getUniqueId());
                player.openInventory(backpack.getInventory());
            }
        }
        return true;
    }
}
