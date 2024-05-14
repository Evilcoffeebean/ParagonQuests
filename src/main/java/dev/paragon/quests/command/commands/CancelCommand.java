package dev.paragon.quests.command.commands;

import dev.paragon.quests.Core;
import dev.paragon.quests.command.QuestCommand;
import dev.paragon.quests.quest.Quest;
import dev.paragon.quests.utilities.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelCommand implements QuestCommand {

    @Override
    public String getCommand() {
        return "cancel";
    }

    @Override
    public String getUsage() {
        return "/quests cancel";
    }

    @Override
    public String getPermission() {
        return "quests.cancel";
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

        Core.getCore().getQuestManager().removeQuest(player);
        sender.sendMessage(StringUtil.color("&aCancelled current active quest."));
    }
}
