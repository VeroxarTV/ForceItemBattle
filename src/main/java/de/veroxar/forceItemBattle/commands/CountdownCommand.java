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
                            "§cDer Countdown läuft bereits.")));
                    break;
                }
                if (!countdown.isStarted()) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cStarte das Spiel mit §6/start.")));
                    break;
                }
                countdown.setRunning(true);
                sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                        "§7Der Countdown läuft nun weiter.")));
                break;
            case "pause":
                if (!countdown.isRunning()) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cDer Countdown läuft nicht.")));
                    break;
                }
                countdown.setRunning(false);
                sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                        "§7Der Countdown wurde pausiert.")));
                break;
            case "time":
                if (args.length != 2) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§7Verwendung§8: §9/countdown time <Zeit>")));
                    break;
                }
                try {
                    int seconds = Integer.parseInt(args[1]);
                    countdown.setTime(seconds);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.PREFIX.append(LegacyComponentSerializer.legacySection().deserialize(
                            "§cDein Parameter 2 muss eine Zahl sein.")));
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
                "§7Verwendung§8: §6/countdown resume, /countdown pause /countdown time <Zeit>")));
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
            subcommands.add("<Zeit>");
        }

        return subcommands;
    }
}

