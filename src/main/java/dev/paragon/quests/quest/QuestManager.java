package dev.paragon.quests.quest;

import com.google.common.collect.Maps;
import dev.paragon.quests.Core;
import dev.paragon.quests.utilities.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class QuestManager {

    private final String PATH = Core.getCore().getDataFolder() + "/quests/";
    private final Map<UUID, Quest> players = Maps.newConcurrentMap();

    public Quest getQuest(final Player player) {
        return players.get(player.getUniqueId());
    }

    public void loadData() {
        CompletableFuture.runAsync((() -> {
            List<File> files = FileUtil.getFiles(PATH);
            if (files != null && !files.isEmpty()) {
                for (File file : files) {
                    Quest quest = new QuestConfig(new FileUtil(file)).read();
                    synchronized (players) {
                        players.put(quest.getUuid(), quest);
                    }
                }
            }
        })).whenComplete((unused, throwable) -> {
            if (throwable != null)
                throwable.fillInStackTrace();

            Bukkit.getLogger().log(Level.INFO, "Data successfully loaded: " + players.size());
        });
    }

    public void clearData() {
        if (!players.isEmpty()) {
            players.values().forEach(quest -> {
                List<File> files = FileUtil.getFiles(PATH);
                if (files != null && !files.isEmpty()) {
                    for (File file : files) {
                        QuestConfig config = new QuestConfig(new FileUtil(file));
                        config.write(quest);
                    }
                }
            });

            players.clear();
            Bukkit.getLogger().log(Level.INFO, "Data successfully cleared from cache.");
        }
    }

    public void assignQuest(final Player player, final Quest quest) {
        QuestConfig config = new QuestConfig(new FileUtil(new File(PATH, player.getUniqueId() + ".yml")));
        config.write(quest);
        players.put(player.getUniqueId(), quest);
    }

    public void removeQuest(final Player player) {
        QuestConfig config = new QuestConfig(new FileUtil(new File(PATH, player.getUniqueId() + ".yml")));
        config.delete();
        players.remove(player.getUniqueId());
    }
}
