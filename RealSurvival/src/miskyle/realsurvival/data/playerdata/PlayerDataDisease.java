package miskyle.realsurvival.data.playerdata;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataDisease {
  private int showDiseaseIndex = -1;
  private ConcurrentHashMap<String, Disease> diseases;

  /**
   * 增加疾病.
   * @param name 疾病名
   */
  public void addDisease(String name) {
    if (diseases.containsKey(name)) {
      diseases.get(name).getDiseaseAgain();
    } else if (ConfigManager.getDiseaseConfig().isContainsDisease(name)) {
      diseases.put(name, new Disease(name));
    }
  }

  /**
   * 吃药.
   * @param name 对应疾病
   * @param drug 药效
   * @param duration 药物持续时间
   */
  public void eatDurg(String name, double drug, int duration) {
    Disease d = diseases.get(name);
    d.setDrug(drug);
    d.setDuration(duration);
  }

  public void removeDisease(String name) {
    diseases.remove(name);
  }

  public ConcurrentHashMap<String, Disease> getDiseases() {
    return diseases;
  }

  public void setDiseases(ConcurrentHashMap<String, Disease> diseases) {
    this.diseases = diseases;
  }
  
  public boolean hasDisease() {
    return !diseases.isEmpty();
  }

  /**
   * 从保存的带有疾病信息的字符串中获取信息.
   * @param str 带有疾病信息的字符串
   */
  public void setDisease(String str) {
    diseases = new ConcurrentHashMap<String, Disease>();
    if (str == null || str.equalsIgnoreCase("null")) {
      return;      
    }
    for (String temp : str.split(";")) {
      Disease d = Disease.getFromString(temp);
      diseases.put(d.getDiseaseName(), d);
    }
  }

  /**
   * 从带有疾病信息的字符串组成的列表中获取信息.
   * @param list 带有疾病信息的字符串组成的列表
   */
  public void setDisease(List<String> list) {
    diseases = new ConcurrentHashMap<String, Disease>();
    list.forEach(temp -> {
      Disease d = Disease.getFromString(temp);
      diseases.put(d.getDiseaseName(), d);
    });
  }

  /**
   * 获取带有疾病信息的字符串.
   * @return 如果无疾病则返回小写的"null"字符串.
   */
  public String getSaveString() {
    if (diseases.isEmpty()) {
      return "null";      
    }
    StringBuilder sb = new StringBuilder();
    diseases.values().forEach(d -> {
      sb.append(d.getDiseaseName() + "," 
                          + d.getRecover() + "," + d.getDrug() 
                          + "," + d.getDuration() + ";");
    });
    return sb.deleteCharAt(sb.length() - 1).toString();
  }

  /**
   * 提供PAPI变量显示的疾病信息.
   * @return 当无疾病时返回null
   */
  public Disease getShowDisease() {
    if (diseases.isEmpty()) {
      return null;
    }
    if (showDiseaseIndex < 0 || showDiseaseIndex >= diseases.size()) {
      updateShowDisease();
    }
    return diseases.values().toArray(new Disease[diseases.size()])[showDiseaseIndex];
  }

  public void updateShowDisease() {
    showDiseaseIndex = getRandomIndex();
  }

  private int getRandomIndex() {
    int index = (int) Math.floor(Math.random() * diseases.size() + 0.5);
    if (index >= diseases.size()) {
      index = diseases.size() - 1;
    }
    return index;
  }
}
