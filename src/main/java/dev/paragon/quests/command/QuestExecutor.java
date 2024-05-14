package dev.paragon.quests.command;

import com.google.common.collect.Maps;
import dev.paragon.quests.command.commands.AssignCommand;
import dev.paragon.quests.command.commands.CancelCommand;
import dev.paragon.quests.command.commands.StatusCommand;
import dev.paragon.quests.utilities.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;

public class QuestExecutor implements CommandExecutor {

    private final Map<String, QuestCommand> commands = Maps.newHashMap();

    public QuestExecutor() {
        commands.put("assign", new AssignCommand());
        commands.put("status", new StatusCommand());
        commands.put("cancel", new CancelCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("quests")) {
            if (args.length == 0) {
                StringBuilder response = new StringBuilder();
                commands.values().forEach(command -> response.append("&e").append(command.getUsage()).append("\n"));
                sender.sendMessage(StringUtil.color(response.toString()));
                return true;
            }

            final QuestCommand command = commands.get(args[0].toLowerCase(Locale.ENGLISH));
            if (command == null) {
                sender.sendMessage(StringUtil.color("&cUnknown command &e" + args[0]));
                return true;
            }

            if (command.playerOnly() && !(sender instanceof Player)) {
                sender.sendMessage(StringUtil.color("&cOnly players can execute this command"));
                return true;
            }

            if (args.length != command.getLength()) {
                sender.sendMessage(StringUtil.color("&cUsage: " + command.getUsage()));
                return true;
            }

            command.execute(sender, args);
            return true;
        }
        return false;
    }
}
