package miskyle.realsurvival.command;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.status.task.TemperatureTask;

public class PlayerCommands {
  @Cmd(subCmd = { "status" }, 
      args = { "" }, 
      des = "cmd.des.player.status")
  public void getStatus(Player p, String[] args) {
    if (PlayerManager.isActive(p.getName())) {
      p.sendMessage(PlayerManager.getPlayerData(p.getName()).getStatusMessage(p.isOp()));
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.info.player.status.freezing"));
    }
  }
  
  
  
  @Cmd(subCmd = { "freeze" }, 
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
  
  @Cmd(subCmd = { "unfreeze" }, 
      args = { "" }, 
      des = "cmd.des.player.unfreeze",
      permission = "RealSurvival.Player.Admin")
  public void unfreeze(Player p, String[] args) {
    PlayerManager.activePlayer(p);
  }
  
  @Cmd(subCmd = { "debugTemperature" }, 
      args = { "" }, 
      des = "cmd.des.player.unfreeze",
      permission = "RealSurvival.Player.Admin")
  public void debugTemperature(Player p, String[] args) {
    TemperatureTask.debug(p.getName());
  }
}
