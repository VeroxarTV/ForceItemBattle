package de.veroxar.forceItemBattle.util;

import de.veroxar.forceItemBattle.ForceItemBattle;
import de.veroxar.forceItemBattle.countdown.GameCountdown;
import de.veroxar.forceItemBattle.data.Data;
import de.veroxar.forceItemBattle.tasks.CompletedTask;
import de.veroxar.forceItemBattle.tasks.TaskManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ResultInventoryManager {

    private final Data data = ForceItemBattle.getData();
    private final JavaPlugin instance = data.getInstance();
    private final TaskManager taskManager = data.getTaskManager();
    private final GameCountdown gameCountdown = data.getGameCountdown();
    private final Logic logic = data.getLogic();
    private int currentPage = 1;
    private int maxPages = 1;
    private boolean moreThanOnePage = false;
    private boolean alreadyUsed = false;
    public boolean inAnimation = false;

    /**
     * Erstellt ein Inventar für abgeschlossene Aufgaben mit animierter Darstellung.
     * @param player Der Spieler, für den das Inventar geöffnet wird.
     * @param pos Der belegte Platz in dieser Runde.
     * @return Das erstellte Inventar.
     */
    public Inventory createResultInv(Player player, int pos) {
        UUID uuid = player.getUniqueId();
        List<CompletedTask> completedTaskList = taskManager.getCompletedTaskList(uuid);

        maxPages = (int) Math.ceil(completedTaskList.size() / 35.0); // Berechnet die Anzahl der benötigten Seiten
        currentPage = 1;
        moreThanOnePage = currentPage < maxPages;
        alreadyUsed = false;
        inAnimation = true;


        return createInventoryWithAnimation(completedTaskList, player, pos);
    }

    /**
     * Erstellt ein Inventar für abgeschlossene Aufgaben ohne Animation.
     * @param player Der Spieler, für den das Inventar geöffnet wird.
     * @return Das erstellte Inventar.
     */
    public Inventory createResultInvWithoutAnimation(Player player) {
        UUID uuid = player.getUniqueId();
        List<CompletedTask> completedTaskList = taskManager.getCompletedTaskList(uuid);
        maxPages = (int) Math.ceil(completedTaskList.size() / 35.0);
        currentPage = 1;

        return createInventoryWithoutAnimation(completedTaskList, player);
    }

    /**
     * Wechselt die Seite des Inventars und zeigt die nächste Seite an.
     * @param inventory Das zu aktualisierende Inventar.
     * @param nextPage Gibt an, ob zur nächsten (true) oder vorherigen (false) Seite gewechselt werden soll.
     */
    public void switchPages(Inventory inventory, boolean nextPage) {
        if (nextPage && currentPage < maxPages) {
            currentPage++;
        } else if (!nextPage && currentPage > 1) {
            currentPage--;
        }
        updateInventoryWithCurrentPage(inventory);
    }

    public void switchPagesAnimated(Inventory inventory, Player player, Integer position, boolean nextPage) {
        if (nextPage && currentPage < maxPages) {
            currentPage++;
        } else if (!nextPage && currentPage > 1) {
            currentPage--;
        }
        updateInventoryWithCurrentPageAnimated(inventory, player, position);
    }

    private Inventory createInventoryWithAnimation(List<CompletedTask> completedTaskList, Player player, int position) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, Component.text("Geschaffte Aufgaben"));

        fillDefaultGlassPanes(inventory);

        int delay = 0;
        int slotIndex = 9;
        for (CompletedTask completedTask : completedTaskList) {
            while (slotIndex < inventory.getSize()) {
                if (slotIndex % 9 != 0 && (slotIndex + 1) % 9 != 0) {
                    final int currentSlot = slotIndex;

                    Bukkit.getScheduler().runTaskLater(instance, () -> {
                        addItemToInventory(inventory, completedTask, currentSlot);
                        playItemPickupSoundForAllPlayers();

                        int nextSlot = getNextAvailableSlot(inventory, currentSlot);
                        // Überprüfen, ob das Inventar voll ist
                        if (nextSlot == 52) {
                            // Update die Navigationsscheiben erst, wenn alle Items hinzugefügt wurden
                            updateNavigationPanes(inventory);

                            // Automatisch zur nächsten Seite wechseln, wenn es mehr Seiten gibt
                            if (currentPage < maxPages) {
                                Bukkit.getScheduler().runTaskLater(instance, () -> switchPagesAnimated(inventory, player, position, true), 10);
                            }
                        }
                    }, delay);
                    slotIndex++;
                    delay += 10;
                    break;
                }
                slotIndex++;
            }
            if (slotIndex == -1) break;
        }
        if (!moreThanOnePage)
            Bukkit.getScheduler().runTaskLater(instance, () -> presentInventoryHolder(inventory, player, position), 10+delay); // 0,5 Sekunden Delay

        return inventory;
    }

    private Inventory createInventoryWithoutAnimation(List<CompletedTask> completedTaskList, Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, Component.text("Geschaffte Aufgaben"));

        fillDefaultGlassPanes(inventory);

        int slotIndex = 9;
        for (CompletedTask completedTask : completedTaskList) {
            while (slotIndex < inventory.getSize()) {
                if (slotIndex % 9 != 0 && (slotIndex + 1) % 9 != 0) {
                    final int currentSlot = slotIndex;
                    addItemToInventory(inventory, completedTask, currentSlot);

                    slotIndex++;
                    break;
                }
                slotIndex++;
            }
            if (slotIndex == -1) break;
}
        updateNavigationPanes(inventory);

        return inventory;
    }

    private void updateInventoryWithCurrentPage(Inventory inventory) {
        inventory.clear();
        Player player = (Player) inventory.getHolder();
        fillDefaultGlassPanes(inventory);
        assert player != null;
        List<CompletedTask> completedTasks = taskManager.getCompletedTaskList(player.getUniqueId());

        int startIndex = (currentPage - 1) * 35;
        int endIndex = Math.min(startIndex + 35, completedTasks.size());
        List<CompletedTask> currentPageTasks = completedTasks.subList(startIndex, endIndex);

        int slotIndex = 9;
        for (CompletedTask completedTask : currentPageTasks) {
            // Überspringe Slots an den Rändern (linke und rechte Spalte)
            while (slotIndex < inventory.getSize()) {
                if (slotIndex % 9 != 0 && (slotIndex + 1) % 9 != 0) {
                    addItemToInventory(inventory, completedTask, slotIndex);
                    slotIndex++;
                    break;
                }
                slotIndex++;
            }
        }

        updateNavigationPanes(inventory);
    }

    private void updateInventoryWithCurrentPageAnimated(Inventory inventory, Player player, int position) {
        inventory.clear();
        fillDefaultGlassPanes(inventory);
        updateRedNavPane(inventory);

        List<CompletedTask> completedTasks = taskManager.getCompletedTaskList(player.getUniqueId());

        int startIndex = (currentPage - 1) * 35;
        int endIndex = Math.min(startIndex + 35, completedTasks.size());
        List<CompletedTask> currentPageTasks = completedTasks.subList(startIndex, endIndex);

        int delay = 0;
        int slotIndex = 9;
        for (CompletedTask completedTask : currentPageTasks) {
            while (slotIndex < inventory.getSize()) {
                if (slotIndex % 9 != 0 && (slotIndex + 1) % 9 != 0) {
                    final int currentSlot = slotIndex;

                    Bukkit.getScheduler().runTaskLater(instance, () -> {
                        addItemToInventory(inventory, completedTask, currentSlot);
                        playItemPickupSoundForAllPlayers();

                        int nextSlot = getNextAvailableSlot(inventory, currentSlot);

                        // Überprüfen, ob das Inventar voll ist
                        if (nextSlot == 52) {
                            // Update die Navigationsscheiben erst, wenn alle Items hinzugefügt wurden
                            updateNavigationPanes(inventory);

                            // Automatisch zur nächsten Seite wechseln, wenn es mehr Seiten gibt
                            if (currentPage < maxPages) {
                                Bukkit.getScheduler().runTaskLater(instance, () -> switchPagesAnimated(inventory, player, position, true), 10);
                            }
                        }
                    }, delay);
                    slotIndex++;
                    delay += 10;
                    break;
                }
                slotIndex++;
            }
        }
        if (currentPage == maxPages) {
            Bukkit.getScheduler().runTaskLater(instance, () -> presentInventoryHolder(inventory, player, position), 10+delay); // 0,5 Sekunden Delay
        }

    }

    private void fillDefaultGlassPanes(Inventory inventory) {
        ItemStack whiteGlassPane = createGlassPane(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack grayGlassPane = createGlassPane(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, whiteGlassPane);
        }

        for (int i = 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, grayGlassPane);
        }
    }

    private void updateNavigationPanes(Inventory inventory) {
        // "Vorherige Seite"-Scheibe von Anfang an hinzufügen, falls nicht auf der ersten Seite
        if (currentPage > 1) {
            ItemStack redGlassPane = createNavGlassPane(Material.RED_STAINED_GLASS_PANE,"Vorherige Seite", NamedTextColor.RED, currentPage, maxPages);
            inventory.setItem(27, redGlassPane);
        }

        // "Nächste Seite"-Scheibe nur hinzufügen, wenn mehr Seiten verfügbar sind
        if (currentPage < maxPages) {
            ItemStack greenGlassPane = createNavGlassPane(Material.LIME_STAINED_GLASS_PANE,"Nächste Seite", NamedTextColor.GREEN, currentPage, maxPages);
            inventory.setItem(35, greenGlassPane);
        }
    }

    private void updateRedNavPane(Inventory inventory) {
        if (currentPage > 1) {
            ItemStack redGlassPane = createNavGlassPane(Material.RED_STAINED_GLASS_PANE,"Vorherige Seite", NamedTextColor.RED,currentPage, maxPages);
            inventory.setItem(27, redGlassPane);
        }
    }

    private ItemStack createNavGlassPane(Material material, String displayName, NamedTextColor color, int currentPage, int maxPages) {
        ItemStack glassPane = new ItemStack(material);
        ItemMeta meta = glassPane.getItemMeta();
        meta.displayName(Component.text(displayName).color(color));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text( currentPage + " / " + maxPages).color(NamedTextColor.GRAY));
        meta.lore(lore);
        glassPane.setItemMeta(meta);

        return glassPane;
    }

    private ItemStack createGlassPane(Material material) {
        ItemStack glassPane = new ItemStack(material);
        ItemMeta meta = glassPane.getItemMeta();
        meta.displayName(Component.text(" "));
        glassPane.setItemMeta(meta);
        return glassPane;
    }

    private void addItemToInventory(Inventory inventory, CompletedTask completedTask, int slot) {
        ItemStack itemStack = new ItemStack(completedTask.material());
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> lore = new ArrayList<>();

        lore.add(Component.text(" "));
        lore.add(Component.text(gameCountdown.formatTime(completedTask.seconds())).color(NamedTextColor.GOLD));

        if (completedTask.usedJoker())
            lore.add(Component.text("[JOKER]").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));

        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slot, itemStack);
    }

    private int getNextAvailableSlot(Inventory inventory, int startIndex) {
        for (int i = startIndex; i < inventory.getSize(); i++) {
            if (i % 9 != 0 && (i + 1) % 9 != 0) {
                return i;
            }
        }
        return -1; // Keine verfügbaren Slots
    }

    private void playItemPickupSoundForAllPlayers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getOpenInventory().title().equals(Component.text("Geschaffte Aufgaben")))
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        }
    }

    private void presentInventoryHolder(Inventory inventory, Player player, int position){
        inventory.close();
        inAnimation = false;
        if (alreadyUsed)
            return;


        Component posTXT = Component.text(position);
        Component playerTXT = Component.text(". " + player.getName());
        Component separatorTXT = Component.text(" - ");
        Component pointsTXT = Component.text(logic.getPoints(player)).color(NamedTextColor.WHITE);
        Component overviewClickable = Component.text(" [Übersicht]").color(NamedTextColor.GREEN).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/result " + player.getName() + " no"));

        for (Player players : Bukkit.getOnlinePlayers()) {
            switch (position) {
                case 3:
                    players.showTitle(Title.title(Component.text( position).color(NamedTextColor.DARK_GRAY).append(Component.text("." + player.getName()).color(NamedTextColor.WHITE)), Component.text(logic.getPoints(player) + " Aufgaben geschafft").color(NamedTextColor.GOLD)));
                    players.sendMessage(posTXT.color(NamedTextColor.DARK_GRAY).append(playerTXT.color(NamedTextColor.WHITE)).append(separatorTXT).append(pointsTXT).append(overviewClickable));
                    break;
                case 2:
                    players.showTitle(Title.title(Component.text( position).color(NamedTextColor.GRAY).append(Component.text("." + player.getName()).color(NamedTextColor.WHITE)), Component.text(logic.getPoints(player) + " Aufgaben geschafft").color(NamedTextColor.GOLD)));
                    players.sendMessage(posTXT.color(NamedTextColor.GRAY).append(playerTXT.color(NamedTextColor.WHITE)).append(separatorTXT).append(pointsTXT).append(overviewClickable));
                    break;
                case 1:
                    players.showTitle(Title.title(Component.text( position).color(NamedTextColor.GOLD).append(Component.text("." + player.getName()).color(NamedTextColor.WHITE)), Component.text(logic.getPoints(player) + " Aufgaben geschafft").color(NamedTextColor.GOLD)));
                    players.sendMessage(posTXT.color(NamedTextColor.GOLD).append(playerTXT.color(NamedTextColor.WHITE)).append(separatorTXT).append(pointsTXT).append(overviewClickable));
                    break;
                default:
                    players.showTitle(Title.title(Component.text(position + "." + player.getName()), Component.text(logic.getPoints(player) + " Aufgaben geschafft").color(NamedTextColor.GOLD)));
                    players.sendMessage(posTXT.append(playerTXT).append(separatorTXT).append(pointsTXT).append(overviewClickable));
                    break;
            }
            players.playSound(players, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        alreadyUsed = true;
    }
    public boolean isInAnimation() {
        return inAnimation;
    }
}