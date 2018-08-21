package com.outlook.schooluniformsama.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.io.Files;
import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.effect.Effect;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.effect.Mob;
import com.outlook.schooluniformsama.data.item.ItemData;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.data.timer.Timer;
import com.outlook.schooluniformsama.data.timer.WorkbenchTimer;
import com.outlook.schooluniformsama.lowversion.converter.Converter;
import com.outlook.schooluniformsama.randomday.RandomDayManager;
import com.outlook.schooluniformsama.task.TemperatureTask;

public class Data {
	
	public static String DATAFOLDER;
	public static List<String> worlds;
	/**
	 * randomData &nbsp; 
	 * deathData &nbsp; 
	 * Sleep &nbsp; 
	 * Thirst &nbsp; 
	 * Energy &nbsp; 
	 * Fracture &nbsp; 
	 * Illness &nbsp; 
	 * Weight &nbsp; 
	 * Temperature &nbsp;
	 */
	public static boolean[] switchs = new boolean[9];
	/**
	 * int - min;&nbsp;
	 * int - max;&nbsp;
	 */
	public static int[] randomData;
	/**
	 * sleep(double) &nbsp; 
	 * thirst(double) &nbsp; 
	 * energy(double) &nbsp; 
	 * temperature(double) &nbsp; 
	 * illness(boolean 1=true 0=false) &nbsp;
	 */
	public static double[] deathData;
	/**
	 * max,mid,min,sub,add
	 */
	public static double[] sleep;
	/**
	 * max,mid,min,sub
	 */
	public static double[] thirst;
	/**
	 * max,min,add,sneaking,sprinting,jumping,swimming
	 */
	public static double[] energy;
	/**
	 * slight: high chance <p> 
	 * severe: high chance <p> 
	 * damage <p> 
	 * slight-chance <p> 
	 * severe-chance
	 */
	public static double[] fracture;
	/**
	 * slight fracture <p>
	 * severe fracture
	 */
	public static String[] fractureString;
	public static double weight;
	/**
	 * Key: ItemName Value: ItemData
	 */
	public static HashMap<String, ItemData> itemData=new HashMap<>();
	/**
	 * long,width,high,heat-source-fix,distance-effect cold fever extraTemperature
	 */
	public static double[] temperature;
	public static String split;
	public static HashMap<String, String> label=new HashMap<>();
	public static HashMap<String,WorkbenchShape> workbenchs=new HashMap<>();
	public static HashMap<String,Food> foodEffect=new HashMap<>();
	public static HashMap<String, Mob> mobEffect=new HashMap<>();
	public static HashMap<String,ArrayList<Effect>> illnessEffects=new HashMap<>();
	
	public static HashMap<UUID, PlayerData> playerData= new HashMap<>();
	public static HashMap<String, Timer> timer = new HashMap<>();
	public static ArrayList<String>workbenchRecipe=new ArrayList<>();
	public static ArrayList<String>furnaceRecipe=new ArrayList<>();
	
	public static boolean enablePrefixInTitle;
	public static HashMap<String, Integer> strainer=new HashMap<>();
	public static long stateCD;
	/**v1_[0]_R[1]*/
	public static int versionData[];
	
