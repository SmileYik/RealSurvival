package miskyle.realsurvival.data.effect;

public class EffectData {
	private String name;
	private int duration;
	private int effecLevel;
	public EffectData() {
		
	}
	public EffectData(String name, int duration, int effecLevel) {
		super();
		this.name = name;
		this.duration = duration;
		this.effecLevel = effecLevel;
	}
	public static EffectData loadFromString(String s) {
		if(s.equalsIgnoreCase("null")) {
			return null;
		}
		String[] temp = s.split(",");
		return new EffectData(
				temp[0], Integer.parseInt(temp[1]), 
				Integer.parseInt(temp[2]));
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
}
