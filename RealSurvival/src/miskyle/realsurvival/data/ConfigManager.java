package miskyle.realsurvival.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.miskyle.mcpt.bstat.Metrics;
import com.github.miskyle.mcpt.i18n.I18N;
import com.github.miskyle.mcpt.mysql.MySQLManager;

import miskyle.realsurvival.data.config.EnergyBreakBlockData;
import miskyle.realsurvival.data.config.EnergyConfig;
import miskyle.realsurvival.data.config.SleepConfig;
import miskyle.realsurvival.data.config.ThirstConfig;
import miskyle.realsurvival.data.config.WeightConfig;
import miskyle.realsurvival.listener.JoinOrLeaveListener;
import miskyle.realsurvival.listener.UseItemListener;
import miskyle.realsurvival.papi.Papi;
import miskyle.realsurvival.status.listener.EnergyListener;
import miskyle.realsurvival.status.listener.SleepListener;
import miskyle.realsurvival.status.listener.ThirstListener;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer1;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer2;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer3;
import miskyle.realsurvival.status.task.EnergyTask;
import miskyle.realsurvival.status.task.SleepTask;
import miskyle.realsurvival.status.task.ThirstTask;
import miskyle.realsurvival.status.task.WeightTask;

public class ConfigManager {
	private static ConfigManager cm;
	
	private Plugin plugin;
	private FileConfiguration c;
	
	private List<String> worlds;
	private int statusCmdCD;
	private boolean enableMySQL = false;
	private int bukkitVersion;
	
	private SleepConfig  sleepc;
	private ThirstConfig thirstc;
	private EnergyConfig energyc;
	private WeightConfig weightc;
	
	public ConfigManager(Plugin plugin) {
		cm = this;
		this.plugin = plugin;
		c = plugin.getConfig();
		bukkitVersion = 
				Integer.parseInt(plugin.getServer().getBukkitVersion().split("-")[0].replace(".", ",").split(",")[1]);
		
		new I18N(c.getString("language"));
		
		setupMySQL();
		setupbStats();
		setupPAPI();
		
		loadNormalConfig();
		loadStatusConfig();
		
		registerStatus();
		registerListener();
	}
	
