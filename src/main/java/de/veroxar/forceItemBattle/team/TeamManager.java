package de.veroxar.forceItemBattle.team;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamManager {

    private final Data data = ForceItemBattle.getData();
    private final Configuration teamsConfig = data.getConfigs().getTeamsConfig();
    private final Configuration playersConfig = data.getConfigs().getPlayersConfig();
    private final FileConfiguration teamsFileConfig = teamsConfig.toFileConfiguration();
    private final FileConfiguration playersFileConfig = playersConfig.toFileConfiguration();
    private final InventoryHolderBlue inventoryHolderBlue = new InventoryHolderBlue();
    private final InventoryHolderRed inventoryHolderRed = new InventoryHolderRed();
    private final InventoryHolderYellow inventoryHolderYellow = new InventoryHolderYellow();
    private final InventoryHolderGreen inventoryHolderGreen = new InventoryHolderGreen();
    private UUID uuid;
    private final ArrayList<String> activeTeams = new ArrayList<>();

    public void createTeam(String teamName, String prefix, NamedTextColor color, boolean allowFriendlyFire) {
        if (!isValidTeam(teamName))  {
            teamsFileConfig.set(teamName + ".prefix", prefix);
            teamsFileConfig.set(teamName + ".color", color.value());
            teamsFileConfig.set(teamName + ".allowFriendlyFire", allowFriendlyFire);
            teamsConfig.saveConfiguration();
        }
        if (!activeTeams.contains(teamName))
            activeTeams.add(teamName);
    }
    public boolean joinTeam (Player player, String teamName) {
       uuid = player.getUniqueId();
       Component teamNameComponent = Component.text(teamName.toLowerCase()).color(NamedTextColor.GRAY);
       if (isValidTeam(teamName)) {
           if (!isInTeam(player, teamName)) {
               playersFileConfig.set(uuid + ".team", teamName);
               playersConfig.saveConfiguration();
               return true;
           } else {
               teamNameComponent = switch (teamName.toLowerCase()) {
                   case "blue" -> Component.text("Blau").color(NamedTextColor.BLUE);
                   case "red" -> Component.text("Rot").color(NamedTextColor.RED);
                   case "yellow" -> Component.text("Gelb").color(NamedTextColor.YELLOW);
                   case "green" -> Component.text("Grün").color(NamedTextColor.GREEN);
                   default -> teamNameComponent;
               };
               player.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacyAmpersand().deserialize("§cDu bist bereits in Team§7: "))
                       .append(teamNameComponent));
               return false;
           }
       } else {
           player.sendMessage(Messages.PREFIX.append(Component.text("Unbekanntes Team: ").color(NamedTextColor.GRAY)
                   .append(teamNameComponent)));
           return false;
       }
    }

    public void quitTeam (Player player, String teamName) {
        uuid = player.getUniqueId();
        if (teamName == null)
            return;
        Component teamNameComponent = Component.text(teamName).color(NamedTextColor.GOLD);
        if (isValidTeam(teamName)) {
            if (isInTeam(player, teamName)) {
                playersFileConfig.set(uuid + ".team", null);
                playersConfig.saveConfiguration();
            } else  {
                player.sendMessage(Messages.PREFIX.append(Component.text("Spieler nicht in Team: ").color(NamedTextColor.RED)
                        .append(teamNameComponent)));
            }
        } else {
            player.sendMessage(Messages.PREFIX.append(Component.text("Unbekanntes Team: ").color(NamedTextColor.RED)
                    .append(teamNameComponent)));
        }
    }

    public boolean isInTeam(OfflinePlayer player, String teamName) {
        uuid = player.getUniqueId();
        if (playersFileConfig.getString(uuid + ".team") == null) {
            return false;
        }
        return Objects.requireNonNull(playersFileConfig.getString(uuid + ".team")).equalsIgnoreCase(teamName);
    }

    public String getTeamName(OfflinePlayer player) {
        uuid = player.getUniqueId();
        return playersFileConfig.getString(uuid + ".team");
    }

    public NamedTextColor getTeamColor(String teamName) {
        return NamedTextColor.namedColor(teamsFileConfig.getInt(teamName + ".color"));
    }

    public String getTeamPrefix(String teamName) {
        return teamsFileConfig.getString(teamName + ".prefix");
    }

    public Boolean getAllowFriendlyFire(String teamName) {
        return teamsFileConfig.getBoolean(teamName + ".allowFriendlyFire");
    }

    public boolean isValidTeam(String teamName) {
        return teamsFileConfig.contains(teamName);
    }

    public boolean hasTeam(OfflinePlayer player) {
        uuid = player.getUniqueId();
     return playersFileConfig.getString(uuid + ".team") != null;
    }

    public ArrayList<String> getActiveTeams() {
        return activeTeams;
    }

    public List<Player> getPlayersInTeam(String teamName) {
        List<Player> playerList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
           if (this.isInTeam(player, teamName)) {
               playerList.add(player);
           }
        }
        return playerList;
    }

    public Inventory getTeamInventory(String teamName) {
        return switch (teamName.toLowerCase()) {
            case "blue" -> inventoryHolderBlue.getInventory();
            case "red" -> inventoryHolderRed.getInventory();
            case "yellow" -> inventoryHolderYellow.getInventory();
            case "green" -> inventoryHolderGreen.getInventory();
            default -> null;
        };
    }

    public String getTeamNameFromHolder(InventoryHolder holder) {
        if (holder instanceof InventoryHolderBlue) {
            return "BLUE";
        } else if (holder instanceof  InventoryHolderRed) {
            return "RED";
        } else if(holder instanceof InventoryHolderYellow) {
            return "YELLOW";
        } else if (holder instanceof InventoryHolderGreen) {
            return "GREEN";
        }
        return null;
    }
}
