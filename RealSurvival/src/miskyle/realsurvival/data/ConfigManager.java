package miskyle.realsurvival.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.github.miskyle.mcpt.bstat.Metrics;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.data.config.DeathConfig;
import miskyle.realsurvival.data.config.status.DiseaseConfig;
import miskyle.realsurvival.data.config.status.EnergyBreakBlockData;
import miskyle.realsurvival.data.config.status.EnergyConfig;
import miskyle.realsurvival.data.config.status.SleepConfig;
import miskyle.realsurvival.data.config.status.TemperatureConfig;
import miskyle.realsurvival.data.config.status.ThirstConfig;
import miskyle.realsurvival.data.config.status.WeightConfig;
import miskyle.realsurvival.listener.JoinOrLeaveListener;
import miskyle.realsurvival.listener.UseItemListener;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTask;
import miskyle.realsurvival.machine.crafttable.CraftTableListener;
import miskyle.realsurvival.machine.furnace.FurnaceListener;
import miskyle.realsurvival.machine.listener.CubeListener;
import miskyle.realsurvival.machine.raincollector.RainCollectorListener;
import miskyle.realsurvival.machine.recipeviewer.RecipeViewerListener;
import miskyle.realsurvival.papi.Papi;
import miskyle.realsurvival.status.listener.EnergyListener;
import miskyle.realsurvival.status.listener.MobMakeDisease;
import miskyle.realsurvival.status.listener.SleepListener;
import miskyle.realsurvival.status.listener.ThirstListenerVer1;
import miskyle.realsurvival.status.listener.ThirstListenerVer2;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer1;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer2;
import miskyle.realsurvival.status.sleepinday.SleepInDayListenerVer3;
import miskyle.realsurvival.status.task.DiseaseTask;
import miskyle.realsurvival.status.task.EffectTask;
import miskyle.realsurvival.status.task.EnergyTask;
import miskyle.realsurvival.status.task.SaveConfigTask;
import miskyle.realsurvival.status.task.SleepTask;
import miskyle.realsurvival.status.task.TemperatureTask;
import miskyle.realsurvival.status.task.ThirstTask;
import miskyle.realsurvival.status.task.WeightTask;
import miskyle.realsurvival.util.watermaker.WaterMakerVer;

public class ConfigManager {
  private static ConfigManager cm;

  private Plugin plugin;
  private FileConfiguration config;

  private List<String> worlds;
  private int statusCmdCd;
  private boolean enableMySql = false;
  private int bukkitVersion;

  private DeathConfig deathc;

  private SleepConfig sleepc;
  private ThirstConfig thirstc;
  private EnergyConfig energyc;
  private WeightConfig weightc;
  private TemperatureConfig temperaturec;
  private DiseaseConfig disease;

  /**
   * 初始化configmanager.

   * @param plugin 插件实例.
   */
  public ConfigManager(Plugin plugin) {
    cm = this;
    this.plugin = plugin;
    config = plugin.getConfig();
    bukkitVersion = 
        Integer.parseInt(
            plugin.getServer().getBukkitVersion().split("-")[0].replace(".", ",").split(",")[1]);

    new I18N(config.getString("language"));

    setupMySql();
    setupbStats();
    setupPapi();

    loadNormalConfig();
    loadDeathConfig();
    loadStatusConfig();
    MachineManager.init();

    registerStatus();
    registerTask();
    registerListener();
  }

  private void registerTask() {
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(
        plugin, new EffectTask(), 20L, 20L);
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(
        plugin, new SaveConfigTask(), 600L, 12000L);
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(
        plugin, new MachineTask(), 20L, 20L);
  }

