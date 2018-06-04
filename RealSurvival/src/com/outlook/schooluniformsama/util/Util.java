package com.outlook.schooluniformsama.util;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import com.outlook.schooluniformsama.data.timer.Timer;


public class Util{
	
	/**
	 * Get the workbench timer ID
	 * */
	public static String getWorkbenchID(Timer tt){
		return tt.getWorldName()+tt.getX()+tt.getY()+tt.getZ();
	}
	
	/**
	 * Get the workbench timer ID
	 * */
	public static String getWorkbenchID(Block b){
		return b.getWorld().getName()+b.getX()+b.getY()+b.getZ();
	}
	
	/**
	 * Reserved n Decimal Places
	 * @param d
	 * @param n
	 * @return String
	 */
	public static String RDP(double d,int n){
		return String.format("%."+n+"f", d);
	}
	
	/**
	 * Get a number from min to max
	 * @param min
	 * @param max
	 * @return
	 */
	public static double randomNum(double min,double max){
		return min+Math.random()*(max-min);
	}
	
	public static String removeColor(String input){
		return input.replaceAll("&r", "").replaceAll("&o", "").replaceAll("&n", "").replaceAll("&m", "").replaceAll("&l", "").replaceAll("&k", "").replaceAll("&f", "")
				.replaceAll("&e", "").replaceAll("&d", "").replaceAll("&c", "").replaceAll("&b", "").replaceAll("&a", "").replaceAll("&9", "").replaceAll("&8", "").replaceAll("&7", "")
		        .replaceAll("&6", "").replaceAll("&5", "").replaceAll("&4", "").replaceAll("&3", "").replaceAll("&2", "").replaceAll("&1", "").replaceAll("&0", "").replaceAll("\247r", "")
		        .replaceAll("\247o", "").replaceAll("\247n", "").replaceAll("\247m", "").replaceAll("\247l", "").replaceAll("\247k", "").replaceAll("\247f", "").replaceAll("\247e", "")
		        .replaceAll("\247d", "").replaceAll("\247c", "").replaceAll("\247b", "").replaceAll("\247a", "").replaceAll("\2479", "").replaceAll("\2478", "").replaceAll("\2477", "")
		        .replaceAll("\2476", "").replaceAll("\2475", "").replaceAll("\2474", "").replaceAll("\2473", "").replaceAll("\2472", "").replaceAll("\2471", "").replaceAll("\2470", "");
	}
	
	public static String setColor(String input){
	       return input.replaceAll("%n", "        ")
			        .replaceAll("&r", (new StringBuilder()).append(ChatColor.RESET).toString()).replaceAll("&o", (new StringBuilder()).append(ChatColor.ITALIC).toString())
			        .replaceAll("&n", (new StringBuilder()).append(ChatColor.UNDERLINE).toString()).replaceAll("&m", (new StringBuilder()).append(ChatColor.STRIKETHROUGH).toString())
			        .replaceAll("&l", (new StringBuilder()).append(ChatColor.BOLD).toString()).replaceAll("&k", (new StringBuilder()).append(ChatColor.MAGIC).toString())
			        .replaceAll("&f", (new StringBuilder()).append(ChatColor.WHITE).toString()).replaceAll("&e", (new StringBuilder()).append(ChatColor.YELLOW).toString())
			        .replaceAll("&d", (new StringBuilder()).append(ChatColor.LIGHT_PURPLE).toString()).replaceAll("&c", (new StringBuilder()).append(ChatColor.RED).toString())
			        .replaceAll("&b", (new StringBuilder()).append(ChatColor.AQUA).toString()).replaceAll("&a", (new StringBuilder()).append(ChatColor.GREEN).toString())
			        .replaceAll("&9", (new StringBuilder()).append(ChatColor.BLUE).toString()).replaceAll("&8", (new StringBuilder()).append(ChatColor.DARK_GRAY).toString())
			        .replaceAll("&7", (new StringBuilder()).append(ChatColor.GRAY).toString()).replaceAll("&6", (new StringBuilder()).append(ChatColor.GOLD).toString())
			        .replaceAll("&5", (new StringBuilder()).append(ChatColor.DARK_PURPLE).toString()).replaceAll("&4", (new StringBuilder()).append(ChatColor.DARK_RED).toString())
			        .replaceAll("&3", (new StringBuilder()).append(ChatColor.DARK_AQUA).toString()).replaceAll("&2", (new StringBuilder()).append(ChatColor.DARK_GREEN).toString())
			        .replaceAll("&1", (new StringBuilder()).append(ChatColor.DARK_BLUE).toString()).replaceAll("&0", (new StringBuilder()).append(ChatColor.BLACK).toString());
	}
}
