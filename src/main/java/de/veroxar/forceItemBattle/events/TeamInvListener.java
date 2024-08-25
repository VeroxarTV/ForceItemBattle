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

public class TeamInvListener implements Listener {

    Data data = ForceItemBattle.getData();
    TeamInventoryManager inventoryManager = data.getTeamInventoryManager();
    TeamManager teamManager = data.getTeamManager();
    TablistManager tablistManager = data.getTablistManager();

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof TeamInventoryManager) {
            Player player = (Player) event.getView().getPlayer();
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cTeams [AUS]")) {
                            inventoryManager.setTeamMode(true);
                            inventoryManager.updateInv(event.getInventory());
                            player.closeInventory();
                            player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Teams wurde §aaktiviert!")));
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aTeams [AN]")) {
                            inventoryManager.setTeamMode(false);
                            inventoryManager.updateInv(event.getInventory());
                            player.closeInventory();
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (teamManager.hasTeam(onlinePlayer))
                                    teamManager.quitTeam(onlinePlayer, teamManager.getTeamName(onlinePlayer));
                            }
                            tablistManager.setAllPlayerTeams();
                            player.playSound(player, Sound.BLOCK_PISTON_CONTRACT, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Teams wurde §cdeaktiviert!")));
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§1Blau") && event.getClick().isLeftClick() && inventoryManager.isTeamMode()) {
                            player.closeInventory();
                            if (!(teamManager.joinTeam(player, "BLUE"))) {
                                player.playSound(player, Sound.ENTITY_CAT_HISS, 1, 1);
                                return;
                            }
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Du bist nun in Team: §1Blau")));
                            tablistManager.setAllPlayerTeams();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRot") && event.getClick().isLeftClick() && inventoryManager.isTeamMode()) {
                            player.closeInventory();
                            if (!(teamManager.joinTeam(player, "RED"))) {
                                player.playSound(player, Sound.ENTITY_CAT_HISS, 1, 1);
                                return;
                            }

                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Du bist nun in Team: §cRot")));
                            tablistManager.setAllPlayerTeams();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eGelb") && event.getClick().isLeftClick() && inventoryManager.isTeamMode()) {
                            player.closeInventory();
                            if (!(teamManager.joinTeam(player, "YELLOW"))) {
                                player.playSound(player, Sound.ENTITY_CAT_HISS, 1, 1);
                                return;
                            }
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Du bist nun in Team: §eGelb")));
                            tablistManager.setAllPlayerTeams();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aGrün") && event.getClick().isLeftClick() && inventoryManager.isTeamMode()) {
                            player.closeInventory();
                            if (!(teamManager.joinTeam(player, "GREEN"))) {
                                player.playSound(player, Sound.ENTITY_CAT_HISS, 1, 1);
                                return;
                            }
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize("§7Du bist nun in Team: §aGrün")));
                            tablistManager.setAllPlayerTeams();
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
