package miskyle.realsurvival.command;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.crafttable.CraftTable;

public class RecipeCommands {
  @Cmd(subCmd = { "ctr", "create" }, 
      args = { "","","cmd.args.ctr.create.recipe-name","cmd.args.ctr.create.machine-name","cmd.args.ctr.create.forge-time" }, 
      des = "cmd.des.ctr.create",
      permission = "RealSurvival.Recipe.CraftTableRecipeCreate")
  public void createCraftTableRecipe(Player p,String[] args) {
    //rsr ctr create [recipeName] [machineName] [forgeTime]
    Integer forgeTime = getInt(args[4]);
    if(forgeTime == null) {
      p.sendMessage(Msg.getPrefix()+I18N.tr("cmd.error.item.not-number"));
      return;
    }
    CraftTableRecipe recipe = new CraftTableRecipe(args[2], args[3], null, forgeTime, null, null);
    String machineTitle = MachineManager.getMachineData(args[3]).getRight();
    CraftTable.openCreatorGUI(p, p.getLocation(), args[3], machineTitle, recipe);
  }
  
  /**
   * 将一个文本转化为数字
   * @param s
   * @return 转化成功返回数字,否则返回null
   */
  private Integer getInt(String s) {
      try {
          return Integer.parseInt(s);
      } catch (NumberFormatException e) {
          return null;
      }
  }
}
