package de.veroxar.forceItemBattle.team;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DefaultTeams {
    final Data data = ForceItemBattle.getData();
    final TeamManager teamManager = data.getTeamManager();

    public DefaultTeams() {
        createDefaultTeams();
    }

    private Component createPrefix(String teamName, NamedTextColor color) {
        Component bracketL = Component.text("[").color(NamedTextColor.GRAY);
        Component teamNameComponent = Component.text(teamName).color(color);
        Component bracketR = Component.text("] ").color(NamedTextColor.GRAY);
        return bracketL.append(teamNameComponent).append(bracketR).append(Component.text("").color(NamedTextColor.WHITE));
    }

    public void createDefaultTeams() {
        teamManager.createTeam(
                "RED", createPrefix("RED", NamedTextColor.RED), NamedTextColor.RED, false);
        teamManager.createTeam(
                "BLUE", createPrefix("BLUE", NamedTextColor.BLUE), NamedTextColor.BLUE, false);
        teamManager.createTeam(
                "YELLOW", createPrefix("YELLOW", NamedTextColor.YELLOW), NamedTextColor.YELLOW, false);
        teamManager.createTeam(
                "GREEN", createPrefix("GREEN", NamedTextColor.GREEN), NamedTextColor.GREEN, false);
    }
}
