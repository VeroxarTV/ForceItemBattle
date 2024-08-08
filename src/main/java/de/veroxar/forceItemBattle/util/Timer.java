package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    Data data = ForceItemBattle.getData();
    Configuration timerConfig = data.getConfigs().getTimerConfig();
    JavaPlugin instance = data.getInstance();

    private boolean running;
    private int time;

    public Timer() {
        if (timerConfig.toFileConfiguration().getInt(".time") != 0) {
            this.time = timerConfig.toFileConfiguration().getInt(".time");
        } else {
            this.time = 0;
        }
        this.running = false;
        run();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private String formatTime(int totalSeconds) {
        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        String formattedTime;

        if (days > 0) {
            formattedTime = String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            formattedTime = String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            formattedTime = String.format("%dm %ds", minutes, seconds);
        } else {
            formattedTime = String.format("%ds", seconds);
        }

        return formattedTime;
    }
    
    public void sendActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!isRunning())  {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED.toString() +
                        ChatColor.ITALIC + "Der Timer ist pausiert!" ));
                continue;
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_PURPLE + formatTime(getTime())));
        }
    }

    public void saveTime(){
         timerConfig.toFileConfiguration().set(".time", getTime());
         timerConfig.saveConfiguration();
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

                sendActionBar();

                if (!isRunning()) {
                    return;
                }
                setTime(getTime() + 1);
            }
        }.runTaskTimer(instance, 20, 20);
    }
}
