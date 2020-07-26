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
  private static HashMap<String,BlockArrayCreator> creators = new HashMap<>();
  
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
  
  public static void creat(Player player) {
    BlockArrayCreator creator = creators.get(player.getName());
    if(creator == null) {
      player.sendMessage(Msg.getPrefix()
          +I18N.tr("cube-array.create.error.no-creator"));
      return;
    }
    
    if(creator.mainBlockLoc == null) {
      player.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.no-main-block"));
      return;
    }
    
    if(creator.checkItem 
        && (creator.item==null ||creator.item.getType() == Material.AIR)) {
      player.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.no-item"));
      return;
    }
    
    if(creator.name == null) {
      player.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.no-name"));
      return;
    }
    Block main = creator.mainBlockLoc.getBlock();
    BlockArrayData mid = getBlockShape(main),
             up = getBlockShape(main.getRelative(BlockFace.UP)),
             down = getBlockShape(main.getRelative(BlockFace.DOWN));
    CubeData cube = new CubeData(up, mid, down, 
        creator.name, 
        "{NO-COMMAND}", 
        creator.item, 
        creator.checkCompass, 
        creator.checkItem, 
        creator.reduceItem);
    cube.save();
    creators.remove(player.getName());
    player.sendMessage(Msg.getPrefix()
            +I18N.tr("cube-array.create.info.success"));
  }
  
  private static BlockArrayData getBlockShape(Block block) {
    return new BlockArrayData(getBlockKey(block.getRelative(BlockFace.NORTH)),
        getBlockKey(block.getRelative(BlockFace.NORTH_WEST)),
        getBlockKey(block.getRelative(BlockFace.WEST)),
        getBlockKey(block.getRelative(BlockFace.SOUTH_WEST)),
        getBlockKey(block.getRelative(BlockFace.SOUTH)),
        getBlockKey(block.getRelative(BlockFace.SOUTH_EAST)),
        getBlockKey(block.getRelative(BlockFace.EAST)),
        getBlockKey(block.getRelative(BlockFace.NORTH_EAST)),
        getBlockKey(block));
  }
  
  public static void chooesBlock(Player p) {
    if(!creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.no-creator"));
    }
    MCPT.plugin.getServer().getPluginManager().registerEvents(
        new BlockArrayCreatorListener(p.getName()), MCPT.plugin);
    p.sendMessage(Msg.getPrefix()
        +I18N.tr("cube-array.create.info.choose-block-start"));
  }

  public static void chooesBlock(String playerName,Location mainBlockLoc) {
    creators.get(playerName).mainBlockLoc = mainBlockLoc;
  }
  
  public static boolean setItem(Player player) {
    if(!creators.containsKey(player.getName())) return false;
    BlockArrayCreator creator = creators.get(player.getName());
    creator.item = player.getInventory().getItemInMainHand().clone();
    creator.checkItem = true;
    return true;
  }
  
  public static boolean setReduceItem(String playerName, boolean bool) {
    if(!creators.containsKey(playerName)) return false;
    creators.get(playerName).reduceItem = bool;
    return true;
  }
  
  public static void setCheckCompass(Player p, boolean bool) {
    if(!creators.containsKey(p.getName())) {
      p.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.no-creator"));
    }
    creators.get(p.getName()).reduceItem = bool;
    p.sendMessage(Msg.getPrefix()
        +I18N.tr("cube-array.create.info.check-compass",bool));
  }
  
  public static void setName(Player p, String name) {
    if(!creators.containsKey(p.getName())) {
        p.sendMessage(Msg.getPrefix()
                +I18N.tr("cube-array.create.error.no-creator"));
    }
    creators.get(p.getName()).name = name;
    p.sendMessage(Msg.getPrefix()
        +I18N.tr("cube-array.create.info.set-name",name));
  }
  
  /**
   * 开始多方块结构编辑
   * @param p
   */
  public static void start(Player p) {
    if(creators.containsKey(p.getName())) {
        p.sendMessage(Msg.getPrefix()
                +I18N.tr("cube-array.create.error.in-creator"));
    }else {
      creators.put(p.getName(), new BlockArrayCreator());
      p.sendMessage(Msg.getPrefix()
                +I18N.tr("cube-array.create.info.start"));
    }
  }
  
  /**
   * 开始多方块结构编辑模式
   * @param p 玩家
   * @param name 多方块结构名
   */
  public static void start(Player p,String name) {
    if(creators.containsKey(p.getName())) {
       p.sendMessage(Msg.getPrefix()
              +I18N.tr("cube-array.create.error.in-creator"));
    }else {
      creators.put(p.getName(), new BlockArrayCreator(name));
      p.sendMessage(Msg.getPrefix()
                +I18N.tr("cube-array.create.info.start"));
    }
  }
  
  /**
   * 退出多方块结构编辑模式
   * @param playerName
   */
  public static void cancel(Player p) {
    creators.remove(p.getName());
    p.sendMessage(Msg.getPrefix()
        +I18N.tr("cube-array.create.info.cancel"));
  }
  
  public static boolean removeItem(String playerName) {
    if(!creators.containsKey(playerName)) return false;
    BlockArrayCreator creator = creators.get(playerName);
    creator.item = null;
    creator.checkItem = false;
    return true;
  }
     
    @SuppressWarnings("deprecation")
    public static String getBlockKey(Block block) {
      if(ConfigManager.getBukkitVersion() >= 13) {
        return block.getType().name();
      }else {
        return block.getType().name()+":"+block.getData();
      }
    }
}
