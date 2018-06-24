package com.outlook.schooluniformsama;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.outlook.schooluniformsama.command.Commands;
import com.outlook.schooluniformsama.api.ReaLSurvivalAPI;
import com.outlook.schooluniformsama.command.CommandsType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.WorkbenchShape;
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
import com.outlook.schooluniformsama.event.DamageEvent;
import com.outlook.schooluniformsama.event.EnergyEvent;
import com.outlook.schooluniformsama.event.FractureEvent;
import com.outlook.schooluniformsama.event.SitEvent;
import com.outlook.schooluniformsama.event.SleepDuringTheDay;
import com.outlook.schooluniformsama.event.SleepEvent;
import com.outlook.schooluniformsama.event.ThirstEvent;
import com.outlook.schooluniformsama.event.basic.*;
import com.outlook.schooluniformsama.lowversion.Commands_1_8;
import com.outlook.schooluniformsama.lowversion.CraftItemEvent_1_8;
import com.outlook.schooluniformsama.lowversion.ThirstEvent_1_7;
import com.outlook.schooluniformsama.lowversion.ThirstEvent_1_8;
import com.outlook.schooluniformsama.lowversion.UseItemEvent_1_8;
import com.outlook.schooluniformsama.lowversion.converter.Converter;
import com.outlook.schooluniformsama.nms.bed.*;
import com.outlook.schooluniformsama.nms.item.*;
import com.outlook.schooluniformsama.papi.Papi;
import com.outlook.schooluniformsama.randomday.RandomDayTask;
import com.outlook.schooluniformsama.task.EffectTask;
import com.outlook.schooluniformsama.task.EnergyTask;
import com.outlook.schooluniformsama.task.SaveConfigTask;
import com.outlook.schooluniformsama.task.SickTask;
import com.outlook.schooluniformsama.task.SleepTask;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.task.ThirstTask;
import com.outlook.schooluniformsama.task.WeightTask;
import com.outlook.schooluniformsama.task.WorkbenchTask;
import com.outlook.schooluniformsama.update.Update;
import com.outlook.schooluniformsama.util.ArrayList;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.bstats.Metrics_1_8_UP;


import com.outlook.schooluniformsama.util.bstats.Metrics;
import com.outlook.schooluniformsama.util.bstats.Metrics_1_7;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;

public class RealSurvival extends JavaPlugin implements ReaLSurvivalAPI{
	
	private Object cmds;
	private CommandsType cmdsType;
	
	@Override
	public void onEnable() {
		firstLoad();
		getNMS();
		loadConfig();
		registerListeners();
		if(Data.versionData[0]<=7)addPlayers_LOW_VERSION(); else addPlayers();
		setupMetrics();
		getLogger().info("[RealSurvival] Successful loading");
		checkUp();
	}
	
	@Override
	public void onDisable() {
		for(PlayerData pd : Data.playerData.values())
			pd.save();
		SaveConfigTask.saveWorkbench();
	}
	
