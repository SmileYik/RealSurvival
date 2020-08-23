package miskyle.realsurvival.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.crafttable.CraftTable;
import miskyle.realsurvival.machine.furnace.Furnace;

public class RecipeCommands {
  @Cmd(subCmd = { "reload" }, args = {
      "" }, des = "cmd.des.recipe.reload", permission = "RealSurvival.Recipe.Reload", needPlayer = false)
  public void reload(CommandSender p, String[] args) {
    MachineManager.init();
  }

  @Cmd(subCmd = { "ctr", "create" }, args = { "", "", "cmd.args.ctr.create.recipe-name",
      "cmd.args.ctr.create.machine-name",
      "cmd.args.ctr.create.forge-time" }, des = "cmd.des.ctr.create", permission = "RealSurvival.Recipe.CraftTableRecipeCreate")
  public void createCraftTableRecipe(Player p, String[] args) {
    // rsr ctr create [recipeName] [machineName] [forgeTime]
    Integer forgeTime = getInt(args[4]);
    if (forgeTime == null) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-number"));
      return;
    }
    CraftTableRecipe recipe = new CraftTableRecipe(args[2], args[3], null, forgeTime, null, null);
    String machineTitle = MachineManager.getMachineData(args[3]).getRight();
    CraftTable.openCreatorGUI(p, p.getLocation(), args[3], machineTitle, recipe);
  }

  @Cmd(subCmd = { "fr", "create" }, args = { "", "", "cmd.args.fr.create.recipe-name",
      "cmd.args.fr.create.machine-name", "cmd.args.fr.create.forge-time", "cmd.args.fr.create.min-temperature",
      "cmd.args.fr.create.max-temperature" }, des = "cmd.des.fr.create", permission = "RealSurvival.Recipe.FurnaceRecipeCreate")
  public void createFurnaceRecipe(Player p, String[] args) {
    // rsr ctr create [recipeName] [machineName] [forgeTime] [minTemperature]
    // [maxTemperature]
    Integer forgeTime = getInt(args[4]);
    Double minTemperature = getDouble(args[5]);
    Double maxTemperature = getDouble(args[6]);
    if (forgeTime == null || minTemperature == null || maxTemperature == null) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-number"));
      return;
    }
    FurnaceRecipe recipe = new FurnaceRecipe(args[2], args[3], null, forgeTime, null, null, maxTemperature,
        minTemperature);
    String machineTitle = MachineManager.getMachineData(args[3]).getRight();
    Furnace.openCreatorGUI(p, p.getLocation(), args[3], machineTitle, recipe);
  }

  /**
   * 将一个文本转化为数字
   * 
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

  /**
   * 将一个文本转化为数字
   * 
   * @param s
   * @return 转化成功返回数字,否则返回null
   */
  private Double getDouble(String s) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
