package com.outlook.schooluniformsama.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RealSurvivalEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
    private Player player;
    private boolean cancelled;

     RealSurvivalEvent(Player player) {
		super();
		this.player = player;
	}

	public boolean isCancelled() {
        return cancelled;
    }
 
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

	public Player getPlayer() {
		return player;
	}
}
