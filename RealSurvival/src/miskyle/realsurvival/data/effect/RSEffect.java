package miskyle.realsurvival.data.effect;

import org.bukkit.entity.Player;

public abstract class RSEffect{
	private final int effectLevel;
	private final int duration;
	private final boolean immediate;
	
	public RSEffect(int duration,int effectLevel) {
		this.duration = duration;
		this.effectLevel = effectLevel;
		this.immediate = true;
	}
	
	public RSEffect(int duration,int effectLevel,boolean immediate) {
		this.duration = duration;
		this.effectLevel = effectLevel;
		this.immediate = immediate;
	}
	
	public abstract void effect(Player player);

	public int getEffectLevel() {
		return effectLevel;
	}

	public int getDuration() {
		return duration;
	}

	public boolean isImmediate() {
		return immediate;
	}
	
}
