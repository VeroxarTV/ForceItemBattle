package de.veroxar.forceItemBattle.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Messages {

    final public static Component PREFIX = Component.text("[")
            .color(NamedTextColor.GRAY)
    .append(Component.text("ForceItem")
        .color(NamedTextColor.AQUA)
        .decorate(TextDecoration.BOLD))
            .append(Component.text("] ")
        .color(NamedTextColor.GRAY));

    final public static String NOPLAYER = PREFIX + "§cDieser Befehl ist nur für Spieler gedacht!";
}
