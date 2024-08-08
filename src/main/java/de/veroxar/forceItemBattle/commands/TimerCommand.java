package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.util.Timer;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TimerCommand implements CommandExecutor, TabCompleter {

    Data data = ForceItemBattle.getData();
    Timer timer = data.getTimer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "resume":
                    if (timer.isRunning()) {
                        sender.sendMessage(Messages.PREFIX + "§cDer Timer läuft bereits!");
                        return true;
                    }

                    timer.setRunning(true);
                    sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Der Timer wurde gestartet!");
                    return true;
                case "pause":
                    if (!timer.isRunning()) {
                        sender.sendMessage(Messages.PREFIX + "§cDer Timer läuft nicht!");
                        return true;
                    }

                    timer.setRunning(false);
                    sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Der Timer wurde gestoppt!");
                    return true;
                case "set":
                    if (args.length != 2) {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/timer <set> <time>");
                        return true;
                    }
                    if (timer.isRunning()) {
                        timer.setRunning(false);
                    }
                    try {
                        timer.setTime(Integer.parseInt(args[1]));
                        sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Die Zeit wurde auf " + args[1] + " Sekunden gesetzt!");
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Dein Parameter 2 muss eine Zahl sein!");
                    }

                    return true;
                case "reset":

                    timer.setRunning(false);
                    timer.setTime(0);
                    timer.saveTime();
                    sender.sendMessage(Messages.PREFIX + ChatColor.GRAY + "Der Timer wurde zurückgesetzt!");
                    return true;
                default:
                    sendUsage(sender);
                    return true;
            }
        } else
            sendUsage(sender);



        return false;
    }

        public void sendUsage(CommandSender sender) {
            sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bitte benutze /timer <resume>, /timer <pause>," +
                                " /timer <set> <time>, /timer <reset>");
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        List<String> subcommands = new ArrayList<>();

        if (args.length == 1) {
            subcommands.add("resume");
            subcommands.add("pause");
            subcommands.add("set");
            subcommands.add("reset");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            subcommands.add("<time>");
        }

        return subcommands;
    }
}
