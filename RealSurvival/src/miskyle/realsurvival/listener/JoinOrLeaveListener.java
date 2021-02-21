package miskyle.realsurvival.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.data.PlayerManager;

public class JoinOrLeaveListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    PlayerManager.addPlayer(e.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    PlayerManager.removePlayer(e.getPlayer().getName());
  }
  
  /*
   * ***********************
   *            2020.8.20               *
   *   修复玩家传送到其他      *
   *   世界时变量失效问题      *
   * ***********************
   */
  /**
   * 当玩家传送时, 确定是否启用插件.

   * @param e 玩家传送事件
   */
  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    if (!e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
      Bukkit.getScheduler().runTaskLaterAsynchronously(MCPT.plugin, () -> {
        PlayerManager.addPlayer(e.getPlayer());        
      }, 20L);
    }
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
