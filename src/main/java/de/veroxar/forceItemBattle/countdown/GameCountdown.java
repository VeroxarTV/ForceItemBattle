package de.veroxar.forceItemBattle.countdown;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.config.Configuration;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import de.veroxar.forceItemBattle.util.Logic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
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

    public String formatTime(long totalSeconds) {
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;


        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void sendActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (isFinished()) {
                continue;
            }

            if (!isRunning())  {
                player.sendActionBar(Component.text("Idle").color(NamedTextColor.GOLD).decorate(TextDecoration.ITALIC));
                continue;
            }

            // Textkomponenten erstellen und formatieren
            Component timeComponent = Component.text(formatTime(getTime()))
                    .color(NamedTextColor.DARK_PURPLE)
                    .decorate(TextDecoration.BOLD);

            Component separatorComponent = Component.text(" - ")
                    .color(NamedTextColor.GRAY);

            Component itemNameComponent = logic.getCurrentItemName(player);

            // Zusammenfügen der Komponenten
            Component actionBarMessage = timeComponent.append(separatorComponent).append(itemNameComponent);

            player.sendActionBar(actionBarMessage);
        }
    }

    public void saveTime(){
        countdownConfig.toFileConfiguration().set(".countdown", getTime());
        countdownConfig.saveConfiguration();
    }

    public void onEnd() {
        setFinished(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("Zeit vorbei!").color(NamedTextColor.GOLD), (Component.text(""))));
            Location spawn = player.getWorld().getSpawnLocation();
            spawn.setYaw(player.getYaw());
            spawn.setPitch(player.getPitch());
            player.teleportAsync(spawn);
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            if (player.hasPermission("forceItemBattle.commands.result")) {
                player.sendMessage(Messages.PREFIX.append(Component.text("Execute /result to display the result!").color(NamedTextColor.GRAY)));
            }
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
