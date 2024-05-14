package dev.paragon.quests.quest;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Quest {

    private final UUID uuid;
    private final int required;
    private final Material material;
    private boolean experience, rewards;
    private int experiencePoints;
    private List<ItemStack> items;
    private int counter;
    private long timestamp;

    public Quest(UUID uuid, int required, Material material) {
        this.uuid = uuid;
        this.required = required;
        this.material = material;
    }
}
