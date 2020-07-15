package miskyle.realsurvival.status.sleepinday;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;

/**
 * 玩家在白天睡觉, 适配版本为1.14及以上 <p>
 * 由于1.14+我无法以正常方式让玩家睡觉 <p>
 * 故以躺下方式让玩家睡觉.<p>
 * 这种方式有个缺点,玩家在一格内移动不会<p>
 * 恢复原状, 超过这一格自动恢复原状<p>
 * 需要到相应的API版本下编译并替换
 * Jar包内对应文件
 * @author MiSkYle
 * @version 3.0.0
 */
public class SleepInDayListenerVer3 implements Listener{
	/*
	 * 此代替睡觉方法漏洞颇多,
	 * 为了美观,我以羊毛毯作为基底方块,
	 * 若玩家脚所在方块为空气,则会自动
	 * 放置羊毛毯,使玩家躺下.
	 */
	
	/**
	 * 储存睡觉玩家及放置羊毛毯,
	 * 若无放置羊毛毯则储存null
	 */
	private static HashMap<String,Block> sleepPlayer = new HashMap<String, Block>();
	
	/**
	 * 玩家想要睡觉事件
	 * @param e
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void wantSleep(final PlayerInteractEvent e) {
		if(!ConfigManager.getSleepConfig().isSleepInDay()) return;
		if(!PlayerManager.isActive(e.getPlayer().getName()))return;
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK 
				||e.getClickedBlock().getType().name().contains("BED")) return;
		if(e.getPlayer().getWorld().getTime()>=13000)return;
		//床太远了
		if(e.getClickedBlock().getLocation().distanceSquared(e.getPlayer().getLocation())>3) {
			PlayerManager.bar.sendActionBar(e.getPlayer(), I18N.tr("sleep-in-day.too-far"));
			e.setCancelled(true);
			return;
		}
		//TODO 周围有怪物在游荡
		
		e.setCancelled(true);
		if(sleepPlayer.containsKey(e.getPlayer().getName())) return;
		//开始睡觉
		Location loc = e.getClickedBlock().getLocation().clone();
		PlayerBedEnterEvent event = 
				new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock(),BedEnterResult.OK);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			sleepPlayer.put(e.getPlayer().getName(),
					PlayerManager.sleep.sleep(e.getPlayer(), loc)?loc.getBlock():null);
			e.getPlayer().sendMessage(I18N.tr("sleep-in-day.sleep-ver2"));
		}
	}
	
	/**
	 * 玩家想要起床事件,
	 * 玩家移动时判断是否在睡觉
	 * @param e
	 */
	@EventHandler
	public void wakeUp(final PlayerMoveEvent e) {
		if(sleepPlayer.containsKey(e.getPlayer().getName())) {
			if(!PlayerManager.sleep.isSleep(e.getPlayer())) {
				wakeUp(e.getPlayer());
			}
		}
	}
	
	/**
	 * 玩家在睡觉时遭受攻击起床事件
	 * @param e
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void damageDuringSleeping(final EntityDamageEvent e) {
		if(!e.isCancelled()
				&&sleepPlayer.containsKey(e.getEntity().getName())) {
			wakeUp((Player) e.getEntity());
		}
	}
	
	@EventHandler
	public void wantBreakBlock(final BlockBreakEvent e) {
		if(!e.isCancelled()
				&&sleepPlayer.values().contains(e.getBlock())) {
			e.setDropItems(false);
			e.setExpToDrop(0);
		}
	}
	
	/**
	 * 使在睡觉的玩家起来
	 * @param p
	 */
	public static void wakeUp(Player p) {
		Block b = sleepPlayer.remove(p.getName());
		if(b!=null) {
			b.setType(Material.valueOf("AIR"));
		}
		Bukkit.getPluginManager().callEvent(
				new PlayerBedLeaveEvent(p, p.getLocation().getBlock(),false));
	}
	
	/**
	 * 强制让玩家在某地点睡着
	 * @param p
	 * @param loc
	 */
	protected static void sleep(Player p,Location loc) {
		PlayerBedEnterEvent event = 
				new PlayerBedEnterEvent(p, loc.getBlock(),BedEnterResult.OK);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			sleepPlayer.put(p.getName(),
					PlayerManager.sleep.sleep(p, loc)?loc.getBlock():null);
			p.sendMessage(I18N.tr("sleep-in-day.sleep-ver2"));
		}
	}
	
	public static void wakeUpAll() {
		sleepPlayer.forEach((s,b)->{
			Player p = Bukkit.getPlayer(s);
			if(b!=null) b.setType(Material.valueOf("AIR"));
			PlayerManager.sleep.leaveSleep(p);
			Bukkit.getPluginManager().callEvent(
					new PlayerBedLeaveEvent(p, p.getLocation().getBlock(),false));			
		});
		sleepPlayer.clear();
	}
	
}
