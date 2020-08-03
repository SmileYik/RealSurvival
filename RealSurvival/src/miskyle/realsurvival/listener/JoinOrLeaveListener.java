package miskyle.realsurvival.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import miskyle.realsurvival.data.PlayerManager;

public class JoinOrLeaveListener implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PlayerManager.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		PlayerManager.removePlayer(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		PlayerManager.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
	  PlayerManager.playerDeath(e.getEntity());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
	  PlayerManager.addPlayer(e.getPlayer());
	}
}
