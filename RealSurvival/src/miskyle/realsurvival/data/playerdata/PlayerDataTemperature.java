package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RSEntry;

public class PlayerDataTemperature {
  private TemperatureStatus value = TemperatureStatus.NORMAL;
  private HashMap<String, RSEntry<Double, Double>> ttTolerance = new HashMap<>();

  public void setValue(TemperatureStatus value) {
    this.value = value;
  }

  public void addTemperatureTolerance(String pluginName, double downTolerance, double upTolerance) {
    ttTolerance.put(pluginName, new RSEntry<Double, Double>(downTolerance, upTolerance));
  }

  public RSEntry<Double, Double> getExtraTolerance() {
    RSEntry<Double, Double> sum = new RSEntry<Double, Double>(0D, 0D);
    ttTolerance.values().forEach(v -> {
      sum.set(sum.getLeft() + v.getLeft(), sum.getRight() + v.getRight());
    });
    return sum;
  }

  public RSEntry<Double, Double> getTotalTolerance() {
    RSEntry<Double, Double> entry = getExtraTolerance();
    entry.setLeft(entry.getLeft() + ConfigManager.getTemperatureConfig().getMin());
    entry.setRight(entry.getRight() + ConfigManager.getTemperatureConfig().getMax());
    return entry;
  }

  public RSEntry<Double, Double> getTotalTolerance(PlayerDataEffect effect) {
    RSEntry<Double, Double> entry = getTotalTolerance();
    RSEntry<Double, Double> extra = effect.getValue();
    entry.setLeft(entry.getLeft() + extra.getLeft());
    entry.setRight(entry.getRight() + extra.getRight());
    return entry;
  }

  public TemperatureStatus getValue() {
    return value;
  }

  public HashMap<String, RSEntry<Double, Double>> getttTolerance() {
    return ttTolerance;
  }

  /**
   * 获取储存String.
   * @return 含有对应信息的String
   */
  public String getSaveString() {
    StringBuilder sb = new StringBuilder();
    if (ttTolerance.isEmpty()) {
      return ";";      
    }
    ttTolerance.forEach((k, v) -> {
      sb.append(k + "," + v.getLeft() + "," + v.getRight() + ";");
    });
    return sb.deleteCharAt(sb.length() - 1).toString();
  }
}
