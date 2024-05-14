package dev.paragon.quests.quest;

import dev.paragon.quests.utilities.FileUtil;
import dev.paragon.quests.utilities.base64.Base64Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record QuestConfig(FileUtil config) {

    public void delete() {
        config.delete();
    }

    public void write(final Quest quest) {
        CompletableFuture.runAsync(() -> {
            try {
                config.set("uuid", quest.getUuid().toString(), true);
                config.set("required", quest.getRequired(), true);
                config.set("material", quest.getMaterial().toString(), true);
                config.set("experience", quest.isExperience(), true);
                config.set("rewards", quest.isRewards(), true);
                config.set("experience_points", quest.getExperiencePoints(), true);
                config.set("counter", quest.getCounter(), true);
                config.set("timestamp", System.currentTimeMillis() + 86400000, true);

                if (quest.getItems() != null && !quest.getItems().isEmpty())
                    config.set("items", Base64Util.itemStackArrayToBase64(quest.getItems()), true);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).whenComplete((unused, throwable) -> {
            if (throwable != null)
                throwable.fillInStackTrace();
        });
    }

    public Quest read() {
        UUID uuid = UUID.fromString(config.getString("uuid"));
        int required = config.getInteger("required");
        Material material = Material.valueOf(config.getString("material").toUpperCase(Locale.ENGLISH));
        boolean experience = config.getBoolean("experience"), rewards = config.getBoolean("rewards");
        int experiencePoints = config.getInteger("experience_points");
        int counter = config.getInteger("counter");
        long timestamp = config.getLong("timestamp");
        String base64 = config.getString("items");
        List<ItemStack> items = base64 != null ? Base64Util.itemStackListFromBase64(base64) : new ArrayList<>();

        Quest quest = new Quest(uuid, required, material);
        quest.setExperience(experience);
        quest.setRewards(rewards);
        quest.setExperiencePoints(experiencePoints);
        quest.setCounter(counter);
        quest.setTimestamp(timestamp);
        quest.setItems(items);

        return quest;
    }
}
