package de.veroxar.forceItemBattle.team;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.data.Data;
import net.kyori.adventure.text.format.NamedTextColor;

public class DefaultTeams {
    final Data data = ForceItemBattle.getData();
    final TeamManager teamManager = data.getTeamManager();

    public DefaultTeams() {
        createDefaultTeams();
    }

    public void createDefaultTeams() {
        teamManager.createTeam(
                "RED", "§8[§cRED§8]§r ", NamedTextColor.RED, false);
        teamManager.createTeam(
                "BLUE", "§8[§9BLUE§8]§r ", NamedTextColor.BLUE, false);
        teamManager.createTeam(
                "YELLOW", "§8[§eYELLOW§8]§r ", NamedTextColor.YELLOW, false);
        teamManager.createTeam(
                "GREEN", "§8[§aGREEN§8]§r ", NamedTextColor.GREEN, false);
    }
}
