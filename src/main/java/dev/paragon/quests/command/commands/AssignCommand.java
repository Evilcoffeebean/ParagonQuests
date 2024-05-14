package dev.paragon.quests.command.commands;

import dev.paragon.quests.Core;
import dev.paragon.quests.command.QuestCommand;
import dev.paragon.quests.quest.Quest;
import dev.paragon.quests.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AssignCommand implements QuestCommand {

    @Override
    public String getCommand() {
        return "assign";
    }

    @Override
    public String getUsage() {
        return "/quest assign <player> <material> <amount>";
    }

    @Override
    public String getPermission() {
        return "quests.assign";
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    private Material parseMaterial(final String identifier) {
        try {
            return Material.valueOf(identifier.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private int parseInteger(final String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(StringUtil.color("&cPlayer not found online: &f" + args[1]));
            return;
        }

        Quest active = Core.getCore().getQuestManager().getQuest(player);
        if (active != null) {
            if (System.currentTimeMillis() < active.getTimestamp()) {
                sender.sendMessage(StringUtil.color("&cYou cannot be assigned another quest for another " + TimeUnit.MILLISECONDS.toHours(active.getTimestamp() - System.currentTimeMillis())) + " hours");
                return;
            }
        }

        Material material = parseMaterial(args[2]);
        if (material == null) {
            sender.sendMessage(StringUtil.color("&cInvalid material identifier: &f" + args[2]));
            return;
        }

        int amount = parseInteger(args[3]);
        if (amount == -1) {
            sender.sendMessage(StringUtil.color("&cInvalid amount specified: &f" + args[3]));
            return;
        }

        Quest quest = new Quest(player.getUniqueId(), amount, material);
        quest.setCounter(0);
        quest.setTimestamp(System.currentTimeMillis() + 86400000);
        quest.setExperience(true);
        quest.setRewards(true);
        quest.setItems(Arrays.asList(new ItemStack(Material.DIAMOND, 32), new ItemStack(Material.GOLD_INGOT, 64), new ItemStack(Material.EMERALD, 10)));
        quest.setExperiencePoints(100);

        Core.getCore().getQuestManager().assignQuest(player, quest);

        sender.sendMessage(StringUtil.color("&aAssigned quest " + quest.getMaterial().getKey().getKey() + " to " + player.getName()));
        StringUtil.title(player, "&a&lQUESTS", "&eObtain " + quest.getRequired() + " x " + quest.getMaterial().getKey().getKey());
    }
}
