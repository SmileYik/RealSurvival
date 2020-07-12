package miskyle.realsurvival.data.item;

import miskyle.realsurvival.util.RSEntry;

public class RSItemData {
	
	private RSEntry<Double, Double> sleep;
	private RSEntry<Double, Double> thirst;
	private RSEntry<Double, Double> energy;
	private RSEntry<Double, Double> hunger;
	private RSEntry<Double, Double> health;
	private double weight;
	
	
	public RSItemData() {
		
	}
	
	public RSItemData(
			RSEntry<Double, Double> sleep, RSEntry<Double, Double> thirst, 
			RSEntry<Double, Double> energy,RSEntry<Double, Double> hunger, 
			RSEntry<Double, Double> health, double weight) {
		super();
		this.sleep = sleep;
		this.thirst = thirst;
		this.energy = energy;
		this.hunger = hunger;
		this.health = health;
		this.weight = weight;
	}
	
	public RSItemData(String[] sleep,String[] thirst,
			String[] energy,String[] hunger,String[] health,double weight) {
		
		this.energy =new RSEntry<Double, Double>(
				Double.parseDouble(energy[0]), Double.parseDouble(energy[1]));
		this.sleep =new RSEntry<Double, Double>(
				Double.parseDouble(sleep[0]), Double.parseDouble(sleep[1]));
		this.thirst =new RSEntry<Double, Double>(
				Double.parseDouble(thirst[0]), Double.parseDouble(thirst[1]));
		this.hunger =new RSEntry<Double, Double>(
				Double.parseDouble(hunger[0]), Double.parseDouble(hunger[1]));
		this.health =new RSEntry<Double, Double>(
				Double.parseDouble(health[0]), Double.parseDouble(health[1]));
		this.weight = weight;
		
	}
	
	public void addStatusValue(String status,double left,double right) {
		switch (status.toUpperCase()) {
		case "SLEEP":
			if(sleep==null) 
				sleep = new RSEntry<Double, Double>(left,right);
			else 
				sleep.set(left+sleep.getLeft(),right+sleep.getRight());
			break;
		case "THIRST":
			if(thirst==null) 
				thirst = new RSEntry<Double, Double>(left,right);
			else 
				thirst.set(left+thirst.getLeft(), right+thirst.getRight());
			break;
		case "ENERGY":
			if(energy==null) 
				energy = new RSEntry<Double, Double>(left,right);
			else 
				energy.set(left+energy.getLeft(), right+energy.getRight());
			break;
		case "HUNGER":
			if(hunger==null) 
				hunger = new RSEntry<Double, Double>(left,right);
			else 
				hunger.set(left+hunger.getLeft(), right+hunger.getRight());
			break;
		case "WEIGHT":
			weight += left;
			break;
		case "HEALTH":
			if(health==null) 
				health = new RSEntry<Double, Double>(left,right);
			else 
				health.set(left+health.getLeft(), right+health.getRight());
			break;
		default:
			break;
		}
	}
	
	public void setStatusValue(String status,String[] values) {
		setStatusValue(status, 
				Double.parseDouble(values[0]), Double.parseDouble(values[1]));
	}
	
	public void setStatusValue(String status,double left,double right) {
		switch (status.toUpperCase()) {
		case "SLEEP":
			if(sleep==null) 
				sleep = new RSEntry<Double, Double>(left,right);
			else 
				sleep.set(left,right);
			break;
		case "THIRST":
			if(thirst==null) 
				thirst = new RSEntry<Double, Double>(left,right);
			else 
				thirst.set(left, right);
			break;
		case "ENERGY":
			if(energy==null) 
				energy = new RSEntry<Double, Double>(left,right);
			else 
				energy.set(left, right);
			break;
		case "HUNGER":
			if(hunger==null) 
				hunger = new RSEntry<Double, Double>(left,right);
			else 
				hunger.set(left, right);
			break;
		case "WEIGHT":
			weight = left;
			break;
		case "HEALTH":
			if(health==null) 
				health = new RSEntry<Double, Double>(left,right);
			else 
				health.set(left, right);
			break;
		default:
			break;
		}
	}
	
	public RSEntry<Double, Double> getSleep() {
		return sleep;
	}
	/**
	 * 获取一个随机的Sleep属性值
	 * @return
	 */
	public double getSleepValue() {
		return random(sleep.getLeft(), sleep.getRight());
	}
	/**
	 * 判断该物品是否有Sleep属性值
	 * @return
	 */
	public boolean isValidSleep() {
		return sleep!=null;
	}
	public void setSleep(RSEntry<Double, Double> sleep) {
		this.sleep = sleep;
	}
	public void setSleep(String[] sleep) {
		this.sleep =new RSEntry<Double, Double>(
				Double.parseDouble(sleep[0]), Double.parseDouble(sleep[1]));
	}
	public RSEntry<Double, Double> getThirst() {
		return thirst;
	}
	public double getThirstValue() {
		return random(thirst.getLeft(),thirst.getRight());
	}
	public boolean isValidThirst() {
		return thirst!=null;
	}
	public void setThirst(RSEntry<Double, Double> thirst) {
		this.thirst = thirst;
	}
	public void setThirst(String[] thirst) {
		this.thirst =new RSEntry<Double, Double>(
				Double.parseDouble(thirst[0]), Double.parseDouble(thirst[1]));
	}
	public RSEntry<Double, Double> getEnergy() {
		return energy;
	}
	public double getEnergyValue() {
		return random(energy.getLeft(), energy.getRight());
	}
	public boolean isValidEnergy() {
		return energy!=null;
	}
	public void setEnergy(RSEntry<Double, Double> energy) {
		this.energy = energy;
	}
	public void setEnergy(String[] energy) {
		this.energy =new RSEntry<Double, Double>(
				Double.parseDouble(energy[0]), Double.parseDouble(energy[1]));
	}
	public RSEntry<Double, Double> getHunger() {
		return hunger;
	}
	public double getHungerValue() {
		return random(hunger.getLeft(), hunger.getRight());
	}
	public boolean isValidHunger() {
		return hunger!=null;
	}
	public void setHunger(RSEntry<Double, Double> hunger) {
		this.hunger = hunger;
	}
	public void 	setHunger(String[] hunger) {
		this.hunger =new RSEntry<Double, Double>(
				Double.parseDouble(hunger[0]), Double.parseDouble(hunger[1]));
	}
	public RSEntry<Double, Double> getHealth() {
		return health;
	}
	public double getHealthValue() {
		return random(health.getLeft(),health.getRight());
	}
	public boolean isValidHealth() {
		return health!=null;
	}
	public void setHealth(RSEntry<Double, Double> health) {
		this.health = health;
	}
	public void setHealth(String[] health) {
		this.health =new RSEntry<Double, Double>(
				Double.parseDouble(health[0]), Double.parseDouble(health[1]));
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public boolean isTool() {
		return true;
	}
	public double getValue(String status) {
		switch (status.toUpperCase()) {
		case "SLEEP":
			return getSleepValue();
		case "THIRST":
			return getThirstValue();
		case "ENERGY":
			return getEnergyValue();
		case "HUNGER":
			return getHungerValue();
		case "WEIGHT":
			return weight;
		case "HEALTH":
			return getHealthValue();
		default:
			return 0;
		}
	}
	
	private double random(double a,double b) {
		return Math.abs(a-b)*Math.random()+Math.min(a, b);
	}
	
}
