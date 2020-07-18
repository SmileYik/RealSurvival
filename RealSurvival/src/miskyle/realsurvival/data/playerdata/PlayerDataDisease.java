package miskyle.realsurvival.data.playerdata;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataDisease {
  private ConcurrentHashMap<String, Disease> diseases;
  
  public void addDisease(String name) {
    if(diseases.containsKey(name)) {
      diseases.get(name).getDiseaseAgain();
    }else if(ConfigManager.getDiseaseConfig().isContainsDisease(name)){
      diseases.put(name, new Disease(name));
    }
  }
  
  public void eatDurg(String name,double drug,int duration) {
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
  
  public void setDisease(String str) {
    diseases = new ConcurrentHashMap<String, Disease>();
    if(str == null)return;
    for(String temp:str.split(";")) {
      Disease d = Disease.getFromString(temp);
      diseases.put(d.getDiseaseName(), d);
    }
  }
  
  public void setDisease(List<String> list) {
    diseases = new ConcurrentHashMap<String, Disease>();
    list.forEach(temp->{
      Disease d = Disease.getFromString(temp);
      diseases.put(d.getDiseaseName(), d);
    });
  }
  
  public String getSaveString() {
    StringBuilder sb = new StringBuilder();
    diseases.values().forEach(d->{
      sb.append(d.getDiseaseName()+","+d.getRecover()+","+d.getDrug()+","+d.getDuration()+";");
    });
    return sb.deleteCharAt(sb.length()-1).toString();
  }
}
