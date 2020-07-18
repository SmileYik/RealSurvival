package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.playerdata.TemperatureStatus;
import miskyle.realsurvival.randomday.RandomDayManager;
import miskyle.realsurvival.util.RSEntry;

public class TemperatureTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
					RSEntry<Double, Double> toleranceEntry 
					= pd.getTemperature().getTotalTolerance(pd.getEffect());
					double temperature = RandomDayManager.getTemperature(p.getLocation());
					if(temperature>toleranceEntry.getRight()) {
					  pd.getTemperature().setValue(TemperatureStatus.HOT);
					  ConfigManager.getTemperatureConfig().getMaxEffect().forEach(effect->{
					    EffectManager.effectPlayer(p, effect);
					  });
					  PlayerManager.bar.sendActionBar(p, 
					      Msg.tr("messages.temperature.hot"));
					}else if(temperature<toleranceEntry.getLeft()) {
					  pd.getTemperature().setValue(TemperatureStatus.COLD);
					  ConfigManager.getTemperatureConfig().getMinEffect().forEach(effect->{
                        EffectManager.effectPlayer(p, effect);
                      });
					  PlayerManager.bar.sendActionBar(p, 
                          Msg.tr("messages.temperature.cold"));
					}else {
					  pd.getTemperature().setValue(TemperatureStatus.NORMAL);
					}
				});
		
	}

}
