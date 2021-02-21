package miskyle.realsurvival.blockarray;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.blockarray.BlockArrayData;
import miskyle.realsurvival.data.blockarray.CubeData;

public class BlockArrayCreator {
  private static HashMap<String, BlockArrayCreator> creators = new HashMap<>();

  private String name = null;
  private Location mainBlockLoc;
  private ItemStack item;
  private boolean checkItem = false;
  private boolean reduceItem = false;
  private boolean checkCompass = false;

  public BlockArrayCreator(String name) {
    this.name = name;
  }

  public BlockArrayCreator() {

  }

  /**
   * 创建多方块结构机器.

   * @param player 玩家
   */
  public static void creat(Player player) {
    BlockArrayCreator creator = creators.get(player.getName());
    if (creator == null) {
      player.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-creator"));
      return;
    }

    if (creator.mainBlockLoc == null) {
      player.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-main-block"));
      return;
    }

    if (creator.checkItem && (creator.item == null || creator.item.getType() == Material.AIR)) {
      player.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-item"));
      return;
    }

    if (creator.name == null) {
      player.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-name"));
      return;
    }
    Block main = creator.mainBlockLoc.getBlock();
    BlockArrayData mid = getBlockShape(main);
    BlockArrayData up = getBlockShape(main.getRelative(BlockFace.UP));
    BlockArrayData down = getBlockShape(main.getRelative(BlockFace.DOWN));
    CubeData cube = new CubeData(up, mid, down, creator.name,
        "{NO-COMMAND}", creator.item, creator.checkCompass,
        creator.checkItem, creator.reduceItem);
    cube.save();
    creators.remove(player.getName());
    player.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.success"));
  }

  private static BlockArrayData getBlockShape(Block block) {
    return new BlockArrayData(
        getBlockKey(block.getRelative(BlockFace.NORTH)),
        getBlockKey(block.getRelative(BlockFace.NORTH_WEST)), 
        getBlockKey(block.getRelative(BlockFace.WEST)),
        getBlockKey(block.getRelative(BlockFace.SOUTH_WEST)), 
        getBlockKey(block.getRelative(BlockFace.SOUTH)),
        getBlockKey(block.getRelative(BlockFace.SOUTH_EAST)), 
        getBlockKey(block.getRelative(BlockFace.EAST)),
        getBlockKey(block.getRelative(BlockFace.NORTH_EAST)), 
        getBlockKey(block));
  }

  /**
   * 选择多方块结构的中心方块.

   * @param p 玩家
   */
  public static void chooesBlock(Player p) {
    if (!creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-creator"));
    }
    MCPT.plugin.getServer().getPluginManager()
          .registerEvents(new BlockArrayCreatorListener(p.getName()), MCPT.plugin);
    p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.choose-block-start"));
  }

  public static void chooesBlock(String playerName, Location mainBlockLoc) {
    creators.get(playerName).mainBlockLoc = mainBlockLoc;
  }

  /**
   * 设置是否消耗物品(暂时无用).

   * @param playerName 玩家名
   * @param bool 消耗
   * @return
   */
  public static boolean setReduceItem(String playerName, boolean bool) {
    if (!creators.containsKey(playerName)) {
      return false;      
    }
    creators.get(playerName).reduceItem = bool;
    return true;
  }

  /**
   * 是否检查多方块结构方位.

   * @param p 玩家名
   * @param bool 是否检查
   */
  public static void setCheckCompass(Player p, boolean bool) {
    if (!creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-creator"));
    }
    creators.get(p.getName()).reduceItem = bool;
    p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.check-compass", bool));
  }

  /**
   * 设置多方块结构名.

   * @param p 玩家名
   * @param name 多方块名
   */
  public static void setName(Player p, String name) {
    if (!creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.no-creator"));
    }
    creators.get(p.getName()).name = name;
    p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.set-name", name));
  }

  /**
   * 开始多方块结构编辑.

   * @param p 玩家
   */
  public static void start(Player p) {
    if (creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.in-creator"));
    } else {
      creators.put(p.getName(), new BlockArrayCreator());
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.start"));
    }
  }

  /**
   * 开始多方块结构编辑模式.

   * @param p    玩家
   * @param name 多方块结构名
   */
  public static void start(Player p, String name) {
    if (creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.error.in-creator"));
    } else {
      creators.put(p.getName(), new BlockArrayCreator(name));
      p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.start"));
    }
  }

  /**
   * 退出多方块结构编辑模式.

   * @param p 玩家
   */
  public static void cancel(Player p) {
    creators.remove(p.getName());
    p.sendMessage(Msg.getPrefix() + I18N.tr("cube-array.create.info.cancel"));
  }

  /**
   * 移除物品.

   * @param playerName 玩家名
   * @return
   */
  public static boolean removeItem(String playerName) {
    if (!creators.containsKey(playerName)) {
      return false;      
    }
    BlockArrayCreator creator = creators.get(playerName);
    creator.item = null;
    creator.checkItem = false;
    return true;
  }

  /**
   * 返回方块对应key.

   * @param block 目的方块
   * @return 相对应Minecraft版本所对应方块key
   */
  @SuppressWarnings("deprecation")
  public static String getBlockKey(Block block) {
    if (block.isEmpty()) {
      return "AIR"; 
    }
    if (ConfigManager.getBukkitVersion() >= 13) {
      return block.getType().name();
    } else {
      return block.getType().name() + ":" + block.getData();
    }
  }
}
