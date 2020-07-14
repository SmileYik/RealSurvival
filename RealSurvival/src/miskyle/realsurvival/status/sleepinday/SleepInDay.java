package miskyle.realsurvival.status.sleepinday;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import miskyle.realsurvival.data.ConfigManager;

public class SleepInDay {
	
	public static void sleep(Player p,Location loc) {
		if(ConfigManager.getSleepConfig().isEnable()
				&&ConfigManager.getSleepConfig().isSleepInDay()) {
			if(ConfigManager.getBukkitVersion()>=14) {
				SleepInDayListenerVer2.sleep(p, loc);
			}else {
				SleepInDayListenerVer1.sleep(p, loc);
			}
		}
	}
	
	public static void wakeUp(Player p) {
		if(ConfigManager.getSleepConfig().isEnable()
				&&ConfigManager.getSleepConfig().isSleepInDay()) {
			if(ConfigManager.getBukkitVersion()>=14) {
				SleepInDayListenerVer2.wakeUp(p);
			}else {
				SleepInDayListenerVer1.wakeUp(p);
			}
		}
	}
}
