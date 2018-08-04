package com.outlook.schooluniformsama.event.basic;

import java.util.Map;

import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.Effect;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.item.NBTItemData;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.task.EffectTask;

public class UseItem {
	public static void eatFood(PlayerData pd,Food f){
		pd.change(EffectType.SLEEP, f.getSleep());
		pd.change(EffectType.THIRST, f.getThirst());
		pd.change(EffectType.ENERGY, f.getEnergy());
		if(f.isHasIllness())
			for(Map.Entry<String,Double> entity:f.getIllness().entrySet())
					pd.addIllness(entity.getKey(),entity.getValue(),null);
	}
	
	public static boolean useItem(ItemLoreData id,PlayerData pd){
		boolean isUsed = false;
		if(id.getSleep()!=ItemLoreData.badCode()){
			pd.change(EffectType.SLEEP, id.getSleep());
			isUsed=true;
		}
		if(id.getThirst()!=ItemLoreData.badCode()){
			pd.change(EffectType.THIRST, id.getThirst());
			isUsed=true;
		}
		if(id.getDrugEffect()!=ItemLoreData.badCode() && id.getMedicineDuration()!=ItemLoreData.badCode()){
			pd.eatMedicine(id.getTreatable(), id.getDrugEffect(), (long)id.getMedicineDuration());
			isUsed=true;
		}
		if(id.getIllnessNames()!=null && id.getIllnessProbability()!=ItemLoreData.badCode()){
			for(String str:id.getIllnessNames())
					pd.addIllness(str,id.getIllnessProbability(),null);
			isUsed=true;
		}
		if(id.getEnergy()!=ItemLoreData.badCode()){
			pd.change(EffectType.ENERGY, id.getEnergy());
			isUsed=true;
		}
		if(id.getHungery()!=ItemLoreData.badCode()){
			pd.getPlayer().setFoodLevel(pd.getPlayer().getFoodLevel()+(int)id.getHungery());
			isUsed=true;
		}
		if(id.getEffectName()!=null) {
			for(Effect effect:Data.illnessEffects.get(id.getEffectName()))
			EffectTask.addEffect(pd.getPlayer(), effect);
			isUsed=true;
		}
		return isUsed;
	}
	
	public static boolean useItem(NBTItemData id,PlayerData pd){
		boolean isUsed = false;
		if(id.getSleep()!=ItemLoreData.badCode()){
			pd.change(EffectType.SLEEP, id.getSleep());
			isUsed=true;
		}
		if(id.getThirst()!=ItemLoreData.badCode()){
			pd.change(EffectType.THIRST, id.getThirst());
			isUsed=true;
		}
		if(id.getDrugEffect()!=ItemLoreData.badCode() && id.getMedicineDuration()!=ItemLoreData.badCode()){
			pd.eatMedicine(id.getTreatable(), id.getDrugEffect(), (long)id.getMedicineDuration());
			isUsed=true;
		}
		if(id.getIllness()!=null){
			for(Map.Entry<String, Double> entity:id.getIllness().entrySet())
				pd.addIllness(entity.getKey(),entity.getValue(),null);
			isUsed=true;
		}
		if(id.getEnergy()!=ItemLoreData.badCode()){
			pd.change(EffectType.ENERGY, id.getEnergy());
			isUsed=true;
		}
		if(id.getHungery()!=ItemLoreData.badCode()){
			pd.getPlayer().setFoodLevel(pd.getPlayer().getFoodLevel()+(int)id.getHungery());
			isUsed=true;
		}
		return isUsed;
	}
}
