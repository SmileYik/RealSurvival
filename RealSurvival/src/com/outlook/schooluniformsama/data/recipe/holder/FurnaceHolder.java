package com.outlook.schooluniformsama.data.recipe.holder;

public class FurnaceHolder extends ForgeHolder{
	private int    saveTime;
	private double minTem;
	private double maxTem;
	public FurnaceHolder(String playerName, String cubeName, String worldName, int x, int y, int z, ForgeState state,
			int saveTime, double minTem, double maxTem) {
		super(playerName, cubeName, worldName, x, y, z, state);
		this.saveTime = saveTime;
		this.minTem = minTem;
		this.maxTem = maxTem;
	}
	public int getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(int saveTime) {
		this.saveTime = saveTime;
	}
	public double getMinTem() {
		return minTem;
	}
	public void setMinTem(double minTem) {
		this.minTem = minTem;
	}
	public double getMaxTem() {
		return maxTem;
	}
	public void setMaxTem(double maxTem) {
		this.maxTem = maxTem;
	}
	
	
}
