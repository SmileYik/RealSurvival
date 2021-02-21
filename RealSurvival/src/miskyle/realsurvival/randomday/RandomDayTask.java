package miskyle.realsurvival.randomday;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.config.RandomDayConfig;

public class RandomDayTask implements Runnable {
  private String name;
  private WorldData worldData;
  private NewDay today;
  private NewDay tomorrow;

  protected RandomDayTask(String worldName, WorldData worldData, NewDay today, NewDay tomorrow) {
    this.name = worldName;
    this.worldData = worldData;
    this.today = today;
    this.tomorrow = tomorrow;
  }

  @Override
  public void run() {
    World world = MCPT.plugin.getServer().getWorld(name);
    if (world.getTime() <= RandomDayConfig.getTick()) {
      today = tomorrow;
      tomorrow = NewDay.newDay(name, worldData, today.getDay() + 1);
    }
    if (Math.random() < today.getRainFrequency()) {
      randomRain(world, today.getHumidity() >= 70);
    } else {
      MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, () -> {
        world.setThunderDuration(0);
        world.setWeatherDuration(0);
      });
    }
    if (Math.random() < today.getWindFrequency()) {
      wind(name, today.getWindSpeed());
    }
  }

  /**
   * 刮风.

   * @param world 要刮风的世界.
   * @param windSpeed 风速.
   */
  public static void wind(String world, double windSpeed) {
    Vector v = new Vector(random(-1, 1), random(-0.5, 0.25), random(-1, 1));
    v = v.normalize().multiply(windSpeed);
    for (Player p : MCPT.plugin.getServer().getOnlinePlayers()) {
      if (p.getWorld().getName().equalsIgnoreCase(world) 
          && PlayerManager.isActive(p.getName())
          && !PlayerManager.getPlayerData(p.getName()).getSleep().isSleep() 
          && canBeWind(p.getLocation(), 0)) {
        p.setVelocity(p.getVelocity().add(v));
      }
    }
  }

  private static boolean canBeWind(Location loc, int times) {
    if (times == 10) {
      if (loc.getBlock().isEmpty()) {
        return true;
      } else {
        return false;
      }
    } else if (!loc.getBlock().isEmpty()) {
      return false;
    } else {
      return canBeWind(loc.add(0, 1, 0), times + 1);
    }
  }

  /**
   * 随机下雨.

   * @param world 要下雨的世界.
   * @param thunder 是否要打雷.
   */
  public static void randomRain(World world, boolean thunder) {
    MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, () -> {
      if (thunder) {
        double d = Math.random();
        if (d < 0.25) {
          rainWithThunder1(world);          
        } else if (d < 0.5) {
          rainWithThunder2(world);          
        } else if (d < 0.75) {
          rainWithThunder3(world);          
        } else {
          rainWithThunder4(world);          
        }
      } else {
        int i = RandomDayConfig.getTick();
        world.setWeatherDuration((int) random(i / 2, i * 2));
        world.setThunderDuration(0);
      }
    });
  }

  /**
   * 先打雷后下雨.

   * @param world 要下雨的世界.
   */
  public static void rainWithThunder1(World world) {
    // 先打雷后下雨
    world.setThundering(true);
    world.setThunderDuration((int) random(100, 500));
    MCPT.plugin.getServer().getScheduler().runTaskLater(MCPT.plugin, () -> {
      int i = RandomDayConfig.getTick();
      world.setWeatherDuration((int) random(i / 2, i * 2));
    }, (int) random(65, 156));
  }

  /**
   * 同时打雷下雨.

   * @param world 要下雨的世界.
   */
  public static void rainWithThunder2(World world) {
    // 同时打雷下雨
    int i = RandomDayConfig.getTick();
    i = (int) random(i / 2, i * 2);
    world.setThundering(true);
    world.setThunderDuration(i);
    world.setWeatherDuration(i);
  }

  /**
   * 先下雨后打雷.

   * @param world 要下雨的世界.
   */
  public static void rainWithThunder3(World world) {
    // 先下雨后打雷
    int i = RandomDayConfig.getTick();
    world.setWeatherDuration((int) random(i / 2, i * 2));
    MCPT.plugin.getServer().getScheduler().runTaskLater(MCPT.plugin, () -> {
      world.setThundering(true);
      world.setThunderDuration((int) random(i / 4, i / 2));
    }, (int) random(0, i / 2));
  }

  /**
   * 完全随机打雷下雨.

   * @param world 要下雨的世界.
   */
  public static void rainWithThunder4(World world) {
    // 完全随机
    int i = RandomDayConfig.getTick();
    MCPT.plugin.getServer().getScheduler().runTaskLater(MCPT.plugin, () -> {
      world.setWeatherDuration((int) random(i / 2, i * 2));
    }, (int) random(0, i / 2));
    MCPT.plugin.getServer().getScheduler().runTaskLater(MCPT.plugin, () -> {
      world.setThundering(true);
      world.setThunderDuration((int) random(i / 2, i * 2));
    }, (int) random(0, i / 2));
  }

  private static double random(double a, double b) {
    return Math.abs(a - b) * Math.random() + Math.min(a, b);
  }

  protected String getName() {
    return name;
  }

  protected WorldData getWorldData() {
    return worldData;
  }

  protected NewDay getToday() {
    return today;
  }

  protected NewDay getTomorrow() {
    return tomorrow;
  }

}
