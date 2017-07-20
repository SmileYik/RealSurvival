package com.outlook.z815007320;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.api.RealSurvivalAPI;
import com.outlook.z815007320.commands.HelpText;
import com.outlook.z815007320.commands.ItemCMD;
import com.outlook.z815007320.commands.StateCMD;
import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.commands.WorkbenchCMD;
import com.outlook.z815007320.data.*;
import com.outlook.z815007320.event.*;
import com.outlook.z815007320.event.basic.*;
import com.outlook.z815007320.gui.CraftItemGUI;
import com.outlook.z815007320.papi.Papi;
import com.outlook.z815007320.task.*;
import com.outlook.z815007320.utils.Utils;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;

public class RealSurvival extends MainDatas implements RealSurvivalAPI{
	
	@Override
	public void onEnable() {
		firstRun();
		try {readConfigs();} catch (Exception e) {}
		addPlayers();
		registerListeners();
		getLogger().info("成功加载"+SF.size()+"个工作台配方, "+FCTR.size()+"个熔炉配方, "+
				effects.size()+"个效果"+heatSource.size()+"个热(冷)源! By: School_Uniform");
	}
	
	@Override
	public void onDisable() {
		Set<UUID> players=playersDatas.keySet();
		for(UUID ps:players)
	    	getPlayerData( getServer().getPlayer(ps)).savaData();
		Utils.saveWorkbench();
		unregisterListeners();
	}
	
