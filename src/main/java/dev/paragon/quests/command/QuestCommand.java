package dev.paragon.quests.command;

import org.bukkit.command.CommandSender;

public interface QuestCommand {

    String getCommand();
    String getUsage();
    String getPermission();
    int getLength();
    boolean playerOnly();
    void execute(CommandSender sender, String[] args);
}
