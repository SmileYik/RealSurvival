package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RSEntry;

public class ThirstTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					RSEntry<Double, Double> values=
							pd.getThirst().modify(ConfigManager.getThirstConfig().getDecreaseValue());
					Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
					for(RSEntry<Double, Double> entry : ConfigManager.getThirstConfig().getEffectData().keySet()) {
						//代表属性值由高到低减少
						if(entry.getLeft()>entry.getRight()
								&&values.getLeft()>entry.getLeft()
								&& values.getRight()<=entry.getLeft()
								&& values.getRight()>entry.getRight()) {
							PlayerManager.bar.sendActionBar(
									p, Msg.tr("message.thirst."+entry.getLeft()+"-"+entry.getRight()));
						}
						
						if(values.getRight()>=Math.min(entry.getLeft(), entry.getRight())
								&& values.getRight()<Math.max(entry.getLeft(), entry.getRight())) {
							ConfigManager.getThirstConfig().getEffectData().get(entry).forEach(effect->{
								EffectManager.effectPlayer(p,effect);
							});
						}
					}
				});
		
	}
	
}
