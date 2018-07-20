package com.outlook.schooluniformsama.event.basic;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.item.NBTItemData;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.gui.FeatureGUI;

/*
 * SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET POTION COOKED_CHICKEN
 */

public class UseItemEvent implements Listener {
	
	private String foods="SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET POTION COOKED_CHICKEN";
	
	@EventHandler
	public void playerEatFood(PlayerItemConsumeEvent e){
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd==null)return;
		
		if(NBTItemData.isNBTItem(e.getItem())){
			if(useItem(NBTItemData.load(e.getItem()),pd)){
				
			}else{
				if(Data.foodEffect.containsKey(e.getItem().getType().name())){
					eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
					return;
				}
			}
		}
		
		
		ItemLoreData id=ItemLoreData.getItemLoreData(e.getItem());
		if(id==null){
			if(!Data.foodEffect.containsKey(e.getItem().getType().name()))
				return;
			else{
				//e.setCancelled(true);
				eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
				e.getItem().setAmount(e.getItem().getAmount()-1);
				return;
			}
		}else{
			if(!useItem(id, pd))
				if(Data.foodEffect.containsKey(e.getItem().getType().name())){
					eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
					//e.getItem().setAmount(e.getItem().getAmount()-1);
					return;
				}
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void useItem(PlayerInteractEvent e){
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd==null)return;
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR))return;
		if(e.getItem()==null||e.getItem().getType()==Material.AIR||foods.contains(e.getItem().getType().name()))return;
		if(NBTItemData.isNBTItem(e.getItem()) && useItem(NBTItemData.load(e.getItem()),pd)){
			e.setCancelled(true);
			if(Data.versionData[0] > 9 || (Data.versionData[0] == 9 && Data.versionData[1] ==1)){
				if(e.getHand()==EquipmentSlot.HAND) e.getPlayer().getInventory().setItemInMainHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInMainHand()));
				else if(e.getHand()==EquipmentSlot.OFF_HAND) e.getPlayer().getInventory().setItemInOffHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInOffHand()));
			}else{
				e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
			}
			return;
		}else{
			ItemLoreData id=ItemLoreData.getItemLoreData(e.getItem());
			if(id!=null && useItem(id, pd)){
				e.setCancelled(true);
				if(Data.versionData[0] > 9 || (Data.versionData[0] == 9 && Data.versionData[1] ==1)){
					if(e.getHand()==EquipmentSlot.HAND) e.getPlayer().getInventory().setItemInMainHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInMainHand()));
					else if(e.getHand()==EquipmentSlot.OFF_HAND) e.getPlayer().getInventory().setItemInOffHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInOffHand()));
				}else{
					e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
				}
				return;
			}else if(id!=null && id.getWorkbenchType()!=null && id.getRecipeName() != null){//Check Recipe
				e.setCancelled(true);
				FeatureGUI.openRecipeViewer(e.getPlayer(),id.getWorkbenchType(), id.getRecipeName());
				return;
			}
		}
		
		if(e.getItem().getItemMeta() instanceof BookMeta){
			BookMeta book = (BookMeta) e.getItem().getItemMeta();
			if(!book.hasTitle())return;
			if(!book.hasPages() || book.getPageCount()!=1)return;
			String temp = book.getPage(1);
			if(temp==null||temp.length()<=0)return;
			String text[] = temp.replace("§0", "").split("\n");
			if(text.length!=3 || !(text[0].equalsIgnoreCase("[RSR]") || text[0].equalsIgnoreCase("[RS_Recipe]")))return;
			e.setCancelled(true);
			FeatureGUI.openRecipeViewer(e.getPlayer(), text[1], text[2]);
			return;
		}
	}
	
	
	private void eatFood(PlayerData pd,Food f){
		pd.change(EffectType.SLEEP, f.getSleep());
		pd.change(EffectType.THIRST, f.getThirst());
		pd.change(EffectType.ENERGY, f.getEnergy());
		if(f.isHasIllness())
			for(Map.Entry<String,Double> entity:f.getIllness().entrySet())
					pd.addIllness(entity.getKey(),entity.getValue(),null);
	}
	
	private boolean useItem(ItemLoreData id,PlayerData pd){
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
		return isUsed;
	}
	
	private boolean useItem(NBTItemData id,PlayerData pd){
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
	
	/**sub a item from player*/
	public static ItemStack sub(ItemStack is){
		if(is.getAmount()>1)
			is.setAmount(is.getAmount()-1);
		else
			is=null;
		return is;
	}
}
