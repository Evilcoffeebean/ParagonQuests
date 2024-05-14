package dev.paragon.quests.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class StringUtil {

    private StringUtil() {} //prevent instantiation

    public static String color(final String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void title(final Player player, final String titleText, final String subtitleText) {
        player.sendTitle(color(titleText), color(subtitleText), 20, 100, 20);
    }
}
