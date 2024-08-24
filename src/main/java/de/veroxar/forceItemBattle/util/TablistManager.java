package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.team.TeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TablistManager {

    final Data data = ForceItemBattle.getData();
    Scoreboard scoreboard;
    Team team;
    String teamPrefix;
    NamedTextColor teamColor;
    final List<String> validTeams = new ArrayList<>();
    private TeamManager teamManager = data.getTeamManager();
    boolean allowFriendlyFire;

    public TablistManager() {
        this.validTeams.add("RED");
        this.validTeams.add("BLUE");
        this.validTeams.add("YELLOW");
        this.validTeams.add("GREEN");
    }

    public void setAllPlayerTeams() {
        Bukkit.getOnlinePlayers().forEach(this::setPlayerTeams);
    }

    public void clearAllPlayerTeams() {
        Bukkit.getOnlinePlayers().forEach(this::clearPlayerTeams);
    }

    public void setPlayerTeams(Player player) {
        scoreboard = player.getScoreboard();

        for (String teamName : validTeams) {
            team = scoreboard.getTeam(teamName);
            if (team == null)
                team = scoreboard.registerNewTeam(teamName);

            this.teamPrefix = teamManager.getTeamPrefix(teamName);
            this.teamColor = teamManager.getTeamColor(teamName);
            this.allowFriendlyFire = teamManager.getAllowFriendlyFire(teamName);

            team.setPrefix(teamPrefix);
            team.setSuffix("");
            team.color(teamColor);
            team.setAllowFriendlyFire(allowFriendlyFire);

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (teamManager.isInTeam(target, teamName)) {
                    team.addPlayer(target);
                    player.setScoreboard(scoreboard);
                }
            }
        }
    }

    public void clearPlayerTeams(Player player) {
        scoreboard = player.getScoreboard();
        if (scoreboard.getTeam(player.getUniqueId().toString()) == null) {
            team = scoreboard.registerNewTeam(player.getUniqueId().toString());
        } else {
            team = scoreboard.getTeam(player.getUniqueId().toString());
            team.prefix(Component.text(""));
            team.color(NamedTextColor.WHITE);
            team.setAllowFriendlyFire(true);
            team.addPlayer(player);
            player.setScoreboard(scoreboard);
        }
    }
}
