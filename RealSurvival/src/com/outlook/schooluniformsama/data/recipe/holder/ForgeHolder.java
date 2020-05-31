package com.outlook.schooluniformsama.data.recipe.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.outlook.schooluniformsama.util.Util;

public class ForgeHolder implements InventoryHolder{
	private String    playerName;
	private String    cubeName;
	private String    worldName;
	private int       x,y,z;
	
	private Inventory inv;
	
	private ForgeState state;
	

	public ForgeHolder(String playerName, String cubeName, String worldName, int x, int y, int z, ForgeState state) {
		this.playerName = playerName;
		this.cubeName = cubeName;
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.state = state;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getCubeName() {
		return cubeName;
	}

	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Inventory getInv() {
		return inv;
	}

	public void setInv(Inventory inv) {
		this.inv = inv;
	}

	public ForgeState getState() {
		return state;
	}

	public void setState(ForgeState state) {
		this.state = state;
	}
	
	public String getWorldKey() {
		return worldName+x+y+z;
	}
	
}
