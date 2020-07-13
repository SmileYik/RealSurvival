package miskyle.realsurvival.data.effect;

public class EffectData {
	private String name;
	private int duration;
	private int effecLevel;
	private boolean immediate;
	public EffectData() {
		
	}
	public EffectData(String name, int duration, int effecLevel, boolean immediate) {
		super();
		this.name = name;
		this.duration = duration;
		this.effecLevel = effecLevel;
		this.immediate = immediate;
	}
	public static EffectData loadFromString(String s) {
		String[] temp = s.split(",");
		return new EffectData(
				temp[0], Integer.parseInt(temp[1]), 
				Integer.parseInt(temp[2]), Boolean.parseBoolean(temp[3]));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getEffecLevel() {
		return effecLevel;
	}
	public void setEffecLevel(int effecLevel) {
		this.effecLevel = effecLevel;
	}
	public boolean isImmediate() {
		return immediate;
	}
	public void setImmediate(boolean immediate) {
		this.immediate = immediate;
	}
	
	
}
