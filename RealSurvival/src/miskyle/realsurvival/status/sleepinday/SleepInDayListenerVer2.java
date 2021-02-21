package miskyle.realsurvival.status.sleepinday;

import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.playerdata.PlayerData;

/**
 * 玩家在白天睡觉, 适配版本为1.13 需要到相应的API版本下编译并替换 Jar包内对应文件.

 * @author MiSkYle
 * @version 2.0.0
 */
public class SleepInDayListenerVer2 implements Listener {
  private static final String WAKE_UP = "wake up";
  private static LinkedList<String> sleepPlayer = new LinkedList<String>();

  /**
   * 玩家想要睡觉事件.

   * @param e 玩家交互事件.
   */
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void wantSleep(final PlayerInteractEvent e) {
    if (e.isCancelled()
        || !ConfigManager.getSleepConfig().isSleepInDay()
        || !PlayerManager.isActive(e.getPlayer().getName())
        || e.getAction() != Action.RIGHT_CLICK_BLOCK
        || (ConfigManager.getBukkitVersion() >= 13 
        && !e.getClickedBlock().getType().name().contains("BED"))
        || e.getPlayer().getWorld().getTime() >= 13000) {
      return;
    }
    
    // 床太远了
    if (e.getClickedBlock().getLocation().distanceSquared(e.getPlayer().getLocation()) > 3) {
      PlayerManager.bar.sendActionBar(e.getPlayer(), I18N.tr("sleep-in-day.too-far"));
      e.setCancelled(true);
      return;
    }
    // TODO 周围有怪物在游荡

    e.setCancelled(true);
    if (sleepPlayer.contains(e.getPlayer().getName())) {
      return;      
    }
    // 开始睡觉
    PlayerBedEnterEvent event = 
        new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock(), BedEnterResult.OK);
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      PlayerManager.sleep.sleep(e.getPlayer(), e.getClickedBlock().getLocation());
      sleepPlayer.add(e.getPlayer().getName());
      e.getPlayer().sendMessage(I18N.tr("sleep-in-day.sleep-ver1"));
    }
  }

  /**
   * 玩家想要起床事件.

   * @param e 玩家说话事件.
   */
  @EventHandler
  public void wakeUp(final AsyncPlayerChatEvent e) {
    if (sleepPlayer.contains(e.getPlayer().getName())) {
      if (e.getMessage().equalsIgnoreCase(WAKE_UP)) {
        e.setCancelled(true);
        wakeUp(e.getPlayer());
      }
    } else {
      PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
      if (pd != null && pd.getSleep().isSleep()) {
        if (e.getMessage().equalsIgnoreCase(WAKE_UP)) {
          e.setCancelled(true);
          wakeUp(e.getPlayer());
        }
      }
    }
  }
  
  /**
   * 使在睡觉的玩家起来.

   * @param p 玩家
   */
  protected static void wakeUp(Player p) {
    PlayerManager.sleep.leaveSleep(p);
    sleepPlayer.remove(p.getName());
    Bukkit.getPluginManager().callEvent(
        new PlayerBedLeaveEvent(p, p.getLocation().getBlock(), false));
  }

  /**
   * 玩家在睡觉时遭受攻击起床事件.

   * @param e 遭受攻击事件.
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void damageDuringSleeping(final EntityDamageEvent e) {
    if (!e.isCancelled() && sleepPlayer.contains(e.getEntity().getName())) {
      wakeUp((Player) e.getEntity());
    }
  }

  /**
   * 强制让玩家在某地点睡着.

   * @param p 玩家.
   * @param loc 地点.
   */
  protected static void sleep(Player p, Location loc) {
    PlayerBedEnterEvent event = new PlayerBedEnterEvent(p, loc.getBlock(), BedEnterResult.OK);
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      if (PlayerManager.isActive(p.getName())) {
        PlayerManager.sleep.sleep(p, loc);
        sleepPlayer.add(p.getName());
        p.sendMessage(I18N.tr("sleep-in-day.sleep-ver1"));
      }
    }
  }

  /**
   * 让所有玩家起床.
   */
  public static void wakeUpAll() {
    for (String playerName : sleepPlayer) {
      Player p = Bukkit.getPlayer(playerName);
      PlayerManager.sleep.leaveSleep(p);
      Bukkit.getPluginManager().callEvent(
          new PlayerBedLeaveEvent(p, p.getLocation().getBlock(), false));
    }
    sleepPlayer.clear();
  }

}
