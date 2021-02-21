package miskyle.realsurvival.data.effect;

public class EffectData {
  private String name;
  private int duration;
  private int effecLevel;
  private double chance;

  public EffectData() {

  }

  /**
   * 初始化.

   * @param name 名字
   * @param duration 持续时间
   * @param effecLevel 效果等级
   * @param chance 几率
   */
  public EffectData(String name, int duration, int effecLevel, double chance) {
    super();
    this.name = name;
    this.duration = duration;
    this.effecLevel = effecLevel;
    this.chance = chance;
  }

  /**
   * 从字符串中读取数据.

   * @param s 包含数据的字符串.
   * @return
   */
  public static EffectData loadFromString(String s) {
    if (s.equalsIgnoreCase("null")) {
      return null;
    }
    String[] temp = s.split(",");
    if (temp.length > 3) {
      return new EffectData(temp[0], 
          Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Double.parseDouble(temp[3]));
    } else {
      return new EffectData(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), 100);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getEffecLevel() {
    return effecLevel;
  }

  public void setEffecLevel(int effecLevel) {
    this.effecLevel = effecLevel;
  }

  public double getChance() {
    return chance;
  }

  public void setChance(double chance) {
    this.chance = chance;
  }

}