	public static void init(RealSurvival plugin){
		new Converter().check();
		
		LinkedList<String> worlds = new LinkedList<>();
		for(String world:plugin.getConfig().getStringList("worlds")){
			worlds.add(world.toLowerCase());
		}
		Data.worlds=worlds;
		
		Data.enablePrefixInTitle = plugin.getConfig().getBoolean("enable-prefix-in-title");
		Data.stateCD = plugin.getConfig().getLong("state-cd");
		
		Data.switchs[0]=plugin.getConfig().getBoolean("create-random-data.enable");
		Data.switchs[1]=plugin.getConfig().getBoolean("death.enable");
		Data.switchs[2]=plugin.getConfig().getBoolean("state.sleep.enable");
		Data.switchs[3]=plugin.getConfig().getBoolean("state.thirst.enable");
		Data.switchs[4]=plugin.getConfig().getBoolean("state.energy.enable");
		Data.switchs[5]=plugin.getConfig().getBoolean("state.fracture.enable");
		Data.switchs[6]=plugin.getConfig().getBoolean("state.illness.enable");
		Data.switchs[7]=plugin.getConfig().getBoolean("state.weight.enable");
		Data.switchs[8]=plugin.getConfig().getBoolean("state.temperature.enable");
		
		if(Data.switchs[0])
			Data.randomData=new int[]{
					plugin.getConfig().getInt("create-random-data.min"), 
					plugin.getConfig().getInt("create-random-data.max")};
		
		if(Data.switchs[1])
			Data.deathData=new double[]{
					plugin.getConfig().getDouble("death.sleep"),
					plugin.getConfig().getDouble("death.thirst"),
					plugin.getConfig().getDouble("death.energy"),
					plugin.getConfig().getBoolean("death.illness")?1:2};
		
		if(Data.switchs[2])
			Data.sleep=new double[]{
					plugin.getConfig().getDouble("state.sleep.max"),
					plugin.getConfig().getDouble("state.sleep.mid"),
					plugin.getConfig().getDouble("state.sleep.min"),
					plugin.getConfig().getDouble("state.sleep.sub"),
					plugin.getConfig().getDouble("state.sleep.add")};
		
		if(Data.switchs[3])
			Data.thirst=new double[]{
					plugin.getConfig().getDouble("state.thirst.max"),
					plugin.getConfig().getDouble("state.thirst.mid"),
					plugin.getConfig().getDouble("state.thirst.min"),
					plugin.getConfig().getDouble("state.thirst.sub")};
		
		if(Data.switchs[4])
			Data.energy=new double[]{
					plugin.getConfig().getDouble("state.energy.max"),
					plugin.getConfig().getDouble("state.energy.min"),
					plugin.getConfig().getDouble("state.energy.add"),
					plugin.getConfig().getDouble("state.energy.sneaking"),
					plugin.getConfig().getDouble("state.energy.sprinting"),
					plugin.getConfig().getDouble("state.energy.jumping"),
					plugin.getConfig().getDouble("state.energy.swimming")};
		
		if(Data.switchs[5]){
			Data.fracture=new double[]{
					plugin.getConfig().getDouble("state.fracture.slight.high"),
					plugin.getConfig().getDouble("state.fracture.slight.chance"),
					plugin.getConfig().getDouble("state.fracture.severe.high"),
					plugin.getConfig().getDouble("state.fracture.severe.chance"),
					plugin.getConfig().getDouble("state.fracture.damage"),
					plugin.getConfig().getDouble("state.fracture.slight-chance"),
					plugin.getConfig().getDouble("state.fracture.severe-chance")};
			Data.fractureString = new String[]{
					plugin.getConfig().getString("state.fracture.slight.name","Slight"),
					plugin.getConfig().getString("state.fracture.severe.name","Severe")
			};
			
		}
		
		if(Data.switchs[7]){
			Data.weight=plugin.getConfig().getDouble("state.weight.max");
			for(String items:plugin.getConfig().getStringList("state.weight.item"))
				Data.itemData.put(items.split(":")[0], ItemData.createData(Double.parseDouble(items.split(":")[1]), 0));
		}
		
		if(Data.switchs[8]){
			Data.temperature=new double[]{
					plugin.getConfig().getDouble("state.temperature.long"),
					plugin.getConfig().getDouble("state.temperature.width"),
					plugin.getConfig().getDouble("state.temperature.high"),
					plugin.getConfig().getDouble("state.temperature.heat-source-fix"),
					plugin.getConfig().getDouble("state.temperature.distance-effect"),
					plugin.getConfig().getDouble("state.temperature.cold"),
					plugin.getConfig().getDouble("state.temperature.fever"),
					plugin.getConfig().getDouble("extra-temperature-distance",1)};
			for(String items:plugin.getConfig().getStringList("state.temperature.heat-source"))
				if(Data.itemData.containsKey(items.split(":")[0]))
					Data.itemData.get(items.split(":")[0]).setHeat(Double.parseDouble(items.split(":")[1]));
				else
					Data.itemData.put(items.split(":")[0], ItemData.createData(0,Double.parseDouble(items.split(":")[1])));
		}
		
		new RandomDayManager(plugin);
		
		Data.split=plugin.getConfig().getString("label.split");
		for(String label:plugin.getConfig().getStringList("label.labels"))
			Data.label.put(label.split(":")[0].toLowerCase(), label.split(":")[1]);

		for(String workbench:plugin.getConfig().getStringList("workbenchs.workbenchs-type")){
			String name=workbench.split(":")[0],type=workbench.split(":")[1];
			Data.workbenchs.put(name, new WorkbenchShape(WorkbenchType.valueOf(type), name, 
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".title"),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".main-block"),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".left-block",null),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".right-block",null),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".up-block",null),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".down-block",null),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".front-block",null),
							plugin.getConfig().getString("workbenchs.workbenchs."+name+".behind-block",null)));
		}
		
		for(String food:plugin.getConfig().getStringList("effect.food-effects.foods")){
			Data.foodEffect.put(food,new Food(
					plugin.getConfig().getString("effect.food-effects.effects."+food+".sleep","0"),
					plugin.getConfig().getString("effect.food-effects.effects."+food+".thirst","0"),
					plugin.getConfig().getString("effect.food-effects.effects."+food+".energy","0"),
					plugin.getConfig().getStringList("effect.food-effects.effects."+food+".illnesses"),
					plugin.getConfig().getBoolean("effect.food-effects.effects."+food+".has-illness",false)));
		}
		for(String mob:plugin.getConfig().getStringList("effect.mob-effects"))
			Data.mobEffect.put(mob.split(":")[0], new Mob(mob.split(":")[0], mob.split(":")[1]));
		for(String effect:plugin.getConfig().getStringList("effect.illness-effects")){
			ArrayList<Effect> l=new ArrayList<>();
			for(String effects:effect.split(":")[1].split(";"))l.add(new Effect(effects));
			Data.illnessEffects.put(effect.split(":")[0],l );
		}
		for(String strainer:plugin.getConfig().getStringList("strainer"))
			Data.strainer.put(strainer.split(":")[0], Integer.parseInt(strainer.split(":")[1]));

		loadRecipes(Data.furnaceRecipe, new File(plugin.getDataFolder()+File.separator+"recipe"+File.separator+"furnace/"),"");
		loadRecipes(Data.workbenchRecipe, new File(plugin.getDataFolder()+File.separator+"recipe"+File.separator+"workbench/"),"");
		
		if(!new Items().createWater())
			plugin.getLogger().info(I18n.tr("water11"));
		
		plugin.getServer().getScheduler().runTaskLater(plugin, ()->{
			try{
				YamlConfiguration timer = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+File.separator+"timer.yml"));
				for(String key:timer.getKeys(false)){
					if(key.equals("list"))continue;
					Timer tt = new Timer(timer.getString(key+".workbenchName"),timer.getString(key+".playerName"), WorkbenchType.valueOf(timer.getString(key+".type")), 
							timer.getString(key+".worldName"), timer.getInt(key+".x"),  timer.getInt(key+".y"),  timer.getInt(key+".z"));
					switch(tt.getType()){
					case FURNACE:
						FurnaceTimer ft = tt.toFurnaceTimer();
						FurnaceRecipe fr = FurnaceRecipe.load(timer.getString(key+".recipeName"));
						ft.loadData(timer.getDouble(key+".extra"), timer.getBoolean(key+".isBad",false),timer.getInt(key+".time"));
						ft.start(fr, TemperatureTask.getBaseTemperature(tt.getLocation(),true));
						Data.timer.put(key, ft);
						break;
					case WORKBENCH:
						WorkbenchTimer wt = tt.toWorkbenchTimer();
						wt.continueStart(WorkbenchRecipe.load(timer.getString(key+".recipeName")),timer.getInt(key+".time"));
						Data.timer.put(key, wt);
						break;
					case RAINWATER_COLLECTOR:
						RainwaterCollectorTimer rct = tt.toRainwaterCollectorTimer();
						rct.start(timer.getInt(key+".time"));
						Data.timer.put(key, rct);
						break;
					}
				}
			}catch(Exception e){
				File timer = new File(plugin.getDataFolder()+File.separator+"timer.yml."+new Date().getTime());
				plugin.getLogger().severe("[RealSurvival] Load workbench's data failed! Backuping workbench's data!");
				try {
					Files.copy(new File(plugin.getDataFolder()+File.separator+"timer.yml"), timer);
				} catch (IOException e1) {
					plugin.getLogger().severe("[RealSurvival] Backuping workbench's data failed!");
				}
			}
		}, 20);
		
	}
	
	private static void loadRecipes(ArrayList<String> recipe,File path,String recipeName){
		for(File sf:path.listFiles()){
			if(sf.isFile()){
				String fileName=sf.getName();
				if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				recipe.add(recipeName+fileName.substring(0, fileName.lastIndexOf(".")));
			}else loadRecipes(recipe,sf,recipeName+sf.getName()+"/");
		}
	}
	
	public static void clear(){
		worlds.clear();
		itemData.clear();
		label.clear();
		workbenchs.clear();
		foodEffect.clear();
		mobEffect.clear();
		illnessEffects.clear();
		playerData.clear();
		timer.clear();
		workbenchRecipe.clear();
		furnaceRecipe.clear();
		strainer.clear();
	}
	
	public static void addPlayer(Player p){
		if(enableInWorld(p.getWorld().getName())&&!p.hasMetadata("NPC")&&!enableInPlayer(p.getUniqueId()))
			addPlayer(p.getUniqueId(), PlayerData.load(p.getUniqueId()),p.isSleeping());
	}
	
	public static boolean enableInWorld(String worldName){
		return worlds.contains(worldName.toLowerCase());
	}
	
	public static boolean enableInPlayer(UUID uuid){
		return playerData.containsKey(uuid);
	}
	
	public static PlayerData getPlayerData(UUID uuid){
		if(enableInPlayer(uuid))return playerData.get(uuid);
		return null;
	}
	
	public static void removePlayer(UUID uuid){
		if(!enableInPlayer(uuid))return;
		PlayerData pd = playerData.get(uuid);
		pd.setWorld();
		pd.save();
		playerData.remove(uuid);
	}
	
	public static void saveAllPlayerData(){
		for(PlayerData pd : Data.playerData.values())
			pd.save();
	}
	
	public static void addPlayer(UUID uuid,PlayerData pd,boolean sleeping){
		if(pd.isUnlimited())return;
		if(!sleeping)pd.getSleep().setHasSleep(false);
		pd.setWorld();
		Data.playerData.put(uuid, pd);
	}
	
	public static boolean isContainsTimer(String id){
		return timer.containsKey(id);
	}
}
