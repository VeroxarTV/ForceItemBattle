package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.util.TablistManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    Data data = ForceItemBattle.getData();
    GameCountdown gameCountdown = data.getGameCountdown();
    TablistManager tablistManager = data.getTablistManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text("+ ").color(NamedTextColor.GREEN).append(Component.text(event.getPlayer().getName()).color(NamedTextColor.GRAY)));
        tablistManager.setAllPlayerTeams();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text("- ").color(NamedTextColor.RED).append(Component.text(event.getPlayer().getName()).color(NamedTextColor.GRAY)));
        if (Bukkit.getOnlinePlayers().size() - 1 <= 0) {
            gameCountdown.setRunning(false);
        }
    }
}