	private void registerListener() {
		plugin.getServer().getPluginManager().registerEvents(new JoinOrLeaveListener(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new UseItemListener(), plugin);
	}
	
	private void registerStatus() {
		if(thirstc.isEnable()) {
			plugin.getServer().getPluginManager().registerEvents(new ThirstListener(), plugin);
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new ThirstTask(), 20L, 20L);
		}
		if(weightc.isEnable()) {
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new WeightTask(), 20L, 20L);
		}
		if(energyc.isEnable()) {
			plugin.getServer().getPluginManager().registerEvents(new EnergyListener(), plugin);
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new EnergyTask(), 20L, 20L);
		}
		if(sleepc.isEnable()) {
			plugin.getServer().getPluginManager().registerEvents(new SleepListener(), plugin);
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new SleepTask(), 20L, 20L);
			if(sleepc.isSleepInDay()) {
				if(PlayerManager.sleep == null) {
					plugin.getLogger().warning(I18N.tr("log.warning.no-suit-sleep-in-day"));
					sleepc.setSleepInDay(false);
				}else {
					if(bukkitVersion>=14)
						plugin.getServer().getPluginManager().registerEvents(new SleepInDayListenerVer3(), plugin);
					else if(bukkitVersion==13)
						plugin.getServer().getPluginManager().registerEvents(new SleepInDayListenerVer2(), plugin);
					else
						plugin.getServer().getPluginManager().registerEvents(new SleepInDayListenerVer1(), plugin);
				}
			}
		}
	}
	
	private void setupPAPI() {
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			if(new Papi(plugin).register()){
				plugin.getLogger().info("Successful loading PlaceholderAPI !");
			}
		}
	}
	
	/**
	 * 启用bStats统计数据
	 */
	private void setupbStats() {
		new Metrics(plugin,1974);
	}
	
	/**
	 * 获取寻常插件设定
	 */
	private void loadNormalConfig() {
		//Load Enable World
		worlds = new ArrayList<String>();
		for(String world : c.getStringList("enable-worlds")) {
			if(plugin.getServer().getWorld(world)!=null) {
				worlds.add(world);
				plugin.getLogger().info(I18N.tr("log.info.enable-world",world));
			}else {
				plugin.getLogger().warning(I18N.tr("log.warning.missing-world",world));
			}
		}
		if(worlds.isEmpty()) 
			plugin.getLogger().warning(I18N.tr("log.warning.enable-world-empty"));
		
		statusCmdCD = c.getInt("status-command-cooldown",600);
	}
	
	/**
	 * 设定MySQL数据库
	 */
	private void setupMySQL() {
		if(c.getBoolean("mysql.enable")) {
			enableMySQL = true;
			MySQLManager.setupMySQl(c.getString("mysql.host"), 
									c.getInt("mysql.port"), 
									c.getString("mysql.database"), 
									c.getString("mysql.username"), 
									c.getString("mysql.password"));
			if(MySQLManager.connect()) {
				try {
					if(!MySQLManager.execute("show tables like 'realsurvival'")
							.executeQuery().next()) {
						//Create Table
						MySQLManager.execute("create table RealSurvival(\r\n" + 
								"Name VARCHAR(50),\r\n" + 
								"Sleep DOUBLE,\r\n" + 
								"Thirst DOUBLE,\r\n" + 
								"Energy DOUBLE,\r\n" + 
								"Weight DOUBLE,\r\n" + 
								"ExtraSleepValue TEXT\r\n" + 
								"ExtraThirstValue TEXT\r\n" + 
								"ExtraEnergyValue TEXT\r\n" + 
								"ExtraWeightValue TEXT\r\n" + 
								")default charset=utf8;").close();;
					}
				} catch (SQLException e) {
					MySQLManager.disconnect();
					e.printStackTrace();
				}
				MySQLManager.disconnect();
			}else {
				enableMySQL = false;
			}
		}
		plugin.getLogger().info(I18N.tr("log.info.mysql",enableMySQL));
	}
	
	
	/**
	 * 从config中取读各属性设定
	 */
	private void loadStatusConfig() {
		//Sleep Config Load
		sleepc = new SleepConfig();
		sleepc.setEnable(c.getBoolean("status.sleep.enable",true));
		sleepc.setSleepInDay(c.getBoolean("status.sleep.sleep-in-day",true));
		sleepc.setMaxValue(c.getDouble("status.sleep.max"));
		sleepc.setIncreaseValue(c.getDouble("status.sleep.add"));
		sleepc.setDecreaseValue(c.getDouble("status.sleep.sub"));
		sleepc.setEffectData(getStatusEffectData("status.sleep.effect-data"));
		
		//Thirst Config Load
		thirstc = new ThirstConfig();
		thirstc.setEnable(c.getBoolean("status.thirst.enable",true));
		thirstc.setMaxValue(c.getDouble("status.thirst.max"));
		thirstc.setDecreaseValue(c.getDouble("status.thirst.sub"));
		thirstc.setEffectData(getStatusEffectData("status.thirst.effect-data"));
		
		//Energy Config Load
		energyc = new EnergyConfig();
		energyc.setEnable(c.getBoolean("status.energy.enable"));
		energyc.setMaxValue(c.getDouble("status.energy.max"));
		energyc.setIncreaseValue(c.getDouble("status.energy.add"));
		energyc.setDecreaseJumping(c.getDouble("status.energy.jumping"));
		energyc.setDecreaseSneaking(c.getDouble("status.energy.sneaking"));
		energyc.setDecreaseSprinting(c.getDouble("status.energy.sprinting"));
		energyc.setDecreaseSwimming(c.getDouble("status.energy.swimming"));
		energyc.setEffectData(getStatusEffectData("status.energy.effect-data"));
		HashMap<EnergyBreakBlockData,Double> actionDecrease = 
									new HashMap<EnergyBreakBlockData, Double>();
		for(String line:c.getStringList("status.energy.break-block")) {
			String[] temp = line.split(",");
			EnergyBreakBlockData ebbd = new EnergyBreakBlockData(temp[0], temp[1]);
			actionDecrease.put(ebbd, Double.parseDouble(temp[2]));
		}
		energyc.setActionDecrease(actionDecrease);
		
		//Weight Config Load
		weightc = new WeightConfig();
		weightc.setEnable(c.getBoolean("status.weight.enable",true));
		weightc.setMaxValue(c.getDouble("status.weight.max"));
		weightc.setEffects(c.getString("status.weight.effect"));
		HashMap<String, Double> itemWeight = new HashMap<String, Double>();
		for(String line:c.getStringList("status.weight.item")) {
			String[] temp = line.split(":");
			itemWeight.put(temp[0], Double.parseDouble(temp[1]));
		}
		weightc.setItemWeight(itemWeight);
	}
	
	/**
	 * 获取对应属性的效果数据
	 * @param key 属性效果数据在config.yml中的路径
	 * @return 对应属性的效果
	 */
	private HashMap<String,String> getStatusEffectData(String key){
		HashMap<String, String> effortData = new HashMap<String, String>();
		for(String line : c.getStringList(key)) {
			String[] temp = line.split(":");
			effortData.put(temp[0], temp[1]);
		}
		return effortData;
	}
	
	public static int getStatusCmdCD() {
		return cm.statusCmdCD;
	}
	
	public static SleepConfig getSleepConfig() {
		return cm.sleepc;
	}
	
	public static ThirstConfig getThirstConfig() {
		return cm.thirstc;
	}
	
	public static WeightConfig getWeightConfig() {
		return cm.weightc;
	}
	
	public static EnergyConfig getEnergyConfig() {
		return cm.energyc;
	}
	
	public static List<String> getEnableWorlds(){
		return cm.worlds;
	}
	
	public static boolean enableInWorld(String worldName) {
		return cm.worlds.contains(worldName);
	}
	
	public static boolean isEnableMySql() {
		return cm.enableMySQL;
	}

	public static int getBukkitVersion() {
		return cm.bukkitVersion;
	}
	
	
}
