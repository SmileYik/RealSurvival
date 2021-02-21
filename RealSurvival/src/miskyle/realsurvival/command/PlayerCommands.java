package miskyle.realsurvival.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.PlayerManager;

public class PlayerCommands {
  /**
   * 查看自身属性.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(
      subCmd = { "status" }, 
      args = { "" }, 
      des = "cmd.des.player.status")
  public void getStatus(Player p, String[] args) {
    if (PlayerManager.isActive(p.getName())) {
      p.sendMessage(PlayerManager.getPlayerData(p.getName()).getStatusMessage(p.isOp()));
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.info.player.status.freezing"));
    }
  }

  /**
   * 冻结自身属性.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(
      subCmd = { "freeze" }, 
      args = { "" }, 
      des = "cmd.des.player.freeze", 
      permission = "RealSurvival.Player.Admin")
  public void freeze(Player p, String[] args) {
    if (PlayerManager.isActive(p.getName())) {
      PlayerManager.freezePlayer(p.getName());
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.info.player.status.freezed"));
    }
  }

  @Cmd(subCmd = { "unfreeze" }, args = {
      "" }, des = "cmd.des.player.unfreeze", permission = "RealSurvival.Player.Admin")
  public void unfreeze(Player p, String[] args) {
    PlayerManager.activePlayer(p);
  }

  /**
   * 重载插件.

   * @param p 发送指令者
   * @param args 参数
   */
  @Cmd(
      subCmd = { "reload" }, 
      args = { "" }, 
      des = "cmd.des.reload", 
      permission = "RealSurvival.Player.Admin", 
      needPlayer = false)
  public void reload(CommandSender p, String[] args) {
    HandlerList.unregisterAll(MCPT.plugin);
    MCPT.plugin.getServer().getScheduler().cancelTasks(MCPT.plugin);
    MCPT.plugin.getServer().getPluginManager().disablePlugin(MCPT.plugin);
    MCPT.plugin.getServer().getPluginManager().enablePlugin(MCPT.plugin);
  }
}
