package miskyle.realsurvival.data.item;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RsEntry;

public class RsItemData {

  private RsEntry<Double, Double> sleep;
  private RsEntry<Double, Double> thirst;
  private RsEntry<Double, Double> energy;
  private RsEntry<Double, Double> hunger;
  private RsEntry<Double, Double> health;
  private RsEntry<Double, Double> temperature;
  private boolean maxSleep = false;
  private boolean maxThirst = false;
  private boolean maxEnergy = false;
  private boolean maxHunger = false;
  private boolean maxHealth = false;
  private double weight;
  private DrugData drugData;

  public RsItemData() {

  }

  /**
   * 初始化.

   * @param sleep 有关属性.
   * @param thirst 有关属性
   * @param energy 有关属性
   * @param hunger 有关属性
   * @param health 有关属性
   * @param temperature 有关属性
   * @param weight 有关属性
   * @param drugData 有关属性
   */
  public RsItemData(RsEntry<Double, Double> sleep, 
      RsEntry<Double, Double> thirst, 
      RsEntry<Double, Double> energy,
      RsEntry<Double, Double> hunger, 
      RsEntry<Double, Double> health, 
      RsEntry<Double, Double> temperature,
      double weight, DrugData drugData) {
    super();
    this.sleep = sleep;
    this.thirst = thirst;
    this.energy = energy;
    this.hunger = hunger;
    this.health = health;
    this.weight = weight;
    this.temperature = temperature;
    this.drugData = drugData;
  }

  /**
   * 初始化有关属性.

   * @param sleep 有关属性
   * @param thirst 有关属性
   * @param energy 有关属性
   * @param hunger 有关属性
   * @param health 有关属性
   * @param temperature 有关属性
   * @param weight 有关属性
   * @param drug 有关属性
   */
  public RsItemData(String[] sleep, 
      String[] thirst, 
      String[] energy, 
      String[] hunger, 
      String[] health,
      String[] temperature, 
      double weight, 
      DrugData drug) {

    this.energy = 
        new RsEntry<Double, Double>(Double.parseDouble(energy[0]), Double.parseDouble(energy[1]));
    this.sleep = 
        new RsEntry<Double, Double>(Double.parseDouble(sleep[0]), Double.parseDouble(sleep[1]));
    this.thirst = 
        new RsEntry<Double, Double>(Double.parseDouble(thirst[0]), Double.parseDouble(thirst[1]));
    this.hunger = 
        new RsEntry<Double, Double>(Double.parseDouble(hunger[0]), Double.parseDouble(hunger[1]));
    this.health = 
        new RsEntry<Double, Double>(Double.parseDouble(health[0]), Double.parseDouble(health[1]));
    this.temperature = 
        new RsEntry<Double, Double>(
            Double.parseDouble(temperature[0]), Double.parseDouble(temperature[1]));
    this.weight = weight;
    this.drugData = drug;
  }

  /**
   * 增加相应属性值.

   * @param status 属性.
   * @param left 左值
   * @param right 右值
   */
  public void addStatusValue(String status, double left, double right) {
    switch (status.toUpperCase()) {
      case "SLEEP":
        if (sleep == null) {
          sleep = new RsEntry<Double, Double>(left, right);        
        } else {
          sleep.set(left + sleep.getLeft(), right + sleep.getRight());        
        }
        break;
      case "THIRST":
        if (thirst == null) {
          thirst = new RsEntry<Double, Double>(left, right);          
        } else {
          thirst.set(left + thirst.getLeft(), right + thirst.getRight());          
        }
        break;
      case "ENERGY":
        if (energy == null) {
          energy = new RsEntry<Double, Double>(left, right);          
        } else {
          energy.set(left + energy.getLeft(), right + energy.getRight());          
        }
        break;
      case "HUNGER":
        if (hunger == null) {
          hunger = new RsEntry<Double, Double>(left, right);          
        } else {
          hunger.set(left + hunger.getLeft(), right + hunger.getRight());          
        }
        break;
      case "WEIGHT":
        weight += left;
        break;
      case "HEALTH":
        if (health == null) {
          health = new RsEntry<Double, Double>(left, right);          
        } else {
          health.set(left + health.getLeft(), right + health.getRight());          
        }
        break;
      case "TEMPERATURE":
        if (temperature == null) {
          temperature = new RsEntry<Double, Double>(left, right);
        } else {
          temperature.set(left + temperature.getLeft(), right + temperature.getRight());
        }
        break;
      default:
        break;
    }
  }