	public static String getVersion(){
		String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return null;
        }
        return version;
	}
	
	private void checkUp(){
		Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				try {
					Update u= Update.getUpdate("update");
					if(u.hasUpdate()){
						getLogger().info(ChatColor.translateAlternateColorCodes('§',Msg.getMsg("HasNewVersion", new String[]{"%now-version%","%version%"},new String[]{Update.now_version_show,u.getVersion_show()},false))+" "
								+(u.isReplace_config()?Msg.getMsg("WillDeleteConfig", false):"")+" "
								+(u.isReplace_message()?Msg.getMsg("WillDeleteMessages", false):""));
						getLogger().info(ChatColor.translateAlternateColorCodes('§',u.getUpdate_info()));
					}
				} catch (Exception e) {
					getLogger().info(Msg.getMsg("CheckUpdateFailed", false));
				}
			}
		}, 20L);
	}
	
	private void getNMS(){
		switch (getVersion()) {
		case "v1_11_R1":
			Data.bnms=new BED_1_11_R1();
			Data.nbtitem=new Item_1_11_R1();
			break;
		case "v1_12_R1":
			Data.bnms=new BED_1_12_R1();
			Data.nbtitem=new Item_1_12_R1();
			break;
		case "v1_10_R1":
			Data.bnms=new BED_1_10_R1();
			Data.nbtitem=new Item_1_10_R1();
			break;
		case "v1_9_R1":
			Data.bnms=new BED_1_9_R1();
			Data.nbtitem=new Item_1_9_R1();
			break;
		case "v1_9_R2":
			Data.bnms=new BED_1_9_R2();
			Data.nbtitem=new Item_1_9_R2();
			break;
		case "v1_8_R1":
			Data.bnms=new BED_1_8_R1();
			Data.nbtitem=new Item_1_8_R1();
			break;
		case "v1_8_R2":
			Data.bnms=new BED_1_8_R2();
			Data.nbtitem=new Item_1_8_R2();
			break;
		case "v1_8_R3":
			Data.bnms=new BED_1_8_R3();
			Data.nbtitem=new Item_1_8_R3();
			break;
		case "v1_7_R4":
			Data.bnms=new BED_1_7_R4();
			Data.nbtitem=new Item_1_7_R4();
		default:
			break;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("rs") || label.equalsIgnoreCase("RealSurvival")){
			if(args.length==0 ||  args[0].equalsIgnoreCase("help")){
				String help = "HELP";
				if(args.length<=1)
					sender.sendMessage(Msg.getMsg("Help1", true));
				else{
					sender.sendMessage(Msg.getMsg("Help2", new String[]{"%type%"}, new String[]{args[1]}, true));
					help = args[1].toLowerCase();
				}
				for(Method method:cmdsType.getClazz().getDeclaredMethods()){
					if(!method.isAnnotationPresent(com.outlook.schooluniformsama.command.Command.class))
						continue;
					com.outlook.schooluniformsama.command.Command cmd=method.getAnnotation(com.outlook.schooluniformsama.command.Command.class);
					if(!cmd.type().equalsIgnoreCase(help))
						continue;
					if( !(cmd.permissions().equals("") || sender.hasPermission(cmd.permissions())))
						continue;
					String arg="";
					if(cmd.hasChildCmds()){
						for(String cc : cmd.childCmds())
							arg+=cc+" ";
					}
					if(cmd.args()[0]!="")
						for(String temp:cmd.args())
							arg+=temp+" ";
					String text = "&c&l/rs &3&l"+cmd.cmd()+" &3"+arg+"&f- &b&l"+Msg.getMsg(cmd.des(),false);
					while(text.contains("  ")){
						text=text.replace("  ", " ");
					}
					 sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"  "+text)); 
				}
				return true;
			}
			
			for(Method method:cmdsType.getClazz().getDeclaredMethods()){
				if(!method.isAnnotationPresent(com.outlook.schooluniformsama.command.Command.class))
					continue;
				com.outlook.schooluniformsama.command.Command cmd=method.getAnnotation(com.outlook.schooluniformsama.command.Command.class);
				if(cmd.argsLenght()>0&&cmd.argsLenght()!=args.length)continue;
				if(!cmd.cmd().equalsIgnoreCase(args[0]))continue;
				if(cmd.hasChildCmds()){
					int i = 1;
					for(String cc:cmd.childCmds())if(!args[i++].equalsIgnoreCase(cc)){i=-1; break;}
					if(i==-1)continue;
				}
				if( !(cmd.permissions()=="" || sender.hasPermission(cmd.permissions()))){
					sender.sendMessage(Msg.getMsg("NoPermissions", true));
					return true;
				}
				if(!(sender instanceof Player)&&cmd.needPlayer()){
					sender.sendMessage(Msg.getMsg("IsNotPlayer", true));
					return true;
				}	
				//((Player)sender).openInventory(FeatureGUI.openRecipeViewer());
				try {method.invoke(cmds, (sender instanceof Player)?(Player)sender:sender,args);}catch (Exception e) { e.printStackTrace();}
				return true;
			}
			sender.sendMessage(Msg.getMsg("CommandNotFind", true));
			return true;
		}
		return false;
	}
	
	private void firstLoad(){
		Data.DATAFOLDER=getDataFolder()+"";
		if(!getDataFolder().exists()) 
	        getDataFolder().mkdir();
		if(!new File(getDataFolder()+File.separator+"playerdata").exists())
			new File(getDataFolder()+File.separator+"playerdata").mkdir();
		if(!new File(getDataFolder()+File.separator+"items").exists())
			new File(getDataFolder()+File.separator+"items").mkdir();
		if(!new File(getDataFolder()+File.separator+"nbtitem").exists())
			new File(getDataFolder()+File.separator+"nbtitem").mkdir();
		if(!new File(getDataFolder()+File.separator+"recipe/furnace").exists())
			new File(getDataFolder()+File.separator+"recipe/furnace").mkdirs();
		if(!new File(getDataFolder()+File.separator+"recipe/workbench").exists())
			new File(getDataFolder()+File.separator+"recipe/workbench").mkdirs();
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
		
		
		if(!new File(getDataFolder()+File.separator+"messages.yml").exists()){
			Msg.writerYml(getConfig().getString("language"));
		}else if(YamlConfiguration.loadConfiguration(new File(getDataFolder()+File.separator+"messages.yml")).getString("language",null)==null || !YamlConfiguration.loadConfiguration(new File(getDataFolder()+File.separator+"messages.yml")).getString("language",null).equalsIgnoreCase(getConfig().getString("language"))){
			Msg.writerYml(getConfig().getString("language"));
		}
		Msg.init();
		
		
		if(!new File(getDataFolder()+File.separator+"timer.yml").exists())
			try {new File(getDataFolder()+File.separator+"timer.yml").createNewFile();}catch (IOException e1) {}
		
		Data.versionData = new int[]{Integer.parseInt(RealSurvival.getVersion().split("_")[1]),Integer.parseInt(RealSurvival.getVersion().split("_")[1].replace("R", ""))};
		if(Data.versionData[0]>8){
			cmds = new Commands(this);
			cmdsType = CommandsType.Commands_1_9_UP;
		}else if(Data.versionData[0]<=8){
			cmds = new Commands_1_8(this);
			cmdsType = CommandsType.Commands_1_8;
		}
		
		
	}
	
	private void loadConfig(){
		
		new Converter().check();
		
		Data.worlds=getConfig().getStringList("worlds");
		Data.enablePrefixInTitle = getConfig().getBoolean("enable-prefix-in-title");
		Data.stateCD = getConfig().getLong("state-cd");
		
		Data.switchs[0]=getConfig().getBoolean("create-random-data.enable");
		Data.switchs[1]=getConfig().getBoolean("death.enable");
		Data.switchs[2]=getConfig().getBoolean("state.sleep.enable");
		Data.switchs[3]=getConfig().getBoolean("state.thirst.enable");
		Data.switchs[4]=getConfig().getBoolean("state.energy.enable");
		Data.switchs[5]=getConfig().getBoolean("state.fracture.enable");
		Data.switchs[6]=getConfig().getBoolean("state.illness.enable");
		Data.switchs[7]=getConfig().getBoolean("state.weight.enable");
		Data.switchs[8]=getConfig().getBoolean("state.temperature.enable");
		
		if(Data.switchs[0])
			Data.randomData=new int[]{
					getConfig().getInt("create-random-data.min"), 
					getConfig().getInt("create-random-data.max")};
		
		if(Data.switchs[1])
			Data.deathData=new double[]{
					getConfig().getDouble("death.sleep"),
					getConfig().getDouble("death.thirst"),
					getConfig().getDouble("death.energy"),
					getConfig().getDouble("death.temperature"),
					getConfig().getBoolean("death.illness")?1:2};
		
		if(Data.switchs[2])
			Data.sleep=new double[]{
					getConfig().getDouble("state.sleep.max"),
					getConfig().getDouble("state.sleep.mid"),
					getConfig().getDouble("state.sleep.min"),
					getConfig().getDouble("state.sleep.sub"),
					getConfig().getDouble("state.sleep.add")};
		
		if(Data.switchs[3])
			Data.thirst=new double[]{
					getConfig().getDouble("state.thirst.max"),
					getConfig().getDouble("state.thirst.mid"),
					getConfig().getDouble("state.thirst.min"),
					getConfig().getDouble("state.thirst.sub")};
		
		if(Data.switchs[4])
			Data.energy=new double[]{
					getConfig().getDouble("state.energy.max"),
					getConfig().getDouble("state.energy.min"),
					getConfig().getDouble("state.energy.add"),
					getConfig().getDouble("state.energy.sneaking"),
					getConfig().getDouble("state.energy.sprinting"),
					getConfig().getDouble("state.energy.jumping"),
					getConfig().getDouble("state.energy.swimming")};
		
		if(Data.switchs[5])
			Data.fracture=new double[]{
					getConfig().getDouble("state.fracture.slight.high"),
					getConfig().getDouble("state.fracture.slight.chance"),
					getConfig().getDouble("state.fracture.severe.high"),
					getConfig().getDouble("state.fracture.severe.chance"),
					getConfig().getDouble("state.fracture.damage"),
					getConfig().getDouble("state.fracture.slight-chance"),
					getConfig().getDouble("state.fracture.severe-chance")};
		
		if(Data.switchs[6])
			Data.defualtIllness=getConfig().getStringList("state.illness.default-illness").toArray(
					new String[getConfig().getStringList("state.illness.default-illness").size()]);
		
		if(Data.switchs[7]){
			Data.weight=getConfig().getDouble("state.weight.max");
			for(String items:getConfig().getStringList("state.weight.item"))
				Data.itemData.put(items.split(":")[0], ItemData.createData(Double.parseDouble(items.split(":")[1]), 0));
		}
		
		if(Data.switchs[8]){
			Data.temperature=new double[]{
					getConfig().getDouble("state.temperature.long"),
					getConfig().getDouble("state.temperature.width"),
					getConfig().getDouble("state.temperature.high"),
					getConfig().getDouble("state.temperature.heat-source-fix"),
					getConfig().getDouble("state.temperature.distance-effect"),
					getConfig().getDouble("state.temperature.cold"),
					getConfig().getDouble("state.temperature.fever")};
			for(String items:getConfig().getStringList("state.temperature.heat-source"))
				if(Data.itemData.containsKey(items.split(":")[0]))
					Data.itemData.get(items.split(":")[0]).setHeat(Double.parseDouble(items.split(":")[1]));
				else
					Data.itemData.put(items.split(":")[0], ItemData.createData(0,Double.parseDouble(items.split(":")[1])));
		}
		
		Data.split=getConfig().getString("label.split");
		for(String label:getConfig().getStringList("label.labels"))
			Data.label.put(label.split(":")[0].toLowerCase(), label.split(":")[1]);

		for(String workbench:getConfig().getStringList("workbenchs.workbenchs-type")){
			String name=workbench.split(":")[0],type=workbench.split(":")[1];
			Data.workbenchs.put(name, new WorkbenchShape(WorkbenchType.valueOf(type), name, 
							getConfig().getString("workbenchs.workbenchs."+name+".title"),
							getConfig().getString("workbenchs.workbenchs."+name+".main-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".left-block"), 
							getConfig().getString("workbenchs.workbenchs."+name+".right-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".up-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".down-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".front-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".behind-block")));
		}
		
		for(String food:getConfig().getStringList("effect.food-effects.foods")){
			Data.foodEffect.put(food,new Food(
					getConfig().getString("effect.food-effects.effects."+food+".sleep"),
					getConfig().getString("effect.food-effects.effects."+food+".thirst"),
					getConfig().getString("effect.food-effects.effects."+food+".energy"),
					getConfig().getDouble("effect.food-effects.effects."+food+".temperature"),
					getConfig().getStringList("effect.food-effects.effects."+food+".illnesses"),
					getConfig().getBoolean("effect.food-effects.effects."+food+".has-illness")));
		}
		for(String mob:getConfig().getStringList("effect.mob-effects"))
			Data.mobEffect.put(mob.split(":")[0], new Mob(mob.split(":")[0], mob.split(":")[1]));
		for(String effect:getConfig().getStringList("effect.illness-effects")){
			ArrayList<Effect> l=new ArrayList<>();
			for(String effects:effect.split(":")[1].split(";"))l.add(new Effect(effects));
			Data.illnessEffects.put(effect.split(":")[0],l );
		}
		for(String strainer:getConfig().getStringList("strainer"))
			Data.strainer.put(strainer.split(":")[0], Integer.parseInt(strainer.split(":")[1]));
		
		
		File path=new File(this.getDataFolder()+File.separator+"recipe/workbench/");
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				Data.workbenchRecipe.add(fileName.substring(0,fileName.lastIndexOf(".")));
		}
		path=new File(this.getDataFolder()+File.separator+"recipe"+File.separator+"furnace/");
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				Data.furnaceRecipe.add(fileName.substring(0,fileName.lastIndexOf(".")));
		}
		
		if(!new Items().createWater())
			getLogger().info(Msg.getMsg("WriterWaterFailed", false));
		
		this.getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				YamlConfiguration timer = YamlConfiguration.loadConfiguration(new File(getDataFolder()+File.separator+"timer.yml"));
				for(String key:timer.getStringList("list")){
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
			}
		}, 60);
		
	}
	
	//Load Online Player's Data
	private void addPlayers(){
		Iterator<? extends Player> ps= Bukkit.getOnlinePlayers().iterator();
	    while (ps.hasNext()){
	    	Player p = (Player)ps.next();
	    	Data.addPlayer(p);
	    }
	}
	
	//Load Online Player's Data
	private void addPlayers_LOW_VERSION(){
	    for(Player p : Bukkit.getOnlinePlayers())
	    	Data.addPlayer(p);
	}
	
	//Register Listeners
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new BasicEvent(), this);
		if(Data.versionData[0]>8)getServer().getPluginManager().registerEvents(new CraftItemEvent(), this);
		else getServer().getPluginManager().registerEvents(new CraftItemEvent_1_8(), this);
		if(Data.versionData[0]>8)getServer().getPluginManager().registerEvents(new UseItemEvent(), this);
		else getServer().getPluginManager().registerEvents(new UseItemEvent_1_8(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new EffectTask(this), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorkbenchTask(), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveConfigTask(), 20L, 600*20L);
		
		if(Data.switchs[2]){
			getServer().getPluginManager().registerEvents(new SleepEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SleepTask(), 20L, 1*20L);
			if(getConfig().getBoolean("state.sleep.sleep-during-the-day"))
				getServer().getPluginManager().registerEvents(new SleepDuringTheDay(), this);
		}
		if(Data.switchs[3]){
			if(Data.versionData[0]<=7) getServer().getPluginManager().registerEvents(new ThirstEvent_1_7(), this);
			else if(Data.versionData[0]<=8) getServer().getPluginManager().registerEvents(new ThirstEvent_1_8(), this);
			else getServer().getPluginManager().registerEvents(new ThirstEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ThirstTask(), 20L, 1*20L);
		}
		if(Data.switchs[4]){
			getServer().getPluginManager().registerEvents(new EnergyEvent() , this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new EnergyTask(), 20L, 20L);
		}
		if(Data.switchs[8]){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RandomDayTask(), 24000-this.getServer().getWorld(Data.worlds.get(0)).getTime(), 24000);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TemperatureTask(), 20L, 10*20L);
		}
		if(Data.switchs[7]){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WeightTask(), 20L, 1*20L);
		}
		if(Data.switchs[6]){
			getServer().getPluginManager().registerEvents(new DamageEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SickTask(), 20L, 1*20L);
		}
		if(Data.switchs[5]){
			getServer().getPluginManager().registerEvents(new FractureEvent(), this);
		}
		
		//PlaceholderAPI
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new Papi(this).hook();
			getLogger().info("§9[RealSurvival] Successful loading PlaceholderAPI !");
		}
		//TitleManager
		if(Bukkit.getPluginManager().isPluginEnabled("TitleManager")){
			Data.tmapi = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
			getLogger().info("§9[RealSurvival] Successful loading TitleManager! ");
		}
		//Chairs
		if(Bukkit.getPluginManager().isPluginEnabled("Chairs")){
			getServer().getPluginManager().registerEvents(new SitEvent(), this);
			getLogger().info("§9[RealSurvival] Successful loading Chairs! ");
		}
	}
	
	public static Player getPlayer(UUID uuid){
		return Data.playerData.get(uuid).getPlayer();
	}
	
	private void setupMetrics(){
		@SuppressWarnings("unused")
		Metrics metrics;
		if(Data.versionData[0]<=7){
			 metrics = new Metrics_1_7(this);
		}else{
			 metrics = new Metrics_1_8_UP(this);
		}
	}
	
}
