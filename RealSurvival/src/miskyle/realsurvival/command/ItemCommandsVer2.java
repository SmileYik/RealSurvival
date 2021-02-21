package miskyle.realsurvival.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.item.RsItem;
import miskyle.realsurvival.util.Utils;

/**
 * 此类中包含了对物品操作的指令及方法, 此类适用于Minecraft更新出双持的版本, 具体对应Minecraft版本:1.9及以上版本.

 * @author MiSkYle
 * @version 2.0.0
 */
public class ItemCommandsVer2 {
  /**
   * 根据文件名取读物品.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "load" }, 
      args = { "", "cmd.args.item.load.item-name" }, 
      des = "cmd.des.item.load", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void loadItem(Player p, String[] args) {
    String name = getLineFromArgs(args, 1).replace("\\", "/");
    RsItem rsItem = RsItem.load(name);
    if (rsItem != null) {
      for (ItemStack i : p.getInventory().addItem(rsItem.getItem()).values()) {
        p.getWorld().dropItem(p.getLocation(), i);
      }
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.warning.item.load.failed"));
    }
  }

  /**
   * 保存一个文件至本地.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "save" }, 
      args = { "", "cmd.args.item.save.item-name" }, 
      des = "cmd.des.item.save", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void saveItem(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    String name = getLineFromArgs(args, 1).replace("\\", "/");
    if (new RsItem(item).save(name)) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.info.item.save.success"));
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.warning.item.save.failed"));
    }
  }

  /**
   * 给物品取名.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "setName" }, 
      args = { "", "cmd.args.item.set-name.name" }, 
      des = "cmd.des.item.set-name", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void setName(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    String name = Utils.setColor(getLineFromArgs(args, 1));
    im.setDisplayName(name);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 移除物品名字.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "removeName" }, 
      args = { "" }, 
      des = "cmd.des.item.remove-name", 
      permission = "RealSurvival.Command.Item")
  public void removeName(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    im.setDisplayName(null);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 设定某个物品为NBT物品.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "setNBTItem" }, 
      args = { "", "cmd.args.item.set-nbtitem.file-name" }, 
      des = "cmd.des.item.set-nbtitem", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void setNbtItem(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    String name = getLineFromArgs(args, 1).replace("\\", "/");
    if (ItemManager.isValidNbtItem(name)) {
      p.getInventory().setItemInMainHand(ItemManager.getNbt().setNBT(item, "RSNBT", name));
    } else {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.warning.item.set-nbtitem"));
    }
  }

  /**
   * 清除物品所有的lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "lore", "clear" }, 
      args = { "", "" }, 
      des = "cmd.des.item.clear-lore", 
      permission = "RealSurvival.Command.Item")
  public void clearLore(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    if (!im.hasLore()) {
      return;      
    }
    im.setLore(new ArrayList<String>());
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 删除末尾行lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "lore", "remove1" }, args = { "",
      "" }, des = "cmd.des.item.remove-lore-1", permission = "RealSurvival.Command.Item")
  public void removeLore1(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    if (!im.hasLore()) {
      return;      
    }
    List<String> lore = im.getLore();
    lore.remove(lore.size() - 1);
    im.setLore(lore);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 删除指定行的lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "lore", "remove2" }, 
      args = { "", "", "cmd.args.item.remove-lore-2.line" }, 
      des = "cmd.des.item.remove-lore-2", 
      permission = "RealSurvival.Command.Item")
  public void removeLore2(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    Integer i = getInt(args[2]);
    if (i == null) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-number"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    if (!im.hasLore()) {
      return;      
    }
    List<String> lore = im.getLore();
    if (i > lore.size()) {
      lore.remove(lore.size() - 1);
    } else {
      lore.remove(i - 1);
    }
    im.setLore(lore);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 在末尾增加一行lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "lore", "add1" }, 
      args = { "", "", "cmd.args.item.add-lore.input-any" }, 
      des = "cmd.des.item.add-lore", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void addLore1(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    String line = Utils.setColor(getLineFromArgs(args, 2));
    lore.add(line);
    im.setLore(lore);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 在指定行增加一行lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(subCmd = { "lore", "add2" }, 
      args = { 
          "", 
          "", 
          "cmd.args.item.add-lore.line",
          "cmd.args.item.add-lore.input-any"
          }, 
      des = "cmd.des.item.add-lore-2", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void addLore2(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    Integer i = getInt(args[2]);
    if (i == null) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-number"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    String line = Utils.setColor(getLineFromArgs(args, 3));
    if (i >= lore.size()) {
      lore.add(line);
    } else {
      lore.add(i - 1, line);
    }
    im.setLore(lore);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 替换某一行的lore.

   * @param p 玩家
   * @param args 参数
   */
  @Cmd(
      subCmd = { "lore", "replace" }, 
      args = { 
          "", 
          "", 
          "cmd.args.item.replace-lore.line",
          "cmd.args.item.replace-lore.input-any" 
          }, 
      des = "cmd.des.item.replace-lore", 
      permission = "RealSurvival.Command.Item", 
      unlimitedLength = true)
  public void replaceLore(Player p, String[] args) {
    ItemStack item = p.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.nothin-in-hand"));
      return;
    }
    Integer i = getInt(args[2]);
    if (i == null) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cmd.error.item.not-number"));
      return;
    }
    ItemMeta im = item.getItemMeta();
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    String line = Utils.setColor(getLineFromArgs(args, 3));
    if (i >= lore.size()) {
      i = lore.size() - 1;
    } else {
      i--;
    }
    lore.set(i, line);
    im.setLore(lore);
    item.setItemMeta(im);
    p.getInventory().setItemInMainHand(item);
  }

  /**
   * 从输入指令所得的args中获取连续性文本.

   * @param args 参数
   * @param index 起始位置
   * @return
   */
  private String getLineFromArgs(String[] args, int index) {
    StringBuilder sb = new StringBuilder();
    for (int i = index; i < args.length; i++) {
      sb.append(args[i]);
      sb.append(" ");
    }
    return sb.substring(0, sb.length() - 1);
  }

  /**
   * 将一个文本转化为数字.

   * @param s 文本
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
