package miskyle.realsurvival.status.task;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RSEntry;

public class EnergyTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					RSEntry<Double, Double> values=
							pd.getThirst().modify(ConfigManager.getEnergyConfig().getIncreaseValue());
					for(RSEntry<Double, Double> entry: ConfigManager.getEnergyConfig().getEffectData().keySet()) {
						//代表属性值由低到高增加
						if(entry.getRight()>=entry.getLeft()&&values.getLeft()<=entry.getLeft()
								&& values.getRight()>entry.getLeft()
								&& values.getRight()<=entry.getRight()) {
							//TODO 给玩家发消息及赋予玩家属性
							break;
						}
					}
				});
		
	}

}
