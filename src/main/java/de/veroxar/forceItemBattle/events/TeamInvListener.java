package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.team.TeamManager;
import de.veroxar.forceItemBattle.util.TablistManager;
import de.veroxar.forceItemBattle.util.TeamInventoryManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class TeamInvListener implements Listener {

    Data data = ForceItemBattle.getData();
    TeamInventoryManager inventoryManager = data.getTeamInventoryManager();
    TeamManager teamManager = data.getTeamManager();
    TablistManager tablistManager = data.getTablistManager();

    private static final Map<String, String> TEAM_DISPLAY_TO_NAME = Map.of(
            "§1Blue", "BLUE",
            "§cRed", "RED",
            "§eYellow", "YELLOW",
            "§aGreen", "GREEN"
    );

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof TeamInventoryManager) {
            Player player = (Player) event.getView().getPlayer();
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cTeams [OFF]")) {
                            inventoryManager.setTeamMode(true);
                            inventoryManager.updateInv(event.getInventory());
                            player.closeInventory();
                            player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Teams was §aactivated!")));
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aTeams [ON]")) {
                            inventoryManager.setTeamMode(false);
                            inventoryManager.updateInv(event.getInventory());
                            player.closeInventory();
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (teamManager.hasTeam(onlinePlayer))
                                    teamManager.quitTeam(onlinePlayer, teamManager.getTeamName(onlinePlayer));
                            }
                            tablistManager.setAllPlayerTeams();
                            player.playSound(player, Sound.BLOCK_PISTON_CONTRACT, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Teams has been §cdeactivated! ")));
                        } else if (event.getClick().isLeftClick() && inventoryManager.isTeamMode()) {
                            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                            handleTeamJoin(player, displayName);
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    private void handleTeamJoin(Player player, String displayName) {
        String teamName = TEAM_DISPLAY_TO_NAME.get(displayName);
        if (teamName == null) {
            return;
        }

        player.closeInventory();
        if (!teamManager.joinTeam(player, teamName)) {
            player.playSound(player, Sound.ENTITY_CAT_HISS, 1, 1);
            return;
        }

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.sendMessage(Messages.PREFIX.append(
                LegacyComponentSerializer.legacySection().deserialize("§7You are now in team: " + displayName)
        ));
        tablistManager.setAllPlayerTeams();
    }
}
