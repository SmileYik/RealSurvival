package miskyle.realsurvival.status.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import miskyle.realsurvival.data.PlayerManager;

public class SleepListener implements Listener{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(final PlayerBedEnterEvent e) {
		if(!e.isCancelled() 
				&& PlayerManager.isActive(e.getPlayer().getName())) {
			PlayerManager.getPlayerData(
					e.getPlayer().getName()).getSleep().setSleep(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void leaveSleep(final PlayerBedLeaveEvent e) {
		if(PlayerManager.isActive(e.getPlayer().getName())) {
			PlayerManager.getPlayerData(
					e.getPlayer().getName()).getSleep().setSleep(false);
		}
	}
}
