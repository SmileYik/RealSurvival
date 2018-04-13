package com.outlook.schooluniformsama.event.basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
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
		
		
/*		String str1=" ";
		for(ItemStack is : e.getInventory())
			if(is!=null)
				str1+=is.getType().name()+" ";
		System.out.println(str1);*/
		
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
					for(int i:Furnace.mSlot)
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
			if(e.getRawSlot()<54&&e.getRawSlot()!=49){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(FeatureGUI.openRainwaterCollector(rct));
			}
			Msg.sendTitleToPlayer(p, rct.hasWater(), true);
			if(e.getInventory().getItem(49)==null||e.getInventory().getItem(49).getType()!=Material.GLASS_BOTTLE||e.getInventory().getItem(49).getAmount()>rct.getSize())return;
			ItemStack is = Items.getWater("Rainwater");
			is.setAmount(e.getInventory().getItem(49).getAmount());
			rct.removeWater(e.getInventory().getItem(49).getAmount());
			p.closeInventory();
			Inventory inv = FeatureGUI.openRainwaterCollector(rct);
			inv.setItem(49, is);
			p.openInventory(inv);
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
	
	public static void givePlayerItem(Player p,ItemStack item,int amount){
		item.setAmount(amount);
		int index=0;
		for(ItemStack is:p.getInventory().getContents())
			if(is==null && index++<p.getInventory().getContents().length-5){
				p.getInventory().addItem(item);
				return;
			}
		p.getWorld().dropItem(p.getLocation(), item);
	}
	
	public static void givePlayerItem(Player p,ItemStack item){
		int index=0;
		for(ItemStack is:p.getInventory().getContents())//.getStorageContents())
			if(is==null && index++<p.getInventory().getContents().length-5){
				p.getInventory().addItem(item);
				return;
			}
		p.getWorld().dropItem(p.getLocation(), item);
	}
	
	
