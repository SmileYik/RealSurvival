package tk.smileyik.realsurvival.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerCoreListener implements Listener {
  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {

  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerKickEvent playerKickEvent) {

  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerTeleport(PlayerTeleportEvent playerTeleportEvent) {

  }
}
