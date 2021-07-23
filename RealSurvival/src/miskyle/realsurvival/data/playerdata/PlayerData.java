package miskyle.realsurvival.data.playerdata;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.MySqlManager;

public class PlayerData implements miskyle.realsurvival.api.player.PlayerData {
  private static LinkedList<String> cdStatusPlayer = new LinkedList<String>();

  private String playerName;

  private PlayerDataSleep sleep;
  private PlayerDataThirst thirst;
  private PlayerDataEnergy energy;
  private PlayerDataWeight weight;
  private PlayerDataEffect effect;
  private PlayerDataTemperature temperature;
  private PlayerDataDisease disease;

  private String status;

  private PlayerData(String playerName, PlayerDataSleep sleep, 
      PlayerDataThirst thirst, PlayerDataEnergy energy, PlayerDataWeight weight, 
      PlayerDataEffect effect, PlayerDataTemperature temperature, PlayerDataDisease disease) {
    this.playerName = playerName;
    this.sleep = sleep;
    this.thirst = thirst;
    this.energy = energy;
    this.weight = weight;
    this.effect = effect;
    this.temperature = temperature;
    this.disease = disease;
  }
  
  /**
   * 获取玩家数据.

   * @param name 玩家名.
   * @return 若玩家数据存在则返回相应数据, 不存在则返回新的玩家数据.
   */
  public static PlayerData getPlayerData(String name) {
    if (ConfigManager.isEnableMySql()) {
      String data = MySqlManager.getPlayerData(name);
      if (data == null) {
        return createNewPlayerData(name);
      }
      return getPlayerData(name, YamlConfiguration.loadConfiguration(new StringReader(data)));
    } else {
      File file = new File(
          MCPT.plugin.getDataFolder() + "/playerdata/" + name.toLowerCase() + ".yml");
      if (file.exists()) {
        return getPlayerData(name, YamlConfiguration.loadConfiguration(file));
      }
      return createNewPlayerData(name);
    }
  }
  
  /**
   * 获取玩家数据.

   * @param name 玩家名字
   * @param data 数据.
   * @return
   */
  public static PlayerData getPlayerData(String name, YamlConfiguration data) {
    PlayerDataSleep sleep = new PlayerDataSleep();
    sleep.setValue(data.getDouble("sleep"));
    PlayerDataThirst thirst = new PlayerDataThirst();
    thirst.setValue(data.getDouble("thirst"));
    PlayerDataEnergy energy = new PlayerDataEnergy();
    energy.setValue(data.getDouble("energy"));
    PlayerDataWeight weight = new PlayerDataWeight();
    weight.setValue(data.getDouble("weight"));
    PlayerDataEffect effect = new PlayerDataEffect();
    effect.setEffect(data.getString("effect", null));
    PlayerDataDisease disease = new PlayerDataDisease();
    disease.setDisease(data.getStringList("disease"));
    
    initExtraValue(weight, data.getStringList("extra-weight"));
    initExtraValue(energy, data.getStringList("extra-energy"));
    initExtraValue(thirst, data.getStringList("extra-thirst"));
    initExtraValue(sleep, data.getStringList("extra-sleep"));

    PlayerDataTemperature temperature = new PlayerDataTemperature();
    data.getStringList("extra-temperature").forEach(s -> {
      String[] temp = s.split(",");
      temperature.addTemperatureTolerance(
          temp[0], Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
    });

    data.getStringList("temperature-effect").forEach(s -> {
      String[] temp = s.split(",");
      effect.addEffect(temp[0], 
          Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), Integer.parseInt(temp[3]));
    });
    return new PlayerData(name, sleep, thirst, energy, weight, effect, temperature, disease);
  }
  
  /**
   * 创建一个新的玩家数据.

   * @param name 玩家名
   * @return 玩家数据
   */
  public static PlayerData createNewPlayerData(String name) {
    PlayerDataSleep sleep = new PlayerDataSleep();
    sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
    PlayerDataThirst thirst = new PlayerDataThirst();
    thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
    PlayerDataEnergy energy = new PlayerDataEnergy();
    energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
    PlayerDataWeight weight = new PlayerDataWeight();
    weight.setValue(0);
    PlayerDataDisease disease = new PlayerDataDisease();
    disease.setDisease(new ArrayList<>());
    PlayerDataEffect effect = new PlayerDataEffect();
    PlayerDataTemperature temperature = new PlayerDataTemperature();
    return new PlayerData(name, sleep, thirst, energy, weight, effect, temperature, disease);
  }