  private void registerListener() {
    plugin.getServer().getPluginManager().registerEvents(new JoinOrLeaveListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new UseItemListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new RainCollectorListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new CraftTableListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new FurnaceListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new CubeListener(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new RecipeViewerListener(), plugin);
  }

  private void registerStatus() {
    if (thirstc.isEnable()) {
      if (!new File(
          plugin.getDataFolder() + "/item/water/unknown.yml").exists()) {        
        WaterMakerVer.makeUnknownWater();
      }
      if (!new File(
          plugin.getDataFolder() + "/item/water/rainwater.yml").exists()) {
        WaterMakerVer.makeRainwater();        
      }
      if (bukkitVersion > 7) {
        plugin.getServer().getPluginManager().registerEvents(new ThirstListenerVer2(), plugin);
      } else {
        plugin.getServer().getPluginManager().registerEvents(new ThirstListenerVer1(), plugin);
      }
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new ThirstTask(), 20L, 20L);
    }
    if (weightc.isEnable()) {
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new WeightTask(), 20L, 20L);
    }
    if (energyc.isEnable()) {
      plugin.getServer().getPluginManager().registerEvents(new EnergyListener(), plugin);
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new EnergyTask(), 20L, 20L);
    }
    if (sleepc.isEnable()) {
      plugin.getServer().getPluginManager().registerEvents(new SleepListener(), plugin);
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new SleepTask(), 20L, 20L);
      if (sleepc.isSleepInDay()) {
        if (PlayerManager.sleep == null) {
          plugin.getLogger().warning(I18N.tr("log.warning.no-suit-sleep-in-day"));
          sleepc.setSleepInDay(false);
        } else {

          if (bukkitVersion >= 14) {
            plugin.getServer().getPluginManager().registerEvents(
                new SleepInDayListenerVer3(), plugin);            
          } else if (bukkitVersion == 13) {
            plugin.getServer().getPluginManager().registerEvents(
                new SleepInDayListenerVer2(), plugin);            
          } else {
            plugin.getServer().getPluginManager().registerEvents(
                new SleepInDayListenerVer1(), plugin);            
          }
        }
      }
    }
    if (temperaturec.isEnable()) {
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new TemperatureTask(), 20L, 200L);
    }
    if (disease.isEnable()) {
      if (disease.isMob()) {
        plugin.getServer().getPluginManager().registerEvents(new MobMakeDisease(), plugin);
      }
      plugin.getServer().getScheduler().runTaskTimerAsynchronously(
          plugin, new DiseaseTask(), 20L, 20L);
    }
  }

  private void setupPapi() {
    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      if (new Papi(plugin).register()) {
        plugin.getLogger().info("Successful loading PlaceholderAPI !");
      }
    }
  }

  /**
   * 启用bStats统计数据.
   */
  private void setupbStats() {
    if (plugin.getConfig().getBoolean("enable-bStats")) {
      if (bukkitVersion <= 7) {
        plugin.getLogger().info("Your server does not able to use bStats, so auto turn off.");
        return;
      }
      new Metrics(plugin, 1974);
    }
  }

  /**
   * 获取寻常插件设定.
   */
  private void loadNormalConfig() {
    // Load Enable World
    worlds = new ArrayList<String>();
    for (String world : config.getStringList("enable-worlds")) {
      if (plugin.getServer().getWorld(world) != null) {
        worlds.add(world);
        plugin.getLogger().info(I18N.tr("log.info.enable-world", world));
      } else {
        plugin.getLogger().warning(I18N.tr("log.warning.missing-world", world));
      }
    }
    if (worlds.isEmpty()) {      
      plugin.getLogger().warning(I18N.tr("log.warning.enable-world-empty"));
    }

    statusCmdCd = config.getInt("status-command-cooldown", 600);
  }

  private void loadDeathConfig() {
    deathc = new DeathConfig();
    deathc.setEnable(plugin.getConfig().getBoolean("on-death.enable"));
    deathc.setRemoveDisease(plugin.getConfig().getBoolean("on-death.remove-all-disease"));
    deathc.setHunger(plugin.getConfig().getDouble("on-death.hunger"));
    deathc.setEnergy(plugin.getConfig().getDouble("on-death.energy"));
    deathc.setSleep(plugin.getConfig().getDouble("on-death.sleep"));
    deathc.setThirst(plugin.getConfig().getDouble("on-death.thirst"));
  }

  /**
   * 设定MySQL数据库.
   */
  private void setupMySql() {
    if (config.getBoolean("mysql.enable")) {
      enableMySql = true;
      new MySqlManager(config.getConfigurationSection("mysql"));
      if (!MySqlManager.isTableExist() 
          && !MySqlManager.createDefaultTable()) {
        plugin.getLogger().severe(I18N.tr("log.severe.mysql.create-table"));
        enableMySql  = false;
      }
    }
    plugin.getLogger().info(I18N.tr("log.info.mysql", enableMySql));
    
    
    //    if (config.getBoolean("mysql.enable")) {
    //      enableMySql = true;
    //      MySQLManager.setupMySQl(
    //          config.getString("mysql.host"), config.getInt("mysql.port"), 
    //    config.getString("mysql.database"),
    //          config.getString("mysql.username"), config.getString("mysql.password"));
    //      if (MySQLManager.connect()) {
    //        try {
    //          if (!MySQLManager.execute("show tables like 'realsurvival'").executeQuery().next()){
    //            // Create Table
    //            MySQLManager.execute("create table RealSurvival(\r\n" 
    //                + "Name VARCHAR(50),\r\n" 
    //                + "Sleep DOUBLE,\r\n"
    //                + "Thirst DOUBLE,\r\n" 
    //                + "Energy DOUBLE,\r\n" 
    //                + "Weight DOUBLE,\r\n"
    //                + "ExtraSleepValue TEXT,\r\n"
    //                + "ExtraThirstValue TEXT,\r\n" 
    //                + "ExtraEnergyValue TEXT,\r\n" 
    //                + "ExtraWeightValue TEXT,\r\n"
    //                + "Effect TEXT,\r\n" 
    //                + "TEffect TEXT,\r\n" 
    //                + "Disease TEXT,\r\n" 
    //                + "Temperature TEXT\r\n"
    //                + ")default charset=utf8;").execute();
    //          }
    //        } catch (SQLException e) {
    //          MySQLManager.disconnect();
    //          e.printStackTrace();
    //        }
    //        MySQLManager.disconnect();
    //      } else {
    //        enableMySql = false;
    //      }
    //    }
    //    plugin.getLogger().info(I18N.tr("log.info.mysql", enableMySql));
  }

  /**
   * 从config中取读各属性设定.
   */
  private void loadStatusConfig() {
    // Sleep Config Load
    sleepc = new SleepConfig();
    sleepc.setEnable(config.getBoolean("status.sleep.enable", true));
    sleepc.setSleepInDay(config.getBoolean("status.sleep.sleep-in-day", true));
    sleepc.setMaxValue(config.getDouble("status.sleep.max", 100));
    sleepc.setIncreaseValue(config.getDouble("status.sleep.add", 0.5));
    sleepc.setDecreaseValue(config.getDouble("status.sleep.sub", 0.1));
    sleepc.setEffectData(getStatusEffectData("status.sleep.effect-data"));
    sleepc.setSleepZero(config.getBoolean("status.sleep.sleep-zero", true));

    // Thirst Config Load
    thirstc = new ThirstConfig();
    thirstc.setEnable(config.getBoolean("status.thirst.enable", true));
    thirstc.setMaxValue(config.getDouble("status.thirst.max", 100));
    thirstc.setDecreaseValue(config.getDouble("status.thirst.sub", 0.05));
    thirstc.setEffectData(getStatusEffectData("status.thirst.effect-data"));
    thirstc.setWater(config.getStringList("status.thirst.water"));

    // Energy Config Load
    energyc = new EnergyConfig();
    energyc.setEnable(config.getBoolean("status.energy.enable", true));
    energyc.setMaxValue(config.getDouble("status.energy.max", 100));
    energyc.setIncreaseValue(config.getDouble("status.energy.add", 5));
    energyc.setDecreaseJumping(config.getDouble("status.energy.jumping", 1));
    energyc.setDecreaseSneaking(config.getDouble("status.energy.sneaking", 1));
    energyc.setDecreaseSprinting(config.getDouble("status.energy.sprinting", 1));
    energyc.setDecreaseSwimming(config.getDouble("status.energy.swimming", 1));
    energyc.setEffectData(getStatusEffectData("status.energy.effect-data"));
    energyc.setToolList(config.getStringList("status.energy.tool-list"));
    HashMap<EnergyBreakBlockData, Double> actionDecrease = new HashMap<>();
    for (String line : config.getStringList("status.energy.break-block")) {
      String[] temp = line.split(",");
      EnergyBreakBlockData ebbd = new EnergyBreakBlockData(temp[0], temp[1]);
      actionDecrease.put(ebbd, Double.parseDouble(temp[2]));
    }
    energyc.setActionDecrease(actionDecrease);

    // Weight Config Load
    weightc = new WeightConfig();
    weightc.setEnable(config.getBoolean("status.weight.enable", true));
    weightc.setMaxValue(config.getDouble("status.weight.max", 100));
    weightc.setEffects(config.getString("status.weight.effect", "null"));

    // Temperature Config Load
    temperaturec = new TemperatureConfig();
    temperaturec.setEnable(config.getBoolean("status.temperature.enable", true));
    temperaturec.setMax(config.getDouble("status.temperature.max", 30));
    temperaturec.setMin(config.getDouble("status.temperature.min", 20));
    temperaturec.setBlock(config.getStringList("status.temperature.block"));
    temperaturec.setMaxEffect(config.getString("status.temperature.max-effect", null));
    temperaturec.setMinEffect(config.getString("status.temperature.min-effect", null));

    // Disease Config Load
    disease = new DiseaseConfig();
    disease.setEnable(config.getBoolean("status.disease.enable", true));
    disease.setDiseases(config.getStringList("status.disease.disease"));
    disease.setMob(config.getBoolean("status.disease.mob", true));
  }

  /**
   * 获取对应属性的效果数据.

   * @param key 属性效果数据在config.yml中的路径
   * @return 对应属性的效果
   */
  private HashMap<String, String> getStatusEffectData(String key) {
    HashMap<String, String> effortData = new HashMap<String, String>();
    for (String line : config.getStringList(key)) {
      String[] temp = line.split(":");
      effortData.put(temp[0], temp[1]);
    }
    return effortData;
  }

  public static int getStatusCmdCd() {
    return cm.statusCmdCd;
  }

  public static DeathConfig getDeathConfig() {
    return cm.deathc;
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

  public static TemperatureConfig getTemperatureConfig() {
    return cm.temperaturec;
  }

  public static DiseaseConfig getDiseaseConfig() {
    return cm.disease;
  }

  public static List<String> getEnableWorlds() {
    return cm.worlds;
  }

  public static boolean enableInWorld(String worldName) {
    return cm.worlds.contains(worldName);
  }

  public static boolean isEnableMySql() {
    return cm.enableMySql;
  }

  public static int getBukkitVersion() {
    return cm.bukkitVersion;
  }
}
