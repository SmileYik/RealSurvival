package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RsEntry;

public class PlayerDataTemperature {
  private TemperatureStatus value = TemperatureStatus.NORMAL;
  private HashMap<String, RsEntry<Double, Double>> ttTolerance = new HashMap<>();

  public void setValue(TemperatureStatus value) {
    this.value = value;
  }

  public void addTemperatureTolerance(String pluginName, double downTolerance, double upTolerance) {
    ttTolerance.put(pluginName, new RsEntry<Double, Double>(downTolerance, upTolerance));
  }

  /**
   * 获取额外容忍度数据.

   * @return
   */
  public RsEntry<Double, Double> getExtraTolerance() {
    RsEntry<Double, Double> sum = new RsEntry<Double, Double>(0D, 0D);
    ttTolerance.values().forEach(v -> {
      sum.set(sum.getLeft() + v.getLeft(), sum.getRight() + v.getRight());
    });
    return sum;
  }

  /**
   * 获取总的容忍度数据.

   * @return
   */
  public RsEntry<Double, Double> getTotalTolerance() {
    RsEntry<Double, Double> entry = getExtraTolerance();
    entry.setLeft(entry.getLeft() + ConfigManager.getTemperatureConfig().getMin());
    entry.setRight(entry.getRight() + ConfigManager.getTemperatureConfig().getMax());
    return entry;
  }

  /**
   * 获取受效果影响的容忍度数据.

   * @param effect 玩家效果.
   * @return
   */
  public RsEntry<Double, Double> getTotalTolerance(PlayerDataEffect effect) {
    RsEntry<Double, Double> entry = getTotalTolerance();
    RsEntry<Double, Double> extra = effect.getValue();
    entry.setLeft(entry.getLeft() + extra.getLeft());
    entry.setRight(entry.getRight() + extra.getRight());
    return entry;
  }

  public TemperatureStatus getValue() {
    return value;
  }

  public HashMap<String, RsEntry<Double, Double>> getttTolerance() {
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
