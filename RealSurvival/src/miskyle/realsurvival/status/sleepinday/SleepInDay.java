package miskyle.realsurvival.status.sleepinday;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import miskyle.realsurvival.data.ConfigManager;

public class SleepInDay {

  /**
   * 睡觉.

   * @param p 玩家
   * @param loc 位置
   */
  public static void sleep(Player p, Location loc) {
    if (ConfigManager.getSleepConfig().isEnable() 
        && ConfigManager.getSleepConfig().isSleepInDay()) {
      if (ConfigManager.getBukkitVersion() >= 14) {
        SleepInDayListenerVer3.sleep(p, loc);
      } else if (ConfigManager.getBukkitVersion() == 13) {
        SleepInDayListenerVer2.sleep(p, loc);
      } else {
        SleepInDayListenerVer1.sleep(p, loc);
      }
    }
  }

  /**
   * 起床.

   * @param p 玩家
   */
  public static void wakeUp(Player p) {
    if (ConfigManager.getSleepConfig().isEnable() 
        && ConfigManager.getSleepConfig().isSleepInDay()) {
      if (ConfigManager.getBukkitVersion() >= 14) {
        SleepInDayListenerVer3.wakeUp(p);
      } else if (ConfigManager.getBukkitVersion() == 13) {
        SleepInDayListenerVer2.wakeUp(p);
      } else {
        SleepInDayListenerVer1.wakeUp(p);
      }
    }
  }
}
