package miskyle.realsurvival.data.config;

public class DeathConfig {
  private boolean enable;
  private boolean removeDisease;
  private double sleep;
  private double thirst;
  private double energy;
  private double hunger;

  public DeathConfig() {

  }

  /**
   * 玩家死亡时的恢复属性配置信息.
   * @param enable 是否启用
   * @param removeDisease 是否移除疾病
   * @param sleep 恢复sleep为最大值的百分之几
   * @param thirst 恢复thirst为最大值的百分之几
   * @param energy 恢复energy为最大值的百分之几
   * @param hunger 恢复hunger为最大值的百分之几
   */
  public DeathConfig(boolean enable, boolean removeDisease, 
      double sleep, double thirst, double energy, double hunger) {
    super();
    this.enable = enable;
    this.removeDisease = removeDisease;
    this.sleep = sleep;
    this.thirst = thirst;
    this.energy = energy;
    this.hunger = hunger;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public boolean isRemoveDisease() {
    return removeDisease;
  }

  public void setRemoveDisease(boolean removeDisease) {
    this.removeDisease = removeDisease;
  }

  public double getSleep() {
    return sleep;
  }

  public void setSleep(double sleep) {
    this.sleep = sleep;
  }

  public double getThirst() {
    return thirst;
  }

  public void setThirst(double thirst) {
    this.thirst = thirst;
  }

  public double getEnergy() {
    return energy;
  }

  public void setEnergy(double energy) {
    this.energy = energy;
  }

  public double getHunger() {
    return hunger;
  }

  public void setHunger(double hunger) {
    this.hunger = hunger;
  }

}
