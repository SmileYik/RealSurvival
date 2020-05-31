package com.outlook.schooluniformsama.data.cube;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.LinkedHashMap;

public class BlockCubeCreator {
	public static LinkedHashMap<String,BlockCubeCreator> creators = 
			new LinkedHashMap<String,BlockCubeCreator>();
	
	private String name = null;
	private Location mainBlockLoc;
	private ItemStack item;
	private boolean checkItem = false;
	private boolean checkCompass = false;

	public enum ReturnCode {
		BlockCubeExist,
		CreateSuccess,
		ChooseMainBlock,
		Creating,
		CheckCompass,
		RemoveItem,
		SetItemSuccess,
		Stop,
		Start,
		SetName,
		NoneCreator,
		NoneMainBlock,
		NoneItem,
		NoneName;

		public String getMsgCode(){
			return "BlockCubeCreator."+this.name();
		}
	}

	public BlockCubeCreator() {
		
	}

	public static ReturnCode start(String playerName) {
		if(creators.containsKey(playerName)) return ReturnCode.Creating;
		creators.put(playerName, new BlockCubeCreator());
		return ReturnCode.Start;
	}

	public static ReturnCode stop(String playerName) {
		creators.remove(playerName);
		return ReturnCode.Stop;
	}

	public static ReturnCode removeItem(String playerName) {
		if(!creators.containsKey(playerName)) return ReturnCode.NoneCreator;
		BlockCubeCreator creator = creators.get(playerName);
		creator.item = null;
		creator.checkItem = false;
		return ReturnCode.RemoveItem;
	}

	public static ReturnCode finish(String playerName){
		BlockCubeCreator creator = creators.get(playerName);
		if(creator == null)
			return ReturnCode.NoneCreator;
		if(creator.mainBlockLoc == null)
			return ReturnCode.NoneMainBlock;
		if(creator.checkItem && (creator.item==null ||creator.item.getType() == Material.AIR))
			return ReturnCode.NoneItem;
		if(creator.name == null || creator.name.isEmpty())
			return ReturnCode.NoneName;
		if(new File(BlockCubeData.getPath()+"/"+creator.name+".yml").exists())
			return ReturnCode.BlockCubeExist;

		Block main = creator.mainBlockLoc.getBlock();
		BlockArrayData mid = getBlockShape(main),
				up = getBlockShape(main.getRelative(BlockFace.UP)),
				down = getBlockShape(main.getRelative(BlockFace.DOWN));
		BlockCubeData cube = new BlockCubeData(up, mid, down,
				creator.name,
				creator.item,
				creator.checkCompass,
				creator.checkItem);
		cube.save();
		creators.remove(playerName);
		return ReturnCode.CreateSuccess;
	}

	public static ReturnCode chooesBlock(String playerName, Listener listener, Plugin plugin) {
		if(!creators.containsKey(playerName)) return ReturnCode.NoneCreator;
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		return ReturnCode.ChooseMainBlock;
	}

	public static void chooesBlock(String playerName,Location mainBlockLoc) {
		creators.get(playerName).mainBlockLoc = mainBlockLoc;
	}

	public static ReturnCode setItem(String playerName, ItemStack item) {
		if(!creators.containsKey(playerName)) return ReturnCode.NoneCreator;
		BlockCubeCreator creator = creators.get(playerName);
		creator.item = item;
		creator.checkItem = true;
		return ReturnCode.SetItemSuccess;
	}

	public static ReturnCode checkCompass(String playerName, boolean bool) {
		if(!creators.containsKey(playerName)) return ReturnCode.NoneCreator;
		creators.get(playerName).checkCompass = bool;
		return ReturnCode.CheckCompass;
	}

	public static ReturnCode setName(String playerName, String name) {
		if(!creators.containsKey(playerName)) return ReturnCode.NoneCreator;
		creators.get(playerName).name = name;
		return ReturnCode.SetName;
	}

	private static BlockArrayData getBlockShape(Block block) {
		return new BlockArrayData(CubeManager.getBlock(block.getRelative(BlockFace.NORTH)),
				CubeManager.getBlock(block.getRelative(BlockFace.NORTH_WEST)),
				CubeManager.getBlock(block.getRelative(BlockFace.WEST)),
				CubeManager.getBlock(block.getRelative(BlockFace.SOUTH_WEST)),
				CubeManager.getBlock(block.getRelative(BlockFace.SOUTH)),
				CubeManager.getBlock(block.getRelative(BlockFace.SOUTH_EAST)),
				CubeManager.getBlock(block.getRelative(BlockFace.EAST)),
				CubeManager.getBlock(block.getRelative(BlockFace.NORTH_EAST)),
				CubeManager.getBlock(block));
	}

}
