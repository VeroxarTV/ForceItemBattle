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
    Component teamPrefix;
    NamedTextColor teamColor;
    final List<String> validTeams = new ArrayList<>();
    boolean allowFriendlyFire;

    final TeamManager teamManager = data.getTeamManager();

    public TablistManager() {
        this.validTeams.add("RED");
        this.validTeams.add("BLUE");
        this.validTeams.add("YELLOW");
        this.validTeams.add("GREEN");
    }

    public void setAllPlayerTeams() {
        Bukkit.getOnlinePlayers().forEach(this::setPlayerTeams);
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

            team.prefix(teamPrefix);
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
}
