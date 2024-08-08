package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.util.Timer;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    Data data = ForceItemBattle.getData();
    Timer timer = data.getTimer();
    GameCountdown gameCountdown = data.getGameCountdown();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() - 1 <= 0) {
            timer.setRunning(false);
            gameCountdown.setRunning(false);
        }
    }
}
