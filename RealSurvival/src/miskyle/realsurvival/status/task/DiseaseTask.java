package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;

public class DiseaseTask implements Runnable{

  @Override
  public void run() {
    PlayerManager.getActivePlayers().forEachValue(
        PlayerManager.getActivePlayers().mappingCount(), pd->{
          Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
          pd.getDisease().getDiseases().forEachValue(
              pd.getDisease().getDiseases().mappingCount(), disease->{
              if(disease.recover()) {
                pd.getDisease().removeDisease(disease.getDiseaseName());
                PlayerManager.bar.sendActionBar(p, 
                    Msg.tr("messages.disease",disease.getDiseaseName()));
              }else {
                ConfigManager.getDiseaseConfig().getDiseaseEffect(
                    disease.getDiseaseName()).forEach(effect->{
                  EffectManager.effectPlayer(p, effect);
                });
              }
          });
        });
    
  }
  
}
