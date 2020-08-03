package miskyle.realsurvival.data.playerdata;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import com.github.miskyle.mcpt.mysql.MySQLManager;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;

public class PlayerData implements miskyle.realsurvival.api.player.PlayerData{
  private static LinkedList<String> cdStatusPlayer = new LinkedList<String>();
  
  private String playerName;
  
  private PlayerDataSleep                  sleep;
  private PlayerDataThirst                  thirst;
  private PlayerDataEnergy                energy;
  private PlayerDataWeight                weight;
  private PlayerDataEffect                   effect;
  private PlayerDataTemperature        temperature;
  private PlayerDataDisease               disease;
  
  private String status;
  
  public PlayerData(String playerName,PlayerDataSleep sleep,PlayerDataThirst thirst,
      PlayerDataEnergy energy,PlayerDataWeight weight,PlayerDataEffect effect,PlayerDataTemperature temperature, PlayerDataDisease disease) {
    this.playerName = playerName;
    this.sleep = sleep;
    this.thirst = thirst;
    this.energy = energy;
    this.weight = weight;
    this.effect = effect;
    this.temperature = temperature;
    this.disease = disease;
  }
  
  public static PlayerData getPlayerData(String name) {
    PlayerDataSleep   sleep     = new PlayerDataSleep();
    PlayerDataThirst   thirst     = new PlayerDataThirst();
    PlayerDataEnergy   energy    = new PlayerDataEnergy();
    PlayerDataWeight weight   = new PlayerDataWeight();
    PlayerDataEffect   effect     = new PlayerDataEffect();
    PlayerDataTemperature temperature = new PlayerDataTemperature();
    PlayerDataDisease disease = new PlayerDataDisease();
    List<String>       extraSleepValue = null;
    List<String>       extraThirstValue = null;
    List<String>       extraEnergyValue = null;
    List<String>       extraWeightValue = null;
    List<String>       temperatureEffect = null;
    List<String>       extraTemperature = null;
    if(ConfigManager.isEnableMySql() && MySQLManager.connect()) {
      //Use MySQL
      try {
        ResultSet rs = MySQLManager.execute(
            "SELECT * FROM RealSurvival WHERE Name='"+name.toLowerCase()+"'").executeQuery();
        if(rs.next()) {
          //Load Player Data From MySQL
          sleep.setValue(rs.getDouble("Sleep"));
          thirst.setValue(rs.getDouble("Thirst"));
          energy.setValue(rs.getDouble("Energy"));
          weight.setValue(rs.getDouble("Weighr"));
          effect.setEffect(rs.getString("Effect"));
          disease.setDisease(rs.getString("Disease"));
          
          extraSleepValue   = Arrays.asList(rs.getString("ExtraSleepValue").split(";"));
          extraThirstValue   = Arrays.asList(rs.getString("ExtraThirstValue").split(";"));
          extraEnergyValue   = Arrays.asList(rs.getString("ExtraEnergyValue").split(";"));
          extraWeightValue   = Arrays.asList(rs.getString("ExtraWeightValue").split(";"));
          temperatureEffect = Arrays.asList(rs.getString("TEffect").split(";"));
          extraTemperature = Arrays.asList(rs.getString("Temperature").split(";"));
        }else {
          //Create New Player Data
          sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
          thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
          energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
          weight.setValue(0);
          disease.setDisease(new ArrayList<>());
          extraSleepValue   = new ArrayList<String>();
          extraThirstValue   = new ArrayList<String>();
          extraEnergyValue   = new ArrayList<String>();
          extraWeightValue = new ArrayList<String>();
          temperatureEffect = new ArrayList<String>();
          extraTemperature = new ArrayList<String>();
        }
        rs.close();
        MySQLManager.disconnect();
      }catch (SQLException e) {
        e.printStackTrace();
        MySQLManager.disconnect();
        return null;
      }
    }else {
      File file = new File(
          MCPT.plugin.getDataFolder()+"/playerdata/"+name.toLowerCase()+".yml");
      YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
      if(file.exists()) {
        //Load Player Data From File
        sleep.setValue(    data.getDouble("sleep"));
        thirst.setValue(    data.getDouble("thirst"));
        energy.setValue(  data.getDouble("energy"));
        weight.setValue(  data.getDouble("weight"));
        effect.setEffect(    data.getString("effect",null));
        disease.setDisease(data.getStringList("disease"));
        
        extraSleepValue   = data.getStringList("extra-sleep");
        extraThirstValue   = data.getStringList("extra-thirst");
        extraEnergyValue   = data.getStringList("extra-energy");
        extraWeightValue   = data.getStringList("extra-weight");
        extraTemperature = data.getStringList("extra-temperature");
        temperatureEffect = data.getStringList("temperature-effect");
      }else {
        //Create New Player Data
        sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
        thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
        energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
        weight.setValue(0);
        disease.setDisease(new ArrayList<>());
        extraSleepValue   = new ArrayList<String>();
        extraThirstValue   = new ArrayList<String>();
        extraEnergyValue   = new ArrayList<String>();
        extraWeightValue   = new ArrayList<String>();
        temperatureEffect = new ArrayList<String>();
        extraTemperature = new ArrayList<String>();
      }
    }
    
    initExtraValue(weight, extraWeightValue);
    initExtraValue(energy, extraEnergyValue);
    initExtraValue(thirst, extraThirstValue);
    initExtraValue(sleep, extraSleepValue);
    
    extraTemperature.forEach(s->{
      String[] temp = s.split(",");
      temperature.addTemperatureTolerance(temp[0], 
          Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
    });
    
    temperatureEffect.forEach(s->{
      String[] temp = s.split(",");
      effect.addEffect(temp[0], Double.parseDouble(temp[1]),
          Double.parseDouble(temp[2]), Integer.parseInt(temp[3]));
    });
    
    return new PlayerData(name, sleep, thirst, energy, weight,effect,temperature,disease);
  }
  
  private static void initExtraValue(PlayerDataStatus status,List<String> extraValue) {
    extraValue.forEach(line->{
      String[] temp = line.split(":");
      if(MCPT.plugin.getServer().getPluginManager().isPluginEnabled(temp[0])) {
        status.setExtraMaxValue(temp[0], Double.parseDouble(temp[1]));
      }
    });
  }
  private static List<String> getExtraValueList(PlayerDataStatus status) {
    List<String> temp = new ArrayList<String>();
    status.getExtraMaxValue().forEach((s,d)->{
      temp.add(s+":"+d);
    });
    return temp;
  }
  
  private static String getExtraValueString(PlayerDataStatus status) {
    StringBuilder sb = new StringBuilder();
    status.getExtraMaxValue().forEach((s,d)->{
      sb.append(s+":"+d);
      sb.append(";");
    });
    return sb.length()>0?sb.substring(0, sb.length()-1):"";
  }
  
  public void save() {
    if(ConfigManager.isEnableMySql() && MySQLManager.connect()) {
      ResultSet rs;
      String extraSleepString = getExtraValueString(sleep);
      String extraThirstString = getExtraValueString(thirst);
      String extraEnergyString = getExtraValueString(energy);
      String extraWeightString = getExtraValueString(weight);
      String effectString = effect.toString();
      String diseaseString = disease.getSaveString();
      String extraTemperature = temperature.getSaveString();
      String temperatureEffectString = effect.getTemperatureSaveString();
      try {
        rs = MySQLManager.execute(
            "SELECT * FROM RealSurvival WHERE Name='"+playerName.toLowerCase()+"'")
            .executeQuery();
        if(rs.next()) {
          String sql = "UPDATA RealSurvival SET "
              +"Sleep = '"+sleep.getValue()+"',"
              +"Thirst = '"+thirst.getValue()+"',"
              +"Energy = '"+energy.getValue()+"',"
              +"Weight = '"+weight.getValue()+"',"
              +"ExtraSleepValue = '"+extraThirstString+"',"
              +"ExtraSleepValue = '"+extraThirstString+"',"
              +"ExtraSleepValue = '"+extraThirstString+"',"
              +"ExtraSleepValue = '"+extraThirstString+"',"
              +"Effect = '"+effectString+"',"
              +"TEffect = '"+temperatureEffectString+"',"
              +"Disease = '"+diseaseString+"',"
              +"Temperature = '"+extraTemperature+"'"
              +" WHERE Name = '"+playerName.toLowerCase()+"'";
          MySQLManager.execute(sql);
        }else {
          String sql = "INSERT INTO RealSurvival VALUES ('"
                +playerName.toLowerCase()+"','"
                +sleep.getValue()+"','"
                +thirst.getValue()+"','"
                +energy.getValue()+"','"
                +weight.getValue()+"','"
                +extraSleepString+"','"
                +extraThirstString+"','"
                +extraEnergyString+"','"
                +extraWeightString+"','"
                +effectString+"','"
                +temperatureEffectString+"','"
                +diseaseString+"','"
                +extraTemperature+"')";
          MySQLManager.execute(sql);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }else {
      File file = new File(
          MCPT.plugin.getDataFolder()+"/playerdata/"+playerName.toLowerCase()+".yml");
      YamlConfiguration data = 
          YamlConfiguration.loadConfiguration(file);
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
      if(diseaseString == null 
          || diseaseString.equalsIgnoreCase("null")) {
        data.set("disease", new ArrayList<>());
      }else {
        data.set("disease", diseaseString.split(";"));        
      }
      data.set("extra-temperature", temperature.getSaveString().split(";"));
      data.set("temperature-effect", effect.getTemperatureSaveString().split(";"));
      try {
        data.save(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
  
  public String getStatusMessage(boolean cd) {
    if(status == null || !(cdStatusPlayer.contains(playerName) || cd)) {
      status = Msg.getPlayerState1(playerName,"正常",
          _2f(sleep.getProportionValue()*100),_2f(sleep.getMaxValue()),_2f(sleep.getValue()),
          _2f(thirst.getProportionValue()*100),_2f(thirst.getMaxValue()),_2f(thirst.getValue()),
          I18N.tr("status.temperature."+temperature.getValue().name().toLowerCase()),
          _2f(energy.getProportionValue()*100),_2f(energy.getMaxValue()),_2f(energy.getValue()),
          _2f(weight.getProportionValue()*100),_2f(weight.getMaxValue()),_2f(weight.getValue()));
      if(!cdStatusPlayer.contains(playerName))
        cdStatusPlayer.add(playerName);
      Bukkit.getScheduler().runTaskLaterAsynchronously(MCPT.plugin, ()->{
        cdStatusPlayer.remove(playerName);
      }, ConfigManager.getStatusCmdCD());
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
      energy.modify(value,effect.getValue(type));
      break;
    case SLEEP:
      sleep.modify(value,effect.getValue(type));
      break;
    case THIRST:
      thirst.modify(value,effect.getValue(type));
      break;
    case WEIGHT:
      weight.modify(value,effect.getValue(type));
      break;
    default:
      break;
    }
  }

  @Override
  public double getStatusValue(StatusType type) {
    switch (type) {
    case ENERGY:
      energy.getValue();
      break;
    case SLEEP:
      sleep.getValue();
      break;
    case THIRST:
      thirst.getValue();
      break;
    case WEIGHT:
      weight.getValue();
      break;
    default:
      break;
    }
    return 0;
  }

  @Override
  public double getStatusMaxValue(StatusType type) {
    switch (type) {
    case ENERGY:
      energy.getMaxValue();
      break;
    case SLEEP:
      sleep.getMaxValue();
      break;
    case THIRST:
      thirst.getMaxValue();
      break;
    case WEIGHT:
      weight.getMaxValue();
      break;
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
  
  private String _2f(double d){
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
      energy.getExtraMaxValue().containsKey(plugin.getName());
      break;
    case SLEEP:
      sleep.getExtraMaxValue().containsKey(plugin.getName());
      break;
    case THIRST:
      thirst.getExtraMaxValue().containsKey(plugin.getName());
      break;
    case WEIGHT:
      weight.getExtraMaxValue().containsKey(plugin.getName());
      break;
    default:
      break;
    }
    return false;
  }

  @Override
  public double getAddMaxValue(StatusType type, Plugin plugin) {
    if(!isValidAddMaxValue(type, plugin))return 0;
    switch (type) {
    case ENERGY:
      energy.getExtraMaxValue().get(plugin.getName());
      break;
    case SLEEP:
      sleep.getExtraMaxValue().get(plugin.getName());
      break;
    case THIRST:
      thirst.getExtraMaxValue().get(plugin.getName());
      break;
    case WEIGHT:
      weight.getExtraMaxValue().get(plugin.getName());
      break;
    default:
      break;
    }
    return 0;
  }
}
