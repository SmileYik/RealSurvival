package miskyle.realsurvival.command;

import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.blockarray.BlockArrayCreator;

public class CubeArrayCommands {

  @Cmd(subCmd = { "run" }, 
      args = { "" }, 
      des = "cmd.des.cube-array.run", 
      permission = "RealSurvival.Command.CubeArray")
  public void runCubeArrayCreator(Player p, String[] args) {
    BlockArrayCreator.start(p);
  }

  @Cmd(subCmd = { "finish" }, 
      args = { "" }, 
      des = "cmd.des.cube-array.finish", 
      permission = "RealSurvival.Command.CubeArray")
  public void finish(Player p, String[] args) {
    BlockArrayCreator.creat(p);
  }

  @Cmd(subCmd = { "cancel" }, 
      args = { "" }, 
      des = "cmd.des.cube-array.cancel", 
      permission = "RealSurvival.Command.CubeArray")
  public void cancel(Player p, String[] args) {
    BlockArrayCreator.cancel(p);
  }

  @Cmd(subCmd = { "chooseBlock" }, 
      args = { "" }, 
      des = "cmd.des.cube-array.choose-block", 
      permission = "RealSurvival.Command.CubeArray")
  public void setMainBlock(Player p, String[] args) {
    BlockArrayCreator.chooesBlock(p);
  }

  @Cmd(subCmd = { "setName" }, 
      args = { "", "cmd.args.cube-array.name" },
      des = "cmd.des.cube-array.set-name", 
      permission = "RealSurvival.Command.CubeArray")
  public void setCubeArrayName(Player p, String[] args) {
    BlockArrayCreator.setName(p, args[1]);
  }

  /**
   * 设定方位指令.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "setCompass" }, 
      args = { "", "cmd.args.cube-array.compass" }, 
      des = "cmd.des.cube-array.set-compass",
      permission = "RealSurvival.Command.CubeArray")
  public void setCubeArrayCompass(Player p, String[] args) {
    Boolean bool = null;
    try {
      bool = Boolean.parseBoolean(args[1]);
    } catch (Exception e) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-boolean"));
      return;
    }
    BlockArrayCreator.setCheckCompass(p, bool);
  }

}
