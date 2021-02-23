package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miskyle.realsurvival.data.effect.EffectData;

public class DiseaseConfig {
  public class MobDisease {
    private String name;
    private float chance;
    
    public MobDisease(String name, float chance) {
      this.chance = chance;
      this.name = name;
    }
    
    public boolean isMakeDisease() {
      return Math.random() < chance;
    }
    
    public String makeDisease() {
      return Math.random() < chance ? name : null;
    }
    
    public String getName() {
      return name;
    }
  }
  
  private boolean enable;
  private boolean mob;
  private HashMap<String, ArrayList<MobDisease>> mobDisease;

  public boolean isMob() {
    return mob;
  }

  public void setMob(boolean mob) {
    this.mob = mob;
  }

  private HashMap<String, ArrayList<EffectData>> diseases;

  public DiseaseConfig() {

  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public HashMap<String, ArrayList<EffectData>> getDiseases() {
    return diseases;
  }

  public void setDiseases(HashMap<String, ArrayList<EffectData>> diseases) {
    this.diseases = diseases;
  }
  
  /**
   * 设定病.

   * @param list 病列表数据.
   */
  public void setDiseases(List<String> list) {
    diseases = new HashMap<String, ArrayList<EffectData>>();
    list.forEach(line -> {
      String[] temp = line.split(":");
      for (String effect : temp[1].split(";")) {
        if (diseases.containsKey(temp[0])) {
          diseases.get(temp[0]).add(EffectData.loadFromString(effect));
        } else {
          ArrayList<EffectData> effects = new ArrayList<>();
          effects.add(EffectData.loadFromString(effect));
          diseases.put(temp[0], effects);
        }
      }
    });
  }

  public boolean isContainsDisease(String name) {
    return diseases.containsKey(name);
  }

  /**
   * 获取病效果数据.

   * @param diseaseName 病名
   * @return
   */
  public ArrayList<EffectData> getDiseaseEffect(String diseaseName) {
    if (diseases.containsKey(diseaseName)) {
      return diseases.get(diseaseName);
    } else {
      return new ArrayList<EffectData>();
    }
  }
  
  /**
   * 设置对应怪物名字致病数据.

   * @param list 配置文件里的列表
   */
  public void setMobDisease(List<String> list) {
    mobDisease = new HashMap<>();
    for (String line : list) {
      String[] temp = line.split(":");
      ArrayList<MobDisease> diseases;
      if (mobDisease.containsKey(temp[0])) {
        diseases = mobDisease.get(temp[0]);
      } else {
        diseases = new ArrayList<>();
        mobDisease.put(temp[0], diseases);
      }
      for (String d : temp[1].split(";")) {
        String[] temp2 = d.split(",");
        diseases.add(new MobDisease(temp2[0], Float.parseFloat(temp2[1])));
      }
    }
  }
  
  public ArrayList<MobDisease> getMobDiseases(String mobName) {
    return mobDisease.getOrDefault(mobName, new ArrayList<>());
  }
}
