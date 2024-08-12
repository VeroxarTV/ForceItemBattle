package de.veroxar.forceItemBattle.events;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.util.Logic;
import de.veroxar.forceItemBattle.util.Timer;
import de.veroxar.forceItemBattle.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        event.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.GRAY + event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.RED + "- " + ChatColor.GRAY + event.getPlayer().getName());
        if (Bukkit.getOnlinePlayers().size() - 1 <= 0) {
            //timer.setRunning(false);
            gameCountdown.setRunning(false);
        }
    }
}
