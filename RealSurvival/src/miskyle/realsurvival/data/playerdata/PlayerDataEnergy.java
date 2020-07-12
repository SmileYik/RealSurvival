package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataEnergy extends PlayerDataStatus{
	
	public double getMaxValue() {
		return super.getExtraValueSum()+ConfigManager.getEnergyConfig().getMaxValue();
	}
}
