package miskyle.realsurvival.machine;

import com.github.miskyle.mcpt.MCPT;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.blockarray.CubeData;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.data.recipe.Recipe;
import miskyle.realsurvival.data.recipe.RecipeType;
import miskyle.realsurvival.machine.crafttable.CraftTableTimer;
import miskyle.realsurvival.machine.furnace.FurnaceTimer;
import miskyle.realsurvival.machine.raincollector.RainCollectorTimer;
import miskyle.realsurvival.util.RsEntry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

public class MachineManager {
  
  /**
   * 存放工作台计时器.
   * 
   * <p>key: 工作台位置
   * 
   * <p>value: 机器计时器
   */
  private static ConcurrentHashMap<String, MachineTimer> timers;
  /**
   * 存放工作台访问权限.
   * 
   * <p>key: 工作台位置
   * 
   * <p>value: 机器访问权限数据.
   */
  private static ConcurrentHashMap<String, MachineAccess> accesses;
  
  /**
   * 以多方块结构名称存放多方块结构数据.
   * 
   * <p>key: 多方块结构名称
   * 
   * <p>value: 多方块结构数据
   */
  private static HashMap<String, CubeData> cubes;
  /**
   * 以中心方块来分类存放多方块结构数据.
   * 
   * <p>key: 多方块结构中心方块对应名称
   * 
   * <p>value: 多方块结构数据列表
   */
  private static HashMap<String, ArrayList<CubeData>> cubesMainBlockCube;
  /**
   * 以多方块结构名分类存放机器名.
   * 
   * <p>key: 多方块结构名
   * 
   * <p>value: 机器名
   */
  private static HashMap<String, String> cubeMachine;
  /**
   * 以机器名分类存放多方块结构名.
   * 
   * <p>key: 机器名
   * 
   * <p>value: 多方块结构名
   */
  private static HashMap<String, String> machineCube;
  private static HashMap<String, RsEntry<RecipeType, String>> machineData;

  private static HashMap<String, ArrayList<Recipe>> craftTableRecipes;
  private static HashMap<String, Recipe> craftTableRecipesMap;

  private static HashMap<String, ArrayList<Recipe>> furnaceRecipes;
  private static HashMap<String, Recipe> furnaceRecipesMap;

  /**
   * 初始化机器管理器.
   */
  public static void init() {
    loadCubes();
    loadMachineData();
    loadCraftTableRecipe();
    loadFurnaceRecipes();
    loadTimers();
    loadMachineAccesses();
  }

  private static void loadCubes() {
    cubes = new HashMap<>();
    cubesMainBlockCube = new HashMap<>();
    loadCubes(CubeData.getPath());
  }

