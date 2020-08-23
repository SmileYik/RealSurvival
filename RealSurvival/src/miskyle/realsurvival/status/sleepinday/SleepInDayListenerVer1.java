package miskyle.realsurvival.status.sleepinday;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;

/**
 * 玩家在白天睡觉, 适配版本为1.7.10到1.13 需要到相应的API下编译后替换Jar包内同名 文件
 * 
 * @author MiSkYle
 * @version 1.0.0
 */
public class SleepInDayListenerVer1 implements Listener {
  private static final String WAKE_UP = "wake up";
  private static LinkedList<String> sleepPlayer = new LinkedList<String>();

  /**
   * 玩家想要睡觉事件
   * 
   * @param e
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void wantSleep(final PlayerInteractEvent e) {
    if (e.isCancelled())
      return;
    if (!ConfigManager.getSleepConfig().isSleepInDay())
      return;
    if (!PlayerManager.isActive(e.getPlayer().getName()))
      return;
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
      return;
    if (!e.getClickedBlock().getType().name().contains("BED_BLOCK"))
      return;
    if (e.getPlayer().getWorld().getTime() >= 13000)
      return;
    // 床太远了
    if (e.getClickedBlock().getLocation().distanceSquared(e.getPlayer().getLocation()) > 3) {
      PlayerManager.bar.sendActionBar(e.getPlayer(), I18N.tr("sleep-in-day.too-far"));
      e.setCancelled(true);
      return;
    }
    // TODO 周围有怪物在游荡

    e.setCancelled(true);
    if (sleepPlayer.contains(e.getPlayer().getName()))
      return;
    // 开始睡觉
    PlayerBedEnterEvent event = new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock());
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      PlayerManager.sleep.sleep(e.getPlayer(), e.getClickedBlock().getLocation());
      sleepPlayer.add(e.getPlayer().getName());
      e.getPlayer().sendMessage(I18N.tr("sleep-in-day.sleep-ver1"));
    }
  }

  /**
   * 玩家想要起床事件
   * 
   * @param e
   */
  @EventHandler
  public void wakeUp(final AsyncPlayerChatEvent e) {
    if (sleepPlayer.contains(e.getPlayer().getName())) {
      if (e.getMessage().equalsIgnoreCase(WAKE_UP)) {
        e.setCancelled(true);
        wakeUp(e.getPlayer());
      }
    }
  }

  /**
   * 玩家在睡觉时遭受攻击起床事件
   * 
   * @param e
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void damageDuringSleeping(final EntityDamageEvent e) {
    if (!e.isCancelled() && e.getEntityType() == EntityType.PLAYER
        && sleepPlayer.contains(((Player) e.getEntity()).getName())) {
      wakeUp((Player) e.getEntity());
    }
  }

  /**
   * 使在睡觉的玩家起来
   * 
   * @param p
   */
  protected static void wakeUp(Player p) {
    PlayerManager.sleep.leaveSleep(p);
    sleepPlayer.remove(p.getName());
    Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
  }

  /**
   * 强制让玩家在某地点睡着
   * 
   * @param p
   * @param loc
   */
  protected static void sleep(Player p, Location loc) {
    PlayerBedEnterEvent event = new PlayerBedEnterEvent(p, loc.getBlock());
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      if (PlayerManager.isActive(p.getName())) {
        PlayerManager.sleep.sleep(p, loc);
        sleepPlayer.add(p.getName());
        p.sendMessage(I18N.tr("sleep-in-day.sleep-ver1"));
      }
    }
  }

  public static void wakeUpAll() {
    for (String playerName : sleepPlayer) {
      Player p = Bukkit.getPlayer(playerName);
      PlayerManager.sleep.leaveSleep(p);
      Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
    }
    sleepPlayer.clear();
  }

}
