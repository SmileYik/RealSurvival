package com.outlook.z815007320.utils;

import org.bukkit.ChatColor;

public class Util_Colours
{

    public static String extractAndReplaceTooltipColour(String input)
    {
	         return input.replaceAll(ChatColor.RESET.toString(), "&r")
				        .replaceAll(ChatColor.ITALIC.toString(), "&o")
				        .replaceAll(ChatColor.UNDERLINE.toString(), "&n")
				        .replaceAll(ChatColor.STRIKETHROUGH.toString(), "&m")
				        .replaceAll(ChatColor.BOLD.toString(), "&l")
				        .replaceAll(ChatColor.MAGIC.toString(), "&k")
				        .replaceAll(ChatColor.WHITE.toString(), "&f")
				        .replaceAll(ChatColor.YELLOW.toString(), "&e")
				        .replaceAll(ChatColor.LIGHT_PURPLE.toString(), "&d")
				        .replaceAll(ChatColor.RED.toString(), "&c")
				        .replaceAll(ChatColor.AQUA.toString(), "&b")
				        .replaceAll(ChatColor.GREEN.toString(), "&a")
				        .replaceAll(ChatColor.BLUE.toString(), "&9")
				        .replaceAll(ChatColor.DARK_GRAY.toString(), "&8")
				        .replaceAll(ChatColor.GRAY.toString(), "&7")
				        .replaceAll(ChatColor.GOLD.toString(), "&6")
				        .replaceAll(ChatColor.DARK_PURPLE.toString(), "&5")
				        .replaceAll(ChatColor.DARK_RED.toString(), "&4")
				        .replaceAll(ChatColor.DARK_AQUA.toString(), "&3")
				        .replaceAll(ChatColor.DARK_GREEN.toString(), "&2")
				        .replaceAll(ChatColor.DARK_BLUE.toString(), "&1")
				        .replaceAll(ChatColor.BLACK.toString(), "&0");
    }

    public static String replaceTooltipColour(String input)
    {
       return input.replaceAll("%n", "        ")
					        .replaceAll("&r", (new StringBuilder()).append(ChatColor.RESET).toString())
					        .replaceAll("&o", (new StringBuilder()).append(ChatColor.ITALIC).toString())
					        .replaceAll("&n", (new StringBuilder()).append(ChatColor.UNDERLINE).toString())
					        .replaceAll("&m", (new StringBuilder()).append(ChatColor.STRIKETHROUGH).toString())
					        .replaceAll("&l", (new StringBuilder()).append(ChatColor.BOLD).toString())
					        .replaceAll("&k", (new StringBuilder()).append(ChatColor.MAGIC).toString())
					        .replaceAll("&f", (new StringBuilder()).append(ChatColor.WHITE).toString())
					        .replaceAll("&e", (new StringBuilder()).append(ChatColor.YELLOW).toString())
					        .replaceAll("&d", (new StringBuilder()).append(ChatColor.LIGHT_PURPLE).toString())
					        .replaceAll("&c", (new StringBuilder()).append(ChatColor.RED).toString())
					        .replaceAll("&b", (new StringBuilder()).append(ChatColor.AQUA).toString())
					        .replaceAll("&a", (new StringBuilder()).append(ChatColor.GREEN).toString())
					        .replaceAll("&9", (new StringBuilder()).append(ChatColor.BLUE).toString())
					        .replaceAll("&8", (new StringBuilder()).append(ChatColor.DARK_GRAY).toString())
					        .replaceAll("&7", (new StringBuilder()).append(ChatColor.GRAY).toString())
					        .replaceAll("&6", (new StringBuilder()).append(ChatColor.GOLD).toString())
					        .replaceAll("&5", (new StringBuilder()).append(ChatColor.DARK_PURPLE).toString())
					        .replaceAll("&4", (new StringBuilder()).append(ChatColor.DARK_RED).toString())
					        .replaceAll("&3", (new StringBuilder()).append(ChatColor.DARK_AQUA).toString())
					        .replaceAll("&2", (new StringBuilder()).append(ChatColor.DARK_GREEN).toString())
					        .replaceAll("&1", (new StringBuilder()).append(ChatColor.DARK_BLUE).toString())
					        .replaceAll("&0", (new StringBuilder()).append(ChatColor.BLACK).toString());
    }

    public static String removeTooltipColour(String input)
    {
        return input.replaceAll("&r", "")
					        .replaceAll("&o", "")
					        .replaceAll("&n", "")
					        .replaceAll("&m", "")
					        .replaceAll("&l", "")
					        .replaceAll("&k", "")
					        .replaceAll("&f", "")
					        .replaceAll("&e", "")
					        .replaceAll("&d", "")
					        .replaceAll("&c", "")
					        .replaceAll("&b", "")
					        .replaceAll("&a", "")
					        .replaceAll("&9", "")
					        .replaceAll("&8", "")
					        .replaceAll("&7", "")
					        .replaceAll("&6", "")
					        .replaceAll("&5", "")
					        .replaceAll("&4", "")
					        .replaceAll("&3", "")
					        .replaceAll("&2", "")
					        .replaceAll("&1", "")
					        .replaceAll("&0", "");
    }

    public static String removeRawTooltipColour(String input)
    {
        return input.replaceAll("\247r", "")
					        .replaceAll("\247o", "")
					        .replaceAll("\247n", "")
					        .replaceAll("\247m", "")
					        .replaceAll("\247l", "")
					        .replaceAll("\247k", "")
					        .replaceAll("\247f", "")
					        .replaceAll("\247e", "")
					        .replaceAll("\247d", "")
					        .replaceAll("\247c", "")
					        .replaceAll("\247b", "")
					        .replaceAll("\247a", "")
					        .replaceAll("\2479", "")
					        .replaceAll("\2478", "")
					        .replaceAll("\2477", "")
					        .replaceAll("\2476", "")
					        .replaceAll("\2475", "")
					        .replaceAll("\2474", "")
					        .replaceAll("\2473", "")
					        .replaceAll("\2472", "")
					        .replaceAll("\2471", "")
					        .replaceAll("\2470", "");
    }
}
