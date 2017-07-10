package com.outlook.z815007320.event.basic;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.z815007320.data.FireCraftTableRecipe;
import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.data.WorkbenchRecipe;
import com.outlook.z815007320.gui.CraftItemGUI;
import com.outlook.z815007320.gui.FeatureGUI;
import com.outlook.z815007320.gui.FireCraftTableGUI;
import com.outlook.z815007320.utils.Util_Colours;
import com.outlook.z815007320.utils.Utils;

public class CraftItemEvent extends PluginRS implements Listener{
	String sFName;
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
					 Utils.sendMsgToPlayer(rs.getPlayerData(p), "MaterialIsNull");
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
						Utils.sendMsgToPlayer(rs.getPlayerData(p), "NoPermissionBuilt");
						return;
					}
					ItemMeta key=e.getInventory().getItem(0).getItemMeta();
					rs.addWorkBench(key.getDisplayName(),p.getName()+","+sf.getName()+",0,"
							+key.getLore().get(0)+","+key.getLore().get(1)+","+key.getLore().get(2)+","+key.getLore().get(3));
					for(int index:CraftItemGUI.materials)
						e.getInventory().setItem(index, null);
					Utils.sendMsgToPlayer(rs.getPlayerData(p), "BuiltStart");
					p.closeInventory();
				}else{
					p.closeInventory();
					Utils.sendMsgToPlayer(rs.getPlayerData(p), "NotFindSF");
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
			if(e.getRawSlot()<54&&!FireCraftTableGUI.mSlot.contains(e.getRawSlot())){
				e.setCancelled(true);
				if(e.getRawSlot()!=49){
					p.closeInventory();
					p.openInventory(FireCraftTableGUI.createFCTGUI(e.getClickedInventory().getItem(0)));
					return;
				}
			}
			if(e.getRawSlot()==49){
				e.setCancelled(true);
				boolean isnull1=false;
				for(int i:FireCraftTableGUI.mSlot)
					if(e.getClickedInventory().getItem(i)!=null)isnull1=true;
				if(!isnull1){
					Utils.sendMsgToPlayer(rs.getPlayerData(p), "MaterialIsNull");
					return;
				}
				FireCraftTableRecipe f = null;
				for(FireCraftTableRecipe fctr:rs.getFCTR())
					if(fctr.containsShape(e.getClickedInventory())!=null){
						f=fctr;
						break;
					}
				if(f==null){
					Utils.sendMsgToPlayer(rs.getPlayerData(p), "NotFindSF");
					return;
				}else{
					ItemMeta im=e.getClickedInventory().getItem(0).getItemMeta();
					rs.addFCT(im.getDisplayName(), p.getName()+","+f.getName()+","+0+","+0+","+f.getmaxTime()+","
							+f.getTemperature()+","+im.getLore().get(0)+","+im.getLore().get(1)+","+
							im.getLore().get(2)+","+im.getLore().get(3));
					for(int i:FireCraftTableGUI.mSlot)
						e.getInventory().setItem(i, null);
					Utils.sendMsgToPlayer(rs.getPlayerData(p), "BuiltStart");
					p.closeInventory();
					p.openInventory(FireCraftTableGUI.openFCTGUI(e.getClickedInventory().getItem(0)));
				}
			}
		}else if(e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]+"§3§l*")){
			if(e.getRawSlot()<54){
				e.setCancelled(true);
				if(e.getRawSlot()!=49){
					p.closeInventory();
					p.openInventory(FireCraftTableGUI.openFCTGUI(e.getClickedInventory().getItem(0)));
					return;
				}
			}
			if(e.getRawSlot()==49){
				ItemMeta im=e.getInventory().getItem(49).getItemMeta();
				if(im==null||im.getDisplayName()==null||!im.getDisplayName().equals("§b§l==>§c§lOK§b§l<=="))return;
				if(e.getClickedInventory().getItem(37).getItemMeta().getDisplayName().equals( "§7§l烧焦了..."))
					p.getWorld().dropItem(p.getLocation(), Items.getJunk());
				else
					addItem(e.getClickedInventory(), p, FireCraftTableGUI.pSlot);
				rs.removeFCT(e.getClickedInventory().getItem(0).getItemMeta().getDisplayName());
				p.closeInventory();
				return;
			}
		}else if(e.getInventory().getTitle().equals(rs.getFireCraftTableItems()[7]+"*")){
			if(e.getRawSlot()<54&&!FireCraftTableGUI.mSlot.contains(e.getRawSlot())&&!FireCraftTableGUI.pSlot.contains(e.getRawSlot())){
				e.setCancelled(true);
				if(e.getRawSlot()!=49)return;
			}
			if(e.getRawSlot()==49){
				boolean isnull1=false,isnull2=false;
				for(int i:FireCraftTableGUI.mSlot)
					if(e.getClickedInventory().getItem(i)!=null)isnull1=true;
				for(int i:FireCraftTableGUI.pSlot)
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
			addItem(e.getInventory(), (Player) e.getPlayer(), FireCraftTableGUI.mSlot);
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
	}
}
