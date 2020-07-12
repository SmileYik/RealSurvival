package miskyle.realsurvival.data.effect;

import org.bukkit.entity.Player;

public abstract class RSEffect{
	private int effectLevel;
	private int duration;
	
	public abstract void effect(Player player);

	public int getEffectLevel() {
		return effectLevel;
	}

	public void setEffectLevel(int effectLevel) {
		this.effectLevel = effectLevel;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
}
