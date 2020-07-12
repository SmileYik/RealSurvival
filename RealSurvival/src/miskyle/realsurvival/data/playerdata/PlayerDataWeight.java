package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataWeight extends PlayerDataStatus{
	
	public double getMaxValue() {
		return super.getExtraValueSum()+ConfigManager.getWeightConfig().getMaxValue();
	}
}
