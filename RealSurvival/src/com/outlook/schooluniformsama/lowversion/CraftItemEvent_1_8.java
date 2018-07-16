package com.outlook.schooluniformsama.lowversion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.api.event.RSMakeItemByFurnaceEvent;
import com.outlook.schooluniformsama.api.event.RSMakeItemByWorkbenchEvent;
import com.outlook.schooluniformsama.api.event.RSMakeItemDoneByFurnaceEvent;
import com.outlook.schooluniformsama.api.event.RSMakeItemDoneByWorkbenchEvent;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.TempData;
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
import com.outlook.schooluniformsama.event.basic.UseItemEvent;
import com.outlook.schooluniformsama.gui.FeatureGUI;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.gui.Workbench;
import com.outlook.schooluniformsama.nms.NBTItem;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.util.Util;

public class CraftItemEvent_1_8 implements Listener{

	@EventHandler
	public void openCarftTable(InventoryClickEvent e){
		
		if(e.getWhoClicked() instanceof Player == false)
			return;
		Player p=(Player) e.getWhoClicked();
		
		if(!Data.playerData.containsKey(p.getUniqueId()))
			return;
		if(e.getSlotType()==SlotType.OUTSIDE)return;
		
		if(e.getInventory().getTitle().equalsIgnoreCase(I18n.tr("recipe1"))){
			e.setCancelled(true);
			p.closeInventory();
			if(e.getRawSlot() == 49)return;
			p.openInventory(e.getInventory());
		}
		
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
						p.sendMessage(I18n.trp("workbench4"));
						Data.workbenchRecipe.add(TempData.createRecipeTemp.get(p.getName()).getName());
						TempData.createRecipeTemp.remove(p.getName());
						p.closeInventory();
					}else{
						p.sendMessage(I18n.trp("workbench3"));
						return;
					}
				} catch (Exception e2) {
					p.sendMessage(I18n.trp("workbench3"));
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
					WorkbenchRecipe wr = wt.getRecipe();
					RSMakeItemDoneByWorkbenchEvent event = new RSMakeItemDoneByWorkbenchEvent(p, wr, wt.getLocation(), wt.getWorkbenchName());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						Data.timer.remove(Util.getWorkbenchID(wt));
						for(ItemStack item : wr.getProduct())
							givePlayerItem(p, item);
						p.closeInventory();
					}
					return;						
				}else{
					p.sendMessage(I18n.trp("workbench8"));
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
					if(e.getInventory().getItem(i)!=null){
						hasMaterials=false;
						break;
					}
				if(hasMaterials)
					 return; 
				
				for(String str:Data.workbenchRecipe){
					WorkbenchRecipe wr = WorkbenchRecipe.load(str);
					WorkbenchTimer wt = TempData.createTimerTemp.get(p.getName()).toWorkbenchTimer();
					if(wr==null)continue;
					if(wr.getTableType()!=null && !wr.getTableType().equalsIgnoreCase(wt.getWorkbenchName()))continue;
					if(wr.containsShape(e.getInventory())){
						if(!hasPermission(p, wr.getPermission()))continue; // if player no permission, skip this
						//Success
						RSMakeItemByWorkbenchEvent event = new RSMakeItemByWorkbenchEvent(p,wr,wt.getLocation(),wt.getWorkbenchName());
						Bukkit.getServer().getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							try{
								wt.start(wr);
								Data.timer.put(Util.getWorkbenchID(wt), wt);
								TempData.openingWorkbench.remove(p.getName());
								for(int i:Workbench.materials)
									e.getInventory().setItem(i, null);
								p.closeInventory();
							}catch(Exception exp){
								p.sendMessage(I18n.trp("workbench2"));
								return;
							}
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
				if(ft.isOver()){
					FurnaceRecipe fr = ft.getRecipe();
					RSMakeItemDoneByFurnaceEvent event = new RSMakeItemDoneByFurnaceEvent(p, fr, ft.getLocation(), ft.getWorkbenchName(), ft.isBad());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						if(ft.isBad()){
							Data.timer.remove(Util.getWorkbenchID(ft));
							p.sendMessage(I18n.trp("furnace2"));
							p.closeInventory();
							return;
						}
						Data.timer.remove(Util.getWorkbenchID(ft));
						for(ItemStack item:fr.getProduct())
							givePlayerItem(p, item);
						p.closeInventory();
					}
					return;
				}else{
					p.sendMessage(I18n.trp("workbench8"));
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
						p.sendMessage(I18n.trp("workbench4"));
						Data.furnaceRecipe.add(TempData.createRecipeTemp.get(p.getName()).getName());
						TempData.createRecipeTemp.remove(p.getName());
						p.closeInventory();
					}else{
						p.sendMessage(I18n.trp("workbench3"));
						return;
					}
				} catch (Exception e2) {
					p.sendMessage(I18n.trp("workbench3"));
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
					FurnaceTimer ft = TempData.createTimerTemp.get(p.getName()).toFurnaceTimer();
					if(fr==null)continue;
					if(fr.getTableType()!=null && !fr.getTableType().equalsIgnoreCase(ft.getWorkbenchName()))continue;
					if(fr.containsShape(e.getInventory())){
						if(!hasPermission(p, fr.getPermission()))continue; // if player no permission, skip this
						//success
						RSMakeItemByFurnaceEvent event = new RSMakeItemByFurnaceEvent(p, fr, ft.getLocation(), ft.getWorkbenchName());
						Bukkit.getServer().getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							try{
								ft.start(fr,TemperatureTask.getBaseTemperature(ft.getLocation(),true));
								Data.timer.put(Util.getWorkbenchID(ft), ft);
								TempData.openingWorkbench.remove(p.getName());
								for(int i:Furnace.mSlot)
									e.getInventory().setItem(i, null);
								e.getInventory().setItem(4, null);
								p.closeInventory();
							}catch(Exception exp){
								p.sendMessage(I18n.trp("workbench2"));
								return;
							}
						}
					}
				}
			}
			return;
		}else if(e.getInventory().getTitle().equals(I18n.tr("rainwater1"))){
			RainwaterCollectorTimer rct = (RainwaterCollectorTimer)Data.timer.get(TempData.openingWorkbench.get(p.getName()));
			if(e.getRawSlot()<27){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(FeatureGUI.openRainwaterCollector(rct));
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
					if(p.isSneaking()){
						if(e.getItem()==null && Data.timer.containsKey(Util.getWorkbenchID(block))){
							Timer t = Data.timer.get(Util.getWorkbenchID(block));
							if(entry.getValue().getType()==WorkbenchType.WORKBENCH){
								if(t.isOver())
									p.sendMessage(I18n.trp("workbench13"));
								else
									p.sendMessage(I18n.trp("workbench12", Util.RDP(t.getTime()/(double)t.getNeedTime()*100,2),t.getNeedTime()-t.getTime()));
							}else if(entry.getValue().getType()==WorkbenchType.FURNACE){
								FurnaceTimer ft = (FurnaceTimer) t;
								if(ft.isBad())
									p.sendMessage(I18n.trp("furnace16"));
								else if(ft.isOver())
									p.sendMessage(I18n.trp("furnace15"));
								else if(ft.getLastTemperature()<ft.getMinTemperature())
									p.sendMessage(I18n.trp("furnace13",Util.RDP(t.getTime()/(double)t.getNeedTime()*100,2),t.getNeedTime()-t.getTime()));
								else p.sendMessage(I18n.trp("furnace14",Util.RDP(t.getTime()/(double)t.getNeedTime()*100,2),t.getNeedTime()-t.getTime()));
							}
							break;
						}
						return;
					}
					e.setCancelled(true);
					if(entry.getValue().getType()==WorkbenchType.WORKBENCH){
						openWorkbenchGUI(p, block,entry.getKey());
						break;
					}
					else if(entry.getValue().getType()==WorkbenchType.FURNACE){
						openFurnaceGUI(p, block,entry.getKey());
						break;
					}
				}
			}
		}
	}
	
	public static void givePlayerItem(Player p,ItemStack item,int amount){
		if(item == null)return;
		item.setAmount(amount);
		if(p.getInventory().firstEmpty()!=-1)
			p.getInventory().addItem(item);
		else
			p.getWorld().dropItem(p.getLocation(), item);
	}
	
	public static void givePlayerItem(Player p,ItemStack item){
		if(item == null)return;
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
			}else p.sendMessage(I18n.trp("rainwater2"));
		}else{	
			RainwaterCollectorTimer rct = new RainwaterCollectorTimer(p.getName(),b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
			rct.start();
			TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
			Data.timer.put(Util.getWorkbenchID(b), rct);
			p.openInventory(FeatureGUI.openRainwaterCollector((RainwaterCollectorTimer) Data.timer.get(Util.getWorkbenchID(b))));
		}
	}
	
	private void openFurnaceGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Furnace.openFurnaceGUI((FurnaceTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else p.sendMessage(I18n.trp("workbench1",Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()));
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.FURNACE, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
			p.openInventory(Furnace.createFurnaceGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
	private void openWorkbenchGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Workbench.openWorkbenchGUI((WorkbenchTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else p.sendMessage(I18n.trp("workbench1",Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()));
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.WORKBENCH, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
			p.openInventory(Workbench.createWorkbenchGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
	private boolean compare(Block b,BlockFace face,String name){
		if(name==null || name.equalsIgnoreCase("null"))
			return true;
		return b.getRelative(face).getType().name().equalsIgnoreCase(name);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void getRainwaterAndStrainer(PlayerInteractEvent e){
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK  || e.getItem()==null || e.getItem().getType()!=Material.GLASS_BOTTLE)return;
		if((e.getClickedBlock().getType()==Material.CAULDRON&&(e.getClickedBlock().getRelative(BlockFace.UP).getState() instanceof Hopper))){
			e.setCancelled(true);
			RainwaterCollectorTimer rct = (RainwaterCollectorTimer)Data.timer.get(Util.getWorkbenchID(e.getClickedBlock()));
			if(!rct.getPlayerName().equalsIgnoreCase(e.getPlayer().getName())){e.getPlayer().sendMessage(I18n.trp("rainwater2")); return ;}
			
			if(rct.hasWater()==null){
				e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
				rct.removeWater(1);
				givePlayerItem(e.getPlayer(), Items.getWater("Rainwater"));
			}else e.getPlayer().sendMessage(I18n.trp("raniwater3"));
		}else if((e.getClickedBlock().getState() instanceof Hopper) && (e.getClickedBlock().getRelative(BlockFace.UP).getType()==Material.DROPPER)){
			e.setCancelled(true);
			Inventory up =( (Dropper)e.getClickedBlock().getRelative(BlockFace.UP).getState()).getInventory();
			Inventory down = ((Hopper)e.getClickedBlock().getState()).getInventory();
			ItemStack strainer = down.getItem(2);
			if(strainer == null || !strainer.hasItemMeta() || !strainer.getItemMeta().hasLore()){e.getPlayer().sendMessage(I18n.trp("raniwater4")); return ;}
			double data = ItemLoreData.getLore("Strainer", strainer.getItemMeta().getLore(), false);
			if(data<=0){e.getPlayer().sendMessage(I18n.trp("raniwater4"));return ;}
			Object allData[] = {null,null,false,null};
			for(ItemStack water : up.getContents()){
				if(water == null)continue;
				String type = NBTItem.getNBTValue(water, "RealSurvival");
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
			if(!(boolean)allData[2]){e.getPlayer().sendMessage(I18n.trp("raniwater5")); return ;}
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
						if(Util.removeColor(loreLine).contains(Data.label.get("Strainer"))){
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
				e.getPlayer().setItemInHand(UseItemEvent.sub(e.getPlayer().getInventory().getItemInHand()));
				if(((String)allData[3]).contains("Ice")) givePlayerItem(e.getPlayer(), Items.getWater("IceWater"));
				else if(((String)allData[3]).contains("Hot")) givePlayerItem(e.getPlayer(), Items.getWater("HotWater"));
				else givePlayerItem(e.getPlayer(), Items.getWater("Freshwater"));
			}
		}
	}
	
	private boolean hasPermission(Player p,String childPermission){
		do{
			if(p.hasPermission(childPermission))return true;
			if(childPermission.equalsIgnoreCase("RealSurvival.Recipe.Workbench.*")||childPermission.equalsIgnoreCase("RealSurvival.Recipe.Furnace.*"))break;
			childPermission = childPermission.substring(0,childPermission.lastIndexOf(".")+1)+"*";
		}while(true);
		return false;
	}
}
