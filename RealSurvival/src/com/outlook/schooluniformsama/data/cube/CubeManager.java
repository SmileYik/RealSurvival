package com.outlook.schooluniformsama.data.cube;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.event.basic.ClickCubeListener;

public class CubeManager {
	private static CubeManager           manager;
	private final RealSurvival           plugin;

	private ArrayList<BlockCubeData> cubes = new ArrayList<>();

	public CubeManager(RealSurvival plugin){
		manager = this;
		this.plugin = plugin;
		loadCubes();

		plugin.getServer().getPluginManager().registerEvents(new ClickCubeListener(plugin),plugin);
	}

	private void loadCubes(){
		if(!BlockCubeData.getPath().exists()) {
			BlockCubeData.getPath().mkdirs();
		}
		for(File sf:BlockCubeData.getPath().listFiles()){
			if(sf.isFile()){
				String fileName=sf.getName();
				if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				cubes.add(BlockCubeData.load(fileName.substring(0, fileName.lastIndexOf("."))));
			}
		}
		plugin.getLogger().info("Loaded "+cubes.size()+" cubes");
	}
	
	public static void reloadCubes() {
		manager.cubes.clear();
		for(File sf:BlockCubeData.getPath().listFiles()){
			if(sf.isFile()){
				String fileName=sf.getName();
				if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				manager.cubes.add(BlockCubeData.load(fileName.substring(0, fileName.lastIndexOf("."))));
			}
		}
	}
	
	public static ArrayList<BlockCubeData> getCubeByBlock(String block) {
		ArrayList<BlockCubeData> returnCubes = new ArrayList<BlockCubeData>();
		for(BlockCubeData cube : manager.cubes) {
			if(cube.getMid().getMain().equals(block))
				returnCubes.add(cube);
		}
		return returnCubes;
	}
	
	@SuppressWarnings("deprecation")
	public static String getBlock(Block block) {
		return (block.getType().name()+":"+block.getData()).toUpperCase();
	}

	public static Plugin getPlugin(){
		return (Plugin) manager.plugin;
	}
}
