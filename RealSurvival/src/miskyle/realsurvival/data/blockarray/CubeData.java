package miskyle.realsurvival.data.blockarray;

import com.github.miskyle.mcpt.MCPT;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CubeData {
  private BlockArrayData up;
  private BlockArrayData mid;
  private BlockArrayData down;
  private String name;
  private String command;
  private ItemStack item;
  private boolean checkCompass;
  private boolean checkItem;
  private boolean reduceItem;

  public static String PATH = MCPT.plugin.getDataFolder() + "/CubeData";

  /**
   * 初始化个多方块结构.

   * @param up 上层方块排列
   * @param mid 中层方块排列
   * @param down 下层方块排列
   * @param name 多方块结构名
   * @param command 附带的指令
   * @param item 使用的物品
   * @param checkCompass 判断方位
   * @param checkItem 判断物品
   * @param reduceItem 消耗物品
   */
  public CubeData(BlockArrayData up, BlockArrayData mid, BlockArrayData down, String name, 
      String command, ItemStack item, boolean checkCompass, boolean checkItem, boolean reduceItem) {
    super();
    this.up = up;
    this.mid = mid;
    this.down = down;
    this.name = name;
    this.command = command;
    this.item = item;
    this.checkCompass = checkCompass;
    this.checkItem = checkItem;
    this.reduceItem = reduceItem;
  }

  public static CubeData load(String name) {
    File file = new File(PATH + "/" + name + ".yml");
    return load(file);
  }

  /**
   * 从文件中读取多方块结构.

   * @param file 指定文件
   * @return 若有效则返回相对应多方块结构, 反之则返回null
   */
  public static CubeData load(File file) {
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    BlockArrayData up;
    BlockArrayData mid;
    BlockArrayData down;
    up = new BlockArrayData(
        data.getString("up.north"),
        data.getString("up.north-west"), 
        data.getString("up.west"),
        data.getString("up.south-west"), 
        data.getString("up.south"), 
        data.getString("up.south-east"),
        data.getString("up.east"), 
        data.getString("up.north-east"), 
        data.getString("up.main"));
    mid = new BlockArrayData(
        data.getString("mid.north"),
        data.getString("mid.north-west"), 
        data.getString("mid.west"),
        data.getString("mid.south-west"), 
        data.getString("mid.south"), 
        data.getString("mid.south-east"),
        data.getString("mid.east"),
        data.getString("mid.north-east"), 
        data.getString("mid.main"));
    down = new BlockArrayData(
        data.getString("down.north"), 
        data.getString("down.north-west"),
        data.getString("down.west"), 
        data.getString("down.south-west"), 
        data.getString("down.south"),
        data.getString("down.south-east"), 
        data.getString("down.east"),
        data.getString("down.north-east"),
        data.getString("down.main"));
    return new CubeData(up, mid, down, 
        data.getString("name"), 
        data.getString("command"), 
        data.getItemStack("item"),
        data.getBoolean("check-compass"), 
        data.getBoolean("check-item"), 
        data.getBoolean("reduce-item"));
  }

  /**
   * 保存多方块结构.
   */
  public void save() {
    File file = new File(PATH + "/" + name + ".yml");
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    data.set("name", name);
    data.set("command", command);
    data.set("item", item);
    data.set("check-item", checkItem);
    data.set("reduce-item", reduceItem);
    data.set("check-compass", checkCompass);

    data.set("up.north", up.getNorth());
    data.set("up.north-west", up.getNorthWest());
    data.set("up.west", up.getWest());
    data.set("up.south-west", up.getSouthWest());
    data.set("up.south", up.getSouth());
    data.set("up.south-east", up.getSouthEast());
    data.set("up.east", up.getEast());
    data.set("up.north-east", up.getNorthEast());
    data.set("up.main", up.getMain());

    data.set("mid.north", mid.getNorth());
    data.set("mid.north-west", mid.getNorthWest());
    data.set("mid.west", mid.getWest());
    data.set("mid.south-west", mid.getSouthWest());
    data.set("mid.south", mid.getSouth());
    data.set("mid.south-east", mid.getSouthEast());
    data.set("mid.east", mid.getEast());
    data.set("mid.north-east", mid.getNorthEast());
    data.set("mid.main", mid.getMain());

    data.set("down.north", down.getNorth());
    data.set("down.north-west", down.getNorthWest());
    data.set("down.west", down.getWest());
    data.set("down.south-west", down.getSouthWest());
    data.set("down.south", down.getSouth());
    data.set("down.south-east", down.getSouthEast());
    data.set("down.east", down.getEast());
    data.set("down.north-east", down.getNorthEast());
    data.set("down.main", down.getMain());

    try {
      data.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static File getPath() {
    return new File(PATH);
  }

  public BlockArrayData getUp() {
    return up;
  }

  public void setUp(BlockArrayData up) {
    this.up = up;
  }

  public BlockArrayData getMid() {
    return mid;
  }

  public void setMid(BlockArrayData mid) {
    this.mid = mid;
  }

  public BlockArrayData getDown() {
    return down;
  }

  public void setDown(BlockArrayData down) {
    this.down = down;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public boolean isCheckCompass() {
    return checkCompass;
  }

  public void setCheckCompass(boolean checkCompass) {
    this.checkCompass = checkCompass;
  }

  public boolean isCheckItem() {
    return checkItem;
  }

  public void setCheckItem(boolean checkItem) {
    this.checkItem = checkItem;
  }

  public boolean isReduceItem() {
    return reduceItem;
  }

  public void setReduceItem(boolean reduceItem) {
    this.reduceItem = reduceItem;
  }

  public ItemStack getItem() {
    return item;
  }

  public void setItem(ItemStack item) {
    this.item = item;
  }

}
