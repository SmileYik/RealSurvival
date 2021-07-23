package miskyle.realsurvival.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.nms.actionbar.NMSActionBar;
import com.github.miskyle.mcpt.nms.sleep.NMSSleep;
import com.github.miskyle.mcpt.nms.title.NMSTitle;
import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.util.ActionNullBar;

public class PlayerManager {
  public static NMSTitle title;
  public static NMSActionBar bar;
  public static NMSSleep sleep;

  private static ConcurrentHashMap<String, PlayerData> playerDatas;
  private static ArrayList<String> freezingPlayer;

  /**
   * 初始化.
   */
  public static void init() {
    playerDatas = new ConcurrentHashMap<String, PlayerData>();
    freezingPlayer = new ArrayList<String>();
    String version = RealSurvival.getVersion();
    title = NMSTitle.getTitle(version);
    bar = NMSActionBar.getActionBar(version);
    try {
      sleep = NMSSleep.getNMSSleep(version);      
    } catch (Exception e) {
      sleep = null;
    }
    if (bar == null) {
      bar = new ActionNullBar();
    }
  }

  /**
   * 添加玩家.

   * @param p 玩家
   */
  public static void addPlayer(Player p) {
    if (playerDatas.containsKey(p.getName())) {
      if (!ConfigManager.enableInWorld(p.getWorld().getName())) {
        playerDatas.remove(p.getName());
      }
      return;
    } else if (!p.hasMetadata("NPC") && !freezingPlayer.contains(p.getName())
        && ConfigManager.enableInWorld(p.getWorld().getName())) {
      PlayerData pd = PlayerData.getPlayerData(p.getName());
      if (pd != null) {
        playerDatas.put(p.getName(), pd);        
      }
    }

  }

  /**
   * 移除玩家并保存该玩家的数据.

   * @param playerName 玩家名
   */
  public static void removePlayer(String playerName) {
    if (playerDatas.containsKey(playerName)) {
      playerDatas.remove(playerName).save();      
    }
  }

  /**
   * 玩家死亡后重置数据.

   * @param p 玩家
   */
  public static void playerDeath(Player p) {
    if (ConfigManager.getDeathConfig().isEnable() && isActive(p)) {
      PlayerData pd = getPlayerData(p.getName());
      if (ConfigManager.getDeathConfig().isRemoveDisease()) {
        pd.getDisease().getDiseases().clear();
      }
      pd.setStatus(StatusType.SLEEP,
          pd.getStatusMaxValue(StatusType.SLEEP) * ConfigManager.getDeathConfig().getSleep() / 100D);
      pd.setStatus(StatusType.THIRST,
          pd.getStatusMaxValue(StatusType.THIRST) 
          * ConfigManager.getDeathConfig().getThirst() / 100D);
      pd.setStatus(StatusType.ENERGY,
          pd.getStatusMaxValue(StatusType.ENERGY) 
          * ConfigManager.getDeathConfig().getEnergy() / 100D);
      p.setFoodLevel((int) (0.2 * ConfigManager.getDeathConfig().getHunger()));
    }
    removePlayer(p.getName());
  }

  /**
   * 冻结玩家属性.

   * @param name 玩家名
   */
  public static void freezePlayer(String name) {
    removePlayer(name);
    if (!freezingPlayer.contains(name)) {
      freezingPlayer.add(name);      
    }
  }

  /**
   * 恢复玩家属性, 与冻结玩家属性对应.

   * @param p 玩家
   */
  public static void activePlayer(Player p) {
    if (freezingPlayer.contains(p.getName())) {
      freezingPlayer.remove(p.getName());      
    }
    addPlayer(p);
  }

  public static ConcurrentHashMap<String, PlayerData> getActivePlayers() {
    return playerDatas;
  }

  public static boolean isActive(Player p) {
    return isActive(p.getName());
  }

  public static boolean isActive(String playerName) {
    return playerDatas.containsKey(playerName);
  }

  public static PlayerData getPlayerData(String playerName) {
    return playerDatas.get(playerName);
  }

}