	@Override
	public boolean onCommand(CommandSender cmd, Command cmds, String label, String[] args) {
		if (!(cmd instanceof Player))
	    { 
			if(args.length==0){
	    	cmd.sendMessage("只有玩家才能执行此命令!");
	    	return true;
			}
			if(args[0].equalsIgnoreCase("Reload")){
				this.reloadConfigs();
				cmd.sendMessage("§9[RealSurvival] §b配置文件重载成功!...");
				return true;
			}
	    }
		Player player = (Player)cmd;
		if(label.equalsIgnoreCase("rs")){
			if (args.length == 1)
		    {
				if(args[0].equalsIgnoreCase("state")){
					player.sendMessage(this.getPlayerData(player).getData());
					return true;
				}else if(args[0].equalsIgnoreCase("help")){
					if(player.hasPermission("RealSurvival.Admin")){
						player.sendMessage(HelpText.getMainHelpText());
						return true;
					}else{
						player.sendMessage("§9===================§bRealSurvival§9===================\n"
															+ "§b/rs help - 查看帮助\n"
															+ "§b/rs state - 查看自身状态\n"
															+ "§9==================================================");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("Reload")&&player.hasPermission("RealSurvival.Admin")){
					this.reloadConfigs();
					player.sendMessage("§9[RealSurvival] §b配置文件重载成功!...");
					return true;
				}
		    }else if(args.length == 2){
		    	if(args[0].equalsIgnoreCase("test")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getTestHelp());
		    			return true;
		    		}
		    		if(args[1].equalsIgnoreCase("open"))
		    			return TestCMD.openTestEvent(player);
		    		if(args[1].equalsIgnoreCase("close"))
		    			return TestCMD.closeTestEvent(player);
		    		if(args[1].equalsIgnoreCase("on"))
		    			return TestCMD.onValue(player);
		    		if(args[1].equalsIgnoreCase("off"))
		    			return TestCMD.offValue(player);
		    	}
		    }
			
			if(args.length >= 2){
				//item 指令集
		    	if(args[0].equalsIgnoreCase("item")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("lore")){
		    			ItemCMD.addLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("setlore")){
		    			ItemCMD.setLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("removelore")){
		    			ItemCMD.removeLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("setName")){
		    			ItemCMD.setName(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("save")){
		    			ItemCMD.saveItem(player, args[2]);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("list")){
		    			player.sendMessage(ItemCMD.getItemList());
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("get")){
		    			ItemStack is=ItemCMD.getItem(player, args[2]);
		    			if(is==null)return false;
		    			int amount=1;
		    			if(args.length==4)
		    				try {
								amount=Integer.parseInt(args[3]);
							} catch (Exception e) {
								player.sendMessage("§9[RealSurvival] §b数量错误!...");
							}
		    			for(int i=0;i<amount;i++)
		    				player.getInventory().addItem(is);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("give")){
		    			Player p=this.getServer().getPlayer(args[2]);
		    			if(p==null||(!p.isOnline())){
		    				player.sendMessage("§9[RealSurvival] §b指定玩家不在线,,,");
		    				return false;
		    			}
		    			ItemStack is=ItemCMD.getItem(player, args[3]);
		    			if(is==null)return false;
		    			int amount=1;
		    			if(args.length==5)
		    				try {
								amount=Integer.parseInt(args[4]);
							} catch (Exception e) {
								player.sendMessage("§9[RealSurvival] §b数量错误!...");
							}
		    			for(int i=0;i<amount;i++)
	    					p.getInventory().addItem(is);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getItemHelp());
		    			return true;
		    		}
		    	}else if(args[0].equalsIgnoreCase("wb")&&player.hasPermission("RealSurvival.Admin")){
		    		//workbench指令集
		    		if(args[1].equalsIgnoreCase("csf")){
		    			if(args.length!=4)return false;
						 return WorkbenchCMD.createSF(player, args[2], args[3]);
						//重新设定配方
					}else if(args[1].equalsIgnoreCase("resetsf")){
						if(args.length!=4)return false;
						return WorkbenchCMD.resetSF(player, args[2], args[3]);
					}else if(args[1].equalsIgnoreCase("openwb")){
						if(args.length!=3)return false;
						WorkbenchCMD.openTestWorkbench(player, args[2]);
						return true;
					}else if(args[1].equalsIgnoreCase("openSFList")){
						WorkbenchCMD.openSFList(player);
						return true;
					}else if(args[1].equalsIgnoreCase("setBlock")){
						if(args.length!=4)return false;
						return WorkbenchCMD.setWorkbench(args[2], args[3]);
					}else if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getWorkbenchHelp());
		    			return true;
					}else if(args[1].equalsIgnoreCase("csff")){
						if(args.length!=6)return false;
						 return WorkbenchCMD.createFCTR(player, args[2], args[3],args[4],args[5]);
					}
		    	}else if(args[0].equalsIgnoreCase("setSwitch")&&player.hasPermission("RealSurvival.Admin")){
		    		//插件功能开关指令集
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getSetSwitchHelp());
		    			return true;
		    		}
		    		if(args.length<3)return false;
		    		if(!(args[1].equals("Sleep")||args[1].equals("Thirst")||
		    				args[1].equals("PhysicalStrength")||args[1].equals("Temperature")||
		    				args[1].equals("Weight")||args[1].equals("Sick")||
		    				args[2].equalsIgnoreCase("true")||args[2].equalsIgnoreCase("false"))){
		    			player.sendMessage(HelpText.getSetSwitchHelp());
		    			return false;
		    		}
		    		if(args[2].equalsIgnoreCase("true"))
		    			this.getConfig().set("Switch."+args[1], true);
		    		else
		    			this.getConfig().set("Switch."+args[1], false);
		    		try {
		    			this.getConfig().save(this.getDataFolder()+File.separator+"config.yml");
		    		} catch (IOException e) {
		    			return false;
		    		}
		    		return true;
		    	}else if(args[0].equalsIgnoreCase("state")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getStateHelp());
		    			return true;
		    		}else 
		    			return StateCMD.state(args, player, this);
		    	}
		    }
		}
		return false;
	}
	
	/**
	 * 为第一次运行做准备
	 */
	private void firstRun(){
		if(!getDataFolder().exists()) 
	        getDataFolder().mkdir();
		if(!new File(getDataFolder()+File.separator+"PlayerDatas").exists())
			new File(getDataFolder()+File.separator+"PlayerDatas").mkdir();
		if(!new File(getDataFolder()+File.separator+"Items").exists())
			new File(getDataFolder()+File.separator+"Items").mkdir();
		if(!new File(getDataFolder()+File.separator+"SyntheticFormula").exists())
			new File(getDataFolder()+File.separator+"SyntheticFormula").mkdir();
		if(!new File(getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable").exists())
			new File(getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable").mkdir();
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		if(!new File(getDataFolder()+File.separator+"Messages.yml").exists())
			this.saveResource("Messages.yml", true);
		if(!new File(getDataFolder()+File.separator+"Workbench.yml").exists())
			try {new File(getDataFolder()+File.separator+"Workbench.yml").createNewFile();}
			catch (IOException e1) {}
		try{reloadConfig();}catch (Exception e){}
		PluginRS.rs=this;
	}
	/**
	 * 重载所有配置
	 */
	public void reloadConfigs(){
		unregisterListeners();
		heatSource.clear();
		playersDatas.clear();
		SF.clear();
		effects.clear();
		loreTabel .clear();
		messages .clear();
		itemWeight .clear();
		mob.clear();
		workbench.clear();
		worlds.clear();
		FCTR.clear();
		firstRun();
		try {readConfigs();} catch (Exception e) {}
		
		registerListeners();
		getLogger().info("§9[RealSurvival] 成功加载"+SF.size()+"个配方,"+
				effects.size()+"个效果"+heatSource.size()+"个热(冷)源! By: School_Uniform");
	}
	private void readConfigs() throws Exception{
		sleepDatas[0]=getConfig().getDouble("Sleep.Max");
		sleepDatas[1]=getConfig().getDouble("Sleep.Mid")*sleepDatas[0];
		sleepDatas[2]=getConfig().getDouble("Sleep.Min")*sleepDatas[0];
		sleepDatas[3]=getConfig().getDouble("Sleep.Sub");
		sleepDatas[4]=getConfig().getDouble("Sleep.Add");
		thirstDatas[0]=getConfig().getDouble("Thirst.Max");
		thirstDatas[1]=getConfig().getDouble("Thirst.Mid")*thirstDatas[0];
		thirstDatas[2]=getConfig().getDouble("Thirst.Min")*thirstDatas[0];
		thirstDatas[3]=getConfig().getDouble("Thirst.Sub");
		psDatas[0]=getConfig().getDouble("PhysicalStrength.Max");
		psDatas[1]=getConfig().getDouble("PhysicalStrength.Min")*psDatas[0];
		psDatas[2]=getConfig().getDouble("PhysicalStrength.Add");
		psDatas[3]=getConfig().getDouble("PhysicalStrength.Sprinting");
		psDatas[4]=getConfig().getDouble("PhysicalStrength.Sneaking");
		weight=getConfig().getDouble("Weight.Max");
		workbenchItems[0]=getConfig().getString("Workbench.Main");//主方块
		workbenchItems[1]=getConfig().getString("Workbench.Left");//左
		workbenchItems[2]=getConfig().getString("Workbench.Right");//右
		workbenchItems[3]=getConfig().getString("Workbench.UP");//上
		workbenchItems[4]=getConfig().getString("Workbench.Down");//下
		workbenchItems[5]=getConfig().getString("Workbench.Front");//前
		workbenchItems[6]=getConfig().getString("Workbench.Behind");//后
		workbenchItems[7]=getConfig().getString("Workbench.Title");
		
		fireCraftTableItems[0]=getConfig().getString("FireCraftTable.Main");//主方块
		fireCraftTableItems[1]=getConfig().getString("FireCraftTable.Left");//左
		fireCraftTableItems[2]=getConfig().getString("FireCraftTable.Right");//右
		fireCraftTableItems[3]=getConfig().getString("FireCraftTable.UP");//上
		fireCraftTableItems[4]=getConfig().getString("FireCraftTable.Down");//下
		fireCraftTableItems[5]=getConfig().getString("FireCraftTable.Front");//前
		fireCraftTableItems[6]=getConfig().getString("FireCraftTable.Behind");//后
		fireCraftTableItems[7]=getConfig().getString("FireCraftTable.Title");
		heatSourceFix=getConfig().getDouble("FireCraftTable.HeatSourceFix");
		
		onDeath[0]=getConfig().getDouble("OnDeath.Sleep")*sleepDatas[0];
		onDeath[1]=getConfig().getDouble("OnDeath.Thirst")*thirstDatas[0];
		onDeath[2]=getConfig().getDouble("OnDeath.PhysicalStrength")*psDatas[0];
		onDeath[3]=getConfig().getDouble("OnDeath.Temperature");
		onDeath[4]=getConfig().getDouble("OnDeath.IsSick");
		fracture[0]=getConfig().getDouble("Fracture.Slight.High");
		fracture[1]=getConfig().getDouble("Fracture.Slight.Chance");
		fracture[2]=getConfig().getDouble("Fracture.Severe.High");
		fracture[3]=getConfig().getDouble("Fracture.Severe.Chance");
		fracture[4]=getConfig().getDouble("Fracture.SlChance");
		fracture[5]=getConfig().getDouble("Fracture.SeChance");
		fracture[6]=getConfig().getDouble("Fracture.Damage");
		defSick=getConfig().getString("Sick.SickName");
		worlds=getConfig().getStringList("Worlds");
		
		//添加热源
		for(String temp:getConfig().getStringList("HeatSource"))
			heatSource.put(temp.split(":")[0], Double.parseDouble(temp.split(":")[1]));
		
		//添加配方
		File path=new File(this.getDataFolder()+File.separator+"SyntheticFormula");
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
			SF.add(getSF(fileName.substring(0,fileName.lastIndexOf("."))));
		}
		
		path=new File(this.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable");
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
			FCTR.add((FireCraftTableRecipe.load(fileName.substring(0,fileName.lastIndexOf(".")))));
		}
		//添加效果
		for(String temp:getConfig().getStringList("Effect")){
			List<String> l=new LinkedList<String>();
			for(String temp2:temp.split(":")[1].split(";"))
				l.add(temp2);
			effects.put(temp.split(":")[0], l);
		}
		for(String temp:getConfig().getStringList("Lore"))
			loreTabel.put(temp.split(":")[0], temp.split(":")[1]);
		List<String> l1=YamlConfiguration.loadConfiguration(
				new File(getDataFolder()+File.separator+"Messages.yml")).getStringList("messages");
		for(String temp:l1)
			messages.put(temp.split(":")[0], temp.split(":")[1].replaceAll("%%", ":").replaceAll("%n", "\n"));
		for(String temp:getConfig().getStringList("Weight.Item"))
			itemWeight.put(temp.split(":")[0], Double.parseDouble(temp.split(":")[1]));
		for(String temp:getConfig().getStringList("Mob"))
			mob.put(temp.split(":")[0], temp.split(":")[1].replaceAll("%", ""));
		for(String temp:getConfig().getStringList("Sick.EatFood"))
			eatFood.put(temp.split(":")[0], temp.split(":")[1].replaceAll("%", ""));
		for(String temp:getConfig().getStringList("EatFood"))
			foods.put(temp.split(":")[0], temp.split(":")[1].replaceAll("%", ""));
		for(String temp:YamlConfiguration.loadConfiguration(new File(
				getDataFolder()+File.separator+"Workbench.yml")).getStringList("workbench"))
			workbench.put(temp.split(":")[0], temp.split(":")[1]);
		for(String temp:YamlConfiguration.loadConfiguration(new File(
				getDataFolder()+File.separator+"Workbench.yml")).getStringList("RainwaterCollector"))
			rainwaterCollector.put(temp.split(":")[0], temp.split(":")[1]);
		for(String temp:YamlConfiguration.loadConfiguration(new File(
				getDataFolder()+File.separator+"Workbench.yml")).getStringList("FireCraftTable"))
			fireCraftTable.put(temp.split(":")[0], temp.split(":")[1]);
	}
	
	//增加玩家
	private void addPlayers(){
		Iterator<? extends Player> ps= Bukkit.getOnlinePlayers().iterator();
	    while (ps.hasNext()){
	    	Player p = (Player)ps.next();
	    	if(worldExists(p.getWorld().getName())&&!p.hasMetadata("NPC"))
	    		playersDatas.put(p.getUniqueId(), Utils.getPlayerData(p));
	    }
	}
	//Register Listeners
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new BasicEvent(), this);
		getServer().getPluginManager().registerEvents(new CraftItemEvent(), this);
		getServer().getPluginManager().registerEvents(new UseItemEvent(), this);
		getServer().getPluginManager().registerEvents(new CreateWorkbenchEvent(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new EffectTask(), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorkBenchTask(), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveConfigTask(), 20L, 60*20L);
		
		if(getConfig().getBoolean("Switch.Sleep")){
			getServer().getPluginManager().registerEvents(new SleepEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SleepTask(), 20L, 1*20L);
		}
		if(getConfig().getBoolean("Switch.Thirst")){
			getServer().getPluginManager().registerEvents(new ThirstEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ThirstTask(), 20L, 1*20L);
		}
		if(getConfig().getBoolean("Switch.PhysicalStrength")){
			getServer().getPluginManager().registerEvents(new PhysicalStrengthEvent() , this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PhysicalStrengthTask(), 20L, 20L);
		}
		if(getConfig().getBoolean("Switch.Temperature")){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TemperatureTask(), 20L, 10*20L);
		}
		if(getConfig().getBoolean("Switch.Weight")){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WeightTask(), 20L, 1*20L);
		}
		if(getConfig().getBoolean("Switch.Sick")){
			getServer().getPluginManager().registerEvents(new DamageEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SickTask(), 20L, 1*20L);
		}
		if(getConfig().getBoolean("Switch.Fracture")){
			getServer().getPluginManager().registerEvents(new FractureEvent(), this);
		}
		
		//PlaceholderAPI
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new Papi(this).hook();
			getLogger().info("§9[RealSurvival] 成功加载PlaceholderAPI功能! ");
		}
		//TitleManager
		if(Bukkit.getPluginManager().isPluginEnabled("TitleManager")){
			tmapi = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
			getLogger().info("§9[RealSurvival] 成功加载TitleManager功能! ");
		}
		//Chairs
		if(Bukkit.getPluginManager().isPluginEnabled("Chairs")){
			getServer().getPluginManager().registerEvents(new SitEvent(), this);
			getLogger().info("§9[RealSurvival] 成功加载Chairs功能! ");
		}
	}
	//Unregister Listeners
	private void unregisterListeners(){
		HandlerList.unregisterAll(this);
	}
	
	public HashMap<UUID, PlayerData> getPlayerDatas(){
		return playersDatas;
	}
	
	public void addPlayerData(Player p,PlayerData pd){
		playersDatas.put(p.getUniqueId(), pd);
	}
	
	public PlayerData getPlayerData(Player p){
		if(playersDatas.containsKey(p.getUniqueId()))
			return playersDatas.get(p.getUniqueId());
		return null;
	}

	public void deletePlayer(Player p){
		playersDatas.get(p.getUniqueId()).savaData();
		
		Iterator<UUID> uuid=playersDatas.keySet().iterator();
		while(uuid.hasNext())
			if(uuid.next().equals(p.getUniqueId())){
				uuid.remove();
				break;
			}
		playersDatas.remove(p.getUniqueId());
	}
	/**
	 * 增加一个配方
	 * @param name 配方名
	 * @return 是否添加成功
	 */
	public boolean addSF(String name){
		return SF.add(getSF(name));
	}
	/**
	 * 在文件中获取一个配方
	 * @param name 配方名
	 * @return 配方数据
	 */
	public WorkbenchRecipe getSF(String name){
		YamlConfiguration temp=YamlConfiguration.loadConfiguration(new File(
				getDataFolder()+File.separator+"SyntheticFormula"+File.separator+name+".yml"));
		HashMap<Integer,ItemStack> material=new HashMap<Integer,ItemStack>();
		HashMap<Integer,ItemStack> product=new HashMap<Integer,ItemStack>();
		
		for(int i:CraftItemGUI.materials)
			material.put(i, temp.getItemStack(name+".material."+i));
		for(int i:CraftItemGUI.products)
			product.put(i, temp.getItemStack(name+".product."+i));
		
		return new WorkbenchRecipe(name, this, material, product,temp.getLong(name+".builtTime"));
	}
	/**
	 * 比较配方原材料是否与配方一致
	 * 如果一致返回配方名,不一致则返回null
	 * @param material 原材料的摆放的HashMap
	 * @return 如果一致返回配方名,不一致则返回null
	 */
	public WorkbenchRecipe compareMaterial(HashMap<Integer,ItemStack> material){
		for(WorkbenchRecipe temp:SF)
			if(temp.compare(material))return temp;
		return null;
	}
	
	public void addFireCraftTableRecipe(FireCraftTableRecipe fctr){
		FCTR.add(fctr);
	}
	
	/**
	 * 获取一个物品的重量值
	 * @param name 物品名
	 * @return double
	 */
	public double getWeight(String name){
		if(itemWeight.containsKey(name))
			return itemWeight.get(name);
		return 0;
	}
	/**
	 * 获取lore字符
	 * @param lore类型
	 * @return String
	 */
	public String getLoreTabel(String lore){
		return loreTabel.get(lore);
	}
	/**
	 * 获取buff
	 * @param kind 效果名字
	 * @return buff列表
	 */
	public List<String> getEffects(String kind){
		return effects.get(kind);
	}
	//工作台类
	/**
	 * 增加一个工作台
	 * @param key 工作台id
	 * @param value 工作台值 PlayerName,SFName,Time
	 */
	public void addWorkBench(String key,String value){
		workbench.put(key, value);
	}
	
	public Object[] getWorkbench(String key){
		String[] temp =workbench.get(key).split(",");
		return new Object[]{temp[0],temp[1],Integer.parseInt(temp[2]),temp[3],
				Integer.parseInt(temp[4]),Integer.parseInt(temp[5]),Integer.parseInt(temp[6])};
	}
	
	public String getWorkbench(String key,int index){
		return workbench.get(key).split(",")[index];
	}
	
	public void removeWorkBench(String key){
		Iterator<String> keys=workbench.keySet().iterator();
		while(keys.hasNext()){
			String k=(String) keys.next();
			if(k.equals(key)){
				keys.remove();
				break;
			}
		}
		workbench.remove(key);
	}
	
	public void resetWorkBench(String key,String value){
		workbench.replace(key, value);
	}
	
	public HashMap<String, String> getWorkbench(){
		return workbench;
	}
	//雨水收集器
	/**
	 * 增加一个雨水收集器
	 * @param key 雨水收集器id
	 * @param value 雨水收集器值 id:PlayerName,Time,WaterAmount,World
	 */
	public void addRwC(String key,String value){
		rainwaterCollector.put(key, value);
	}
	
	public String getRwC(String key){
		return rainwaterCollector.get(key);
	}
	
	public void removeRwC(String key){
		Iterator<String> keys=rainwaterCollector.keySet().iterator();
		while(keys.hasNext()){
			String k=(String) keys.next();
			if(k.equals(key)){
				keys.remove();
				break;
			}
		}
		rainwaterCollector.remove(key);
	}
	
	public void resetRwC(String key,String value){
		rainwaterCollector.replace(key, value);
	}
	
	public HashMap<String, String> getRwC(){
		return rainwaterCollector;
	}
	//..............................
	//FireCraftTable
	/**
	 * 增加一个FireCraftTable
	 * @param key FireCraftTable id
	 * @param value FireCraftTable值 id:PlayerName,repice,Time,tem,maxTime,maxTem,World,x,y,z
	 */
	public void addFCT(String key,String value){
		fireCraftTable.put(key, value);
	}
	
	public String[] getFCT(String key){
		return fireCraftTable.get(key).split(",");
	}
	
	public void removeFCT(String key){
		Iterator<String> keys=fireCraftTable.keySet().iterator();
		while(keys.hasNext()){
			String k=(String) keys.next();
			if(k.equals(key)){
				keys.remove();
				break;
			}
		}
		fireCraftTable.remove(key);
	}
	
	public void resetFCT(String key,String value){
		fireCraftTable.replace(key, value);
	}
	
	public HashMap<String, String> getFCT(){
		return fireCraftTable;
	}
	//........................................
	public List<Object[]> getMob(String  mobName){
		if(!mob.containsKey(mobName))return null;
		List<Object[]> l=new LinkedList<>();
		for(String temp:mob.get(mobName).split(";"))
			l.add(new Object[]{temp.split(",")[0],
					Double.parseDouble(temp.split(",")[1])});
		return l;
	}
	
	public boolean mobContains(String  mobName){
		return mob.containsKey(mobName);
	}
	
	public List<Object[]> getFoodSick(String  foodName){
		if(!eatFood.containsKey(foodName))return null;
		List<Object[]> l=new LinkedList<>();
		for(String temp:eatFood.get(foodName).split(";"))
			l.add(new Object[]{temp.split(",")[0],
					Double.parseDouble(temp.split(",")[1])});
		return l;
	}
	
	public boolean sickFoodContains(String  foodName){
		return eatFood.containsKey(foodName);
	}
	
	public Double[] getFoods(String name){
		if(!foods.containsKey(name))return null;
		String[] s=foods.get(name).split(",");
		return new Double[]{Double.parseDouble(s[0]),Double.parseDouble(s[1]),Double.parseDouble(s[2])};
	}
	
	public boolean containsFoods(String name){
		return foods.containsKey(name);
	}
	
	public String getMessage(String id,PlayerData pd){
		String[] msgs=messages.get(id).split(";");
		String sick="";
		String d="";
		String r="";
		if(pd.getSickKindMap().size()!=0){
			sick=pd.getSickKind()[0];
			d=pd.getDuration()[0];
			r=pd.getRecovery()[0];
		}
		return msgs[(int)(Math.random()*msgs.length)]
				.replaceAll("%sleep%", _2f(pd.getSleep()/sleepDatas[0]*100))
				.replaceAll("%thirst%", _2f(pd.getThirst()/thirstDatas[0]*100))
				.replaceAll("%tem%", _2f(pd.getTemperature()))
				.replaceAll("%sick%", sick)
				.replaceAll("%weight%", _2f(pd.getWeight()))
				.replaceAll("%ps%", _2f(pd.getPhysical_strength()/psDatas[0]*100))
				.replaceAll("%md%", d)
				.replaceAll("%player%", Bukkit.getPlayer(pd.getUuid()).getName())
				.replaceAll("%recovery%", r);
	}
	public String getMessage(String id){
		String[] msgs=messages.get(id).split(";");
		return msgs[(int)(Math.random()*msgs.length)];
	}
	
	public String getDefSick(){
		return defSick;
	}
	private String _2f(double d){
		return String.format("%.2f", d);
	}
}
