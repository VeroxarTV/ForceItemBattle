package de.veroxar.forceItemBattle.team;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.UUID;

public class TeamManager {

    private final Data data = ForceItemBattle.getData();
    private final Configuration teamsConfig = data.getConfigs().getTeamsConfig();
    private final Configuration playersConfig = data.getConfigs().getPlayersConfig();
    private final FileConfiguration teamsFileConfig = teamsConfig.toFileConfiguration();
    private final FileConfiguration playersFileConfig = playersConfig.toFileConfiguration();
    private UUID uuid;

    public void createTeam(String teamName, Component prefix, NamedTextColor color, boolean allowFriendlyFire) {
        if (!isValidTeam(teamName))  {
            teamsFileConfig.set(".", teamName);
            teamsFileConfig.set(teamName + ".prefix", prefix);
            teamsFileConfig.set(teamName + ".color", color);
            teamsFileConfig.set(teamName + ".allowFriendlyFire", allowFriendlyFire);
            teamsConfig.saveConfiguration();
        }
    }
    public boolean joinTeam (CommandSender sender, OfflinePlayer player, String teamName) {
       uuid = player.getUniqueId();
       Component teamNameComponent = Component.text(teamName).color(NamedTextColor.GOLD);
       if (isValidTeam(teamName)) {
           if (!isInTeam(player, teamName)) {
               playersFileConfig.set(uuid + ".team", teamName);
               playersConfig.saveConfiguration();
               return true;
           } else {
               sender.sendMessage(Messages.PREFIX.append(Component.text("Spieler bereits in Team: ").color(NamedTextColor.RED)
                       .append(teamNameComponent)));
               return false;
           }
       } else {
           sender.sendMessage(Messages.PREFIX.append(Component.text("Unbekanntes Team: ").color(NamedTextColor.RED)
                   .append(teamNameComponent)));
           return false;
       }
    }

    public boolean quitTeam (CommandSender sender, OfflinePlayer player, String teamName, World world) {
        uuid = player.getUniqueId();
        Component teamNameComponent = Component.text(teamName).color(NamedTextColor.GOLD);
        if (isValidTeam(teamName)) {
            if (isInTeam(player, teamName)) {
                playersFileConfig.set(uuid + ".team", null);
                playersConfig.saveConfiguration();
                return true;
            } else  {
                sender.sendMessage(Messages.PREFIX.append(Component.text("Spieler nicht in Team: ").color(NamedTextColor.RED)
                        .append(teamNameComponent)));
                return false;
            }
        } else {
            sender.sendMessage(Messages.PREFIX.append(Component.text("Unbekanntes Team: ").color(NamedTextColor.RED)
                    .append(teamNameComponent)));
            return false;
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
       return (NamedTextColor) teamsFileConfig.get(teamName + ".color");
    }

    public Component getTeamPrefix(String teamName) {
        return (Component) teamsFileConfig.get(teamName + ".prefix");
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
}
