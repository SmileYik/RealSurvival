package com.outlook.schooluniformsama.data.recipe.holder;

public class WorkbenchHolder extends ForgeHolder{
	private String recipeName;
	private long   needTime;
	public WorkbenchHolder(String playerName, String cubeName, String worldName, int x, int y, int z, ForgeState state,
			String recipeName, long needTime) {
		super(playerName, cubeName, worldName, x, y, z, state);
		this.recipeName = recipeName;
		this.needTime = needTime;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public long getNeedTime() {
		return needTime;
	}
	public void setNeedTime(long needTime) {
		this.needTime = needTime;
	}
	
}
