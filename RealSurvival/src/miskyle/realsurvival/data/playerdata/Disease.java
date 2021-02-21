package miskyle.realsurvival.data.playerdata;

public class Disease {
  private String diseaseName;
  private double recover;
  private double drug;
  private int duration;

  /**
   * 恢复一个疾病.

   * @param diseaseName 疾病名
   * @param recover 恢复程度
   * @param drug 药物效果
   * @param duration 药物持续时间
   */
  public Disease(String diseaseName, double recover, double drug, int duration) {
    super();
    this.diseaseName = diseaseName;
    this.recover = recover;
    this.drug = drug;
    this.duration = duration;
  }

  /**
   * 创建一个未治愈疾病.

   * @param diseaseName 疾病名
   */
  public Disease(String diseaseName) {
    super();
    this.diseaseName = diseaseName;
    this.recover = 0;
    this.drug = 0;
  }

  /**
   * 从带有信息的字符串中取读疾病信息.

   * @param str 带有信息的字符串.
   * @return
   */
  public static Disease getFromString(String str) {
    String[] temp = str.split(",");
    return new Disease(temp[0], 
        Double.parseDouble(temp[1]), 
        Double.parseDouble(temp[2]), 
        Integer.parseInt(temp[3]));
  }
  
  public boolean isTakeDrug() {
    return duration > 0;
  }

  public String getDiseaseName() {
    return diseaseName;
  }

  public void setDiseaseName(String diseaseName) {
    this.diseaseName = diseaseName;
  }

  public double getRecover() {
    return recover;
  }

  /**
   * 直接替换治愈情况.

   * @param recover 替换值
   * @return 痊愈返回true
   */
  public boolean setRecover(double recover) {
    this.recover = recover;
    return recover >= 100;
  }

  public void getDiseaseAgain() {
    this.recover /= 2;
  }

  /**
   * 修改治愈情况.

   * @param recover 治愈程度
   * @return 痊愈返回true
   */
  public boolean modifyRecover(double recover) {
    this.recover += recover;
    if (this.recover < 0) {
      this.recover = 0;
    }
    return this.recover >= 100;
  }

  /**
   * 让疾病恢复.

   * @return 疾病痊愈返回true
   */
  public boolean recover() {
    if (duration > 0) {
      this.recover += drug;
      duration--;
    }
    return recover >= 100;
  }

  public double getDrug() {
    return drug;
  }

  public void setDrug(double drug) {
    this.drug = drug;
  }

  public void modifyDrug(double drug) {
    this.drug += drug;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

}
