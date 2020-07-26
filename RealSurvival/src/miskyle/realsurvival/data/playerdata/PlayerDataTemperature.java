package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RSEntry;

public class PlayerDataTemperature {
  private TemperatureStatus value = TemperatureStatus.NORMAL;
  private HashMap<String, RSEntry<Double, Double>> tTolerance = new HashMap<>();
  
  public void setValue(TemperatureStatus value) {
    this.value = value;
  }
  
  public void addTemperatureTolerance(String pluginName, double downTolerance, double upTolerance) {
    tTolerance.put(pluginName, new RSEntry<Double, Double>(downTolerance, upTolerance));
  }
  
  public RSEntry<Double, Double> getExtraTolerance(){
    RSEntry<Double, Double> sum = new RSEntry<Double, Double>(0D,0D);
    tTolerance.values().forEach(v->{
      sum.set(sum.getLeft()+v.getLeft(), sum.getRight()+v.getRight());
    });
    return sum;
  }
  
  public RSEntry<Double, Double> getTotalTolerance(){
    RSEntry<Double, Double> entry = getExtraTolerance();
    entry.setLeft(entry.getLeft()+ConfigManager.getTemperatureConfig().getMin());
    entry.setRight(entry.getRight()+ConfigManager.getTemperatureConfig().getMax());
    return entry;
  }
  
  public RSEntry<Double, Double> getTotalTolerance(PlayerDataEffect effect){
    RSEntry<Double, Double> entry = getTotalTolerance();
    RSEntry<Double, Double> extra = effect.getValue();
    entry.setLeft(entry.getLeft()+extra.getLeft());
    entry.setRight(entry.getRight()+extra.getRight());
    return entry;
  }

  public TemperatureStatus getValue() {
    return value;
  }

  public HashMap<String, RSEntry<Double, Double>> gettTolerance() {
    return tTolerance;
  }
  
  public String getSaveString() {
    StringBuilder sb = new StringBuilder();
    if(tTolerance.isEmpty())return ";";
    tTolerance.forEach((k,v)->{
      sb.append(k+","+v.getLeft()+","+v.getRight()+";");
    });
    return sb.deleteCharAt(sb.length()-1).toString();
  }
}
