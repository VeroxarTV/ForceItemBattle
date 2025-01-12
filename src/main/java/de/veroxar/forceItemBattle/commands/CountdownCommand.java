package de.veroxar.forceItemBattle.commands;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.messages.Messages;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CountdownCommand implements CommandExecutor, TabCompleter {

    private final Data data = ForceItemBattle.getData();
    private final GameCountdown countdown = data.getGameCountdown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "resume":
                if (countdown.isRunning()) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cThe countdown has already begun.")));
                    break;
                }
                if (!countdown.isStarted()) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cStart the game with §6/start.")));
                    break;
                }
                countdown.setRunning(true);
                sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                        "§7The countdown continues.")));
                break;
            case "pause":
                if (!countdown.isRunning()) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cThe countdown is not running.")));
                    break;
                }
                countdown.setRunning(false);
                sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                        "§7The countdown has been paused.")));
                break;
            case "time":
                if (args.length != 2) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§7Use§8: §9/countdown time <time>")));
                    break;
                }
                try {
                    int seconds = Integer.parseInt(args[1]);
                    countdown.setTime(seconds);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cYour parameter 2 must be a number.")));
                }
                break;
            default:
                sendUsage(sender);
                break;
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                "§7Use§8: §6/countdown resume, /countdown pause /countdown time <time>")));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        List<String> availableSubcommands = new ArrayList<>();
        List<String> subcommands = new ArrayList<>();

        availableSubcommands.add("resume");
        availableSubcommands.add("pause");
        availableSubcommands.add("time");

        if (args.length == 1) {
            for (String s : availableSubcommands) {
                if (s.contains(args[0].toLowerCase())) {
                    subcommands.add(s);
                }
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("time")) {
            subcommands.add("<time>");
        }

        return subcommands;
    }
}

