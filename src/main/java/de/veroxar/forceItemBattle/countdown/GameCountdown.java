package de.veroxar.forceItemBattle.countdown;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import de.veroxar.forceItemBattle.util.Logic;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdown {

    Data data = ForceItemBattle.getData();
    Configuration countdownConfig = data.getConfigs().getCountdownConfig();
    JavaPlugin instance = data.getInstance();
    Logic logic = data.getLogic();

    private boolean running;
    private boolean finished;
    private int time;

    public GameCountdown() {
        if (countdownConfig.toFileConfiguration().getInt(".countdown") != 0) {
            this.time = countdownConfig.toFileConfiguration().getInt(".countdown");
        } else if (instance.getConfig().getInt(".time") != 0) {
            this.time = instance.getConfig().getInt(".time");
        } else {
            this.time = 10800;
        }
        this.running = false;
        this.finished = false;
        run();
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private String formatTime(long totalSeconds) {
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);


        return formattedTime;
    }

    public void sendActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (isFinished()) {
                continue;
            }

            if (!isRunning())  {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD.toString() +
                        ChatColor.ITALIC + "Idle" ));
                continue;
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + formatTime(getTime())));
        }
    }

    public void saveTime(){
        countdownConfig.toFileConfiguration().set(".countdown", getTime());
        countdownConfig.saveConfiguration();
    }

    public void onEnd() {
        setFinished(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.GOLD + "Zeit vorbei!", "");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }
        logic.removeAllTasks();
        setTime(instance.getConfig().getInt(".time"));
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

                sendActionBar();

                if (!isRunning()) {
                    return;
                }

                if (isFinished()) {
                    return;
                }

                 if (getTime() <= 0) {
                     onEnd();
                 }

                setTime(getTime() - 1);
            }
        }.runTaskTimer(instance, 20, 20);
    }
}
