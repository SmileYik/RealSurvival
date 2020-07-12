package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataThirst extends PlayerDataStatus{
	
	public double getMaxValue() {
		return super.getExtraValueSum()+ConfigManager.getThirstConfig().getMaxValue();
	}
}
