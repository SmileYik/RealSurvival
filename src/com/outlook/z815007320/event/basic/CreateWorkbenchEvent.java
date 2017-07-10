package com.outlook.z815007320.event.basic;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.gui.CraftItemGUI;
import com.outlook.z815007320.gui.FeatureGUI;
import com.outlook.z815007320.gui.FireCraftTableGUI;
import com.outlook.z815007320.utils.Utils;
public class CreateWorkbenchEvent extends PluginRS implements Listener{
	
	//工作台
   /*
	*<School_Uniform SAMA> 1
	[17:36:54 INFO]: FENCE
	[17:36:54 INFO]: AIR
	[17:36:54 INFO]: BOOKSHELF
	[17:36:54 INFO]: WORKBENCH
	[17:37:01 INFO]: <School_Uniform SAMA> 2
	[17:37:03 INFO]: WORKBENCH
	[17:37:03 INFO]: BOOKSHELF
	[17:37:03 INFO]: FENCE
	[17:37:03 INFO]: AIR
	[17:37:08 INFO]: <School_Uniform SAMA> 3 no
	[17:37:11 INFO]: AIR
	[17:37:11 INFO]: FENCE
	[17:37:11 INFO]: WORKBENCH
	[17:37:11 INFO]: BOOKSHELF
	[17:37:16 INFO]: <School_Uniform SAMA> 4 no
	[17:37:20 INFO]: BOOKSHELF
	[17:37:20 INFO]: WORKBENCH
	[17:37:20 INFO]: AIR
	[17:37:20 INFO]: FENCE
	[17:37:22 INFO]: <School_Uniform SAMA> 5
	*/
	@EventHandler
	public void openTable(PlayerInteractEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		Block sign=e.getClickedBlock();
		if(sign==null||sign.getType().name().equalsIgnoreCase("air")||
				e.getAction()!=Action.RIGHT_CLICK_BLOCK)return;	
		
		if(sign.getType().name().equalsIgnoreCase("BOOKSHELF")){
			//书架-合成表
			if(sign.getRelative(BlockFace.DOWN).getType()==Material.BOOKSHELF&&
					sign.getRelative(BlockFace.UP).getType()==Material.BOOKSHELF){
				e.setCancelled(true);
				e.getPlayer().openInventory(new CraftItemGUI().sFList(0,e.getPlayer()));
				return;
			}
			if(sign.getRelative(BlockFace.UP).getType()==Material.BOOKSHELF&&
					sign.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType()==Material.BOOKSHELF){
				e.setCancelled(true);
				e.getPlayer().openInventory(new CraftItemGUI().sFList(0,e.getPlayer()));
				return;
			}
			if(sign.getRelative(BlockFace.DOWN).getType()==Material.BOOKSHELF&&
					sign.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType()==Material.BOOKSHELF){
				e.setCancelled(true);
				e.getPlayer().openInventory(new CraftItemGUI().sFList(0,e.getPlayer()));
				return;
			}
		}
		
		//工作台
		//当上方方块与下方方块不为指定方块时返回.	
		if(sign.getType().name().equalsIgnoreCase(rs.getWorkbenchItems()[0])&&
				compare(sign.getRelative(BlockFace.UP).getType(),rs.getWorkbenchItems()[3])&&
				compare(sign.getRelative(BlockFace.DOWN).getType(),rs.getWorkbenchItems()[4])){
			
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getWorkbenchItems()[1])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getWorkbenchItems()[2])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getWorkbenchItems()[6])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getWorkbenchItems()[5])){
				e.setCancelled(true);
				openWB(e.getPlayer(), sign);
				return;
			}
			
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getWorkbenchItems()[6])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getWorkbenchItems()[5])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getWorkbenchItems()[2])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getWorkbenchItems()[1])){
				e.setCancelled(true);
				openWB(e.getPlayer(), sign);
				return;
			}
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getWorkbenchItems()[2])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getWorkbenchItems()[1])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getWorkbenchItems()[5])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getWorkbenchItems()[6])){
				e.setCancelled(true);
				openWB(e.getPlayer(), sign);
				return;
			}
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getWorkbenchItems()[5])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getWorkbenchItems()[6])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getWorkbenchItems()[1])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getWorkbenchItems()[2])){
				e.setCancelled(true);
				openWB(e.getPlayer(), sign);
				return;
			}
		}
		//工作台 End
		
		//雨水收集器 Start
		if(sign.getState() instanceof Hopper){
			Hopper h=(Hopper)sign.getState();
			if(h.getCustomName()==null)return;
			if(h.getCustomName().equals("§3§l雨水收集器")){
				e.setCancelled(true);
				ItemStack itemKey=Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,
						sign.getWorld().getName(), sign.getX(), sign.getY(), sign.getZ());
						
				if(rs.getRwC().containsKey(itemKey.getItemMeta().getDisplayName())){
					if(!rs.getRwC(itemKey.getItemMeta().getDisplayName()).split(",")[0].equals(e.getPlayer().getName()))
						Utils.sendMsgToPlayer(e.getPlayer().getName(), rs.getMessage("UseByOther"));
				}else rs.addRwC(itemKey.getItemMeta().getDisplayName(), e.getPlayer().getName()+",0,0,"+h.getWorld().getName()+","+h.getX()+","+h.getY()+","+h.getZ());
				
				e.getPlayer().openInventory(FeatureGUI.openRwC(itemKey));
			}
			return;
		}
		//雨水收集器 End
		
		//净化装置 Start
		if(sign.getType()==Material.CARPET&&(sign.getRelative(BlockFace.DOWN).getType()==Material.CAULDRON)){
			e.getPlayer().openInventory(FeatureGUI.openPD());
			return;
		}
		if(sign.getType()==Material.CAULDRON&&(sign.getRelative(BlockFace.UP).getType()==Material.CARPET)){
			e.getPlayer().openInventory(FeatureGUI.openPD());
			return;
		}
		//净化装置 End

		//Fire Craft Table
		//当上方方块与下方方块不为指定方块时返回.	
		if(sign.getType().name().equalsIgnoreCase(rs.getFireCraftTableItems()[0])&&
				compare(sign.getRelative(BlockFace.UP).getType(),rs.getFireCraftTableItems()[3])&&
				compare(sign.getRelative(BlockFace.DOWN).getType(),rs.getFireCraftTableItems()[4])){
			
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getFireCraftTableItems()[1])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getFireCraftTableItems()[2])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getFireCraftTableItems()[6])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getFireCraftTableItems()[5])){
				e.setCancelled(true);
				openFCT(e.getPlayer(), sign);
				return;
			}
			
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getFireCraftTableItems()[6])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getFireCraftTableItems()[5])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getFireCraftTableItems()[2])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getFireCraftTableItems()[1])){
				e.setCancelled(true);
				openFCT(e.getPlayer(), sign);
				return;
			}
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getFireCraftTableItems()[2])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getFireCraftTableItems()[1])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getFireCraftTableItems()[5])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getFireCraftTableItems()[6])){
				e.setCancelled(true);
				openFCT(e.getPlayer(), sign);
				return;
			}
			if(compare(sign.getRelative(BlockFace.NORTH).getType(),rs.getFireCraftTableItems()[5])&&
					compare(sign.getRelative(BlockFace.SOUTH).getType(),rs.getFireCraftTableItems()[6])&&
					compare(sign.getRelative(BlockFace.EAST).getType(),rs.getFireCraftTableItems()[1])&&
					compare(sign.getRelative(BlockFace.WEST).getType(),rs.getFireCraftTableItems()[2])){
				e.setCancelled(true);
				openFCT(e.getPlayer(), sign);
				return;
			}
		}
		//Fire Craft Table End
		
		/*System.out.println(sign.getRelative(BlockFace.NORTH ).getType());
		System.out.println(sign.getRelative(BlockFace.SOUTH).getType());
		System.out.println(sign.getRelative(BlockFace.EAST ).getType());
		System.out.println(sign.getRelative(BlockFace.WEST ).getType());*/
		
	}
	
	private void openFCT(Player p,Block b){
		ItemStack itemKey=Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,
				b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
		if(rs.getFCT().containsKey(itemKey.getItemMeta().getDisplayName()))
			if(rs.getFCT(itemKey.getItemMeta().getDisplayName())[0].equals(p.getName()))
				p.openInventory(FireCraftTableGUI.openFCTGUI(itemKey));
			else
				Utils.sendMsgToPlayer(p.getName(), rs.getMessage("UseByOther"));
		else
			p.openInventory(FireCraftTableGUI.createFCTGUI(itemKey));
	}
	
	private void openWB(Player p, Block b) {
		ItemStack itemKey=Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,
				b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
		if(rs.getWorkbench().containsKey(itemKey.getItemMeta().getDisplayName()))
			if(rs.getWorkbench(itemKey.getItemMeta().getDisplayName(), 0).equals(p.getName()))
				p.openInventory(CraftItemGUI.openWorkbenchGUI(itemKey));
			else
				Utils.sendMsgToPlayer(p.getName(), rs.getMessage("UseByOther"));
		else
			p.openInventory(CraftItemGUI.createWorkbenchGUI(itemKey));
	}
	
	private boolean compare(Material type, String name){
		if(name==null||name.equalsIgnoreCase("null"))return true;
		return type.name().equalsIgnoreCase(name);
	}

}
