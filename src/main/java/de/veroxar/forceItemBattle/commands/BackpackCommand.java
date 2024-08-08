package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.backpack.Backpack;
import de.veroxar.forceItemBattle.backpack.BackpackManager;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {

    Data data = ForceItemBattle.getData();
    BackpackManager backpackManager = data.getBackpackManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            Backpack backpack = backpackManager.getBackpack(player.getUniqueId());

            player.openInventory(backpack.getInventory());

        }
        return true;
    }
}