  private static void initExtraValue(PlayerDataStatus status, List<String> extraValue) {
    extraValue.forEach(line -> {
      String[] temp = line.split(":");
      if (MCPT.plugin.getServer().getPluginManager().isPluginEnabled(temp[0])) {
        status.setExtraMaxValue(temp[0], Double.parseDouble(temp[1]));
      }
    });
  }

  private static List<String> getExtraValueList(PlayerDataStatus status) {
    List<String> temp = new ArrayList<String>();
    status.getExtraMaxValue().forEach((s, d) -> {
      temp.add(s + ":" + d);
    });
    return temp;
  }

  /*
   * ***********************
   *            2020.8.22               *
   *      修复 MySQL语句          *
   * ***********************
   * 
   */
  /**
   * 保存玩家数据.
   */
  public void save() {
    if (ConfigManager.isEnableMySql()) {
      MySqlManager.updateItem(playerName, getSaveData().saveToString());
    } else {
      try {
        getSaveData().save(
            new File(
                MCPT.plugin.getDataFolder() + "/playerdata/" + playerName.toLowerCase() + ".yml"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * 获取玩家的保存数据.

   * @return
   */
  public YamlConfiguration getSaveData() {
    YamlConfiguration data = 
        YamlConfiguration.loadConfiguration(new StringReader(""));
    data.set("sleep", sleep.getValue());
    data.set("thirst", thirst.getValue());
    data.set("weight", weight.getValue());
    data.set("energy", energy.getValue());
    data.set("extra-sleep", getExtraValueList(sleep));
    data.set("extra-thirst", getExtraValueList(thirst));
    data.set("extra-weight", getExtraValueList(weight));
    data.set("extra-energy", getExtraValueList(energy));
    data.set("effects", effect.toString());
    String diseaseString = disease.getSaveString();
    if (diseaseString == null || diseaseString.equalsIgnoreCase("null")) {
      data.set("disease", new ArrayList<>());
    } else {
      data.set("disease", diseaseString.split(";"));
    }
    data.set("extra-temperature", temperature.getSaveString().split(";"));
    data.set("temperature-effect", effect.getTemperatureSaveString().split(";"));
    return data;
  }

  public String getPlayerName() {
    return playerName;
  }

  public PlayerDataSleep getSleep() {
    return sleep;
  }

  public PlayerDataThirst getThirst() {
    return thirst;
  }

  public PlayerDataEnergy getEnergy() {
    return energy;
  }

  public PlayerDataWeight getWeight() {
    return weight;
  }

  public PlayerDataTemperature getTemperature() {
    return temperature;
  }

  public PlayerDataDisease getDisease() {
    return disease;
  }

  public String getStatus() {
    return status;
  }

  public PlayerDataEffect getEffect() {
    return effect;
  }

  /**
   * 获取玩家状态.
   * 对应/rs status指令

   * @param cd 是否需要cd
   * @return
   */
  public String getStatusMessage(boolean cd) {
    if (status == null || !cdStatusPlayer.contains(playerName) || cd) {
      status = Msg.getPlayerState1(playerName, 
          I18N.tr("status.fine"), 
          format(sleep.getProportionValue() * 100), 
          format(sleep.getMaxValue()),
          format(sleep.getValue()), 
          format(thirst.getProportionValue() * 100), 
          format(thirst.getMaxValue()),
          format(thirst.getValue()), 
          I18N.tr("status.temperature." + temperature.getValue().name().toLowerCase()),
          format(energy.getProportionValue() * 100), 
          format(energy.getMaxValue()), 
          format(energy.getValue()),
          format(weight.getProportionValue() * 100), 
          format(weight.getMaxValue()), 
          format(weight.getValue()));   
      if (disease.hasDisease()) {
        StringBuilder diseases = new StringBuilder();
        disease.getDiseases().forEachValue(disease.getDiseases().mappingCount(), v -> {
          diseases.append(v.getDiseaseName()).append(": \n");
          diseases.append(I18N.tr("status.disease.recover"))
                          .append(": §3")
                          .append(format(v.getRecover()))
                          .append("%");
          if (v.isTakeDrug()) {
            diseases.append(I18N.tr("status.disease.drug"))
                        .append(": §3")
                        .append(format(v.getDrug()));
            diseases.append(I18N.tr("status.disease.drug-duration"))
                          .append(": §3")
                          .append(format(v.getDuration()));
          }
        });
        status += Msg.getPlayerState2(diseases.toString());
      }
      if (!cdStatusPlayer.contains(playerName)) {
        cdStatusPlayer.add(playerName);        
      }
      Bukkit.getScheduler().runTaskLaterAsynchronously(MCPT.plugin, () -> {
        cdStatusPlayer.remove(playerName);
      }, ConfigManager.getStatusCmdCd());
    }
    return status;
  }

  @Override
  public void modify(StatusType type, double value) {
    switch (type) {
      case ENERGY:
        energy.modify(value);
        break;
      case SLEEP:
        sleep.modify(value);
        break;
      case THIRST:
        thirst.modify(value);
        break;
      case WEIGHT:
        weight.modify(value);
        break;
      default:
        break;
    }
  }

  @Override
  public void modifyWithEffect(StatusType type, double value) {
    switch (type) {
      case ENERGY:
        energy.modify(value, effect.getValue(type));
        break;
      case SLEEP:
        sleep.modify(value, effect.getValue(type));
        break;
      case THIRST:
        thirst.modify(value, effect.getValue(type));
        break;
      case WEIGHT:
        weight.modify(value, effect.getValue(type));
        break;
      default:
        break;
    }
  }

  @Override
  public double getStatusValue(StatusType type) {
    switch (type) {
      case ENERGY:
        return energy.getValue();
      case SLEEP:
        return sleep.getValue();
      case THIRST:
        return thirst.getValue();
      case WEIGHT:
        return weight.getValue();
      default:
        break;
    }
    return 0;
  }

  @Override
  public double getStatusMaxValue(StatusType type) {
    switch (type) {
      case ENERGY:
        return energy.getMaxValue();
      case SLEEP:
        return sleep.getMaxValue();
      case THIRST:
        return thirst.getMaxValue();
      case WEIGHT:
        return weight.getMaxValue();
      default:
        break;
    }
    return 0;
  }

  @Override
  public void setStatus(StatusType type, double value) {
    switch (type) {
      case ENERGY:
        energy.setValue(value);
        break;
      case SLEEP:
        sleep.setValue(value);
        break;
      case THIRST:
        thirst.setValue(value);
        break;
      case WEIGHT:
        weight.setValue(value);
        break;
      default:
        break;
    }
  }

  @Override
  public void addEffect(StatusType type, String pluginName, double value, int duration) {
    effect.addEffect(type, pluginName, value, duration);
  }
  
  @Override
  public void addTemperatureEffect(String pluginName, double up, double down, int duration) {
    effect.addEffect(pluginName, up, down, duration);
  }

  private String format(double d) {
    return String.format("%.2f", d);
  }

  @Override
  public void addStatusMaxValue(StatusType type, Plugin plugin, double value) {
    switch (type) {
      case ENERGY:
        energy.setExtraMaxValue(plugin.getName(), value);
        break;
      case SLEEP:
        sleep.setExtraMaxValue(plugin.getName(), value);
        break;
      case THIRST:
        thirst.setExtraMaxValue(plugin.getName(), value);
        break;
      case WEIGHT:
        weight.setExtraMaxValue(plugin.getName(), value);
        break;
      default:
        break;
    }
  }

  @Override
  public boolean isValidAddMaxValue(StatusType type, Plugin plugin) {
    switch (type) {
      case ENERGY:
        return energy.getExtraMaxValue().containsKey(plugin.getName());
      case SLEEP:
        return sleep.getExtraMaxValue().containsKey(plugin.getName());
      case THIRST:
        return thirst.getExtraMaxValue().containsKey(plugin.getName());
      case WEIGHT:
        return weight.getExtraMaxValue().containsKey(plugin.getName());
      default:
        break;
    }
    return false;
  }

  @Override
  public double getAddMaxValue(StatusType type, Plugin plugin) {
    if (!isValidAddMaxValue(type, plugin)) {
      return 0;      
    }
    switch (type) {
      case ENERGY:
        return energy.getExtraMaxValue().get(plugin.getName());
      case SLEEP:
        return sleep.getExtraMaxValue().get(plugin.getName());
      case THIRST:
        return thirst.getExtraMaxValue().get(plugin.getName());
      case WEIGHT:
        return weight.getExtraMaxValue().get(plugin.getName());
      default:
        break;
    }
    return 0;
  }
}