  public void save(String fileName) {
    save(new File(MCPT.plugin.getDataFolder() + "/nbtitem/" + fileName + ".yml"));
  }

  /**
   * 保存物品数据.

   * @param file 指定文件.
   */
  public void save(File file) {
    if (file.exists()) {
      return;      
    }
    file.getParentFile().mkdirs();
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    if (sleep != null && sleep.getLeft() != 0 && sleep.getRight() != 0) {
      data.set("sleep", sleep.getLeft() + "/" + sleep.getRight());
      data.set("sleep-max", maxSleep);
    }
    if (thirst != null && thirst.getLeft() != 0 && thirst.getRight() != 0) {
      data.set("thirst", thirst.getLeft() + "/" + thirst.getRight());
      data.set("thirst-max", maxThirst);
    }
    if (energy != null && energy.getLeft() != 0 && energy.getRight() != 0) {
      data.set("energy", energy.getLeft() + "/" + energy.getRight());
      data.set("energy-max", maxEnergy);
    }
    if (hunger != null && hunger.getLeft() != 0 && hunger.getRight() != 0) {
      data.set("hunger", hunger.getLeft() + "/" + hunger.getRight());
      data.set("hunger-max", maxHunger);
    }
    if (health != null && health.getLeft() != 0 && health.getRight() != 0) {
      data.set("health", health.getLeft() + "/" + health.getRight());
      data.set("health-max", maxHealth);
    }
    if (weight != 0) {
      data.set("weight", weight);
    }
    try {
      data.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setStatusValue(String status, String[] values) {
    setStatusValue(status, Double.parseDouble(values[0]), Double.parseDouble(values[1]));
  }

  /**
   * 设定(增加) 属性值.

   * @param status 属性.
   * @param left 左值
   * @param right 右值
   */
  public void setStatusValue(String status, double left, double right) {
    switch (status.toUpperCase()) {
      case "SLEEP":
        if (sleep == null) {
          sleep = new RsEntry<Double, Double>(left, right);          
        } else {
          sleep.set(left, right);          
        }
        break;
      case "THIRST":
        if (thirst == null) {
          thirst = new RsEntry<Double, Double>(left, right);          
        } else {
          thirst.set(left, right);          
        }
        break;
      case "ENERGY":
        if (energy == null) {
          energy = new RsEntry<Double, Double>(left, right);          
        } else {
          energy.set(left, right);          
        }
        break;
      case "HUNGER":
        if (hunger == null) {
          hunger = new RsEntry<Double, Double>(left, right);          
        } else {
          hunger.set(left, right);          
        }
        break;
      case "WEIGHT":
        weight = left;
        break;
      case "HEALTH":
        if (health == null) {
          health = new RsEntry<Double, Double>(left, right);          
        } else {
          health.set(left, right);          
        }
        break;
      case "TEMPERATURE":
        if (temperature == null) {
          temperature = new RsEntry<Double, Double>(left, right);          
        } else {
          temperature.set(left, right);          
        }
        break;
      default:
        break;
    }
  }

  public RsEntry<Double, Double> getSleep() {
    return sleep;
  }

  /**
   * 获取一个随机的Sleep属性值.

   * @return
   */
  public double getSleepValue() {
    return random(sleep.getLeft(), sleep.getRight());
  }

  /**
   * 判断该物品是否有Sleep属性值.

   * @return
   */
  public boolean isValidSleep() {
    if (ConfigManager.getSleepConfig().isEnable()) {
      return sleep != null && !(sleep.getLeft() == 0 && sleep.getRight() == 0);
    } else {
      return false;
    }
  }

  public void setSleep(RsEntry<Double, Double> sleep) {
    this.sleep = sleep;
  }

  public void setSleep(String[] sleep) {
    this.sleep = 
        new RsEntry<Double, Double>(Double.parseDouble(sleep[0]), Double.parseDouble(sleep[1]));
  }

  public RsEntry<Double, Double> getThirst() {
    return thirst;
  }

  public double getThirstValue() {
    return random(thirst.getLeft(), thirst.getRight());
  }

  /**
   * Thirst属性是否有效.

   * @return
   */
  public boolean isValidThirst() {
    if (ConfigManager.getThirstConfig().isEnable()) {
      return thirst != null && !(thirst.getLeft() == 0 && thirst.getRight() == 0);
    } else {
      return false;
    }
  }

  public void setThirst(RsEntry<Double, Double> thirst) {
    this.thirst = thirst;
  }

  public void setThirst(String[] thirst) {
    this.thirst =
        new RsEntry<Double, Double>(Double.parseDouble(thirst[0]), Double.parseDouble(thirst[1]));
  }

  public RsEntry<Double, Double> getEnergy() {
    return energy;
  }

  public double getEnergyValue() {
    return random(energy.getLeft(), energy.getRight());
  }

  /**
   * energy 属性是否有效.

   * @return
   */
  public boolean isValidEnergy() {
    if (ConfigManager.getEnergyConfig().isEnable()) {
      return energy != null && !(energy.getLeft() == 0 && energy.getRight() == 0);
    } else {
      return false;
    }
  }

  public void setEnergy(RsEntry<Double, Double> energy) {
    this.energy = energy;
  }

  public void setEnergy(String[] energy) {
    this.energy = 
        new RsEntry<Double, Double>(Double.parseDouble(energy[0]), Double.parseDouble(energy[1]));
  }

  public RsEntry<Double, Double> getHunger() {
    return hunger;
  }

  public double getHungerValue() {
    return random(hunger.getLeft(), hunger.getRight());
  }

  public boolean isValidHunger() {
    return hunger != null && !(hunger.getLeft() == 0 && hunger.getRight() == 0);
  }

  public void setHunger(RsEntry<Double, Double> hunger) {
    this.hunger = hunger;
  }

  public void setHunger(String[] hunger) {
    this.hunger = 
        new RsEntry<Double, Double>(Double.parseDouble(hunger[0]), Double.parseDouble(hunger[1]));
  }

  public RsEntry<Double, Double> getHealth() {
    return health;
  }

  public double getHealthValue() {
    return random(health.getLeft(), health.getRight());
  }

  public boolean isValidHealth() {
    return health != null && !(health.getLeft() == 0 && health.getRight() == 0);
  }

  public void setHealth(RsEntry<Double, Double> health) {
    this.health = health;
  }

  public void setHealth(String[] health) {
    this.health = 
        new RsEntry<Double, Double>(Double.parseDouble(health[0]), Double.parseDouble(health[1]));
  }

  /**
   * 获取重力.

   * @return
   */
  public double getWeight() {
    if (ConfigManager.getWeightConfig().isEnable()) {
      return weight;
    } else {
      return 0;
    }
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public boolean isTool() {
    return temperature != null;
  }

  /**
   * 获取值.

   * @param status 属性值.
   * @return
   */
  public double getValue(String status) {
    switch (status.toUpperCase()) {
      case "SLEEP":
        return getSleepValue();
      case "THIRST":
        return getThirstValue();
      case "ENERGY":
        return getEnergyValue();
      case "HUNGER":
        return getHungerValue();
      case "WEIGHT":
        return weight;
      case "HEALTH":
        return getHealthValue();
      default:
        return 0;
    }
  }

  public boolean isMaxSleep() {
    return maxSleep;
  }

  public void setMaxSleep(boolean maxSleep) {
    this.maxSleep = maxSleep;
  }

  public boolean isMaxThirst() {
    return maxThirst;
  }

  public void setMaxThirst(boolean maxThirst) {
    this.maxThirst = maxThirst;
  }

  public boolean isMaxEnergy() {
    return maxEnergy;
  }

  public void setMaxEnergy(boolean maxEnergy) {
    this.maxEnergy = maxEnergy;
  }

  public boolean isMaxHunger() {
    return maxHunger;
  }

  public void setMaxHunger(boolean maxHunger) {
    this.maxHunger = maxHunger;
  }

  public boolean isMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(boolean maxHealth) {
    this.maxHealth = maxHealth;
  }

  private double random(double a, double b) {
    return Math.abs(a - b) * Math.random() + Math.min(a, b);
  }

  /**
   * 获取温度容忍度.

   * @return
   */
  public RsEntry<Double, Double> getTemperature() {
    if (ConfigManager.getTemperatureConfig().isEnable()) {
      return temperature;
    } else {
      return null;
    }
  }

  public void setTemperature(String[] str) {
    this.temperature = 
        new RsEntry<Double, Double>(Double.parseDouble(str[0]), Double.parseDouble(str[1]));
  }

  /**
   * 获取药品数据.

   * @return
   */
  public DrugData getDrugData() {
    if (ConfigManager.getDiseaseConfig().isEnable()) {
      return drugData;
    } else {
      return null;
    }
  }

  public void setDrugData(DrugData drugData) {
    this.drugData = drugData;
  }

}
