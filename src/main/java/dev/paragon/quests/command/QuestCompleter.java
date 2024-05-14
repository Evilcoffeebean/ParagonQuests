package dev.paragon.quests.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class QuestCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("quests")) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], Arrays.asList("status", "assign", "cancel"), new ArrayList<>());
            }

            if (args[0].equalsIgnoreCase("assign")) {
                if (args.length == 3) {
                    List<String> materials = new ArrayList<>();
                    for (Material material : Material.values())
                        materials.add(material.getKey().getKey().toUpperCase(Locale.ENGLISH));

                    return StringUtil.copyPartialMatches(args[2], materials, new ArrayList<>());
                }
            }
        }

        return null;
    }
}
