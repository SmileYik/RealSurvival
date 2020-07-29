package miskyle.realsurvival;

import com.github.miskyle.mcpt.MCPT;
import java.io.File;
import miskyle.realsurvival.api.RealSurvivalAPI;
import miskyle.realsurvival.api.player.PlayerData;
import miskyle.realsurvival.command.CommandManager;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.randomday.RandomDayManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RealSurvival extends JavaPlugin implements RealSurvivalAPI {
  @Override
  public void onEnable() {
    MCPT.plugin = this;
    prepare();
    PlayerManager.init();
    new ItemManager();
    new EffectManager();
    new ConfigManager(this);
    RandomDayManager.init();
    new Msg(this);
    new CommandManager(this);
    loadPlayer();
    getLogger().info("RealSurvival is Ready");
  }

  @Override
  public void onDisable() {
    for (Player p : this.getServer().getOnlinePlayers()) {
      PlayerManager.removePlayer(p.getName());
    }
    RandomDayManager.save();
    MachineManager.saveTimers();
  }

  private void loadPlayer() {
    for (Player p : this.getServer().getOnlinePlayers()) {
      PlayerManager.addPlayer(p);
    }
  }

  /**
   * 为插件运行做准备工作 主要为创建目录和放置 默认配置文件.
   */
  private void prepare() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();      
    }
    if (!new File(getDataFolder() + "/effect/").exists()) {
      new File(getDataFolder() + "/effect/").mkdir();      
    }
    if (!new File(getDataFolder() + "/nbtitem/").exists()) {
      new File(getDataFolder() + "/nbtitem/").mkdir();      
    }
    if (!new File(getDataFolder() + "/item/water").exists()) {
      new File(getDataFolder() + "/item/water").mkdirs();      
    }
    if (!new File(getDataFolder() + "/playerdata/").exists()) {
      new File(getDataFolder() + "/playerdata/").mkdir();      
    }
    if (!new File(getDataFolder() + "/CubeData/").exists()) {
      new File(getDataFolder() + "/CubeData/").mkdir();      
    }
    if (!new File(getDataFolder() + "/recipe/crafttable/").exists()) {
      new File(getDataFolder() + "/recipe/crafttable/").mkdirs();      
    }
    if (!new File(getDataFolder() + "/recipe/furnace/").exists()) {
      new File(getDataFolder() + "/recipe/furnace/").mkdirs();      
    }
    if (!new File(getDataFolder() + "/config.yml").exists()) {
      saveDefaultConfig();      
    }
    try {
      reloadConfig();
    } catch (Exception e) {
      ;
    }
  }

  /**
   * 获取NMS的版本信息.
   * @return
   */
  public static String getVersion() {
    String version;
    try {
      version = Bukkit.getServer().getClass()
          .getPackage().getName().replace(".", ",").split(",")[3];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
    return version;
  }

  @Override
  public PlayerData getPlayerData(String playerName) {
    return PlayerManager.getPlayerData(playerName);
  }

  @Override
  public boolean isPlayerActive(String playerName) {
    return PlayerManager.isActive(playerName);
  }
}
