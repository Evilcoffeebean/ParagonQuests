package dev.paragon.quests.command.commands;

import dev.paragon.quests.Core;
import dev.paragon.quests.command.QuestCommand;
import dev.paragon.quests.quest.Quest;
import dev.paragon.quests.utilities.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StatusCommand implements QuestCommand {

    @Override
    public String getCommand() {
        return "status";
    }

    @Override
    public String getUsage() {
        return "/quest status";
    }

    @Override
    public String getPermission() {
        return "quest.status";
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Quest quest = Core.getCore().getQuestManager().getQuest(player);

        if (quest == null) {
            sender.sendMessage(StringUtil.color("&aYou don't have any active quests."));
            return;
        }

        sender.sendMessage(StringUtil.color("&aStatus: &e" + quest.getMaterial().getKey().getKey().toUpperCase(Locale.ENGLISH) + " (" + quest.getCounter() + "/" + quest.getRequired() + ")"));
    }
}
