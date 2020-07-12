package miskyle.realsurvival.status.task;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RSEntry;

public class ThirstTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					RSEntry<Double, Double> values=
							pd.getThirst().modify(ConfigManager.getThirstConfig().getDecreaseValue());
					for(RSEntry<Double, Double> entry : ConfigManager.getThirstConfig().getEffectData().keySet()) {
						//代表属性值由高到低减少
						if(entry.getLeft()>entry.getRight()&&values.getLeft()>entry.getLeft()
								&& values.getRight()<=entry.getLeft()
								&& values.getRight()>entry.getRight()) {
							//TODO 给玩家发消息及赋予玩家属性
							break;
						}
					}
				});
		
	}
	
}
