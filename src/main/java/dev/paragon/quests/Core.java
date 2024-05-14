package dev.paragon.quests;

import dev.paragon.quests.command.QuestCompleter;
import dev.paragon.quests.command.QuestExecutor;
import dev.paragon.quests.quest.Quest;
import dev.paragon.quests.quest.QuestManager;
import dev.paragon.quests.utilities.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Core extends JavaPlugin implements Listener {

    @Getter
    private static Core core;
    private QuestManager questManager;

    @Override
    public void onEnable() {
        core = this;
        questManager = new QuestManager();
        questManager.loadData();
        getCommand("quests").setExecutor(new QuestExecutor());
        getCommand("quests").setTabCompleter(new QuestCompleter());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        questManager.clearData();
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onItemDrop(final PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Quest quest = questManager.getQuest(player);

        if (quest == null)
            return;

        Material material = event.getItemDrop().getItemStack().getType();
        int amount = event.getItemDrop().getItemStack().getAmount();

        if (quest.getUuid() == player.getUniqueId() && quest.getMaterial() == material) {
            if (quest.getCounter() - amount > 0)
                quest.setCounter(quest.getCounter() - amount);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onItemPickup(final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        Quest quest = questManager.getQuest(player);
        if (quest == null)
            return;

        Material material = event.getItem().getItemStack().getType();
        int amount = event.getItem().getItemStack().getAmount();

        if (quest.getUuid() == player.getUniqueId() && quest.getMaterial() == material) {
            if (quest.getCounter() + amount < quest.getRequired()) {
                quest.setCounter(quest.getCounter() + amount);
            } else {
                if (quest.getItems() != null && !quest.getItems().isEmpty() && quest.isRewards())
                    quest.getItems().forEach(item -> player.getInventory().addItem(item));
                if (quest.isExperience() && quest.getExperiencePoints() > 0)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "experience add " + player.getName() + " " + quest.getExperiencePoints());

                questManager.removeQuest(player);
                player.sendMessage(StringUtil.color("&aQuest complete."));
            }
        }
    }
}
