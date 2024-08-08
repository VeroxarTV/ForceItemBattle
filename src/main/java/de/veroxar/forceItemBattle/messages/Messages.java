package de.veroxar.forceItemBattle.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

public class Messages {

    final public static String PREFIX = ChatColor.GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD
            + "ForceItem" + ChatColor.GRAY + "] " + ChatColor.RESET;
    final public static String NOPLAYER = PREFIX + "§cDieser Befehl ist nur für Spieler gedacht!";

    final public static Component COMPONENT_PREFIX = Component.text("[")
            .color(NamedTextColor.GRAY)
    .append(Component.text("ForceItem")
        .color(NamedTextColor.AQUA)
        .decorate(TextDecoration.BOLD))
            .append(Component.text("] ")
        .color(NamedTextColor.GRAY));
}
