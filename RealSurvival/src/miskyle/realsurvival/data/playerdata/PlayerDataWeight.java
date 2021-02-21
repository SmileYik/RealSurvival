package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RsEntry;

public class PlayerDataWeight extends PlayerDataStatus {

  public double getMaxValue() {
    return super.getExtraValueSum() + ConfigManager.getWeightConfig().getMaxValue();
  }

  /**
   * 取最大属性值的比例.

   * @param p 比例值, 范围0~100
   * @return
   */
  public double getMaxValue(double p) {
    return getMaxValue() * p / 100;
  }

  /**
   * 修改属性值,若为加则数值为正/若为减则数值为负.

   * @param value 值
   * @return 属性的原始值(左)及新值(右)
   */
  @Override
  public RsEntry<Double, Double> modify(double value) {
    double oldValue = this.value;
    this.value += value;
    double max = getMaxValue();
    if (super.value < 0) {
      super.value = 0;
    } else if (super.value > max) {
      super.value = max;      
    }
    return new RsEntry<Double, Double>(oldValue, this.value);
  }

  public RsEntry<Double, Double> setValue(double value, double effect) {
    return super.setValue(value * (effect + 1));
  }

  @Override
  public RsEntry<Double, Double> setValue(double value) {
    return super.setValue(value);
  }

  public double getProportionValue() {
    return getValue() / getMaxValue();
  }
}
