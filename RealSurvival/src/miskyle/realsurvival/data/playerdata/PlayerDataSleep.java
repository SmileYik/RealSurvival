package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerDataSleep extends PlayerDataStatus{
	
	private boolean sleep = false;
	
	public PlayerDataSleep() {
		
	}
	
	public PlayerDataSleep(double value) {
		super(value);
	}
	
	/**
	 * 获取Sleep最大属性值
	 * @return
	 */
	public double getMaxValue() {
		return super.getExtraValueSum()+ConfigManager.getSleepConfig().getMaxValue();
	}

	/**
	 * 玩家是否在RealSurvival意义上睡着
	 * @return
	 */
	public boolean isSleep() {
		return sleep;
	}

	/**
	 * 设定玩家在RealSurvival意义上睡着
	 * @param sleep
	 */
	public void setSleep(boolean sleep) {
		this.sleep = sleep;
	}
	
	
	
}
