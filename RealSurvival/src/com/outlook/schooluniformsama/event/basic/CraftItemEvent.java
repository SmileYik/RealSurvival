package com.outlook.schooluniformsama.event.basic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.WorkbenchShape;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.data.timer.Timer;
import com.outlook.schooluniformsama.data.timer.WorkbenchTimer;
import com.outlook.schooluniformsama.gui.FeatureGUI;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.gui.Workbench;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class CraftItemEvent implements Listener{
	@EventHandler
	public void openCarftTable(InventoryClickEvent e){
		
		if(e.getWhoClicked() instanceof Player == false)
			return;
		Player p=(Player) e.getWhoClicked();
		
		if(!Data.playerData.containsKey(p.getUniqueId()))
			return;
		if(e.getSlotType()==SlotType.OUTSIDE)return;
		
		
		if(e.getInventory().getTitle().contains(" §f- W*")){
			if(e.getRawSlot()<54&&!Workbench.materials.contains(e.getRawSlot())&&!Workbench.products.contains(e.getRawSlot())&&e.getRawSlot()!=49){
				e.setCancelled(true);
				return;
			}
			
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				boolean hasMaterials=true;
				for(int i:Workbench.materials)
					if(e.getInventory().getItem(i)!=null)
						hasMaterials=false;
				if(hasMaterials)
					 return; 
				for(int i:Workbench.products)
					if(e.getInventory().getItem(i)!=null)
						hasMaterials=true;
				if(!hasMaterials)
					 return;
				
				try {
					if(WorkbenchRecipe.createRecipe(e.getInventory(),(WorkbenchRecipe) TempData.createRecipeTemp.get(p.getName()))){
						Msg.sendMsgToPlayer(p, "CreateRecipeSucceed", true);
						Data.workbenchRecipe.add(TempData.createRecipeTemp.get(p.getName()).getName());
						TempData.createRecipeTemp.remove(p.getName());
						p.closeInventory();
					}else{
						Msg.sendMsgToPlayer(p, "CreateRecipeFailed", true);
						return;
					}
				} catch (Exception e2) {
					Msg.sendMsgToPlayer(p, "CreateRecipeFailed", true);
					return;
				}
				return;
			}
			return;
		}else if(e.getInventory().getTitle().contains(" §f- W§3§l*")){
			e.setCancelled(true);
			WorkbenchTimer wt=(WorkbenchTimer) Data.timer.get(TempData.openingWorkbench.get(p.getName()));
			if(e.getRawSlot()==49){
				if(wt.isOver()){
					Data.timer.remove(Util.getWorkbenchID(wt));
					for(int i:Workbench.products)
						if(e.getInventory().getItem(i)!=null)
							givePlayerItem(p, e.getInventory().getItem(i));
					p.closeInventory();
					return;
				}else{
					Msg.sendMsgToPlayer(p, "WorkbenchUndone", true);
					return;
				}
			}else{
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(Workbench.checkPass(e.getInventory(), wt));
				return;
			}
		}else if(e.getInventory().getTitle().contains(" §f- W")){
			if((!Workbench.materials.contains(e.getRawSlot())&&e.getRawSlot()<54)&&e.getRawSlot()!=49){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(Workbench.createWorkbenchGUI(TempData.createTimerTemp.get(p.getName()).getWorkbenchShape().getTitle()));
				return;
			}
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				boolean hasMaterials=true;
				for(int i:Workbench.materials)
					if(e.getInventory().getItem(i)!=null)
						hasMaterials=false;
				if(hasMaterials)
					 return; 
				
				for(String str:Data.workbenchRecipe){
					WorkbenchRecipe wr = WorkbenchRecipe.load(str);
					if(wr==null)continue;
					if(wr.containsShape(e.getInventory())){
						//Success
						try{
							WorkbenchTimer wt = TempData.createTimerTemp.get(p.getName()).toWorkbenchTimer();
							wt.start(wr);
							Data.timer.put(Util.getWorkbenchID(wt), wt);
							TempData.openingWorkbench.remove(p.getName());
							for(int i:Workbench.materials)
								e.getInventory().setItem(i, null);
							p.closeInventory();
						}catch(Exception exp){
							Msg.sendMsgToPlayer(p, "Exception", true);
							return;
						}
						return;
					}
				}
				//Make Failed
				return;
			}
			return;
		}else if(e.getInventory().getTitle().contains(" §f- F§3§l*")){
			e.setCancelled(true);
			FurnaceTimer ft=(FurnaceTimer) Data.timer.get(TempData.openingWorkbench.get(p.getName()));
			if(e.getRawSlot()==49){
				if(ft.isBad()){
					Data.timer.remove(Util.getWorkbenchID(ft));
					Msg.sendMsgToPlayer(p, "FurnaceFailedMsg", true);
					p.closeInventory();
					return;
				}
				if(ft.isOver()){
					Data.timer.remove(Util.getWorkbenchID(ft));
					for(int i:Furnace.pSlot)
						if(e.getInventory().getItem(i)!=null)
							givePlayerItem(p, e.getInventory().getItem(i));
					p.closeInventory();
					return;
				}else{
					Msg.sendMsgToPlayer(p, "WorkbenchUndone", true);
					return;
				}
			}else{
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(Furnace.checkHeatSource(e.getInventory(), ft));
				return;
			}
		}else if(e.getInventory().getTitle().contains(" §f- F*")){
			if(e.getRawSlot()<54&&!Furnace.mSlot.contains(e.getRawSlot())&&!Furnace.pSlot.contains(e.getRawSlot())&&e.getRawSlot()!=49){
				e.setCancelled(true);
				return;
			}
			if(e.getRawSlot()==49){
				boolean hasMaterials=true;
				for(int i:Furnace.mSlot)
					if(e.getInventory().getItem(i)!=null)
						hasMaterials=false;
				if(hasMaterials)
					 return; 
				for(int i:Furnace.pSlot)
					if(e.getInventory().getItem(i)!=null)
						hasMaterials=true;
				if(!hasMaterials)
					 return;
				
				try {
					if(FurnaceRecipe.createFurnaceRecipe(e.getInventory(),(FurnaceRecipe) TempData.createRecipeTemp.get(p.getName()))){
						Msg.sendMsgToPlayer(p, "CreateRecipeSucceed", true);
						Data.furnaceRecipe.add(TempData.createRecipeTemp.get(p.getName()).getName());
						TempData.createRecipeTemp.remove(p.getName());
						p.closeInventory();
					}else{
						Msg.sendMsgToPlayer(p, "CreateRecipeFailed", true);
						return;
					}
				} catch (Exception e2) {
					Msg.sendMsgToPlayer(p, "CreateRecipeFailed", true);
					return;
				}
			}
		}else if(e.getInventory().getTitle().contains(" §f- F")){
			if(e.getRawSlot()<54&&!Furnace.mSlot.contains(e.getRawSlot())&&e.getRawSlot()!=4&&e.getRawSlot()!=49){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(Furnace.createFurnaceGUI(TempData.createTimerTemp.get(p.getName()).getWorkbenchShape().getTitle()));
				return;
			}
			
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				
				for(String str:Data.furnaceRecipe){
					FurnaceRecipe fr = FurnaceRecipe.load(str);
					if(fr==null)continue;
					if(fr.containsShape(e.getInventory())){
						try{
							FurnaceTimer ft = TempData.createTimerTemp.get(p.getName()).toFurnaceTimer();
							ft.start(fr,TemperatureTask.getBaseTemperature(ft.getLocation(),true));
							Data.timer.put(Util.getWorkbenchID(ft), ft);
							TempData.openingWorkbench.remove(p.getName());
							for(int i:Furnace.mSlot)
								e.getInventory().setItem(i, null);
							e.getInventory().setItem(4, null);
							p.closeInventory();
						}catch(Exception exp){
							Msg.sendMsgToPlayer(p, "Exception", true);
							return;
						}
					}
				}
			}
			return;
		}else if(e.getInventory().getTitle().equals(Msg.getMsg("RainwaterCollectorTitle", false))){
			RainwaterCollectorTimer rct = (RainwaterCollectorTimer)Data.timer.get(TempData.openingWorkbench.get(p.getName()));
			if(e.getRawSlot()<27){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(FeatureGUI.openRainwaterCollector(rct));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void getRainwaterAndStrainer(PlayerInteractEvent e){
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK  || e.getItem()==null || e.getItem().getType()!=Material.GLASS_BOTTLE)return;
		if((e.getClickedBlock().getType()==Material.CAULDRON&&(e.getClickedBlock().getRelative(BlockFace.UP).getState() instanceof Hopper))){
			e.setCancelled(true);
			RainwaterCollectorTimer rct = (RainwaterCollectorTimer)Data.timer.get(Util.getWorkbenchID(e.getClickedBlock()));
			if(!rct.getPlayerName().equalsIgnoreCase(e.getPlayer().getName())){Msg.sendTitleToPlayer(e.getPlayer(),"rainwater-not-yours", Data.enablePrefixInTitle); return ;}
			if(!Msg.sendTitleToPlayer(e.getPlayer(), rct.hasWater(), Data.enablePrefixInTitle)){
				if(Data.versionData[0] > 9 || (Data.versionData[0] == 9 && Data.versionData[1] ==1)){
					if(e.getHand()==EquipmentSlot.HAND) e.getPlayer().getInventory().setItemInMainHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInMainHand()));
					else if(e.getHand()==EquipmentSlot.OFF_HAND) e.getPlayer().getInventory().setItemInOffHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInOffHand()));
				}else{
					e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
				}
				rct.removeWater(1);
				givePlayerItem(e.getPlayer(), Items.getWater("Rainwater"));
			}
		}else if((e.getClickedBlock().getState() instanceof Hopper) && (e.getClickedBlock().getRelative(BlockFace.UP).getType()==Material.DROPPER)){
			e.setCancelled(true);
			Inventory up =( (Dropper)e.getClickedBlock().getRelative(BlockFace.UP).getState()).getInventory();
			Inventory down = ((Hopper)e.getClickedBlock().getState()).getInventory();
			ItemStack strainer = down.getItem(2);
			if(strainer == null || !strainer.hasItemMeta() || !strainer.getItemMeta().hasLore()){Msg.sendTitleToPlayer(e.getPlayer(),"strainer-not-enoug", Data.enablePrefixInTitle); return ;}
			double data = ItemLoreData.getLore("Strainer", strainer.getItemMeta().getLore(), false);
			if(data<=0){Msg.sendTitleToPlayer(e.getPlayer(),"strainer-not-enoug", Data.enablePrefixInTitle); return ;}
			Object allData[] = {null,null,false,null};
			for(ItemStack water : up.getContents()){
				if(water == null)continue;
				String type = Data.nbtitem.getNBTValue(water, "RealSurvival");
				if(type == null || type.matches("Freshwater"))continue;
				int waterData = Data.strainer.get(type);
				if(data>waterData){
					allData[0]=water;
					allData[1] = waterData;
					allData[2] = true;
					allData[3] = type;
					break;
				}else if(data == waterData){
					allData[0]=water;
					strainer = null;
					allData[2] = true;
					allData[3] = type;
					break;
				}
			}
			if(!(boolean)allData[2]){Msg.sendTitleToPlayer(e.getPlayer(),"strainer-cannot-continue", Data.enablePrefixInTitle); return ;}
			else{
				up.setItem(up.first((ItemStack)allData[0]),new ItemStack(Material.GLASS_BOTTLE));
				if(strainer == null)  down.setItem(2,null);
				else{
					ItemMeta im = strainer.getItemMeta();
					List<String> lore = im.getLore();
					Iterator<String> line = lore.iterator();
					short index = 0;
					while(line.hasNext()){
						String loreLine = line.next();
						if(Util.removeColor(loreLine).contains(Data.label.get("strainer"))){
							loreLine = loreLine.replace(((int)data)+"", (int)(data-(int)allData[1])+"");
							lore.set(index, loreLine);
							break;
						}
						index ++;
					}
					im.setLore(lore);
					strainer.setItemMeta(im);
					down.setItem(2,strainer);
				}
				if(Data.versionData[0] > 9 || (Data.versionData[0] == 9 && Data.versionData[1] ==1)){
					if(e.getHand()==EquipmentSlot.HAND) e.getPlayer().getInventory().setItemInMainHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInMainHand()));
					else if(e.getHand()==EquipmentSlot.OFF_HAND) e.getPlayer().getInventory().setItemInOffHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInOffHand()));
				}else{
					e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
				}
				if(((String)allData[3]).contains("Ice")) givePlayerItem(e.getPlayer(), Items.getWater("IceWater"));
				else if(((String)allData[3]).contains("Hot")) givePlayerItem(e.getPlayer(), Items.getWater("HotWater"));
				else givePlayerItem(e.getPlayer(), Items.getWater("Freshwater"));
			}
		}
	}
	
	@EventHandler
	public void closeCraftTables(InventoryCloseEvent e){
		if(e.getInventory().getTitle().contains("*"))return;
		if(e.getInventory().getTitle().contains(" §f- W")){
			for(int i:Workbench.materials)
				if(e.getInventory().getItem(i)!=null)
					givePlayerItem((Player)e.getPlayer(),e.getInventory().getItem(i));
		}else if(e.getInventory().getTitle().contains(" §f- F")){
			for(int i:Furnace.mSlot)
				if(e.getInventory().getItem(i)!=null)
					givePlayerItem((Player)e.getPlayer(),e.getInventory().getItem(i));
		}
	}
	
	@EventHandler
	public void openTable(PlayerInteractEvent e){
		if(e.getItem()!=null)return;
		Block block=e.getClickedBlock();
		if(!(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) || block==null || block.getType()==Material.AIR)
			return;
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		Player p = e.getPlayer();
		
		//RainwaterCollector
		if((block.getType()==Material.CAULDRON&&(block.getRelative(BlockFace.UP).getState() instanceof Hopper))){
			if(Data.timer.containsKey(Util.getWorkbenchID(block)))
			e.setCancelled(true);
			openRainwaterCollector(p, block);
			return;
		}
		
		for(Map.Entry<String,WorkbenchShape> entry:Data.workbenchs.entrySet()){
			if(block.getType().name().equalsIgnoreCase(entry.getValue().getMain()) && compare(block,BlockFace.UP,entry.getValue().getUp()) && compare(block,BlockFace.DOWN,entry.getValue().getDown())){
				if((compare(block,BlockFace.NORTH,entry.getValue().getLeft())&&compare(block,BlockFace.SOUTH,entry.getValue().getRight())&&compare(block,BlockFace.EAST,entry.getValue().getBehind())&&compare(block,BlockFace.WEST,entry.getValue().getFront())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getBehind())&&compare(block,BlockFace.SOUTH,entry.getValue().getFront())&&compare(block,BlockFace.EAST,entry.getValue().getRight())&&compare(block,BlockFace.WEST,entry.getValue().getLeft())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getRight())&&compare(block,BlockFace.SOUTH,entry.getValue().getLeft())&&compare(block,BlockFace.EAST,entry.getValue().getFront())&&compare(block,BlockFace.WEST,entry.getValue().getBehind())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getFront())&&compare(block,BlockFace.SOUTH,entry.getValue().getBehind())&&compare(block,BlockFace.EAST,entry.getValue().getLeft())&&compare(block,BlockFace.WEST,entry.getValue().getRight()))){
					e.setCancelled(true);
					if(entry.getValue().getType()==WorkbenchType.WORKBENCH)
						openWorkbenchGUI(p, block,entry.getKey());
					else if(entry.getValue().getType()==WorkbenchType.FURNACE)
						openFurnaceGUI(p, block,entry.getKey());
				}
			}
		}
	}
	
	public static void givePlayerItem(Player p,ItemStack item,int amount){
		item.setAmount(amount);
		if(p.getInventory().firstEmpty()!=-1)
			p.getInventory().addItem(item);
		else
			p.getWorld().dropItem(p.getLocation(), item);
	}
	
	public static void givePlayerItem(Player p,ItemStack item){
		if(p.getInventory().firstEmpty()!=-1)
			p.getInventory().addItem(item);
		else
			p.getWorld().dropItem(p.getLocation(), item);
		
	}
	
	private void openRainwaterCollector(Player p,Block b){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(FeatureGUI.openRainwaterCollector((RainwaterCollectorTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "RainwaterCollectorError1",true);
		}else{	
			RainwaterCollectorTimer rct = new RainwaterCollectorTimer(p.getName(),b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
			rct.start();
			Data.timer.put(Util.getWorkbenchID(b), rct);
			p.openInventory(FeatureGUI.openRainwaterCollector((RainwaterCollectorTimer) Data.timer.get(Util.getWorkbenchID(b))));
		}
	}
	
	private void openFurnaceGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Furnace.openFurnaceGUI((FurnaceTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "WorkbenchIsUsing", new String[]{"%player%"},
					new String[]{Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()},true);
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.FURNACE, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			p.openInventory(Furnace.createFurnaceGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
	private void openWorkbenchGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Workbench.openWorkbenchGUI((WorkbenchTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "WorkbenchIsUsing", new String[]{"%player%"},
					new String[]{Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()},true);
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.WORKBENCH, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			p.openInventory(Workbench.createWorkbenchGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
	private boolean compare(Block b,BlockFace face,String name){
		if(name==null || name.equalsIgnoreCase("null"))
			return true;
		return b.getRelative(face).getType().name().equalsIgnoreCase(name);
	}
}