  private static void loadCubes(File file) {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        loadCubes(f);
      }
    } else {
      CubeData cube = CubeData.load(file);
      cubes.put(cube.getName(), cube);
      if (cubesMainBlockCube.containsKey(cube.getMid().getMain())) {
        cubesMainBlockCube.get(cube.getMid().getMain()).add(cube);
      } else {
        ArrayList<CubeData> data = new ArrayList<>();
        data.add(cube);
        cubesMainBlockCube.put(cube.getMid().getMain(), data);
      }
    }
  }

  private static void loadFurnaceRecipes() {
    furnaceRecipes = new HashMap<>();
    furnaceRecipesMap = new HashMap<>();
    loadFurnaceRecipes(new File(MCPT.plugin.getDataFolder() + "/recipe/furnace"));
  }

  private static void loadFurnaceRecipes(File file) {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        loadFurnaceRecipes(f);
      }
    } else {
      FurnaceRecipe recipe = FurnaceRecipe.loadRecipe(file);
      furnaceRecipesMap.put(recipe.getRecipeName(), (Recipe) recipe);
      if (furnaceRecipes.containsKey(recipe.getMachineName())) {
        furnaceRecipes.get(recipe.getMachineName()).add((Recipe) recipe);
      } else {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add((Recipe) recipe);
        furnaceRecipes.put(recipe.getMachineName(), recipes);
      }
    }
  }

  private static void loadCraftTableRecipe() {
    craftTableRecipes = new HashMap<>();
    craftTableRecipesMap = new HashMap<>();
    loadCraftTableRecipe(new File(MCPT.plugin.getDataFolder() + "/recipe/crafttable"));
  }

  private static void loadCraftTableRecipe(File file) {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        loadCraftTableRecipe(f);
      }
    } else {
      CraftTableRecipe recipe = CraftTableRecipe.loadRecipe(file);
      craftTableRecipesMap.put(recipe.getRecipeName(), (Recipe) recipe);
      if (craftTableRecipes.containsKey(recipe.getMachineName())) {
        craftTableRecipes.get(recipe.getMachineName()).add((Recipe) recipe);
      } else {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add((Recipe) recipe);
        craftTableRecipes.put(recipe.getMachineName(), recipes);
      }
    }
  }

  private static void loadMachineData() {
    // MachineName,CubeName,WorkbenchType,GUITitle
    cubeMachine = new HashMap<>();
    machineCube = new HashMap<>();
    machineData = new HashMap<>();
    MCPT.plugin.getConfig().getStringList("machine").forEach(line -> {
      String[] temp = line.split(",");
      if (cubeMachine.containsKey(temp[1])) {
        MCPT.plugin.getLogger().info(temp[0] + "所使用的多方块结构被占用.");
        return;
      }
      cubeMachine.put(temp[1], temp[0]);
      machineCube.put(temp[0], temp[1]);
      machineData.put(temp[0], 
          new RsEntry<RecipeType, String>(RecipeType.valueOf(temp[2]), temp[3]));
    });
  }

  public static RsEntry<RecipeType, String> getMachineData(String machineName) {
    return machineData.get(machineName);
  }

  public static RsEntry<RecipeType, String> getMachineDataByCubeName(String cubeName) {
    return getMachineData(cubeMachine.get(cubeName));
  }

  public static String getMachineNameByCubeName(String cubeName) {
    return cubeMachine.get(cubeName);
  }

  public static boolean isActiveTimer(Location loc) {
    return timers.containsKey(getTimerKey(loc));
  }

  public static boolean isActiveTimer(String key) {
    return timers.containsKey(key);
  }


  public static MachineTimer getTimer(Location loc) {
    return timers.get(getTimerKey(loc));
  }

  public static MachineTimer getTimer(String key) {
    return timers.get(key);
  }

  /**
   * 加入机器计时器.

   * @param timer 机器计时器
   */
  public static void addTimer(MachineTimer timer) {
    if (!timers.containsKey(getTimerKey(timer))) {
      timers.put(getTimerKey(timer), timer);
    }
  }

  public static MachineTimer removeTimer(MachineTimer timer) {
    return timers.remove(getTimerKey(timer));
  }

  public static MachineTimer removeTimer(Location loc) {
    return timers.remove(getTimerKey(loc));
  }

  public static MachineTimer removeTimer(String key) {
    return timers.remove(key);
  }

  public static String getTimerKey(MachineTimer timer) {
    return timer.getWorldName() + timer.getX() + timer.getY() + timer.getZ();
  }

  public static String getTimerKey(String worldName, int x, int y, int z) {
    return worldName + x + y + z;
  }

  public static String getTimerKey(Location loc) {
    return loc.getWorld().getName() + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ();
  }

  /**
   * 获取某种类型的机器的所有配方.

   * @param type 机器类型
   * @param machineName 机器名
   * @return
   */
  public static ArrayList<Recipe> getRecipes(MachineType type, String machineName) {
    if (type == MachineType.CRAFT_TABLE) {
      return craftTableRecipes.get(machineName);
    } else if (type == MachineType.FURNACE) {
      return furnaceRecipes.get(machineName);
    }
    return null;
  }

  /**
   * 获取配方.

   * @param type 机器类型
   * @param recipeName 配方名
   * @return
   */
  public static Recipe getRecipe(MachineType type, String recipeName) {
    if (type == MachineType.CRAFT_TABLE) {
      return craftTableRecipesMap.get(recipeName);
    } else if (type == MachineType.FURNACE) {
      return furnaceRecipesMap.get(recipeName);
    }
    return null;
  }

  public static ConcurrentHashMap<String, MachineTimer> getTimers() {
    return timers;
  }

  /**
   * 获取多方块结构数据.

   * @param cubeName 多方块结构名
   * @return 多方块结构数据
   */
  public static CubeData getCubeData(String cubeName) {
    return cubes.get(cubeName);
  }

  /**
   * 获取指定机器的多方块结构名.

   * @param machineName 机器名
   * @return 多方块结构名
   */
  public static String getMachineCubeName(String machineName) {
    return machineCube.get(machineName);
  }

  /**
   * 获取指定机器的多方块结构数据.

   * @param machineName 机器名
   * @return 多方块结构数据
   */
  public static CubeData getMachineCube(String machineName) {
    return getCubeData(machineCube.get(machineName));
  }

  /**
   * 以多方块结构的中心方块获取多方块结构.

   * @param b 中心方块
   * @return 符合条件的多方块结构列表.
   */
  public static ArrayList<CubeData> getCubesByMainBlock(Block b) {
    return cubesMainBlockCube.getOrDefault(BlockArrayCreator.getBlockKey(b), new ArrayList<>());
  }
  
  /**
   * 设定机器权限.

   * @param loc 机器所在地址
   * @param access 权限
   */
  public static void setMachineAccess(Location loc, MachineAccess access) {
    String key = getTimerKey(loc);
    if (accesses.containsKey(key)) {
      accesses.replace(key, access);
    } else {
      accesses.put(key, access);
    }
  }
  
  public static MachineAccess getMachineAccess(Location loc) {
    return accesses.get(getTimerKey(loc));
  }
  
  public static void removeMachineAccess(Location loc) {
    getMachineAccess(loc).toDefault();
  }

  /**
   * 保存计时器.
   */
  public static void saveTimers() {
    File file = new File(MCPT.plugin.getDataFolder(), "timer.yml");
    file.delete();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    timers.forEach(timers.mappingCount(), (k, v) -> {
      config.set(k + ".type", v.getType().name());
      config.set(k + ".world-name", v.getWorldName());
      config.set(k + ".x", v.getX());
      config.set(k + ".y", v.getY());
      config.set(k + ".z", v.getZ());
      config.set(k + ".time", v.getTime());
      config.set(k + ".player-name", v.getPlayerName());
      if (v.getType() == MachineType.CRAFT_TABLE) {
        CraftTableTimer timer = (CraftTableTimer) v;
        config.set(k + ".times", timer.getTimes());
        config.set(k + ".recipe-name", timer.getRecipe().getRecipeName());
      } else if (v.getType() == MachineType.FURNACE) {
        FurnaceTimer timer = (FurnaceTimer) v;
        config.set(k + ".times", timer.getTimes());
        config.set(k + ".recipe-name", timer.getRecipe().getRecipeName());
        config.set(k + ".extra-temperature", timer.getExtraTemperature());
      }
    });
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadTimers() {
    timers = new ConcurrentHashMap<>();
    File file = new File(MCPT.plugin.getDataFolder(), "timer.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    config.getKeys(false).forEach(k -> {
      MachineType type = MachineType.valueOf(config.getString(k + ".type"));
      String worldName = config.getString(k + ".world-name");
      String playerName = config.getString(k + ".player-name");
      int x = config.getInt(k + ".x");
      int y = config.getInt(k + ".y");
      int z = config.getInt(k + ".z");
      int time = config.getInt(k + ".time");
      if (type == MachineType.RAIN_COLLECTOR) {
        RainCollectorTimer temp = new RainCollectorTimer(playerName, time, worldName, x, y, z);
        timers.put(k, temp);
      } else if (type == MachineType.CRAFT_TABLE) {
        String recipeName = config.getString(k + ".recipe-name");
        int times = config.getInt(k + ".times");
        CraftTableTimer temp = new CraftTableTimer(playerName, worldName, x, y, z,
            (CraftTableRecipe) getRecipe(MachineType.CRAFT_TABLE, recipeName), time, times);
        timers.put(k, temp);
      } else if (type == MachineType.FURNACE) {
        String recipeName = config.getString(k + ".recipe-name");
        int times = config.getInt(k + ".times");
        double extraTem = config.getDouble(k + ".extra-temperature");
        FurnaceTimer temp = new FurnaceTimer(playerName, worldName, x, y, z,
            (FurnaceRecipe) getRecipe(MachineType.FURNACE, recipeName), time, times, extraTem);
        timers.put(k, temp);
      }
    });
  }
  
  private static void loadMachineAccesses() {
    accesses = new ConcurrentHashMap<>();
    File file = new File(MCPT.plugin.getDataFolder(), "MachineAccess.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    config.getKeys(false).forEach(key -> {
      accesses.put(key, new MachineAccess(
          config.getString(key + ".owner", "null"), 
          (short) config.getInt(key + ".mode"), 
          config.getStringList(key + ".players")));
    });
  }
  
  /**
   * 保存机器权限.
   */
  public static void saveMachineAccesses() {
    File file = new File(MCPT.plugin.getDataFolder(), "MachineAccess.yml");
    file.delete();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    accesses.forEach(accesses.mappingCount(), (k, v) -> {
      config.set(k + ".owner", v.getOwner());
      config.set(k + ".mode", v.getMode());
      config.set(k + ".players", v.getPlayers());
    });
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
