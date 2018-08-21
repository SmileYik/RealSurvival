package com.outlook.schooluniformsama.event.basic;

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
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.item.NBTItemData;
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
		if(pd==null) {
			if(NBTItemData.isNBTItem(e.getItem())) e.setCancelled(true);
			else if(Data.foodEffect.containsKey(e.getItem().getType().name()))e.setCancelled(true);
			else if(ItemLoreData.getItemLoreData(e.getItem())!=null)e.setCancelled(true);
			return;
		}
		
		if(NBTItemData.isNBTItem(e.getItem())){
			if(UseItem.useItem(NBTItemData.load(e.getItem()),pd));
			else if(Data.foodEffect.containsKey(e.getItem().getType().name())){
				UseItem.eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
				return;
			}
		}
		
		
		ItemLoreData id=ItemLoreData.getItemLoreData(e.getItem());
		if(id==null){
			if(!Data.foodEffect.containsKey(e.getItem().getType().name())) return;
			else{
				UseItem.eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
				e.getItem().setAmount(e.getItem().getAmount()-1);
				return;
			}
		}else if(!UseItem.useItem(id, pd)&&Data.foodEffect.containsKey(e.getItem().getType().name())){
			UseItem.eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
			return;
		}
		return;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void useItem(PlayerInteractEvent e){
		if(e.getItem()!=null && e.getItem().getType()==Material.WRITTEN_BOOK){
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
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd==null) {
			if(NBTItemData.isNBTItem(e.getItem())) e.setCancelled(true);
			else if(ItemLoreData.getItemLoreData(e.getItem())!=null)e.setCancelled(true);
			return;
		}
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR))return;
		if(e.getItem()==null||e.getItem().getType()==Material.AIR||foods.contains(e.getItem().getType().name()))return;
		
		if(NBTItemData.isNBTItem(e.getItem()) && UseItem.useItem(NBTItemData.load(e.getItem()),pd)){
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
			if(id!=null && UseItem.useItem(id, pd)){
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
	}
	
	public static ItemStack sub(ItemStack is){
		is.setAmount(is.getAmount()-1);
		return is;
	}
}
