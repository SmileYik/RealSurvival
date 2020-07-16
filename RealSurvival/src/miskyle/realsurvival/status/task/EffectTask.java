package miskyle.realsurvival.status.task;

import miskyle.realsurvival.data.PlayerManager;

public class EffectTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					pd.getEffect().getEffect().forEach((k,v)->{
						v.forEach((name,entry)->{
							entry.setRight(entry.getRight()-1);
							if(entry.getRight()<=0)
								pd.getEffect().removeEffect(k, name);
						});
					});
				});
	}
	
}
