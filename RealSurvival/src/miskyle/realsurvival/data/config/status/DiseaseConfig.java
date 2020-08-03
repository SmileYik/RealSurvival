package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miskyle.realsurvival.data.effect.EffectData;

public class DiseaseConfig {
	private boolean enable;
	private boolean mob;
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
  public boolean isContainsDisease(String name) {
    return diseases.containsKey(name);
  }
  public ArrayList<EffectData> getDiseaseEffect(String diseaseName){
    if(diseases.containsKey(diseaseName)) {
      return diseases.get(diseaseName);
    }else {
      return new ArrayList<EffectData>();
    }
  }
  public void setDiseases(List<String> list) {
    diseases = new HashMap<String, ArrayList<EffectData>>();
    list.forEach(line->{
      String[] temp = line.split(":");
      for(String effect:temp[1].split(";")) {
        if(diseases.containsKey(temp[0])) {
          diseases.get(temp[0]).add(EffectData.loadFromString(effect));
        }else {
          ArrayList<EffectData> effects = new ArrayList<>();
          effects.add(EffectData.loadFromString(effect));
          diseases.put(temp[0], effects);
        }
      }
    });
  }
}