/*	String sFName;
	long builtTime;

	@EventHandler
	public void openCarftTable(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player == false) { return;}
		Player p=(Player) e.getWhoClicked();
		if(rs.getPlayerData(p)==null)return;
		if(e.getSlotType()==SlotType.OUTSIDE)return;
		//workbench start
		//Player first open
		if (e.getInventory().getTitle().equals(rs.getWorkbenchItems()[7]) ){
			if((!CraftItemGUI.materials.contains(e.getRawSlot())&&e.getRawSlot()<54)&&e.getRawSlot()!=49){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(CraftItemGUI.createWorkbenchGUI(e.getClickedInventory().getItem(0)));
			}
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				boolean isAllNull1=true;
				for(int i:CraftItemGUI.materials)
					if(e.getInventory().getItem(i)!=null)
						isAllNull1=false;
				if(isAllNull1){
					 p.closeInventory();
					 OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "MaterialIsNull");
					 return;
				 }
				
				HashMap<Integer,ItemStack> material=new HashMap<Integer,ItemStack>();
				for(int i:CraftItemGUI.materials)
						material.put(i, e.getInventory().getItem(i));
				WorkbenchRecipe sf=rs.compareMaterial(material);
				if(sf!=null){
					//如果配方不为空及是否有权限使用这个配方
					if(!(p.hasPermission("RealSurvival.Items."+sf.getName())||(
								p.hasPermission("RealSurvival.Items.*")&&p.hasPermission("RealSurvival.Items."+sf.getName())))){
						OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "NoPermissionBuilt");
						return;
					}
					ItemMeta key=e.getInventory().getItem(0).getItemMeta();
					rs.addWorkBench(key.getDisplayName(),p.getName()+","+sf.getName()+",0,"
							+key.getLore().get(0)+","+key.getLore().get(1)+","+key.getLore().get(2)+","+key.getLore().get(3));
					for(int index:CraftItemGUI.materials)
						e.getInventory().setItem(index, null);
					OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "BuiltStart");
					p.closeInventory();
				}else{
					p.closeInventory();
					OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "NotFindSF");
					p.openInventory(CraftItemGUI.createWorkbenchGUI(e.getClickedInventory().getItem(0)));
				}
				
			}
			return;
			//OP Create a Recipe
		}else if(e.getInventory().getTitle().equals(rs.getWorkbenchItems()[7]+"*")){
			if(!CraftItemGUI.materials.contains(e.getRawSlot())&&
					!CraftItemGUI.products.contains(e.getRawSlot())&&e.getRawSlot()<54)
				e.setCancelled(true);
			if(e.getRawSlot()==49){
				//判断是否不为空
				 if(isNull(e)){
					 p.closeInventory();
					 p.sendMessage("§9[RealSurvival] §c合成公式创建失败......");
					 return;
				 }
				 sFName=e.getInventory().getItem(0).getItemMeta().getDisplayName().split(":")[0];
				 builtTime=Long.parseLong(e.getInventory().getItem(0).getItemMeta().getDisplayName().split(":")[1]);
				 HashMap<Integer,ItemStack> material=new HashMap<Integer,ItemStack>();
				 HashMap<Integer,ItemStack> product=new HashMap<Integer,ItemStack>();
				
					 for(int i:CraftItemGUI.materials)
						 material.put(i,e.getInventory().getItem(i) );
					 for(int i:CraftItemGUI.products)
						 product.put(i,e.getInventory().getItem(i) );
					 
					new WorkbenchRecipe(sFName, rs, material, product,builtTime).save();
				 rs.addSF(sFName);
				 p.closeInventory();
				 p.sendMessage("§9[RealSurvival] §b合成公式创建成功.");
				 return;
			}
		}else if(e.getInventory().getTitle().equals(rs.getWorkbenchItems()[7]+"§3§l*")){
			e.setCancelled(true);
			if(e.getRawSlot()!=49)return;
			
			if((e.getInventory().getItem(49)==null||!e.getInventory().getItem(49).
					getItemMeta().getDisplayName().equals("§9§l已完成 - 点击获取物品!")))
				return;
			
			addItem(e.getInventory(), p, CraftItemGUI.products);
			p.closeInventory();
			rs.removeWorkBench(e.getInventory().getItem(0).getItemMeta().getDisplayName());
			return;
		}
		
		//Recipe Wather Start
		if(e.getInventory().getTitle().contains("§9§l 配方列表") ){
			e.setCancelled(true);

			if(e.getRawSlot()==53){
				if(e.getInventory().getItem(53)==null)return;
				if(e.getInventory().getItem(53).getItemMeta().getLore().equals(Items.getPlaceholder7().getItemMeta().getLore())){
					p.closeInventory();
					p.openInventory(new CraftItemGUI().sFList(53,p));
				}
			}
			
			if(e.getInventory().getItem(e.getRawSlot())==null||e.getInventory().getItem(
					e.getRawSlot()).getType()==Material.AIR)return;
			
			String temp=Util_Colours.removeRawTooltipColour(
					Util_Colours.removeTooltipColour(e.getInventory().getItem(
							e.getRawSlot()).getItemMeta().getDisplayName()));
			p.closeInventory();
			p.openInventory(new CraftItemGUI().sFWatcher(rs.getSF(temp)));
			return;	
		}else if(e.getInventory().getTitle().contains("§9§l配方 - ") ){
			e.setCancelled(true);
			if(e.getRawSlot()==45){
				p.closeInventory();
				p.openInventory(new CraftItemGUI().sFList(0,p));
			}
		}
		//Recipe Wather End
		
		//雨水收集器 Start
		if(e.getInventory().getTitle().contains("§3§l雨水收集器")){
			if(e.getRawSlot()<26){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(FeatureGUI.openRwC(e.getClickedInventory().getItem(0)));
			}else if(e.getRawSlot()>27)return;
			int i=Integer.parseInt(rs.getRwC(e.getInventory().getItem(0).getItemMeta().getDisplayName()).split(",")[2]);
			
			if(e.getInventory().getItem(26)==null||e.getInventory().getItem(26).getType()!=Material.GLASS_BOTTLE)return;
			if(e.getInventory().getItem(26).getAmount()>1)return;
			if(i>0){
				e.getInventory().setItem(26, Items.getRainwater());
				String value[]=rs.getRwC(e.getInventory().getItem(0).getItemMeta().getDisplayName()).split(",");
				rs.resetRwC(e.getInventory().getItem(0).getItemMeta().getDisplayName(), 
						value[0]+","+value[1]+","+(i-1)+","+value[3]+","+value[4]+","+value[5]+","+value[6]);
				p.closeInventory();
				p.openInventory(FeatureGUI.openRwC(e.getClickedInventory().getItem(0)));
			}
			return;	
		}
		//雨水收集器 End
		//净化装置Start
		if(e.getInventory().getTitle().contains("§2§l净化装置")){
			
			if(!(e.getRawSlot()==1||e.getRawSlot()==7||e.getRawSlot()==4||e.getRawSlot()==13)&&e.getRawSlot()<18){
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(FeatureGUI.openPD());
				return;
			}
			
			if(e.getRawSlot()==13){
				e.setCancelled(true);
				if(e.getInventory().getItem(1)==null || e.getInventory().getItem(4)==null || e.getInventory().getItem(7)!=null)
					return;
				ItemStack is=e.getInventory().getItem(4);
				if(is.getAmount()>1)return;
				if(is.getItemMeta()==null || is.getItemMeta().getDisplayName()==null || 
						is.getItemMeta().getLore()==null || !is.getItemMeta().getDisplayName().contains("滤网"))
					return;
				int d=searchWater(e.getInventory().getItem(1));
				if(d==-1)return;
				ItemMeta im=is.getItemMeta();
				List<String> lore=im.getLore();
				int d2=Integer.parseInt(Util_Colours.removeRawTooltipColour(Util_Colours.removeTooltipColour(lore.get(0).split(":")[1].replaceAll(" ", ""))));
				if(d2<d)return;
				d2-=d;
				if(d2>0){
					lore.set(0, lore.get(0).split(":")[0]+": "+d2);
					im.setLore(lore);
					is.setItemMeta(im);
					e.getInventory().setItem(4, is);
				}else e.getInventory().setItem(4, null);
				e.getInventory().setItem(1, null);
				e.getInventory().setItem(7, Items.getFreshWater());
				return;
			}
			return;
		}
		//净化装置 End
		//Fire Craft Table
		if (e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]) ){
			if(e.getRawSlot()<54&&!FurnaceGUI.mSlot.contains(e.getRawSlot())){
				e.setCancelled(true);
				if(e.getRawSlot()!=49){
					p.closeInventory();
					p.openInventory(FurnaceGUI.createFCTGUI(e.getClickedInventory().getItem(0)));
					return;
				}
			}
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				boolean isnull1=false;
				for(int i:FurnaceGUI.mSlot)
					if(e.getClickedInventory().getItem(i)!=null)isnull1=true;
				if(!isnull1){
					OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "MaterialIsNull");
					return;
				}
				FireCraftTableRecipe f = null;
				for(FireCraftTableRecipe fctr:rs.getFCTR())
					if(fctr.containsShape(e.getClickedInventory())!=null){
						f=fctr;
						break;
					}
				if(f==null){
					OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "NotFindSF");
					return;
				}else{
					ItemMeta im=e.getClickedInventory().getItem(0).getItemMeta();
					rs.addFCT(im.getDisplayName(), p.getName()+","+f.getName()+","+0+","+0+","+f.getmaxTime()+","
							+f.getTemperature()+","+im.getLore().get(0)+","+im.getLore().get(1)+","+
							im.getLore().get(2)+","+im.getLore().get(3));
					for(int i:FurnaceGUI.mSlot)
						e.getInventory().setItem(i, null);
					OldUtils.sendMsgToPlayer(rs.getPlayerData(p), "BuiltStart");
					p.closeInventory();
					p.openInventory(FurnaceGUI.openFCTGUI(e.getClickedInventory().getItem(0)));
				}
			}
		}else if(e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]+"§3§l*")){
			if(e.getRawSlot()<54){
				e.setCancelled(true);
				if(e.getRawSlot()!=49){
					p.closeInventory();
					p.openInventory(FurnaceGUI.openFCTGUI(e.getClickedInventory().getItem(0)));
					return;
				}
			}
			if(e.getRawSlot()==49){
				ItemMeta im=e.getInventory().getItem(49).getItemMeta();
				if(im==null||im.getDisplayName()==null||!im.getDisplayName().equals("§b§l==>§c§lOK§b§l<=="))return;
				if(e.getClickedInventory().getItem(37).getItemMeta().getDisplayName().equals( "§7§l烧焦了..."))
					p.getWorld().dropItem(p.getLocation(), Items.getJunk());
				else
					addItem(e.getClickedInventory(), p, FurnaceGUI.pSlot);
				rs.removeFCT(e.getClickedInventory().getItem(0).getItemMeta().getDisplayName());
				p.closeInventory();
				return;
			}
		}else if(e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]+"*")){
			if(e.getRawSlot()<54&&!FurnaceGUI.mSlot.contains(e.getRawSlot())&&!FurnaceGUI.pSlot.contains(e.getRawSlot())){
				e.setCancelled(true);
				if(e.getRawSlot()!=49)return;
			}
			if(e.getRawSlot()==49){
				boolean isnull1=false,isnull2=false;
				for(int i:FurnaceGUI.mSlot)
					if(e.getClickedInventory().getItem(i)!=null)isnull1=true;
				for(int i:FurnaceGUI.pSlot)
					if(e.getClickedInventory().getItem(i)!=null)isnull2=true;
				if(!(isnull1&&isnull2)||!FireCraftTableRecipe.createFireCraftTableRecipe(e.getClickedInventory())){
					p.sendMessage("§9[RealSurvival] §b合成公式创建失败.");
					return;
				}else{
					rs.addFireCraftTableRecipe(FireCraftTableRecipe.load(e.getClickedInventory().getItem(0).getItemMeta().getDisplayName().split(":")[0]));
					p.sendMessage("§9[RealSurvival] §b合成公式创建成功.");
					p.closeInventory();
					return;
				}
			}
		}
		//Fire Craft Table
	}
	
	@EventHandler
	public void closeGUI(InventoryCloseEvent e){
		if(e.getInventory().getTitle().equals(rs.getWorkbenchItems()[7])){
			addItem(e.getInventory(), (Player) e.getPlayer(), CraftItemGUI.materials);
			return;
		}
		
		//§3§l雨水收集器
		if(e.getInventory().getTitle().contains("§3§l雨水收集器") ){
			if(e.getInventory().getItem(26)!=null)
				e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(),e.getInventory().getItem(26));
			return;
		}
		
		//净化装置
		if(e.getInventory().getTitle().contains("§2§l净化装置") ){
			if(e.getInventory().getItem(1)!=null)
				e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(),e.getInventory().getItem(1));
			if(e.getInventory().getItem(7)!=null)
				e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(),e.getInventory().getItem(7));
			if(e.getInventory().getItem(4)!=null)
				e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(),e.getInventory().getItem(4));
			return;
		}
		
		//Fire Craft Table
		if(e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]) ){
			addItem(e.getInventory(), (Player) e.getPlayer(), FurnaceGUI.mSlot);
			return;
		}
		
	}
	
	private int searchWater(ItemStack is){
		if(is.getItemMeta()==null)return -1;
		if(is.getItemMeta().getDisplayName()==null)return -1;
		switch(is.getItemMeta().getDisplayName()){
			case "§1§l海水":return 5;
			case "§2§l湖水":return 3;
			case "§8§l沼泽水":return 8;
			case "§b§l雨水":return 1;
			default:return -1;
		}
	}
	
	private boolean isNull(InventoryClickEvent e){
		boolean isAllNull1=true;
		boolean isAllNull2=true;
		for(int i:CraftItemGUI.materials)
			if(e.getInventory().getItem(i)!=null)
				isAllNull1=false;
		for(int i:CraftItemGUI.products)
			if(e.getInventory().getItem(i)!=null)
				isAllNull2=false;
			return (isAllNull1||isAllNull2);
	}
	
	private void addItem(Inventory inv,Player p,List<Integer> is){
		for(int i:is)
			if(inv.getItem(i)!=null)
					p.getWorld().dropItem(p.getLocation(), inv.getItem(i));
	}*/
}
